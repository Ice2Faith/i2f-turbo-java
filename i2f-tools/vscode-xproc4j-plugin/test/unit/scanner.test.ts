import { describe, expect, it } from 'vitest';
import { scanXml } from '../../src/domain/xml/scanner';
import { parseStartTag } from '../../src/domain/xml/start-tag';
import { LineIndex } from '../../src/domain/xml/line-index';

const SAMPLE = `<?xml version="1.0"?>
<!DOCTYPE procedure SYSTEM "procedure.dtd">
<procedure id="demo" author="tester" xmlns:xproc4j="urn:xproc4j">
  <!-- <sql-query-list id="ignored"> -->
  <sql-query-list id="q1" datasource="main">
    <sql-update>
      SELECT * FROM t WHERE id = #{id}
    </sql-update>
  </sql-query-list>
  <lang-eval-java id="j1"><![CDATA[
    int a = 1;
  ]]></lang-eval-java>
  <script-segment id="seg">
    <lang-shell result="text">echo hi</lang-shell>
  </script-segment>
</procedure>`;

describe('scanXml', () => {
  it('识别根节点、嵌套层级与自闭合', () => {
    const scan = scanXml(SAMPLE);
    const root = scan.elements[scan.rootIndex]!;
    expect(root.name).toBe('procedure');
    expect(root.startTagClosed).toBe(true);
    expect(root.closed).toBe(true);

    const query = scan.elements.find((item) => item.name === 'sql-query-list')!;
    expect(query.depth).toBe(1);
    expect(query.attributes.map((item) => item.baseName)).toEqual(['id', 'datasource']);
    expect(query.attributes[0]!.value).toBe('q1');

    const update = scan.elements.find((item) => item.name === 'sql-update')!;
    expect(update.depth).toBe(2);
    expect(update.selfClosing).toBe(false);
    expect(update.closed).toBe(true);
  });

  it('不把注释、CDATA、PI、DOCTYPE 中的标签当作元素', () => {
    const scan = scanXml(SAMPLE);
    expect(scan.elements.map((item) => item.name)).toEqual([
      'procedure',
      'sql-query-list',
      'sql-update',
      'lang-eval-java',
      'script-segment',
      'lang-shell',
    ]);
    expect(scan.protectedRanges.map((item) => item.kind)).toEqual([
      'pi',
      'doctype',
      'comment',
      'cdata',
    ]);
  });

  it('自闭合标签不进入栈，也不影响后续兄弟节点', () => {
    const text = '<procedure><sql-update id="a"/><sql-query-list id="b"/></procedure>';
    const scan = scanXml(text);
    expect(scan.issues).toHaveLength(0);
    expect(scan.elements.map((item) => item.name)).toEqual([
      'procedure',
      'sql-update',
      'sql-query-list',
    ]);
    expect(scan.elements[1]!.selfClosing).toBe(true);
    expect(scan.elements[2]!.depth).toBe(1);
  });

  it('容错未闭合标签并记录 issue', () => {
    const scan = scanXml('<procedure><sql-update id="x">');
    expect(scan.truncated).toBe(false);
    expect(scan.issueCount).toBe(2);
    expect(scan.issues.map((item) => item.code)).toEqual(['unclosed-element', 'unclosed-element']);
    expect(scan.elements[1]!.closed).toBe(false);
  });

  it('遇到多余结束标签只记录 issue，不抛异常', () => {
    const scan = scanXml('</procedure>');
    expect(scan.rootIndex).toBe(-1);
    expect(scan.issueCount).toBe(1);
    expect(scan.issues[0]!.code).toBe('unexpected-end-tag');
  });

  it('尊重最大元素数与最大深度', () => {
    const limited = scanXml('<a><b/><c/></a>', { maxElements: 2 });
    expect(limited.truncated).toBe(true);
    expect(limited.truncatedReason).toBe('max-elements');

    const deep = scanXml('<a><b><c/></b></a>', { maxDepth: 2 });
    expect(deep.truncated).toBe(true);
    expect(deep.truncatedReason).toBe('max-depth');
  });
});

describe('parseStartTag', () => {
  it('解析单双引号、无引号与布尔属性', () => {
    const parsed = parseStartTag(`<sql-update result='x' id=a enabled>`, 0)!;
    expect(parsed.name).toBe('sql-update');
    expect(parsed.closed).toBe(true);
    expect(parsed.attributes.map((item) => [item.name, item.value, item.quote])).toEqual([
      ['result', 'x', "'"],
      ['id', 'a', null],
      ['enabled', '', null],
    ]);
  });

  it('拆分点号属性名并对实体解码', () => {
    const parsed = parseStartTag('<lang-shell result.int="1" database="a&amp;b">', 0)!;
    expect(parsed.attributes[0]!.baseName).toBe('result');
    expect(parsed.attributes[0]!.features).toEqual(['int']);
    expect(parsed.attributes[1]!.value).toBe('a&b');
    expect(parsed.attributes[1]!.rawValue).toBe('a&amp;b');
  });

  it('未闭合开始标签返回 closed=false', () => {
    const parsed = parseStartTag('<procedure id="a"', 0)!;
    expect(parsed.closed).toBe(false);
    expect(parsed.startTagEnd).toBe(17);
  });
});

describe('LineIndex', () => {
  it('在 LF 与 CRLF 下互相转换位置', () => {
    const text = 'a\nbb\r\nccc';
    const index = new LineIndex(text);
    expect(index.lineCount).toBe(3);
    expect(index.positionAt(0)).toEqual({ line: 0, character: 0 });
    expect(index.positionAt(2)).toEqual({ line: 1, character: 0 });
    expect(index.positionAt(4)).toEqual({ line: 1, character: 2 });
    expect(index.positionAt(6)).toEqual({ line: 2, character: 0 });
    expect(index.offsetAt({ line: 1, character: 2 })).toBe(4);
    expect(index.offsetAt({ line: 99, character: 99 })).toBe(text.length);
  });
});
