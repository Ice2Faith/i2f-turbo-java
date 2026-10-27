#!/usr/bin/env node
/**
 * 校验或更新 tools/sources 的 SHA-256 清单。
 *
 * 清单只保存相对路径、大小与哈希，不写入绝对路径或复制时间，
 * 以保证仓库可移植且生成结果确定。
 */
import { createHash } from 'node:crypto';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

export const SOURCE_FILES = [
  'AttrConsts.java',
  'FeatureConsts.java',
  'ParamsConsts.java',
  'TagConsts.java',
  'XProc4j.xml',
  'procedure.dtd',
  'procedure.xml',
  'procedure.xml.ft',
];

export const CHECKSUM_FILE = 'SHA256SUMS.json';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const sourcesDir = path.join(root, 'tools/sources');

function hashFile(file) {
  return createHash('sha256').update(fs.readFileSync(file)).digest('hex');
}

export function buildManifest(dir = sourcesDir) {
  const files = SOURCE_FILES.map((name) => {
    const full = path.join(dir, name);
    const data = fs.readFileSync(full);
    return { path: name, size: data.byteLength, sha256: hashFile(full) };
  });
  return { algorithm: 'sha256', files };
}

export function writeManifest(dir = sourcesDir) {
  const manifest = buildManifest(dir);
  const target = path.join(dir, CHECKSUM_FILE);
  fs.writeFileSync(target, `${JSON.stringify(manifest, null, 2)}\n`, 'utf8');
  return manifest;
}

export function verifyManifest(dir = sourcesDir) {
  const target = path.join(dir, CHECKSUM_FILE);
  if (!fs.existsSync(target)) {
    return [`缺少 ${CHECKSUM_FILE}；请执行 npm run source:update 生成源校验清单`];
  }
  let manifest;
  try {
    manifest = JSON.parse(fs.readFileSync(target, 'utf8'));
  } catch {
    return [`${CHECKSUM_FILE} 不是合法 JSON；请执行 npm run source:update 重新生成`];
  }
  if (manifest.algorithm !== 'sha256' || !Array.isArray(manifest.files)) {
    return [`${CHECKSUM_FILE} 结构无效；请执行 npm run source:update 重新生成`];
  }
  const errors = [];
  const recorded = new Map(manifest.files.map((item) => [item.path, item]));
  for (const name of SOURCE_FILES) {
    const expected = recorded.get(name);
    if (!expected) {
      errors.push(`源校验清单缺少 ${name}；请执行 npm run source:update`);
      continue;
    }
    const full = path.join(dir, name);
    if (!fs.existsSync(full)) {
      errors.push(`缺少 vendored 源文件 ${name}`);
      continue;
    }
    const actual = hashFile(full);
    if (actual !== expected.sha256) {
      errors.push(
        `vendored 源文件 ${name} 已变化；确认上游版本后执行 npm run source:update`,
      );
    }
  }
  return errors;
}

function main() {
  const update = process.argv.includes('--update');
  if (update) {
    const manifest = writeManifest();
    console.log(`[sources] 已更新 ${CHECKSUM_FILE}（${manifest.files.length} 个文件）`);
    return;
  }
  const errors = verifyManifest();
  if (errors.length) {
    console.error(`[sources] 校验失败（${errors.length}）：`);
    for (const error of errors) {
      console.error(`  - ${error}`);
    }
    process.exitCode = 1;
    return;
  }
  console.log(`[sources] ${SOURCE_FILES.length} 个 vendored 源文件校验通过`);
}

if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) {
  main();
}
