import { describe, expect, it } from 'vitest';
import { collectBlockBackgroundRanges } from '../../src/domain/semantics/block-background';
import { buildDocumentModel } from '../../src/domain/xml/document-model';

function modelOf(text: string) {
  return buildDocumentModel('file:///demo.xml', 1, text);
}

describe('collectBlockBackgroundRanges', () => {
  it('合并嵌套 SQL 标签，返回不含结束标签的正文范围', () => {
    const text = [
      '<procedure>',
      '  <sql-query-object result="rows">',
      '    <sql-dialect databases="mysql">',
      '      select 1',
      '    </sql-dialect>',
      '  </sql-query-object>',
      '</procedure>',
    ].join('\n');
    const ranges = collectBlockBackgroundRanges(modelOf(text));
    expect(ranges).toHaveLength(1);
    const block = text.slice(ranges[0]!.start, ranges[0]!.end);
    expect(ranges[0]!.kind).toBe('sql');
    expect(block).toContain('select 1');
    expect(block).not.toContain('</sql-query-object>');
  });

  it('为多个并列 SQL 块返回独立范围', () => {
    const text = [
      '<procedure>',
      '  <sql-update id="a">update t set x = 1</sql-update>',
      '  <sql-query-list id="b">select 1</sql-query-list>',
      '</procedure>',
    ].join('\n');
    const ranges = collectBlockBackgroundRanges(modelOf(text));
    expect(ranges.map((range) => text.slice(range.start, range.end).trim())).toEqual([
      'update t set x = 1',
      'select 1',
    ]);
    expect(ranges.map((range) => range.kind)).toEqual(['sql', 'sql']);
  });

  it('把所有 lang-eval* 正文标记为 eval 类型', () => {
    const text = [
      '<procedure>',
      '  <lang-eval-java result="a">int a = 1;</lang-eval-java>',
      '  <lang-eval-ts result="b">const b = 2;</lang-eval-ts>',
      '  <lang-eval result="c">return true;</lang-eval>',
      '</procedure>',
    ].join('\n');
    const ranges = collectBlockBackgroundRanges(modelOf(text));
    expect(ranges.map((range) => text.slice(range.start, range.end).trim())).toEqual([
      'int a = 1;',
      'const b = 2;',
      'return true;',
    ]);
    expect(ranges.map((range) => range.kind)).toEqual(['eval', 'eval', 'eval']);
  });

  it('同时存在 SQL 与 eval 块时按类型分别返回', () => {
    const text = [
      '<procedure>',
      '  <sql-query-list id="a">select 1</sql-query-list>',
      '  <lang-eval-js result="b">return 2;</lang-eval-js>',
      '</procedure>',
    ].join('\n');
    const ranges = collectBlockBackgroundRanges(modelOf(text));
    expect(ranges.map((range) => range.kind)).toEqual(['sql', 'eval']);
  });

  it('不同类型嵌套时保留各自范围，不互相合并', () => {
    const text = [
      '<procedure>',
      '  <sql-query-list id="a">',
      '    <lang-eval-js result="b">return 2;</lang-eval-js>',
      '    select 1',
      '  </sql-query-list>',
      '</procedure>',
    ].join('\n');
    const ranges = collectBlockBackgroundRanges(modelOf(text));
    expect(ranges.map((range) => range.kind)).toEqual(['sql', 'eval']);
  });

  it('非 procedure 根节点与非 SQL/非 eval 正文不产生背景范围', () => {
    const pom = modelOf(
      '<project><sql-update>select 1</sql-update><lang-eval-java>int x = 1;</lang-eval-java></project>',
    );
    expect(collectBlockBackgroundRanges(pom)).toEqual([]);

    const other = modelOf(
      '<procedure><lang-println>hello</lang-println><script-segment>return 1;</script-segment></procedure>',
    );
    expect(collectBlockBackgroundRanges(other)).toEqual([]);
  });
});
