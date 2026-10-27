/**
 * 生成 TextMate 注入语法。
 *
 * 设计要点：
 * - 注入目标是 text.xml，只匹配已知标签，避免污染普通 XML；
 * - 非正文注入标签用 begin/end 覆盖完整开始标签（含属性），不能只匹配 `<tag`：
 *   注入规则一旦消费标签起始符号，宿主 XML 就不会进入 meta.tag，剩余属性会失去 scope；
 * - 正文注入标签消费开始标签并在 beginCaptures 中重新着色属性；
 * - 动态 SQL 标签只在 #sqlContent 内生效；SQL 正文中的 XML 子标签（如 sql-dialect）
 *   必须先于 source.sql 匹配，否则会被宿主语言当作比较符/标识符。
 */

// 变量使用 support.variable 而不是 variable.other：部分主题（如 VS Code Light
// 的 2026 默认主题）把 variable.other 映射为正文色，导致 ${x}/#{x} 看似未高亮；
// support.variable 在主流主题中通常是醒目的蓝色，与括号/标点区分明显。
const VAR_PATTERNS = [
  { match: '[#$]!?\\{\\s*\\}', name: 'invalid.illegal.xproc4j' },
  { match: '[#$]!?\\{[^{}]*\\}', name: 'support.variable.xproc4j' },
  { match: '\\$[A-Za-z0-9_]+', name: 'support.variable.xproc4j' },
];

