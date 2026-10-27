#!/usr/bin/env node
/**
 * 从 vendored 的 DTD / Java 常量 / 注入配置生成运行期元数据。
 *
 * 生成物必须确定性：无时间戳、无绝对路径；数组按固定规则排序。
 * 本脚本只做构建期工作，运行期不读取 DTD 或 Java 源文件。
 */
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { parseDtd } from './lib/dtd-parser.mjs';
import { parseJavaStringValues } from './lib/java-const-parser.mjs';
import { readJson, readText, writeJson, writeText, ensureDir } from './lib/fs-utils.mjs';
import { verifyManifest } from './source-checksums.mjs';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const sources = path.join(root, 'tools/sources');
const config = path.join(root, 'tools/config');
const outDir = path.join(root, 'metadata');

const CATEGORY_ORDER = [
  'root', 'fragment', 'call', 'flow', 'sql', 'etl', 'java', 'script',
  'concurrent', 'io', 'string', 'assign', 'output', 'log', 'context',
  'event', 'dynamic', 'unknown',
];

const OPEN_ATTRIBUTE_TAGS = new Set([
  'procedure',
  'script-segment',
  'procedure-call',
  'function-call',
  'script-include',
  'lang-new-params',
  'lang-println',
  'lang-printf',
  'lang-thread-pool-submit',
  'event-send',
  'event-publish',
]);

const errors = [];
const warnings = [];
errors.push(...verifyManifest(sources));

function assertEqual(actual, expected, label) {
  if (actual !== expected) {
    errors.push(`${label} 期望 ${expected}，实际 ${actual}`);
  }
}

function classify(name, classifyConfig) {
  if (classifyConfig.explicit[name]) {
    return classifyConfig.explicit[name];
  }
  let best = null;
  for (const rule of classifyConfig.prefixRules) {
    if (name.startsWith(rule.prefix) && (!best || rule.prefix.length > best.prefix.length)) {
      best = rule;
    }
  }
  return best?.category ?? 'unknown';
}

function splitFeatureName(rawName) {
  const dot = rawName.indexOf('.');
  if (dot <= 0) {
    return { baseName: rawName, features: [] };
  }
  return {
    baseName: rawName.slice(0, dot),
    features: rawName.slice(dot + 1).split('.').filter(Boolean),
  };
}

function mergeType(current, next) {
  if (!current || current === 'CDATA') {
    return next;
  }
  if (next === 'CDATA') {
    return current;
  }
  if (current === next) {
    return current;
  }
  // 同一基础属性在不同标签上有不同类型属于上游不严谨，保留第一个并告警。
  warnings.push(`属性类型冲突：${current} vs ${next}`);
  return current;
}

const dtd = parseDtd(readText(path.join(sources, 'procedure.dtd')));
const tagConsts = parseJavaStringValues(readText(path.join(sources, 'TagConsts.java')));
const featureValues = parseJavaStringValues(readText(path.join(sources, 'FeatureConsts.java')));
const attrConsts = new Set(parseJavaStringValues(readText(path.join(sources, 'AttrConsts.java'))));
const classifyConfig = readJson(path.join(config, 'classify.json'));
const injectionsConfig = readJson(path.join(config, 'injections.json'));
const enumsConfig = readJson(path.join(config, 'enums.json'));
const keywordsConfig = readJson(path.join(config, 'keywords.json'));

// ---- 校验输入统计 ----
assertEqual(dtd.elements.length, 111, 'DTD ELEMENT 数量');
assertEqual(dtd.attlistCount, 910, 'DTD ATTLIST 声明数量');
assertEqual(dtd.noAttributes.length, 18, '无属性标签数量');
assertEqual(dtd.multiAttlist.length, 71, '多 ATTLIST 标签数量');
assertEqual(dtd.anomalies.length, 19, '异常 ATTLIST 声明数量');
assertEqual(tagConsts.length, 97, 'TagConsts 标签数量');
assertEqual(featureValues.length, 58, 'FeatureConsts 数量');

const dynamicTags = new Set(classifyConfig.dynamicSqlTags ?? []);
const dtdOnlyDynamic = dtd.elements.map((e) => e.name).filter((name) => !tagConsts.includes(name));
assertEqual(dtdOnlyDynamic.length, 14, 'DTD-only 动态标签数量');
for (const name of dtdOnlyDynamic) {
  if (!dynamicTags.has(name)) {
    errors.push(`DTD-only 标签 ${name} 不在 dynamicSqlTags 中`);
  }
}
for (const name of tagConsts) {
  if (!dtd.elementSet.has(name)) {
    errors.push(`TagConsts 标签 ${name} 不存在于 DTD`);
  }
}

// ---- 标签与属性 ----
const attributesByName = new Map();
const tags = [];

