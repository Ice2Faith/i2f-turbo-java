import { describe, expect, it } from 'vitest';
import { collectEvalKeywordRanges } from '../../src/domain/semantics/eval-keywords';
import { buildDocumentModel } from '../../src/domain/xml/document-model';

function rangesOf(text: string) {
  const model = buildDocumentModel('file:///demo.xml', 1, text);
  return collectEvalKeywordRanges(model, text);
}

describe('collectEvalKeywordRanges', () => {
  it('在 lang-eval-java 正文中匹配 Java/JavaScript 关键字并与 SQL 块隔离', () => {
    const text = [
      '<procedure>',
      '  <lang-eval-java>',
      '    for (int i = 0; i < 10; i++) {',
      '      if (i > 3) { continue; } else { break; }',
      '    }',
      '    String name = "select";',
      '  </lang-eval-java>',
      '  <sql-query-list>',
      '    SELECT * FROM demo_table WHERE id = 1;',
      '  </sql-query-list>',
      '</procedure>',
    ].join('\n');
    const ranges = rangesOf(text);
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual([
      'for',
      'int',
      'if',
      'continue',
      'else',
      'break',
    ]);
    // 字符串字面量内的关键字不按关键字着色（交给字符串装饰统一处理）。
    expect(ranges.some((range) => text.slice(range.start, range.end) === 'select')).toBe(
      false,
    );
    expect(ranges.some((range) => text.slice(range.start, range.end) === 'SELECT')).toBe(
      false,
    );
  });

  it('只处理 procedure 根下的 lang-eval* 元素', () => {
    const text = [
      '<procedure>',
      '  <sql-query-list>select * from demo_table where id = 1</sql-query-list>',
      '  <lang-eval>let x = 1;</lang-eval>',
      '</procedure>',
    ].join('\n');
    const ranges = rangesOf(text);
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual(['let']);

    const pom = [
      '<project>',
      '  <lang-eval>if (ready) return;</lang-eval>',
      '</project>',
    ].join('\n');
    expect(rangesOf(pom)).toEqual([]);
  });

  it('按词边界排除标识符，并保留大小写不敏感匹配', () => {
    const text = [
      '<procedure>',
      '  <lang-eval>format imported classy whileLoop foreach forEach for SELECT</lang-eval>',
      '</procedure>',
    ].join('\n');
    const ranges = rangesOf(text);
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual([
      'foreach',
      'forEach',
      'for',
      'SELECT',
    ]);
    expect(ranges.map((range) => range.keyword)).toEqual([
      'foreach',
      'forEach',
      'for',
      'SELECT',
    ]);
  });

  it('字符串字面量内的关键字被跳过，注释中的关键字仍命中', () => {
    const text = [
      '<procedure>',
      "  <lang-eval>let name = 'select'; // for while</lang-eval>",
      '</procedure>',
    ].join('\n');
    const ranges = rangesOf(text);
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual([
      'let',
      'for',
      'while',
    ]);
  });
});
