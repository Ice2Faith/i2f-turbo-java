import type { IndexQuery } from '../index/query';
import type { LineRange, TextRange, XProcDocumentModel, XmlAttributeModel, XmlElementModel } from '../types';
import { LineIndex } from '../xml/line-index';

export interface NavigationTarget {
  uri: string;
  range: LineRange;
}

export interface NavigationResult {
  id: string;
  targets: NavigationTarget[];
}

const ENTRY_TAGS = new Set(['procedure', 'script-segment']);

function findAttributeAt(
  model: XProcDocumentModel,
  offset: number,
  baseName: string,
): { element: XmlElementModel; attribute: XmlAttributeModel } | undefined {
  for (const element of model.scan.elements) {
    if (offset < element.startOffset || offset > element.endOffset) {
      continue;
    }
    for (const attribute of element.attributes) {
      if (
        attribute.baseName === baseName &&
        offset >= attribute.valueRange.start &&
        offset <= attribute.valueRange.end
      ) {
        return { element, attribute };
      }
    }
  }
  return undefined;
}

export function refidAtOffset(
  model: XProcDocumentModel,
  offset: number,
): { id: string; range: TextRange } | undefined {
  const found = findAttributeAt(model, offset, 'refid');
  const id = found?.attribute.value.trim() ?? '';
  if (!found || !id) {
    return undefined;
  }
  return { id, range: found.attribute.valueRange };
}

export function declarationIdAtOffset(
  model: XProcDocumentModel,
  offset: number,
): { id: string; element: XmlElementModel; range: TextRange } | undefined {
  const found = findAttributeAt(model, offset, 'id');
  if (!found || !ENTRY_TAGS.has(found.element.name)) {
    return undefined;
  }
  const id = found.attribute.value.trim();
  return id ? { id, element: found.element, range: found.attribute.valueRange } : undefined;
}

function localEntries(model: XProcDocumentModel, text: string, id: string) {
  const index = new LineIndex(text);
  const result: NavigationTarget[] = [];
  for (const element of model.scan.elements) {
    if (!ENTRY_TAGS.has(element.name)) {
      continue;
    }
    const attribute = element.attributes.find((item) => item.baseName === 'id');
    if (!attribute || attribute.value.trim() !== id) {
      continue;
    }
    result.push({ uri: model.uri, range: index.lineRange(attribute.valueRange) });
  }
  return result;
}

function localRefs(model: XProcDocumentModel, text: string, id: string) {
  const index = new LineIndex(text);
  const result: NavigationTarget[] = [];
  for (const element of model.scan.elements) {
    for (const attribute of element.attributes) {
      if (attribute.baseName === 'refid' && attribute.value.trim() === id) {
        result.push({ uri: model.uri, range: index.lineRange(attribute.valueRange) });
      }
    }
  }
  return result;
}

function dedupe(targets: NavigationTarget[]): NavigationTarget[] {
  const seen = new Set<string>();
  return targets.filter((target) => {
    const key = `${target.uri}:${target.range.start.line}:${target.range.start.character}:${target.range.end.line}:${target.range.end.character}`;
    if (seen.has(key)) {
      return false;
    }
    seen.add(key);
    return true;
  });
}

export function findDefinitionsAt(
  text: string,
  offset: number,
  model: XProcDocumentModel,
  index?: IndexQuery,
): NavigationResult | undefined {
  const refid = refidAtOffset(model, offset);
  if (!refid) {
    return undefined;
  }
  const local = localEntries(model, text, refid.id);
  const external = (index?.findDefinitions(refid.id, model.uri) ?? [])
    .filter((entry) => entry.uri !== model.uri)
    .map((entry) => ({ uri: entry.uri, range: entry.idRange }));
  return { id: refid.id, targets: dedupe([...local, ...external]) };
}

export function findReferencesAt(
  text: string,
  offset: number,
  model: XProcDocumentModel,
  index: IndexQuery | undefined,
  includeDeclaration: boolean,
): NavigationResult | undefined {
  const declaration = declarationIdAtOffset(model, offset);
  if (!declaration) {
    return undefined;
  }
  const local = localRefs(model, text, declaration.id);
  const external = (index?.findReferences(declaration.id) ?? [])
    .filter((ref) => ref.uri !== model.uri)
    .map((ref) => ({ uri: ref.uri, range: ref.range }));

  const targets = [...local, ...external];
  if (includeDeclaration) {
    targets.unshift(...localEntries(model, text, declaration.id));
    for (const entry of index?.findDefinitions(declaration.id) ?? []) {
      if (entry.uri !== model.uri) {
        targets.push({ uri: entry.uri, range: entry.idRange });
      }
    }
  }
  return { id: declaration.id, targets: dedupe(targets) };
}
