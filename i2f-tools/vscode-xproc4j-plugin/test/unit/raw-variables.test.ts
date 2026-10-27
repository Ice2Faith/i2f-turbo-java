import { describe, expect, it } from 'vitest';
import { extractRawVariables } from '../../src/domain/semantics/variable-extractor';
import {
  DEFAULT_VARIABLE_HIGHLIGHT_EXTENSIONS,
  fileMatchesExtensions,
  normalizeFileExtensions,
} from '../../src/domain/semantics/variable-file-extensions';

describe('extractRawVariables', () => {
  it('识别全部变量形式、空占位符与短变量', () => {
    const text = '${a} $!{b} #{c} #!{d} $short ${} $!';
    const items = extractRawVariables(text);
    expect(items.map((item) => item.name)).toEqual(['a', 'b', 'c', 'd', 'short', '']);
    expect(items.map((item) => item.empty)).toEqual([false, false, false, false, false, true]);
    expect(items.map((item) => text.slice(item.range.start, item.range.end))).toEqual([
      '${a}',
      '$!{b}',
      '#{c}',
      '#!{d}',
      '$short',
      '${}',
    ]);
  });

  it('不重复匹配 ${...} 中的短变量，并保持源码顺序', () => {
    const text = 'line1 $x\nline2 ${y} #{z}';
    const items = extractRawVariables(text);
    expect(items.map((item) => item.name)).toEqual(['x', 'y', 'z']);
    expect(items.map((item) => item.range.start)).toEqual([6, 15, 20]);
  });
});

describe('variable file extensions', () => {
  it('规范化后缀写法并允许显式关闭', () => {
    expect(normalizeFileExtensions(undefined)).toEqual([
      ...DEFAULT_VARIABLE_HIGHLIGHT_EXTENSIONS,
    ]);
    expect(normalizeFileExtensions(['XML', '.yaml', '*.java', 'd.ts', '', 'bad/name'])).toEqual([
      'xml',
      'yaml',
      'java',
      'd.ts',
    ]);
    expect(normalizeFileExtensions([])).toEqual([]);
  });

  it('按后缀边界匹配路径，大小写不敏感', () => {
    expect(fileMatchesExtensions('/work/Foo.YAML', ['yaml'])).toBe(true);
    expect(fileMatchesExtensions('/work/foo.d.ts', ['d.ts'])).toBe(true);
    expect(fileMatchesExtensions('/work/foo.d.ts', ['ts'])).toBe(true);
    expect(fileMatchesExtensions('/work/notxml', ['xml'])).toBe(false);
    expect(fileMatchesExtensions('/work/foo.txt', ['yaml', 'java'])).toBe(false);
  });
});
