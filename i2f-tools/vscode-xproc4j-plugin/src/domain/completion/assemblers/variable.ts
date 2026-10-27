import type { XProcDocumentModel } from '../../types';
import type { CompletionEntry, VariablePrefix } from '../text-edit';
import type { CompletionRuntime } from './runtime';

export function assembleVariables(
  prefix: VariablePrefix,
  offset: number,
  model: XProcDocumentModel,
  runtime: CompletionRuntime,
): CompletionEntry[] {
  const range = { start: prefix.replaceStart, end: offset };
  const suffix = prefix.form === 'placeholder' && prefix.needsClosingBrace ? '}' : '';
  const entries: CompletionEntry[] = [];
  const seen = new Set<string>();

  for (const name of model.variableNames) {
    if (!name || seen.has(name)) {
      continue;
    }
    seen.add(name);
    entries.push({
      label: name,
      kind: 'variable',
      insertText: `${name}${suffix}`,
      filterText: name,
      sortText: `4_0_${name}`,
      detail: '变量 · 当前文件',
      documentation: `当前文件中已出现的变量 ${name}`,
      range,
    });
  }

  if (runtime.builtinVariables) {
    for (const name of runtime.catalog.getBuiltinVariables()) {
      if (seen.has(name)) {
        continue;
      }
      seen.add(name);
      entries.push({
        label: name,
        kind: 'variable',
        insertText: `${name}${suffix}`,
        filterText: name,
        sortText: `5_0_${name}`,
        detail: '变量 · 内置',
        documentation: `XProc4J 内置变量 ${name}`,
        range,
      });
    }
  }
  return entries;
}
