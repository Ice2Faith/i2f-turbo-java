import { describe, expect, it } from 'vitest';
import { resolveCompletionContext } from '../../src/domain/completion/context';
import { assembleCompletions } from '../../src/domain/completion/orchestrator';
import { buildDocumentModel } from '../../src/domain/xml/document-model';
import { getCatalog } from '../../src/domain/metadata/catalog';
import { indexFile } from '../../src/domain/index/index-scan';
import { buildSnapshot } from '../../src/domain/index/index-model';
import { IndexStore } from '../../src/domain/index/query';

const catalog = getCatalog();

function complete(text: string, offset = text.length, index?: IndexStore) {
  const model = buildDocumentModel('file:///demo.xml', 1, text);
  const context = resolveCompletionContext(text, offset, model, catalog);
  const entries = assembleCompletions(context, text, offset, model, {
    catalog,
    index,
    builtinVariables: true,
    sqlKeywords: true,
  });
  return { context, entries, model };
}

function has(entries: ReturnType<typeof complete>['entries'], label: string): boolean {
  return entries.some((entry) => entry.label === label);
}

describe('补全上下文', () => {
  it('标签名上下文', () => {
    const { context, entries } = complete('<procedure><sql-');
    expect(context.kind).toBe('tag');
    expect(has(entries, 'sql-update')).toBe(true);
    expect(has(entries, 'procedure')).toBe(true);
    const dynamic = entries.find((entry) => entry.label === 'if')!;
    expect(dynamic.sortText.startsWith('0_9_')).toBe(true);
  });

  it('行首裸 < 后接换行仍进入标签补全', () => {
    const text = '<procedure id="X">\n    <\n</procedure>\n';
    const { context, entries } = complete(text, text.indexOf('<\n') + 1);
    expect(context.kind).toBe('tag');
    expect(has(entries, 'procedure-call')).toBe(true);
    expect(has(entries, 'script-segment')).toBe(true);
  });

  it('SQL 正文中的 < 比较符不触发标签补全', () => {
    const text = '<procedure id="X">\n  <sql-query>SELECT * FROM t WHERE a < b</sql-query>\n</procedure>';
    const { context, entries } = complete(text, text.indexOf('< b') + 1);
    expect(context.kind).toBe('text');
    expect(entries.some((entry) => entry.label === 'procedure-call')).toBe(false);
  });

  it('属性名上下文排除已出现属性', () => {
    const { context, entries } = complete('<procedure><sql-update id="x" ');
    expect(context.kind).toBe('attribute');
    expect(has(entries, 'datasource')).toBe(true);
    expect(has(entries, 'result')).toBe(true);
    expect(has(entries, 'id')).toBe(false);
  });

  it('未知标签只给兜底属性', () => {
    const { entries } = complete('<procedure><custom-tag ');
    expect(entries.length).toBeGreaterThan(0);
    expect(entries.every((entry) => entry.sortText.startsWith('8_'))).toBe(true);
  });

  it('feature 上下文', () => {
    const { context, entries } = complete('<procedure><sql-update result.');
    expect(context.kind).toBe('attribute');
    expect((context as { featurePrefix: string | null }).featurePrefix).toBe('');
    expect(has(entries, 'int')).toBe(true);
    expect(has(entries, 'body-text')).toBe(true);
    const entry = entries.find((item) => item.label === 'int')!;
    expect(entry.range?.start).toBeGreaterThan(context.kind === 'attribute' ? context.replaceRange.start : 0);
  });

  it('枚举值上下文与多值段替换', () => {
    const single = complete('<procedure><sql-update propagation="');
    expect(single.context.kind).toBe('value');
    expect(single.entries.filter((entry) => ['REQUIRED', 'NESTED'].includes(entry.label))).toHaveLength(2);

    const multiText = '<procedure><sql-update databases="mysql,';
    const multi = complete(multiText);
    expect(multi.context.kind).toBe('value');
    if (multi.context.kind === 'value') {
      expect(multi.context.replaceRange).toEqual({ start: multiText.length, end: multiText.length });
    }
    expect(multi.entries.some((entry) => entry.label === 'oracle')).toBe(true);
  });
});

describe('补全装配', () => {
  it('SQL 区域提供关键字，非 SQL 区域不提供', () => {
    const sqlText = '<procedure><sql-update>\n  sel';
    const sql = complete(sqlText);
    expect(sql.context.kind).toBe('text');
    expect(sql.context.kind === 'text' && sql.context.sqlRegion).toBe(true);
    const select = sql.entries.find((entry) => entry.label === 'SELECT')!;
    expect(select).toBeTruthy();
    expect(select.range).toEqual({ start: sqlText.length - 3, end: sqlText.length });

    const plain = complete('<procedure>\n  sel');
    expect(plain.entries.some((entry) => entry.label === 'SELECT')).toBe(false);
  });

  it('动态 SQL 标签内仍识别为 SQL 区域', () => {
    const text = '<procedure><sql-update><if test="$x">\n sel';
    const { context, entries } = complete(text);
    expect(context.kind === 'text' && context.sqlRegion).toBe(true);
    expect(has(entries, 'SELECT')).toBe(true);
  });

  it('变量补全区分占位符与短变量', () => {
    const placeholder = complete('<procedure><sql-update>${');
    expect(placeholder.context.kind).toBe('text');
    expect(has(placeholder.entries, 'lock')).toBe(true);
    expect(placeholder.entries.find((entry) => entry.label === 'lock')?.insertText).toBe('lock}');

    const short = complete('<procedure><sql-update>$lo');
    expect(short.entries.find((entry) => entry.label === 'lock')?.insertText).toBe('lock');
  });

  it('script-segment 正文中的占位符变量补全', () => {
    const text = '<procedure id="X">\n    <script-segment id="S">SELECT ${co</script-segment>\n</procedure>\n';
    const offset = text.indexOf('${co') + '${co'.length;
    const { context, entries } = complete(text, offset);
    expect(context.kind).toBe('text');
    expect(has(entries, 'context')).toBe(true);
  });

  it('refid 值与调用目标参数来自索引', () => {
    const target = indexFile(
      'file:///b.xml',
      0,
      '<procedure id="B" author="bob" timeout.int="10"/>',
    );
    const store = new IndexStore();
    store.setBase(buildSnapshot([target], 1));

    const refid = complete('<procedure><procedure-call refid="', undefined, store);
    expect(refid.context.kind).toBe('value');
    expect(has(refid.entries, 'B')).toBe(true);
    expect(refid.entries.find((entry) => entry.label === 'B')?.detail).toContain('定义');

    const params = complete('<procedure><procedure-call refid="B" ', undefined, store);
    expect(params.context.kind).toBe('attribute');
    expect(has(params.entries, 'author')).toBe(true);
    expect(has(params.entries, 'timeout')).toBe(true);
    expect(params.entries.some((entry) => entry.detail?.includes('目标 B'))).toBe(true);
  });

  it('注释内不产生上下文', () => {
    const text = '<procedure><!-- <sql- --></procedure>';
    const offset = text.indexOf('sql-') + 4;
    const { context, entries } = complete(text, offset);
    expect(context.kind).toBe('none');
    expect(entries).toEqual([]);
  });
});
