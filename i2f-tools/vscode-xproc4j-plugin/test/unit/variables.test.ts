import { describe, expect, it } from 'vitest';
import { extractVariables, collectVariableNames } from '../../src/domain/semantics/variable-extractor';
import { buildVariableTokens } from '../../src/domain/semantics/variable-tokens';
import { buildDocumentModel } from '../../src/domain/xml/document-model';
import { getCatalog } from '../../src/domain/metadata/catalog';
import { scanXml } from '../../src/domain/xml/scanner';

const catalog = getCatalog();

function extract(text: string) {
  return extractVariables(
    text,
    scanXml(text),
    (name) => catalog.isKnownTag(name),
    (name) => catalog.isKnownAttribute(name),
    (name) => catalog.isOpenTag(name),
  );
}

describe('extractVariables', () => {
  it('识别 #{}、${}、$!{} 与短变量，并区分空占位符', () => {
    const text = '<procedure id="p"><sql-update>select ${a}, #{b}, $!{c}, $d, ${}, #!{}</sql-update></procedure>';
    const items = extract(text);
    expect(items.map((item) => item.name)).toEqual(['a', 'b', 'c', 'd', '', '']);
    expect(items.map((item) => item.empty)).toEqual([false, false, false, false, true, true]);
    expect(items.map((item) => item.attribute)).toEqual([false, false, false, false, false, false]);
  });

  it('只收集已知标签内的变量', () => {
    const text = '<procedure><unknown-tag>$ignored</unknown-tag><sql-update>$kept</sql-update></procedure>';
    expect(extract(text).map((item) => item.name)).toEqual(['kept']);
  });

  it('属性变量要求已知属性或标签开放属性', () => {
    const text = [
      '<procedure data-x="$open" id="$id">',
      '<sql-update bogus="$closed" datasource="$ds">$body</sql-update>',
      '</procedure>',
    ].join('');
    const names = extract(text).map((item) => [item.name, item.attribute]);
    expect(names).toEqual([
      ['open', true],
      ['id', true],
      ['ds', true],
      ['body', false],
    ]);
  });

  it('忽略注释与 CDATA 中的变量', () => {
    const text = [
      '<procedure>',
      '<!-- $comment -->',
      '<lang-eval-java><![CDATA[ $cdata ]]></lang-eval-java>',
      '<lang-eval-java>$java</lang-eval-java>',
      '</procedure>',
    ].join('');
    expect(extract(text).map((item) => item.name)).toEqual(['java']);
  });

  it('未知标签的正文变量不产生结果', () => {
    expect(extract('<foo>$x</foo>')).toEqual([]);
  });
});

describe('collectVariableNames', () => {
  it('去重、去空并排序', () => {
    const items = extract('<procedure id="$b"><sql-update>${a} $b ${}</sql-update></procedure>');
    expect(collectVariableNames(items)).toEqual(['a', 'b']);
  });
});

describe('buildVariableTokens', () => {
  const text = '<procedure id="p" refid="$r"><sql-update id="u" refid="q1">${name} ${}</sql-update></procedure>';

  it('生成变量 token 与空占位符修饰符', () => {
    const model = buildDocumentModel('file:///demo.xml', 1, text);
    const tokens = buildVariableTokens(model, text, catalog);
    const variables = tokens.filter((item) => item.tokenType === 0);
    expect(variables).toHaveLength(3);
    expect(variables.map((item) => item.tokenModifiers)).toEqual([0, 0, 1]);
  });

  it('覆盖已知标签正文与非正文注入标签的属性变量', () => {
    const text = [
      '<procedure>',
      '<lang-if test.eval-ts="is_empty(${a})"/>',
      '<lang-ai>$!{b}</lang-ai>',
      '<sql-update>#{c}</sql-update>',
      '</procedure>',
    ].join('');
    const model = buildDocumentModel('file:///demo.xml', 1, text);
    const tokens = buildVariableTokens(model, text, catalog);
    const values = tokens
      .filter((item) => item.tokenType === 0)
      .map((item) => text.slice(item.range.start, item.range.end));
    expect(values).toEqual(['${a}', '$!{b}', '#{c}']);
  });

  it('仅在引用可解析时标记 refid', () => {
    const model = buildDocumentModel('file:///demo.xml', 1, text);
    const all = buildVariableTokens(model, text, catalog);
    expect(all.some((item) => item.tokenType === 1)).toBe(true);

    const resolved = buildVariableTokens(model, text, catalog, new Set(['q1']));
    const refids = resolved.filter((item) => item.tokenType === 1);
    expect(refids).toHaveLength(1);
  });

  it('可按配置关闭变量 token，同时保留 refid token', () => {
    const model = buildDocumentModel('file:///demo.xml', 1, text);
    const tokens = buildVariableTokens(model, text, catalog, undefined, false);
    expect(tokens.every((item) => item.tokenType === 1)).toBe(true);
  });
});
