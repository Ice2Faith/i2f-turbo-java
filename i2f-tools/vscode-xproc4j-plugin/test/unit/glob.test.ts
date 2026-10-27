import { describe, expect, it } from 'vitest';
import { globMatch } from '../../src/infrastructure/glob';

describe('globMatch', () => {
  it('**/ 可匹配零层或多层目录', () => {
    expect(globMatch('**/node_modules/**', 'node_modules/x.xml')).toBe(true);
    expect(globMatch('**/node_modules/**', 'a/node_modules/x.xml')).toBe(true);
    expect(globMatch('**/node_modules/**', 'a/node_modulesX/x.xml')).toBe(false);
  });

  it('支持 XML 文件通配与大小写', () => {
    expect(globMatch('**/*.xml', 'a.xml')).toBe(true);
    expect(globMatch('**/*.xml', 'x/y/A.XML')).toBe(true);
    expect(globMatch('**/*.xml', 'x/y/a.xml.bak')).toBe(false);
  });

  it('支持花括号与 Windows 分隔符', () => {
    expect(globMatch('**/*.{xml,xproc}', 'a\\b\\c.xproc')).toBe(true);
    expect(globMatch('**/*.{xml,xproc}', 'a\\b\\c.sql')).toBe(false);
  });

  it('? 不跨目录', () => {
    expect(globMatch('a/?.xml', 'a/b.xml')).toBe(true);
    expect(globMatch('a/?.xml', 'a/bc.xml')).toBe(false);
  });
});
