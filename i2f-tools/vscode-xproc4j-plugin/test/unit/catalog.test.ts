import { describe, expect, it } from 'vitest';
import { getCatalog } from '../../src/domain/metadata/catalog';
import { BUILTIN_VARIABLES } from '../../src/domain/metadata/builtin-variables';

const catalog = getCatalog();

describe('元数据目录', () => {
  it('规模与构建期报告一致', () => {
    expect(catalog.getTags()).toHaveLength(111);
    expect(catalog.getAttributes()).toHaveLength(89);
    expect(catalog.getFeatures()).toHaveLength(58);
    expect(catalog.getSnippets()).toHaveLength(90);
    expect(catalog.getBuiltinVariables()).toHaveLength(42);
  });

  it('内置变量唯一且包含 lock', () => {
    expect(new Set(BUILTIN_VARIABLES).size).toBe(BUILTIN_VARIABLES.length);
    expect(BUILTIN_VARIABLES).toContain('lock');
  });

  it('标签分类、开放属性与动态标签可用', () => {
    expect(catalog.getTag('procedure')?.category).toBe('root');
    expect(catalog.getTag('procedure')?.openAttributes).toBe(true);
    expect(catalog.isOpenTag('procedure')).toBe(true);
    expect(catalog.isOpenTag('sql-update')).toBe(false);

    expect(catalog.isKnownTag('sql-update')).toBe(true);
    expect(catalog.isKnownTag('not-a-tag')).toBe(false);
    expect(catalog.getKnownAttribute('datasource')?.name).toBe('datasource');
    expect(catalog.isKnownAttribute('xproc4j:unknown')).toBe(false);

    expect(catalog.getDynamicTags().size).toBe(14);
    expect(catalog.isDynamicTag('foreach')).toBe(true);
    expect(catalog.isDynamicTag('sql-update')).toBe(false);
  });
});

describe('注入与枚举', () => {
  it('正文注入覆盖 SQL/Java/JS/Shell 与仅变量标签', () => {
    expect(catalog.getBodyInjection('sql-update')?.id).toBe('sql');
    expect(catalog.getBodyInjection('lang-eval-java')?.id).toBe('java');
    expect(catalog.getBodyInjection('lang-eval-js')?.id).toBe('javascript');
    expect(catalog.getBodyInjection('lang-shell')?.id).toBe('shell');
    expect(catalog.getBodyInjection('lang-eval')?.id).toBe('dsl-vars-only');
    expect(catalog.getBodyInjection('lang-body')).toBeUndefined();
    expect(catalog.getBodyInjection('script-segment')).toBeUndefined();
  });

  it('_lang 动态映射只包含 lang-string/lang-render', () => {
    expect(catalog.getLangDynamic('lang-string')?.sql?.languageId).toBe('sql');
    expect(catalog.getLangDynamic('lang-render')?.typescript?.scope).toBe('source.ts');
    expect(catalog.getLangDynamic('sql-update')).toBeUndefined();
  });

  it('属性枚举、布尔值与分隔符', () => {
    expect(catalog.getEnumsForAttribute('propagation')).toHaveLength(7);
    expect(catalog.getEnumsForAttribute('time-unit')).toContain('SECONDS');
    expect(catalog.getEnumsForAttribute('jdbc-type')).toHaveLength(39);
    expect(catalog.getEnumsForAttribute('method')).toHaveLength(21);
    expect(catalog.getEnumsForAttribute('await')).toEqual(['true', 'false']);
    expect(catalog.getEnumsForAttribute('datasource')).toContain('primary');

    expect(catalog.getSeparatorsForAttribute('databases')).toEqual([',', ';', '|', '/']);
    expect(catalog.getSeparatorsForAttribute('datasources')).toEqual([',', ';']);
    expect(catalog.getSeparatorsForAttribute('_lang')).toEqual([',', ';', '|', '/']);
    expect(catalog.getSeparatorsForAttribute('id')).toBeUndefined();
  });

  it('SQL 关键字库非空且包含常用词', () => {
    const keywords = catalog.getSqlKeywords();
    expect(keywords.length).toBeGreaterThan(50);
    expect(keywords).toContain('SELECT');
    expect(catalog.getSqlFunctions()).toContain('COUNT');
  });
});

describe('Snippets 元数据', () => {
  it('模板前缀保留 <tag 风格且包含 procedure 骨架', () => {
    const snippets = catalog.getSnippets();
    expect(snippets.filter((item) => item.prefix === '<procedure')).toHaveLength(1);
    const query = snippets.find((item) => item.prefix === '<sql-query-list');
    expect(query?.body).toContain('sql-query-list');
  });
});
