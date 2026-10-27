import fs from 'node:fs';
import path from 'node:path';

export function ensureDir(dir) {
  fs.mkdirSync(dir, { recursive: true });
}

const DRY_RUN = process.env.XPROC4J_DRY_RUN === '1';

function normalizeEol(text) {
  return text.replace(/\r\n/g, '\n');
}

/** 干跑模式只比对，不写盘；不一致时置失败退出码。 */
function writeOrCheck(file, content) {
  if (!DRY_RUN) {
    ensureDir(path.dirname(file));
    fs.writeFileSync(file, content, 'utf8');
    return;
  }
  let actual = null;
  try {
    actual = fs.readFileSync(file, 'utf8');
  } catch {
    actual = null;
  }
  if (actual === null || normalizeEol(actual) !== normalizeEol(content)) {
    process.exitCode = 1;
    console.error(`[gen:check] 生成物过期或缺失：${file}`);
  }
}

export function writeJson(file, value) {
  writeOrCheck(file, `${JSON.stringify(value, null, 2)}\n`);
}

export function writeText(file, text) {
  writeOrCheck(file, text.endsWith('\n') ? text : `${text}\n`);
}

export function readJson(file) {
  return JSON.parse(fs.readFileSync(file, 'utf8'));
}

export function readText(file) {
  return fs.readFileSync(file, 'utf8');
}

export function cleanDir(dir) {
  fs.rmSync(dir, { recursive: true, force: true });
}
