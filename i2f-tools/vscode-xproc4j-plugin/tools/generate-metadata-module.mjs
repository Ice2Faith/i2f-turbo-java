#!/usr/bin/env node
/**
 * 把构建期 JSON 打包为 TypeScript 常量，运行期不做文件 I/O。
 */
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { readJson, readText, writeText } from './lib/fs-utils.mjs';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');

const metadata = {
  tags: readJson(path.join(root, 'metadata/tags.json')).tags,
  attributes: readJson(path.join(root, 'metadata/attributes.json')).attributes,
  features: readJson(path.join(root, 'metadata/features.json')).features,
  injections: readJson(path.join(root, 'metadata/injections.json')),
  enums: readJson(path.join(root, 'metadata/enums.json')),
  keywords: readJson(path.join(root, 'metadata/keywords.json')),
  snippets: readJson(path.join(root, 'metadata/snippets.json')).snippets,
};
delete metadata.injections.version;
delete metadata.enums.version;
delete metadata.keywords.version;

const header = '// 该文件由 tools/generate-metadata-module.mjs 生成，请勿手工修改。\n';

writeText(
  path.join(root, 'src/generated/metadata.ts'),
  `${header}import type { MetadataBundle } from '../domain/metadata/types';

export const METADATA: MetadataBundle = ${JSON.stringify(metadata, null, 2)};
`,
);

const templates = [
  ['{{id}}', '{{author}}', '{{date}}', '{{time}}'],
];
void templates;

const tpl = readText(path.join(root, 'templates/procedure.xml.tpl'));
writeText(
  path.join(root, 'src/generated/procedure-template.ts'),
  `${header}export const PROCEDURE_TEMPLATE = ${JSON.stringify(tpl)};\n`,
);
console.log(`[module] metadata.ts tags=${metadata.tags.length} snippets=${metadata.snippets.length}`);
