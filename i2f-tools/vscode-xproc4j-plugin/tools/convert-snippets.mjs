#!/usr/bin/env node
/**
 * 将 XProc4j.xml 的 90 个 IDEA Live Template 转换为：
 * - snippets/xproc4j.code-snippets（VS Code 原生片段）
 * - metadata/snippets.json（补全 Provider 兜底用的中间产物）
 */
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { parseIdeaTemplates } from './lib/idea-template-parser.mjs';
import {
  convertTemplateValue,
  PROCEDURE_OVERRIDE_BODY,
} from './lib/snippet-builder.mjs';
import { readText, writeJson } from './lib/fs-utils.mjs';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const xml = readText(path.join(root, 'tools/sources/XProc4j.xml'));
const templates = parseIdeaTemplates(xml);
const errors = [];

if (templates.length !== 90) {
  errors.push(`模板数量期望 90，实际 ${templates.length}`);
}
const names = new Set();
for (const template of templates) {
  if (!template.name.startsWith('<')) {
    errors.push(`模板名不以 '<' 开头：${template.name}`);
  }
  if (/\s/.test(template.name)) {
    errors.push(`模板名包含空白：${template.name}`);
  }
  if (names.has(template.name)) {
    errors.push(`模板名重复：${template.name}`);
  }
  names.add(template.name);
}

const declarations = templates.flatMap((t) => t.variables);
const variableCount = declarations.length;
const emptyExpressions = declarations.filter((v) => v.expression === '').length;
const macros = declarations.filter((v) => v.expression !== '');
if (variableCount !== 187) {
  errors.push(`变量数量期望 187，实际 ${variableCount}`);
}
if (emptyExpressions !== 183) {
  errors.push(`空表达式数量期望 183，实际 ${emptyExpressions}`);
}
if (macros.length !== 4) {
  errors.push(`宏数量期望 4，实际 ${macros.length}`);
}

const snippets = {};
const providerSnippets = [];
for (const template of templates) {
  let body;
  if (template.name === '<procedure') {
    body = PROCEDURE_OVERRIDE_BODY;
  } else {
    const converted = convertTemplateValue(template.value, template.variables);
    if (!converted.body.includes('$0') && template.name !== '<lang-thread-pool') {
      errors.push(`模板缺少 $END$：${template.name}`);
    }
    body = converted.body;
  }
  snippets[template.name] = {
    prefix: template.name,
    body: body.split('\n'),
    description: template.description || `XProc4J ${template.name}`,
    scope: 'xml',
  };
  providerSnippets.push({
    prefix: template.name,
    description: template.description || `XProc4J ${template.name}`,
    body,
    sourceName: template.name.slice(1),
  });
}

if (errors.length) {
  console.error(`Snippet 转换失败（${errors.length}）：`);
  for (const error of errors) {
    console.error(`  - ${error}`);
  }
  process.exit(1);
}

writeJson(path.join(root, 'snippets/xproc4j.code-snippets'), snippets);
writeJson(path.join(root, 'metadata/snippets.json'), { version: 1, snippets: providerSnippets });
console.log(
  `[snippets] templates=${templates.length} variables=${variableCount} ` +
    `empty=${emptyExpressions} macros=${macros.length}`,
);
