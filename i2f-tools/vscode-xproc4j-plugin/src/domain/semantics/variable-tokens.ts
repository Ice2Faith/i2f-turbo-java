import type { TextRange, XProcDocumentModel } from '../types';
import type { MetadataCatalog } from '../metadata/catalog';
import { extractVariables } from './variable-extractor';

export interface SemanticTokenEntry {
  range: TextRange;
  tokenType: number;
  tokenModifiers: number;
}

export const TOKEN_TYPES = ['variable', 'xprocRefid'] as const;
export const TOKEN_MODIFIERS = ['xprocEmptyPlaceholder'] as const;
const VARIABLE_TYPE = 0;
const REFID_TYPE = 1;
const EMPTY_MODIFIER = 1;

export function buildVariableTokens(
  model: XProcDocumentModel,
  text: string,
  catalog: MetadataCatalog,
  resolvedIds?: ReadonlySet<string>,
  includeVariables = true,
): SemanticTokenEntry[] {
  const entries: SemanticTokenEntry[] = [];
  if (includeVariables) {
    const occurrences = extractVariables(
      text,
      model.scan,
      (name) => catalog.isKnownTag(name),
      (name) => catalog.isKnownAttribute(name),
      (name) => catalog.isOpenTag(name),
    );
    for (const occurrence of occurrences) {
      entries.push({
        range: occurrence.range,
        tokenType: VARIABLE_TYPE,
        tokenModifiers: occurrence.empty ? EMPTY_MODIFIER : 0,
      });
    }
  }

  for (const refid of model.refidValues) {
    if (resolvedIds && !resolvedIds.has(refid.value)) {
      continue;
    }
    entries.push({ range: refid.range, tokenType: REFID_TYPE, tokenModifiers: 0 });
  }

  entries.sort((a, b) => a.range.start - b.range.start || a.range.end - b.range.end);
  return entries;
}
