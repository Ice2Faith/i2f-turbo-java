#!/usr/bin/env node
/**
 * 生成物一致性检查：以 XPROC4J_DRY_RUN=1 依次执行生成器。
 * 生成器只比对、不写盘；任何差异都会使本脚本以非零状态退出。
 */
import { spawnSync } from 'node:child_process';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const scripts = [
  'extract-metadata.mjs',
  'generate-grammar.mjs',
  'convert-snippets.mjs',
  'generate-metadata-module.mjs',
];

let failed = false;
for (const script of scripts) {
  const result = spawnSync(process.execPath, [path.join(root, 'tools', script)], {
    stdio: 'inherit',
    env: { ...process.env, XPROC4J_DRY_RUN: '1' },
  });
  if (result.status !== 0) {
    failed = true;
  }
}

if (failed) {
  console.error('[gen:check] 生成物与源不一致，请运行 npm run gen 后重试。');
  process.exit(1);
}
console.log('[gen:check] 生成物一致');
