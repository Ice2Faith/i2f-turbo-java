import { describe, expect, it } from 'vitest';
import { collectAttributeValueRanges } from '../../src/domain/semantics/attribute-value-background';
import { buildDocumentModel } from '../../src/domain/xml/document-model';

function modelOf(text: string) {
  return buildDocumentModel('file:///demo.xml', 1, text);
}

describe('collectAttributeValueRanges', () => {
  it('只返回引号内的属性值范围', () => {
    const text = '<procedure result="alpha" other=\'beta\'>x</procedure>';
    const ranges = collectAttributeValueRanges(modelOf(text));
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual([
      'alpha',
      'beta',
    ]);
    expect(text.slice(ranges[0]!.start - 1, ranges[0]!.start)).toBe('"');
    expect(text.slice(ranges[0]!.end, ranges[0]!.end + 1)).toBe('"');
  });

  it('覆盖嵌套元素、开放参数与未知属性', () => {
    const text = [
      '<procedure IN_X.string="value">',
      '  <procedure-call refid="p" custom="c">',
      '    <sql-query-list id="q">select 1</sql-query-list>',
      '  </procedure-call>',
      '</procedure>',
    ].join('\n');
    const ranges = collectAttributeValueRanges(modelOf(text));
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual([
      'value',
      'p',
      'c',
      'q',
    ]);
  });

  it('跳过无值布尔属性与空值属性', () => {
    const text = '<procedure result flag="" empty=\'\' note="keep">x</procedure>';
    const ranges = collectAttributeValueRanges(modelOf(text));
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual(['keep']);
  });

  it('多行属性值与普通 XML 的边界行为正确', () => {
    const text = '<procedure value="line1\nline2">x</procedure>';
    const ranges = collectAttributeValueRanges(modelOf(text));
    expect(ranges.map((range) => text.slice(range.start, range.end))).toEqual([
      'line1\nline2',
    ]);

    const pom = modelOf('<project><name value="demo"/></project>');
    expect(collectAttributeValueRanges(pom)).toEqual([]);
  });
});
