import { describe, expect, it } from 'vitest';
import { buildDocumentModel, DocumentModelCache } from '../../src/domain/xml/document-model';

const TEXT = `<procedure id="demo">
  <script-segment id="s1"/>
  <script-segment id="s2"/>
  <sql-query-list id="q1">
    <sql-update refid="target">select ${'$'}x</sql-update>
  </sql-query-list>
  <sql-update id="target"/>
</procedure>`;

describe('buildDocumentModel', () => {
  it('识别 procedure 根节点与脚本片段', () => {
    const model = buildDocumentModel('file:///demo.xml', 1, TEXT);
    expect(model.isXProc).toBe(true);
    expect(model.rootTag).toBe('procedure');
    expect(model.rootTagOffset).toBe(1);
    expect(model.procedureIndexes).toHaveLength(3);
    expect(model.scan.elements[model.procedureIndexes[0]!]!.name).toBe('procedure');
    expect(model.scan.elements[model.procedureIndexes[1]!]!.name).toBe('script-segment');
  });

  it('收集变量名与 refid 值', () => {
    const model = buildDocumentModel('file:///demo.xml', 1, TEXT);
    expect(model.variableNames).toEqual(['x']);
    expect(model.refidValues.map((item) => item.value)).toEqual(['target']);
  });

  it('非 procedure 根节点不是 XProc 文档', () => {
    const model = buildDocumentModel('file:///pom.xml', 1, '<project/>');
    expect(model.isXProc).toBe(false);
    expect(model.rootTag).toBe('project');
    expect(model.procedureIndexes).toEqual([]);
  });

  it('空文档与异常文本不抛异常', () => {
    const model = buildDocumentModel('file:///empty.xml', 1, '');
    expect(model.isXProc).toBe(false);
    expect(model.rootTag).toBeNull();
    expect(model.rootTagOffset).toBe(-1);
  });
});

describe('DocumentModelCache', () => {
  it('同版本同长度复用，版本变化重建', () => {
    const cache = new DocumentModelCache();
    const first = cache.get('file:///demo.xml', 1, TEXT);
    const second = cache.get('file:///demo.xml', 1, TEXT);
    expect(second).toBe(first);

    const third = cache.get('file:///demo.xml', 2, TEXT.replace('demo', 'demo2'));
    expect(third).not.toBe(first);
    expect(cache.peek('file:///demo.xml')).toBe(third);
  });

  it('invalidate 与 clear 清空缓存', () => {
    const cache = new DocumentModelCache();
    cache.get('file:///a.xml', 1, '<procedure/>');
    cache.invalidate('file:///a.xml');
    expect(cache.peek('file:///a.xml')).toBeUndefined();
    cache.get('file:///b.xml', 1, '<procedure/>');
    cache.clear();
    expect(cache.peek('file:///b.xml')).toBeUndefined();
  });
});
