import { describe, expect, it } from 'vitest';
import { collectEvalLiteralRanges } from '../../src/domain/semantics/eval-literals';
import { buildDocumentModel } from '../../src/domain/xml/document-model';

function collect(text: string) {
  const model = buildDocumentModel('file:///demo.xml', 1, text);
  return collectEvalLiteralRanges(model, text);
}

function slice(text: string, ranges: readonly { start: number; end: number }[]) {
  return ranges.map((range) => text.slice(range.start, range.end));
}

describe('collectEvalLiteralRanges', () => {
  it('在 lang-eval* 正文中收集整数与小数', () => {
    const text = [
      '<procedure>',
      '  <lang-eval-java>',
      '    int total = 0;',
      '    double rate = 1.25;',
      '    total = 10;',
      '  </lang-eval-java>',
      '</procedure>',
    ].join('\n');
    const result = collect(text);
    expect(slice(text, result.numbers)).toEqual(['0', '1.25', '10']);
    expect(result.strings).toEqual([]);
  });

  it('收集单引号与双引号字符串并记录引号类型，空字符串也计入', () => {
    const text = [
      '<procedure>',
      `  <lang-eval>let a = 'hello'; let b = "world"; let c = '';</lang-eval>`,
      '</procedure>',
    ].join('\n');
    const result = collect(text);
    expect(slice(text, result.strings)).toEqual(["'hello'", '"world"', "''"]);
    expect(result.strings.map((range) => range.quote)).toEqual(["'", '"', "'"]);
    expect(result.numbers).toEqual([]);
  });

  it('字符串内部的数字不按数值着色', () => {
    const text = [
      '<procedure>',
      `  <lang-eval>let a = "12.5"; let b = '34';</lang-eval>`,
      '</procedure>',
    ].join('\n');
    const result = collect(text);
    expect(slice(text, result.numbers)).toEqual([]);
    expect(slice(text, result.strings)).toEqual(['"12.5"', "'34'"]);
  });

  it('只处理 procedure 根下的 lang-eval* 元素，SQL 块不参与', () => {
    const text = [
      '<procedure>',
      '  <sql-query-list>select 1 from demo_table where name = \'abc\'</sql-query-list>',
      "  <lang-eval>let n = 1; let s = 'abc';</lang-eval>",
      '</procedure>',
    ].join('\n');
    const result = collect(text);
    expect(slice(text, result.numbers)).toEqual(['1']);
    expect(slice(text, result.strings)).toEqual(["'abc'"]);

    const pom = [
      '<project>',
      "  <lang-eval>let n = 1; let s = 'abc';</lang-eval>",
      '</project>',
    ].join('\n');
    expect(collect(pom)).toEqual({ numbers: [], strings: [] });
  });

  it('按原始正则语义处理：未闭合引号吞到下一个同型引号，注释中的字面量也会命中', () => {
    const text = [
      '<procedure>',
      `  <lang-eval>// 1.5 与 'note'`,
      `let a = 2; let b = 'x`,
      `y'; let c = 3;`,
      '  </lang-eval>',
      '</procedure>',
    ].join('\n');
    const result = collect(text);
    expect(slice(text, result.numbers)).toEqual(['1.5', '2', '3']);
    // 未闭合引号会跨行匹配到下一个同型引号，这是正则语义的已知限制。
    expect(slice(text, result.strings)).toEqual(["'note'", "'x\ny'"]);
  });
});
