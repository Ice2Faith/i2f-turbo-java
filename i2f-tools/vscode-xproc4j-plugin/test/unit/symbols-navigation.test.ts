import { describe, expect, it } from 'vitest';
import { buildDocumentSymbols } from '../../src/domain/symbols/document-symbols';
import { buildFoldingRegions } from '../../src/domain/symbols/folding';
import { renderProcedureTemplate, TemplatePlaceholderError } from '../../src/domain/templates/render';
import { validateFileName, validateProcedureId, normalizeFileName } from '../../src/domain/templates/new-file';
import { findDefinitionsAt, findReferencesAt, refidAtOffset } from '../../src/domain/navigation/navigation';
import { buildDocumentModel } from '../../src/domain/xml/document-model';
import { getCatalog } from '../../src/domain/metadata/catalog';
import { indexFile } from '../../src/domain/index/index-scan';
import { buildSnapshot } from '../../src/domain/index/index-model';
import { IndexStore } from '../../src/domain/index/query';

const catalog = getCatalog();
const TEXT = `<procedure id="P">
  <sql-query-list id="Q">
    <sql-update refid="target"/>
    <custom-node id="unknown"/>
  </sql-query-list>
  <procedure-call refid="P"/>
</procedure>`;
const documentModel = buildDocumentModel('file:///demo.xml', 1, TEXT);

describe('document symbols', () => {
  it('构建标签树并格式化关键属性', () => {
    const symbols = buildDocumentSymbols(documentModel, catalog);
    expect(symbols).toHaveLength(1);
    const root = symbols[0]!;
    expect(root.name).toBe('procedure id=P');
    expect(root.kind).toBe('class');
    expect(root.detail).toBe('根节点');
    expect(root.children[0]!.name).toBe('sql-query-list id=Q');
    expect(root.children[0]!.detail).toBe('SQL');
    expect(root.children[0]!.children.map((item) => item.name)).toEqual([
      'sql-update refid=target',
      'custom-node id=unknown',
    ]);
    expect(root.children[0]!.children[1]!.detail).toBe('未知标签');
    expect(root.children[0]!.children[1]!.kind).toBe('object');
  });
});

describe('folding', () => {
  it('收拢多行元素并保留结束标签行', () => {
    const regions = buildFoldingRegions(TEXT, documentModel);
    expect(regions).toEqual([
      { startLine: 0, endLine: 5, kind: 'region' },
      { startLine: 1, endLine: 3, kind: 'region' },
    ]);
  });

  it('单行与未闭合元素不折叠', () => {
    const text = '<procedure><sql-update></sql-update>\n<sql-query-list>';
    const model = buildDocumentModel('file:///a.xml', 1, text);
    expect(buildFoldingRegions(text, model)).toEqual([]);
  });
});

describe('procedure 模板', () => {
  const template = '<procedure id="{{id}}"><!--\n@author {{author}}\n@date {{date}} @time {{time}}\n--></procedure>\n';

  it('渲染并转义占位符', () => {
    const text = renderProcedureTemplate(template, {
      id: 'a&b',
      author: 'bad--name\r\nnext',
      date: '2026-10-23',
      time: '08:00:00',
      eol: '\n',
    });
    expect(text).toContain('id="a&amp;b"');
    expect(text).toContain('@author bad- -name  next');
    expect(text).toContain('@date 2026-10-23 @time 08:00:00');
    expect(text.endsWith('</procedure>\n')).toBe(true);
  });

  it('CRLF 输出且未知占位符抛错', () => {
    const text = renderProcedureTemplate(template, {
      id: 'p',
      author: '',
      date: '2026-10-23',
      time: '08:00:00',
      eol: '\r\n',
    });
    expect(text).toContain('\r\n');
    expect(text.endsWith('\r\n')).toBe(true);
    expect(() =>
      renderProcedureTemplate('<procedure>{{unknown}}</procedure>', {
        id: 'p',
        author: 'a',
        date: 'd',
        time: 't',
        eol: '\n',
      }),
    ).toThrow(TemplatePlaceholderError);
  });
});

describe('新建文件校验', () => {
  it('id 与文件名规则', () => {
    expect(validateProcedureId('demo').valid).toBe(true);
    expect(validateProcedureId('').message).toBeTruthy();
    expect(validateProcedureId('a/b').valid).toBe(false);
    expect(validateProcedureId('.hidden').valid).toBe(false);

    expect(validateFileName('demo.xml').valid).toBe(true);
    expect(validateFileName('demo').valid).toBe(true);
    expect(normalizeFileName('demo')).toBe('demo.xml');
    expect(validateFileName('demo.txt').valid).toBe(false);
    expect(validateFileName('../demo.xml').valid).toBe(false);
    expect(validateFileName('demo.xml ').valid).toBe(true);
  });
});

describe('refid 导航', () => {
  const refidOffset = TEXT.indexOf('target') + 2;
  const idOffset = TEXT.indexOf('id="P"') + 4;

  it('识别 refid 与声明 id', () => {
    expect(refidAtOffset(documentModel, refidOffset)?.id).toBe('target');
    expect(refidAtOffset(documentModel, idOffset)).toBeUndefined();
  });

  it('同文件定义与跨文件索引合并', () => {
    const other = indexFile('file:///other.xml', 0, '<procedure id="target"/>');
    const store = new IndexStore();
    store.setBase(buildSnapshot([other], 1));
    const result = findDefinitionsAt(TEXT, refidOffset, documentModel, store)!;
    expect(result.id).toBe('target');
    expect(result.targets.map((target) => target.uri)).toEqual(['file:///other.xml']);
  });

  it('引用查找包含声明开关', () => {
    const store = new IndexStore();
    store.setBase(buildSnapshot([indexFile('file:///demo.xml', 0, TEXT)], 1));
    const without = findReferencesAt(TEXT, idOffset, documentModel, store, false)!;
    expect(without.targets.map((target) => target.uri)).toEqual(['file:///demo.xml']);

    const withDeclaration = findReferencesAt(TEXT, idOffset, documentModel, store, true)!;
    expect(withDeclaration.targets.length).toBe(2);
  });
});
