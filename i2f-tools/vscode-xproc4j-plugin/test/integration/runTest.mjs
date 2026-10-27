#!/usr/bin/env node
/**
 * 使用本机安装的 VS Code 运行扩展宿主集成测试。
 * 可通过 VSCODE_EXECUTABLE 指定 Code 可执行文件；未找到时回退到下载。
 * Linux 无显示环境需使用 xvfb-run。
 */
import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import { fileURLToPath } from 'node:url';
import { runTests } from '@vscode/test-electron';

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../..');

function findVSCode() {
  const platform = process.platform;
  const candidates = [
    process.env.VSCODE_EXECUTABLE,
    platform === 'win32'
      ? path.join(process.env.LOCALAPPDATA ?? '', 'Programs', 'Microsoft VS Code', 'Code.exe')
      : undefined,
    platform === 'win32' ? 'C:\\Program Files\\Microsoft VS Code\\Code.exe' : undefined,
    platform === 'darwin'
      ? '/Applications/Visual Studio Code.app/Contents/MacOS/Electron'
      : undefined,
    platform === 'linux' ? '/usr/share/code/code' : undefined,
    platform === 'linux' ? '/usr/bin/code' : undefined,
  ].filter(Boolean);
  return candidates.find((candidate) => fs.existsSync(candidate));
}

const temp = fs.mkdtempSync(path.join(os.tmpdir(), 'xproc4j-it-'));
const vscodeExecutablePath = findVSCode();

try {
  const exitCode = await runTests({
    ...(vscodeExecutablePath ? { vscodeExecutablePath } : {}),
    extensionDevelopmentPath: root,
    extensionTestsPath: path.join(root, 'test/integration/suite.js'),
    launchArgs: [
      path.join(root, 'test/fixtures/workspace'),
      '--disable-workspace-trust',
      '--disable-gpu',
      '--skip-welcome',
      '--skip-release-notes',
      `--user-data-dir=${path.join(temp, 'user-data')}`,
      `--extensions-dir=${path.join(temp, 'extensions')}`,
    ],
  });
  process.exitCode = exitCode;
} finally {
  try {
    fs.rmSync(temp, { recursive: true, force: true, maxRetries: 5, retryDelay: 200 });
  } catch {
    // Windows 上 VS Code 进程可能仍短暂持有 profile 目录；临时目录可由系统清理。
  }
}
