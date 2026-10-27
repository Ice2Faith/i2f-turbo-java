import type { LineRange, TextRange, XmlElementModel } from '../types';
import { LineIndex } from '../xml/line-index';
import { scanXml } from '../xml/scanner';
import { RESERVED_PARAM_NAMES } from '../metadata/builtin-attributes';
import type { FileIndex, IndexedEntry, IndexedParam, IndexedRef } from './index-model';
import { normalizeUriKey } from './uri-key';

const ENTRY_TAGS = new Set(['procedure', 'script-segment']);
const INDEX_MAX_ELEMENTS = 100_000;

function trimRange(text: string, range: TextRange): TextRange {
  let start = range.start;
  let end = range.end;
  while (start < end && /\s/.test(text[start]!)) {
    start++;
  }
  while (end > start && /\s/.test(text[end - 1]!)) {
    end--;
  }
  return { start, end };
}

function compareParams(a: IndexedParam, b: IndexedParam): number {
  return a.name.localeCompare(b.name);
}

function findParentEntryIndex(elements: XmlElementModel[], element: XmlElementModel): number {
  let index = element.parentIndex;
  while (index >= 0) {
    if (ENTRY_TAGS.has(elements[index]!.name)) {
      return index;
    }
    index = elements[index]!.parentIndex;
  }
  return -1;
}

export function indexFile(
  uri: string,
  version: number,
  text: string,
): FileIndex {
  const normalizedUri = normalizeUriKey(uri);
  const scan = scanXml(text, { maxElements: INDEX_MAX_ELEMENTS });
  const root = scan.rootIndex >= 0 ? scan.elements[scan.rootIndex] : undefined;
  const rootTag = root?.name ?? null;

  if (rootTag !== 'procedure') {
    return {
      uri: normalizedUri,
      version,
      entries: [],
      refs: [],
      rootTag,
      skippedReason: scan.truncated ? 'parse-error' : 'not-xproc',
    };
  }

  const lineIndex = new LineIndex(text);
  const linesOf = (range: TextRange): LineRange => lineIndex.lineRange(range);
  const entryIndexById = new Map<number, IndexedEntry>();
  const entries: IndexedEntry[] = [];
  const refs: IndexedRef[] = [];

  for (let i = 0; i < scan.elements.length; i++) {
    const element = scan.elements[i]!;
    let idAttribute: XmlElementModel['attributes'][number] | undefined;
    let refidAttribute: XmlElementModel['attributes'][number] | undefined;

    for (const attribute of element.attributes) {
      if (attribute.baseName === 'id') {
        idAttribute = attribute;
      } else if (attribute.baseName === 'refid') {
        refidAttribute = attribute;
      }
    }

    if (refidAttribute) {
      const range = trimRange(text, refidAttribute.valueRange);
      const refid = text.slice(range.start, range.end);
      if (refid) {
        refs.push({
          refid,
          uri: normalizedUri,
          range: linesOf(range),
          tagName: element.name,
        });
      }
    }

    if (!ENTRY_TAGS.has(element.name) || !idAttribute) {
      continue;
    }
    const idRange = trimRange(text, idAttribute.valueRange);
    const id = text.slice(idRange.start, idRange.end);
    if (!id) {
      continue;
    }

    const params = new Map<string, Set<string>>();
    for (const attribute of element.attributes) {
      if (RESERVED_PARAM_NAMES.has(attribute.baseName)) {
        continue;
      }
      const features = params.get(attribute.baseName) ?? new Set<string>();
      for (const feature of attribute.features) {
        features.add(feature);
      }
      params.set(attribute.baseName, features);
    }

    const parentIndex = findParentEntryIndex(scan.elements, element);
    const parentEntry = parentIndex >= 0 ? entryIndexById.get(parentIndex) : undefined;
    const entry: IndexedEntry = {
      id,
      kind: element.name === 'procedure' ? 'procedure' : 'script-segment',
      uri: normalizedUri,
      range: linesOf({ start: element.startOffset, end: element.endOffset }),
      idRange: linesOf(idRange),
      parentId: parentEntry?.id,
      params: [...params.entries()]
        .map(([name, features]) => ({ name, features: [...features].sort() }))
        .sort(compareParams),
    };
    entries.push(entry);
    entryIndexById.set(i, entry);
  }

  return {
    uri: normalizedUri,
    version,
    entries,
    refs,
    rootTag,
    skippedReason: scan.truncated ? 'parse-error' : undefined,
  };
}
