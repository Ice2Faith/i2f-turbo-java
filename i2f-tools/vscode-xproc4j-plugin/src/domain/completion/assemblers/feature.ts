import type { CompletionContext } from '../context';
import type { CompletionEntry } from '../text-edit';
import type { CompletionRuntime } from './runtime';

export function assembleFeatures(
  context: Extract<CompletionContext, { kind: 'attribute' }>,
  runtime: CompletionRuntime,
): CompletionEntry[] {
  const attributeName = context.prefix;
  const dot = attributeName.lastIndexOf('.');
  if (dot < 0) {
    return [];
  }
  const chain = attributeName.slice(0, dot).split('.').slice(1);
  const already = new Set(chain);
  const known = runtime.catalog.getKnownAttribute(context.baseName);
  const preferred = new Set(known?.features ?? []);
  const range = { start: context.replaceRange.start + dot + 1, end: context.replaceRange.end };

  return runtime.catalog
    .getFeatures()
    .filter((feature) => !already.has(feature.name))
    .map((feature) => ({
      label: feature.name,
      kind: 'property' as const,
      insertText: feature.name,
      filterText: feature.name,
      sortText: `${preferred.has(feature.name) ? '2_0' : '2_1'}_${feature.name}`,
      detail: `修饰符 · ${feature.group || 'feature'}`,
      documentation: feature.description || `属性修饰符 ${feature.name}`,
      range,
    }));
}