for (const element of dtd.elements) {
  const category = classify(element.name, classifyConfig);
  if (category === 'unknown') {
    errors.push(`标签 ${element.name} 未分类`);
  }
  const rawAttributes = dtd.attributesByTag.get(element.name) ?? [];
  const byBase = new Map();
  for (const raw of rawAttributes) {
    const { baseName, features } = splitFeatureName(raw.name);
    const existing = byBase.get(baseName);
    if (existing) {
      if (features.length) {
        existing.features.add(...features);
      }
      existing.type = mergeType(existing.type, raw.type);
      existing.required = existing.required || raw.required;
      existing.rawNames.push(raw.name);
    } else {
      byBase.set(baseName, {
        name: baseName,
        type: raw.type,
        required: raw.required,
        features: new Set(features),
        rawNames: [raw.name],
      });
    }
  }

  const attributes = [...byBase.values()].map((attr) => {
    const type = attr.type.startsWith('(')
      ? attr.type.slice(1, -1).split('|').map((v) => v.trim()).filter(Boolean).join('|')
      : attr.type;
    const global = attributesByName.get(attr.name);
    if (global) {
      global.tags.add(element.name);
      global.type = mergeType(global.type, type);
      for (const f of attr.features) {
        global.features.add(f);
      }
    } else {
      attributesByName.set(attr.name, {
        name: attr.name,
        type,
        features: new Set(attr.features),
        tags: new Set([element.name]),
        source: 'dtd',
      });
    }
    return {
      name: attr.name,
      type,
      required: attr.required,
      features: [...attr.features].sort(),
      rawNames: attr.rawNames.sort(),
    };
  }).sort((a, b) => a.name.localeCompare(b.name));

  tags.push({
    name: element.name,
    category,
    openAttributes: OPEN_ATTRIBUTE_TAGS.has(element.name),
    source: dynamicTags.has(element.name) ? 'dynamic' : 'dtd',
    attributes,
  });
}

// 开放属性标签必须在 DTD 中；数量与参考实现一致。
assertEqual(tags.filter((t) => t.openAttributes).length, 11, '开放属性标签数量');

tags.sort((a, b) => {
  const ca = CATEGORY_ORDER.indexOf(a.category);
  const cb = CATEGORY_ORDER.indexOf(b.category);
  if (ca !== cb) {
    return ca - cb;
  }
  return a.name.localeCompare(b.name);
});

const attributes = [...attributesByName.values()]
  .map((attr) => ({
    name: attr.name,
    type: attr.type,
    features: [...attr.features].sort(),
    tags: [...attr.tags].sort(),
    source: attr.source,
  }))
  .sort((a, b) => a.name.localeCompare(b.name));

// ---- feature 分组 ----
const FEATURE_GROUPS = {
  annotation: ['in', 'out'],
  type: ['int', 'double', 'float', 'string', 'long', 'short', 'char', 'byte', 'boolean', 'null', 'date'],
  string: ['render', 'trim', 'align', 'body-text', 'body-xml'],
  location: ['location-file', 'location-line', 'location-tag', 'location'],
  spacing: ['spacing-left', 'spacing-right', 'spacing'],
  transform: ['lower', 'upper', 'hashcode'],
  process: ['visit', 'eval', 'test', 'context', 'executor', 'node', 'not', 'dialect'],
  language: ['eval-java', 'eval-js', 'eval-tinyscript', 'eval-ts', 'eval-groovy', 'eval-funic', 'class'],
  predicate: ['is-null', 'is-not-null', 'is-empty', 'is-not-empty'],
  value: ['date-now', 'uuid', 'current-time-millis', 'snow-uid', 'new-map', 'new-list', 'new-set'],
  exception: ['cause-first', 'cause-last', 'cause-raw', 'cause-root'],
};
const featureGroup = new Map();
for (const [group, names] of Object.entries(FEATURE_GROUPS)) {
  for (const name of names) {
    featureGroup.set(name, group);
  }
}
const features = featureValues.map((name) => ({
  name,
  group: featureGroup.get(name) ?? 'general',
  description: '',
}));

// ---- 枚举 ----
for (const key of ['jdbcType', 'propagation', 'timeUnit']) {
  if (!Array.isArray(enumsConfig[key]) || enumsConfig[key].length === 0) {
    errors.push(`枚举 ${key} 为空`);
  }
}
assertEqual(enumsConfig.jdbcType.length, 39, 'jdbcType 数量');
assertEqual(enumsConfig.propagation.length, 7, 'propagation 数量');
assertEqual(enumsConfig.timeUnit.length, 7, 'timeUnit 数量');

const languageIds = [...new Set(Object.keys(injectionsConfig.langDynamic.values))].sort();
const enums = {
  ...enumsConfig,
  languageIds,
};

