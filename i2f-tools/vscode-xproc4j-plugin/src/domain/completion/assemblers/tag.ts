import type { XProcDocumentModel } from '../../types';
import type { CompletionContext } from '../context';
import { isSqlRegion } from '../context';
import type { CompletionEntry } from '../text-edit';
import type { CompletionRuntime } from './runtime';

function findContainer(model: XProcDocumentModel, tagStart: number, offset: number) {
  let best: XProcDocumentModel['scan']['elements'][number] | undefined;
  for (const element of model.scan.elements) {
    if (element.startOffset >= tagStart || element.endOffset < offset) {
      continue;
    }
    if (!best || element.depth >= best.depth) {
      best = element;
    }
  }
  return best;
}

export function assembleTags(
  context: Extract<CompletionContext, { kind: 'tag' }>,
  model: XProcDocumentModel,
  runtime: CompletionRuntime,
): CompletionEntry[] {
  const container = findContainer(model, context.replaceRange.start - 1, context.replaceRange.start);
  const sqlRegion = isSqlRegion(container, model, runtime.catalog);
  return runtime.catalog.getTags().map((tag) => {
    const dynamic = runtime.catalog.isDynamicTag(tag.name);
    const sortText = sqlRegion
      ? dynamic
        ? `0_1_${tag.name}`
        : `0_2_${tag.name}`
      : dynamic
        ? `0_9_${tag.name}`
        : `0_2_${tag.name}`;
    return {
      label: tag.name,
      kind: 'class',
      insertText: tag.name,
      filterText: tag.name,
      sortText,
      detail: `标签 · ${tag.category}`,
      documentation: [
        `XProc4J 标签 <${tag.name}>`,
        `分类：${tag.category}`,
        dynamic ? '仅在 SQL 标签正文中有效。' : '插入后请补充属性。',
      ].join('\n'),
    };
  });
}
