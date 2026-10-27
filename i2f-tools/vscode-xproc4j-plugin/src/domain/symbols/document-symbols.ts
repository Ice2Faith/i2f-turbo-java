import type { MetadataCatalog } from '../metadata/catalog';
import type { TagCategory } from '../metadata/types';
import type { TextRange, XProcDocumentModel, XmlElementModel } from '../types';

export type OutlineSymbolKind =
  | 'class'
  | 'function'
  | 'method'
  | 'operator'
  | 'event'
  | 'namespace'
  | 'object';

export interface OutlineSymbol {
  name: string;
  detail: string;
  kind: OutlineSymbolKind;
  range: TextRange;
  selectionRange: TextRange;
  children: OutlineSymbol[];
}

const CATEGORY_DETAIL: Record<TagCategory, string> = {
  root: '根节点',
  fragment: '脚本片段',
  call: '调用',
  flow: '流程',
  sql: 'SQL',
  etl: 'ETL',
  java: 'Java',
  script: '脚本',
  concurrent: '并发',
  io: 'IO',
  assign: '赋值',
  output: '输出',
  log: '日志',
  context: '上下文',
  event: '事件',
  string: '字符串',
  dynamic: '动态 SQL',
  unknown: '未知标签',
};

const KEY_ATTRIBUTES = ['id', 'refid', 'result', 'name', 'test', 'datasource', 'databases', 'value', 'method'];
const CONTROL_TAGS = new Set([
  'lang-if',
  'choose',
  'lang-choose',
  'lang-while',
  'lang-foreach',
  'lang-fori',
  'lang-try',
]);

function symbolKind(element: XmlElementModel, catalog: MetadataCatalog): OutlineSymbolKind {
  if (element.name === 'procedure') {
    return 'class';
  }
  if (element.name === 'script-segment') {
    return 'function';
  }
  if (element.name === 'procedure-call' || element.name === 'function-call' || element.name === 'java-call') {
    return 'method';
  }
  if (element.name.startsWith('sql-') || element.name.startsWith('etl-')) {
    return 'function';
  }
  if (CONTROL_TAGS.has(element.name)) {
    return 'operator';
  }
  if (element.name.startsWith('log-') || element.name === 'debugger' || element.name === 'lang-println' || element.name === 'lang-printf') {
    return 'event';
  }
  if (element.name.startsWith('context-') || element.name.startsWith('event-')) {
    return 'namespace';
  }
  if (!catalog.isKnownTag(element.name)) {
    return 'object';
  }
  if (element.name.startsWith('lang-')) {
    return 'function';
  }
  return 'object';
}

function formatName(element: XmlElementModel): string {
  const parts = [element.name];
  let used = 0;
  for (const key of KEY_ATTRIBUTES) {
    if (used >= 2) {
      break;
    }
    const attribute = element.attributes.find((item) => item.baseName === key);
    if (!attribute) {
      continue;
    }
    const value = attribute.value.length > 40 ? `${attribute.value.slice(0, 39)}…` : attribute.value;
    parts.push(`${key}=${value}`);
    used++;
  }
  const name = parts.join(' ');
  return name.length > 80 ? `${name.slice(0, 79)}…` : name;
}

export function buildDocumentSymbols(
  model: XProcDocumentModel,
  catalog: MetadataCatalog,
): OutlineSymbol[] {
  const symbols: OutlineSymbol[] = [];
  const byIndex = new Map<number, OutlineSymbol>();

  for (let i = 0; i < model.scan.elements.length; i++) {
    const element = model.scan.elements[i]!;
    const tag = catalog.getTag(element.name);
    const detail = tag ? CATEGORY_DETAIL[tag.category] : '未知标签';
    const symbol: OutlineSymbol = {
      name: formatName(element),
      detail: model.scan.truncated ? `${detail} · 已截断` : detail,
      kind: symbolKind(element, catalog),
      range: { start: element.startOffset, end: element.endOffset },
      selectionRange: element.nameRange,
      children: [],
    };
    byIndex.set(i, symbol);
    if (element.parentIndex >= 0) {
      byIndex.get(element.parentIndex)?.children.push(symbol);
    } else {
      symbols.push(symbol);
    }
  }
  return symbols;
}