// ---- 注入映射校验 ----
const tagNames = new Set(tags.map((t) => t.name));
for (const rule of injectionsConfig.body) {
  for (const tag of rule.tags) {
    if (!tagNames.has(tag)) {
      errors.push(`注入规则 ${rule.id} 引用不存在的标签 ${tag}`);
    }
    if (rule.scope && !/^(source|text)\./.test(rule.scope)) {
      errors.push(`注入规则 ${rule.id} 的 scope 非法：${rule.scope}`);
    }
  }
}
for (const [name, rule] of Object.entries(injectionsConfig.langDynamic.values)) {
  if (rule.scope && !/^(source|text)\./.test(rule.scope)) {
    errors.push(`_lang 注入 ${name} 的 scope 非法：${rule.scope}`);
  }
}
const excluded = new Set(injectionsConfig.excludedBodyTags);
for (const rule of injectionsConfig.body) {
  if (rule.id === 'vars-only' || rule.id === 'dsl-vars-only') {
    continue;
  }
  for (const tag of rule.tags) {
    if (excluded.has(tag)) {
      errors.push(`excludedBodyTags 与 body.tags 冲突：${tag}`);
    }
  }
}

// ---- procedure.xml 覆盖统计（warn only） ----
const sampleXml = readText(path.join(sources, 'procedure.xml'))
  .replace(/<!--[\s\S]*?-->/g, '')
  .replace(/<!\[CDATA\[[\s\S]*?\]\]>/g, '');
const usedTags = new Set();
const usedRe = /<\s*([A-Za-z][\w.-]*)/g;
let usedMatch;
while ((usedMatch = usedRe.exec(sampleXml)) !== null) {
  usedTags.add(usedMatch[1]);
}
const covered = [...tagNames].filter((name) => usedTags.has(name)).sort();
const missingInSample = [...tagNames].filter((name) => !usedTags.has(name)).sort();
if (covered.length !== 106) {
  warnings.push(`procedure.xml 覆盖标签数期望 106，实际 ${covered.length}`);
}

// ---- 写出 ----
ensureDir(outDir);
writeJson(path.join(outDir, 'tags.json'), { version: 1, tags });
writeJson(path.join(outDir, 'attributes.json'), { version: 1, attributes });
writeJson(path.join(outDir, 'features.json'), { version: 1, features });
writeJson(path.join(outDir, 'injections.json'), {
  version: 1,
  ...injectionsConfig,
  dynamicSqlTags: [...dynamicTags].sort(),
});
writeJson(path.join(outDir, 'enums.json'), { version: 1, ...enums });
writeJson(path.join(outDir, 'keywords.json'), { version: 1, ...keywordsConfig });

const report = [
  '# XProc4J 元数据构建报告',
  '',
  // 生成物必须确定性：不写入时间戳与绝对路径。
  `- 源目录：tools/sources`,
  `- DTD 标签：${tags.length}`,
  `- 属性基础名：${attributes.length}`,
  `- ATTLIST 声明：${dtd.attlistCount}`,
  `- 多 ATTLIST 标签：${dtd.multiAttlist.length}`,
  `- 无属性标签：${dtd.noAttributes.length}`,
  `- feature：${features.length}`,
  `- 动态 SQL 标签：${dynamicTags.size}`,
  `- 开放属性标签：${tags.filter((t) => t.openAttributes).length}`,
  `- 注入规则：${injectionsConfig.body.length}`,
  `- procedure.xml 覆盖：${covered.length}/111`,
  '',
  '## 分类计数',
  '',
  ...CATEGORY_ORDER
    .map((category) => ({ category, count: tags.filter((t) => t.category === category).length }))
    .filter((item) => item.count > 0)
    .map((item) => `- ${item.category}: ${item.count}`),
  '',
  '## 异常属性声明',
  '',
  ...dtd.anomalies.map((a) => `- ${a.tag}.${a.name}：${a.reason === 'missing-type' ? '缺类型' : '非法类型'}（${a.rawType}）`),
  '',
  '## procedure.xml 未覆盖标签',
  '',
  ...missingInSample.map((name) => `- ${name}`),
  '',
  '## 警告',
  '',
  ...(warnings.length ? warnings.map((w) => `- ${w}`) : ['- 无']),
  '',
  '## 错误',
  '',
  ...(errors.length ? errors.map((e) => `- ${e}`) : ['- 无']),
  '',
].join('\n');
writeText(path.join(outDir, 'report.md'), report);

if (errors.length) {
  console.error(`元数据校验失败（${errors.length}）：`);
  for (const error of errors) {
    console.error(`  - ${error}`);
  }
  process.exitCode = 1;
} else {
  console.log(
    `[metadata] tags=${tags.length} attributes=${attributes.length} features=${features.length} ` +
      `injections=${injectionsConfig.body.length} dynamic=${dynamicTags.size} ` +
      `anomalies=${dtd.anomalies.length} sample=${covered.length}/111`,
  );
  if (warnings.length) {
    console.warn(`[metadata] ${warnings.length} warning(s)，详见 metadata/report.md`);
  }
}
