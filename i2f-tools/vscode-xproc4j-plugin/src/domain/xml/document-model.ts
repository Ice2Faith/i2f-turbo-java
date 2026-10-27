import type { XProcDocumentModel } from '../types';
import { scanXml } from './scanner';
import { collectVariableNames, extractVariables } from '../semantics/variable-extractor';
import { getCatalog } from '../metadata/catalog';

export function buildDocumentModel(uri: string, version: number, text: string): XProcDocumentModel {
  const scan = scanXml(text);
  const root = scan.rootIndex >= 0 ? scan.elements[scan.rootIndex] : undefined;
  const catalog = getCatalog();
  const occurrences = extractVariables(
    text,
    scan,
    (name) => catalog.isKnownTag(name),
    (name) => catalog.isKnownAttribute(name),
    (name) => catalog.isOpenTag(name),
  );

  const procedureIndexes: number[] = [];
  const refidValues: Array<{ value: string; range: import('../types').TextRange }> = [];
  for (let i = 0; i < scan.elements.length; i++) {
    const element = scan.elements[i]!;
    if (element.name === 'procedure' || element.name === 'script-segment') {
      procedureIndexes.push(i);
    }
    for (const attribute of element.attributes) {
      if (attribute.baseName === 'refid' && attribute.value.trim()) {
        refidValues.push({ value: attribute.value.trim(), range: attribute.valueRange });
      }
    }
  }

  return {
    uri,
    version,
    textLength: text.length,
    isXProc: root?.name === 'procedure',
    rootTag: root?.name ?? null,
    rootTagOffset: root?.nameRange.start ?? -1,
    scan,
    procedureIndexes,
    variableNames: collectVariableNames(occurrences),
    refidValues,
    builtAt: Date.now(),
  };
}

export class DocumentModelCache {
  private readonly cache = new Map<string, XProcDocumentModel>();
  private readonly maxEntries = 64;

  get(uri: string, version: number, text: string): XProcDocumentModel {
    const existing = this.cache.get(uri);
    if (existing && existing.version === version && existing.textLength === text.length) {
      return existing;
    }
    const model = buildDocumentModel(uri, version, text);
    if (this.cache.size >= this.maxEntries && !this.cache.has(uri)) {
      const oldest = this.cache.keys().next().value;
      if (oldest !== undefined) {
        this.cache.delete(oldest);
      }
    }
    this.cache.set(uri, model);
    return model;
  }

  peek(uri: string): XProcDocumentModel | undefined {
    return this.cache.get(uri);
  }

  invalidate(uri: string): void {
    this.cache.delete(uri);
  }

  clear(): void {
    this.cache.clear();
  }
}
