import { createHash } from 'node:crypto';
import { readFile, stat, writeFile } from 'node:fs/promises';
import path from 'node:path';

const VSIX = 'dist/vscode-xproc4j-0.1.0.vsix';
const OUTPUT = 'dist/SHA256SUMS.json';

async function main() {
  const target = path.resolve(process.cwd(), VSIX);
  const data = await readFile(target);
  const info = await stat(target);
  const checksum = createHash('sha256').update(data).digest('hex');
  const record = {
    algorithm: 'sha256',
    file: path.basename(VSIX),
    version: '0.1.0',
    size: info.size,
    sha256: checksum,
  };
  await writeFile(OUTPUT, `${JSON.stringify(record, null, 2)}\n`, 'utf8');
  console.log(`[checksum] ${record.file} ${checksum} (${record.size} bytes)`);
}

main().catch((error) => {
  console.error('[checksum] 生成校验文件失败', error instanceof Error ? error.message : error);
  process.exitCode = 1;
});
