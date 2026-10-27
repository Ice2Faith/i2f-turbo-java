import { describe, expect, it } from 'vitest';
import { indexFile } from '../../src/domain/index/index-scan';
import { buildSnapshot, emptySnapshot } from '../../src/domain/index/index-model';
import { IndexStore } from '../../src/domain/index/query';

const FILE_A = `<procedure id="A" author="alice" timeout.int="10">
  <script-segment id="S1" mode="fast">
    <lang-shell>echo hi</lang-shell>
  </script-segment>
  <procedure-call refid="B" datasource="main"/>
</procedure>`;

const FILE_B = `<procedure id="B" author="bob">
  <sql-update refid="A"/>
</procedure>`;

describe('indexFile', () => {
  it('提取 procedure/script-segment、参数与 refid', () => {
    const file = indexFile('file:///a.xml', 1, FILE_A);
    expect(file.rootTag).toBe('procedure');
    expect(file.skippedReason).toBeUndefined();
    expect(file.entries.map((entry) => [entry.id, entry.kind])).toEqual([
      ['A', 'procedure'],
      ['S1', 'script-segment'],
    ]);
    expect(file.entries[1]!.parentId).toBe('A');
    expect(file.entries[0]!.params).toEqual([
      { name: 'author', features: [] },
      { name: 'timeout', features: ['int'] },
    ]);
    expect(file.refs.map((ref) => [ref.refid, ref.tagName])).toEqual([['B', 'procedure-call']]);
    expect(file.entries[0]!.idRange.start).toEqual({ line: 0, character: 15 });
  });

  it('非 procedure 根节点不产生贡献', () => {
    const file = indexFile('file:///pom.xml', 0, '<project id="x"><procedure id="fake"/></project>');
    expect(file.rootTag).toBe('project');
    expect(file.skippedReason).toBe('not-xproc');
    expect(file.entries).toEqual([]);
    expect(file.refs).toEqual([]);
  });

  it('空 id 不生成条目', () => {
    const file = indexFile('file:///a.xml', 0, '<procedure id=""><script-segment id="S"/></procedure>');
    expect(file.entries.map((entry) => entry.id)).toEqual(['S']);
  });

  it('未闭合元素仍可容错索引', () => {
    const file = indexFile('file:///a.xml', 0, '<procedure id="A"><script-segment id="S">');
    expect(file.entries.map((entry) => entry.id)).toEqual(['A', 'S']);
  });
});

describe('IndexStore', () => {
  const a = indexFile('file:///a.xml', 0, FILE_A);
  const b = indexFile('file:///b.xml', 0, FILE_B);
  const base = buildSnapshot([a, b], 1);

  it('查询定义、引用与参数', () => {
    const store = new IndexStore();
    store.setBase(base);
    expect(store.findDefinitions('A').map((entry) => entry.uri)).toEqual(['file:///a.xml']);
    expect(store.findReferences('B').map((ref) => ref.uri)).toEqual(['file:///a.xml']);
    expect(store.getParams('B')?.params.map((param) => param.name)).toEqual(['author']);
    expect(store.peekId('A')).toBe(true);
    expect(store.peekId('missing')).toBe(false);
  });

  it('覆盖层优先并屏蔽旧磁盘结果', () => {
    const store = new IndexStore();
    store.setBase(base);
    const overlay = indexFile('file:///a.xml', 2, '<procedure id="A2"/>');
    store.updateOverlay(overlay);

    expect(store.findDefinitions('A')).toEqual([]);
    expect(store.findDefinitions('A2')).toHaveLength(1);
    expect(store.getAllIds(10).map((item) => item.id)).toEqual(['A2', 'B']);

    store.removeOverlay('file:///a.xml');
    expect(store.findDefinitions('A')).toHaveLength(1);
  });

  it('当前文件 id 排序优先', () => {
    const c = indexFile('file:///c.xml', 0, '<procedure id="A"/>');
    const store = new IndexStore();
    store.setBase(buildSnapshot([a, c], 1));
    expect(store.findDefinitions('A', 'file:///c.xml').map((entry) => entry.uri)[0]).toBe('file:///c.xml');
    expect(store.getAllIds(10, 'file:///c.xml')[0]?.id).toBe('A');
  });

  it('空快照查询安全', () => {
    const store = new IndexStore();
    store.setBase(emptySnapshot());
    expect(store.findDefinitions('x')).toEqual([]);
    expect(store.getAllIds(5)).toEqual([]);
    expect(store.getStats().entryCount).toBe(0);
  });

  it('统计覆盖层替换同名文件而不是重复计数', () => {
    const store = new IndexStore();
    store.setBase(base);
    const before = store.getStats();
    store.updateOverlay(indexFile('file:///a.xml', 2, '<procedure id="A2"/>'));
    const after = store.getStats();
    expect(after.fileCount).toBe(before.fileCount);
    expect(after.entryCount).toBe(before.entryCount - 1);
    expect(after.refCount).toBe(before.refCount - 1);
  });
});