function escRe(text) {
  return String(text).replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function tagNameScopes(tag) {
  return `entity.name.tag.${tag.category}.xproc4j entity.name.tag.xproc4j`;
}

function attrsPattern(attributes, openAttributes, suffixRules = []) {
  const patterns = [];

  // 属性值语言注入：arg0.eval-java="..." 等。
  for (const rule of suffixRules) {
    const suffix = escRe(rule.suffix);
    patterns.push({
      begin: `([A-Za-z_][\\w-]*\\.${suffix})(\\s*=\\s*)(")`,
      beginCaptures: {
        1: { name: 'entity.other.attribute-name.xproc4j' },
        2: { name: 'keyword.operator.assignment.xml' },
        3: { name: 'punctuation.definition.string.begin.xml' },
      },
      end: '"',
      endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
      contentName: rule.contentName,
      patterns: [
        { include: '#variables' },
        ...(rule.scope ? [{ include: rule.scope }] : []),
      ],
    });
    patterns.push({
      begin: `([A-Za-z_][\\w-]*\\.${suffix})(\\s*=\\s*)(')`,
      beginCaptures: {
        1: { name: 'entity.other.attribute-name.xproc4j' },
        2: { name: 'keyword.operator.assignment.xml' },
        3: { name: 'punctuation.definition.string.begin.xml' },
      },
      end: "'",
      endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
      contentName: rule.contentName,
      patterns: [
        { include: '#variables' },
        ...(rule.scope ? [{ include: rule.scope }] : []),
      ],
    });
  }

  const known = attributes
    .map((a) => a.name)
    .filter(Boolean)
    .sort((a, b) => b.length - a.length);
  if (known.length) {
    const alternation = known.map((name) => `${escRe(name)}(?:\\.[A-Za-z0-9-]+)*`).join('|');
    for (const [quote, end] of [
      ['"', '"'],
      ["'", "'"],
    ]) {
      patterns.push({
        begin: `\\b(${alternation})(\\s*=\\s*)(${quote === '"' ? '"' : "'"})`,
        beginCaptures: {
          1: { name: 'entity.other.attribute-name.xproc4j' },
          2: { name: 'keyword.operator.assignment.xml' },
          3: { name: 'punctuation.definition.string.begin.xml' },
        },
        end,
        endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
        contentName: 'string.quoted.xml',
        patterns: [
          { include: '#variables' },
          { include: '#attributeValueFeature' },
        ],
      });
    }
    patterns.push({
      match: `\\b(${alternation})\\b(?=\\s*/?>)`,
      captures: { 1: { name: 'entity.other.attribute-name.xproc4j' } },
    });
  }

  // 兜底：保证未声明/开放属性仍按 XML 属性着色。
  if (openAttributes) {
    patterns.push({ include: '#openAttribute' });
  } else {
    patterns.push({ include: '#genericAttribute' });
  }
  return patterns;
}

function narrowRule(tag) {
  return {
    match: `(</?)(${escRe(tag.name)})(?=[\\s/>])`,
    captures: {
      1: { name: 'punctuation.definition.tag.xml' },
      2: { name: tagNameScopes(tag) },
    },
  };
}

/**
 * 非正文标签的开始标签规则。
 *
 * 不能只给标签名着色而把属性留给宿主 XML：注入语法的 match 会先消费 `<tag`，
 * 宿主 XML 不再进入 meta.tag 状态，剩余属性会失去任何 scope（表现为黑色）。
 * 因此这里用 begin/end 覆盖整个开始标签，并在属性段内复用 attrsPattern。
 * end 放在首个未加引号的 `>`，因此属性可以跨行（相对 startAttrsRegex 的单行限制）。
 */
function startTagRule(tag, suffixRules) {
  return {
    begin: `(<)(${escRe(tag.name)})(?=[\\s/>])`,
    beginCaptures: {
      1: { name: 'punctuation.definition.tag.begin.xml' },
      2: { name: tagNameScopes(tag) },
    },
    end: '(/?)>',
    endCaptures: { 0: { name: 'punctuation.definition.tag.end.xml' } },
    patterns: attrsPattern(tag.attributes, tag.openAttributes, suffixRules),
  };
}

function startAttrsRegex() {
  // 开始标签属性段：只允许同一行，允许引号内出现 >，避免跨行注入（DD 已知限制）。
  return `((?:[ \\t]+(?:[^<>"'\\n]|"[^"\\n]*"|'[^'\\n]*')*)?)`;
}

function selfClosingRule(tag, attrRules, suffixRules) {
  return {
    match: `(<)(${escRe(tag.name)})${startAttrsRegex()}(/>)`,
    captures: {
      1: { name: 'punctuation.definition.tag.begin.xml' },
      2: { name: tagNameScopes(tag) },
      3: { patterns: attrsPattern(tag.attributes, tag.openAttributes, suffixRules) },
      4: { name: 'punctuation.definition.tag.end.xml' },
    },
  };
}

function bodyRule(tag, contentPatterns, contentName, attrRules, suffixRules) {
  return {
    begin: `(<)(${escRe(tag.name)})${startAttrsRegex()}(>)`,
    beginCaptures: {
      1: { name: 'punctuation.definition.tag.begin.xml' },
      2: { name: tagNameScopes(tag) },
      3: { patterns: attrRules },
      4: { name: 'punctuation.definition.tag.end.xml' },
    },
    end: `(</)(${escRe(tag.name)})(\\s*>)`,
    endCaptures: {
      1: { name: 'punctuation.definition.tag.begin.xml' },
      2: { name: tagNameScopes(tag) },
      3: { name: 'punctuation.definition.tag.end.xml' },
    },
    ...(contentName ? { contentName } : {}),
    patterns: contentPatterns,
  };
}

/**
 * @param {{tags: any[], injections: any, suffixRules: any[]}} input
 */
export function buildGrammar({ tags, injections, suffixRules }) {
  const byName = new Map(tags.map((tag) => [tag.name, tag]));
  const repository = {
    variables: { patterns: VAR_PATTERNS },
    attributeValueFeature: { patterns: [] },
    openAttribute: {
      patterns: [
        {
          begin: '([A-Za-z_][A-Za-z0-9_.:-]*)(\\s*=\\s*)(")',
          beginCaptures: {
            1: { name: 'entity.other.attribute-name.xproc4j' },
            2: { name: 'keyword.operator.assignment.xml' },
            3: { name: 'punctuation.definition.string.begin.xml' },
          },
          end: '"',
          endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
          contentName: 'string.quoted.xml',
          patterns: [{ include: '#variables' }],
        },
        {
          begin: "([A-Za-z_][A-Za-z0-9_.:-]*)(\\s*=\\s*)(')",
          beginCaptures: {
            1: { name: 'entity.other.attribute-name.xproc4j' },
            2: { name: 'keyword.operator.assignment.xml' },
            3: { name: 'punctuation.definition.string.begin.xml' },
          },
          end: "'",
          endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
          contentName: 'string.quoted.xml',
          patterns: [{ include: '#variables' }],
        },
      ],
    },
    genericAttribute: {
      patterns: [
        {
          begin: '([A-Za-z_][A-Za-z0-9_.:-]*)(\\s*=\\s*)(")',
          beginCaptures: {
            1: { name: 'entity.other.attribute-name.xml' },
            2: { name: 'keyword.operator.assignment.xml' },
            3: { name: 'punctuation.definition.string.begin.xml' },
          },
          end: '"',
          endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
          contentName: 'string.quoted.xml',
          patterns: [{ include: '#variables' }],
        },
        {
          begin: "([A-Za-z_][A-Za-z0-9_.:-]*)(\\s*=\\s*)(')",
          beginCaptures: {
            1: { name: 'entity.other.attribute-name.xml' },
            2: { name: 'keyword.operator.assignment.xml' },
            3: { name: 'punctuation.definition.string.begin.xml' },
          },
          end: "'",
          endCaptures: { 0: { name: 'punctuation.definition.string.end.xml' } },
          contentName: 'string.quoted.xml',
          patterns: [{ include: '#variables' }],
        },
      ],
    },
    sqlContent: { patterns: [] },
    javaContent: { patterns: [] },
    jsContent: { patterns: [{ include: '#variables' }] },
    genericContent: { patterns: [{ include: '#variables' }] },
    varsOnlyContent: { patterns: [{ include: '#variables' }] },
  };

  const attrRulesCache = new Map();
  const getAttrRules = (tag) => {
    const key = `${tag.name}:${tag.openAttributes}`;
    if (!attrRulesCache.has(key)) {
      const cacheKey = `attr-${tag.name}`;
      repository[cacheKey] = { patterns: attrsPattern(tag.attributes, tag.openAttributes, suffixRules) };
      attrRulesCache.set(key, [{ include: `#${cacheKey}` }]);
    }
    return attrRulesCache.get(key);
  };

  const bodyTags = [];
  for (const rule of injections.body) {
    if (rule.id === 'dsl-vars-only') {
      continue;
    }
    for (const name of rule.tags) {
      const tag = byName.get(name);
      if (!tag) {
        continue;
      }
      bodyTags.push({ tag, rule });
    }
  }
  for (const name of injections.varsOnlyBodyTags ?? []) {
    if (bodyTags.some((item) => item.tag.name === name)) {
      continue;
    }
    const tag = byName.get(name);
    if (tag) {
      bodyTags.push({
        tag,
        rule: {
          id: 'dsl-vars-only',
          contentName: 'meta.embedded.block.variables.xproc4j',
          variables: true,
        },
      });
    }
  }

  const dynamicTags = (injections.dynamicSqlTags ?? []).map((name) => byName.get(name)).filter(Boolean);
  const topPatterns = [];
  const bodyTagNames = new Set(bodyTags.map((item) => item.tag.name));
  const dynamicNames = new Set(dynamicTags.map((tag) => tag.name));

  // 1) 正文注入标签：自闭合 → 正文 → 名称兜底。
  for (const { tag, rule } of bodyTags) {
    const attrs = getAttrRules(tag);
    let contentPatterns;
    if (rule.id === 'sql') {
      contentPatterns = [{ include: '#sqlContent' }];
    } else if (rule.id === 'java') {
      contentPatterns = [{ include: '#javaContent' }];
    } else if (rule.id === 'javascript') {
      contentPatterns = [{ include: '#jsContent' }];
    } else if (rule.id === 'groovy' || rule.id === 'shell') {
      contentPatterns = [{ include: '#variables' }, ...(rule.scope ? [{ include: rule.scope }] : [])];
    } else {
      contentPatterns = [{ include: '#varsOnlyContent' }];
    }
    const contentName = rule.contentName;

    // _lang 变体必须排在默认规则之前。
    if ((injections.langDynamic?.tags ?? []).includes(tag.name)) {
      const seenTargets = new Set();
      for (const [alias, target] of Object.entries(injections.langDynamic.values)) {
        const targetKey = `${target.scope ?? ''}|${target.contentName}`;
        if (seenTargets.has(targetKey)) {
          continue;
        }
        seenTargets.add(targetKey);
        let langPatterns;
        if (target.scope === 'source.sql') {
          langPatterns = [{ include: '#sqlContent' }];
        } else if (target.scope === 'source.java') {
          langPatterns = [{ include: '#javaContent' }];
        } else {
          langPatterns = [
            { include: '#variables' },
            ...(target.scope ? [{ include: target.scope }] : []),
          ];
        }
        const base = bodyRule(tag, langPatterns, target.contentName, attrs, suffixRules);
        topPatterns.push({
          ...base,
          begin: `(<)(${escRe(tag.name)})(?=[^>\\n]*\\b_lang\\s*=\\s*["'][^"'\\n]*\\b${escRe(alias)}\\b[^"'\\n]*["'])${startAttrsRegex()}(>)`,
        });
      }
    }

    topPatterns.push(selfClosingRule(tag, attrs, suffixRules));
    topPatterns.push(bodyRule(tag, contentPatterns, contentName, attrs, suffixRules));
  }

  // 2) 非正文标签：覆盖开始标签（含属性），结束标签只着色名称。
  for (const tag of tags) {
    if (bodyTagNames.has(tag.name) || dynamicNames.has(tag.name)) {
      continue;
    }
    topPatterns.push(startTagRule(tag, suffixRules));
    topPatterns.push(narrowRule(tag));
  }
  // 3) 正文注入标签的 fallback 名称规则（多行开始标签等）。
  for (const tag of tags) {
    if (bodyTagNames.has(tag.name)) {
      topPatterns.push(narrowRule(tag));
    }
  }

  // 4) 动态 SQL 标签规则，只在 SQL 正文内引用。
  const sqlPatterns = [{ include: '#variables' }];
  // sql-dialect 是 SQL 正文里的 XML 子标签（如 sql-query-object 内按数据库分派）。
  // 必须先于 source.sql 匹配，否则 `<sql-dialect` 会被 SQL 当成比较符/标识符。
  for (const name of ['sql-dialect']) {
    const tag = byName.get(name);
    if (!tag) {
      continue;
    }
    const attrs = getAttrRules(tag);
    repository[`sql-nested-self-${name}`] = selfClosingRule(tag, attrs, suffixRules);
    repository[`sql-nested-body-${name}`] = bodyRule(
      tag,
      [{ include: '#sqlContent' }],
      'meta.embedded.block.sql.xproc4j',
      attrs,
      suffixRules,
    );
    repository[`sql-nested-name-${name}`] = narrowRule(tag);
    sqlPatterns.push({ include: `#sql-nested-self-${name}` });
    sqlPatterns.push({ include: `#sql-nested-body-${name}` });
    sqlPatterns.push({ include: `#sql-nested-name-${name}` });
  }
  for (const tag of dynamicTags) {
    const attrs = getAttrRules(tag);
    repository[`dyn-self-${tag.name}`] = selfClosingRule(tag, attrs, suffixRules);
    repository[`dyn-body-${tag.name}`] = bodyRule(
      tag,
      [{ include: '#sqlContent' }],
      'meta.embedded.block.sql.xproc4j',
      attrs,
      suffixRules,
    );
    repository[`dyn-name-${tag.name}`] = narrowRule(tag);
    sqlPatterns.push({ include: `#dyn-self-${tag.name}` });
    sqlPatterns.push({ include: `#dyn-body-${tag.name}` });
    sqlPatterns.push({ include: `#dyn-name-${tag.name}` });
  }
  sqlPatterns.push({ include: 'source.sql' });
  repository.sqlContent.patterns = sqlPatterns;

  // 5) Java 子标签。
  const javaPatterns = [{ include: '#variables' }];
  for (const name of ['lang-java-import', 'lang-java-member', 'lang-java-body']) {
    const tag = byName.get(name);
    if (!tag) {
      continue;
    }
    const attrs = getAttrRules(tag);
    repository[`java-self-${name}`] = selfClosingRule(tag, attrs, suffixRules);
    repository[`java-body-${name}`] = bodyRule(
      tag,
      [{ include: '#javaContent' }],
      'meta.embedded.block.java.xproc4j',
      attrs,
      suffixRules,
    );
    repository[`java-name-${name}`] = narrowRule(tag);
    javaPatterns.push({ include: `#java-self-${name}` });
    javaPatterns.push({ include: `#java-body-${name}` });
    javaPatterns.push({ include: `#java-name-${name}` });
  }
  javaPatterns.push({ include: 'source.java' });
  repository.javaContent.patterns = javaPatterns;

  // 6) JS/Groovy/Shell 的正文内容。
  repository.jsContent.patterns = [{ include: '#variables' }, { include: 'source.js' }];

  return {
    $schema: 'https://raw.githubusercontent.com/martinring/tmlanguage/master/tmlanguage.json',
    name: 'XProc4J Injection',
    scopeName: 'xproc4j.injection',
    injectionSelector: 'L:text.xml',
    patterns: topPatterns,
    repository,
  };
}
