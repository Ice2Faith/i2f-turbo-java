import type { CompletionContext } from '../context';
import type { CompletionEntry } from '../text-edit';
import type { CompletionRuntime } from './runtime';

export function assembleSqlKeywords(
  context: Extract<CompletionContext, { kind: 'text' }>,
  runtime: CompletionRuntime,
): CompletionEntry[] {
  if (!context.sqlRegion || !runtime.sqlKeywords || context.variablePrefix || !context.prefix) {
    return [];
  }
  const range = context.replaceRange;
  const entries: CompletionEntry[] = [];
  const seen = new Set<string>();
  for (const keyword of runtime.catalog.getSqlKeywords()) {
    if (seen.has(keyword)) {
      continue;
    }
    seen.add(keyword);
    entries.push({
      label: keyword,
      kind: 'keyword',
      insertText: keyword,
      filterText: keyword,
      sortText: `6_0_${keyword}`,
      detail: 'SQL 关键字',
      documentation: `SQL 关键字 ${keyword}`,
      range,
    });
  }
  for (const fn of runtime.catalog.getSqlFunctions()) {
    if (seen.has(fn)) {
      continue;
    }
    seen.add(fn);
    entries.push({
      label: fn,
      kind: 'keyword',
      insertText: fn,
      filterText: fn,
      sortText: `6_1_${fn}`,
      detail: 'SQL 函数',
      documentation: `SQL 函数 ${fn}`,
      range,
    });
  }
  return entries.slice(0, 200);
}
