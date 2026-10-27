#!/usr/bin/env node
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { buildGrammar } from './lib/grammar-builder.mjs';
import { readJson, readText, writeJson } from './lib/fs-utils.mjs';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const tags = readJson(path.join(root, 'metadata/tags.json')).tags;
const injections = readJson(path.join(root, 'metadata/injections.json'));
const classify = readJson(path.join(root, 'tools/config/classify.json'));
injections.dynamicSqlTags = classify.dynamicSqlTags;
const injectionsConfig = readJson(path.join(root, 'tools/config/injections.json'));

const grammar = buildGrammar({
  tags,
  injections,
  suffixRules: injectionsConfig.attribute ?? [],
});
writeJson(path.join(root, 'syntaxes/xproc4j.injection.tmLanguage.json'), grammar);

let ruleCount = 0;
let maxRegex = 0;
const visit = (value) => {
  if (Array.isArray(value)) {
    for (const item of value) {
      visit(item);
    }
    return;
  }
  if (!value || typeof value !== 'object') {
    return;
  }
  for (const key of ['match', 'begin', 'end']) {
    if (typeof value[key] === 'string') {
      ruleCount++;
      maxRegex = Math.max(maxRegex, value[key].length);
    }
  }
  for (const child of Object.values(value)) {
    visit(child);
  }
};
visit(grammar);

const size = JSON.stringify(grammar).length;
writeJson(path.join(root, 'metadata/grammar-report.json'), {
  ruleCount,
  maxRegexLength: maxRegex,
  bytes: size,
  topLevelPatterns: grammar.patterns.length,
  repositoryEntries: Object.keys(grammar.repository).length,
});
console.log(
  `[grammar] rules=${ruleCount} top=${grammar.patterns.length} ` +
    `repository=${Object.keys(grammar.repository).length} bytes=${size} maxRegex=${maxRegex}`,
);
