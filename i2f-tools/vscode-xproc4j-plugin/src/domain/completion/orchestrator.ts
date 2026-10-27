import type { XProcDocumentModel } from '../types';
import type { CompletionContext } from './context';
import { assembleAttributes } from './assemblers/attribute';
import { assembleSqlKeywords } from './assemblers/sql-keyword';
import { assembleTags } from './assemblers/tag';
import { assembleValues } from './assemblers/value';
import { assembleVariables } from './assemblers/variable';
import type { CompletionRuntime } from './assemblers/runtime';
import { findVariablePrefix } from './text-edit';
import type { CompletionEntry } from './text-edit';

export interface AssembleOptions {
  isCancelled?: () => boolean;
  onError?: (assembler: string, error: unknown) => void;
}

const MAX_ENTRIES = 1000;

function guard(
  name: string,
  options: AssembleOptions,
  run: () => CompletionEntry[],
): CompletionEntry[] {
  if (options.isCancelled?.()) {
    return [];
  }
  try {
    return run();
  } catch (error) {
    options.onError?.(name, error);
    return [];
  }
}

function dedupe(entries: CompletionEntry[]): CompletionEntry[] {
  const seen = new Set<string>();
  const result: CompletionEntry[] = [];
  for (const entry of entries) {
    const key = `${entry.kind}\u0000${entry.label}\u0000${entry.insertText}`;
    if (seen.has(key)) {
      continue;
    }
    seen.add(key);
    result.push(entry);
    if (result.length >= MAX_ENTRIES) {
      break;
    }
  }
  return result;
}

export function assembleCompletions(
  context: CompletionContext,
  text: string,
  offset: number,
  model: XProcDocumentModel,
  runtime: CompletionRuntime,
  options: AssembleOptions = {},
): CompletionEntry[] {
  if (context.kind === 'none') {
    return [];
  }

  if (context.kind === 'tag') {
    return guard('tag', options, () => assembleTags(context, model, runtime));
  }

  if (context.kind === 'attribute') {
    return guard('attribute', options, () => assembleAttributes(context, model, runtime));
  }

  if (context.kind === 'value') {
    const entries = guard('value', options, () => assembleValues(context, model, runtime));
    const variablePrefix = findVariablePrefix(text, offset, context.valueRange.start);
    if (variablePrefix) {
      entries.push(
        ...guard('variable', options, () =>
          assembleVariables(variablePrefix, offset, model, runtime),
        ),
      );
    }
    return dedupe(entries);
  }

  const entries = guard('sql-keyword', options, () => assembleSqlKeywords(context, runtime));
  if (context.variablePrefix) {
    entries.push(
      ...guard('variable', options, () =>
        assembleVariables(context.variablePrefix!, offset, model, runtime),
      ),
    );
  }
  return dedupe(entries);
}
