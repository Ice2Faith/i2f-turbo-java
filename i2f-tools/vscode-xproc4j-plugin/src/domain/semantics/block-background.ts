import { getCatalog } from '../metadata/catalog';
import type { TextRange, XProcDocumentModel } from '../types';
import { bodyRange } from '../xml/scanner';

/** 嵌入块类型；颜色由适配层按类型映射，领域层不关心具体色值。 */
export type BlockBackgroundKind = 'sql' | 'eval';

export interface BlockBackgroundRange extends TextRange {
  kind: BlockBackgroundKind;
}

/**
 * 收集需要显示背景色的嵌入块正文范围。
 *
 * SQL 使用黄色背景，lang-eval* 系列（lang-eval、lang-eval-java、lang-eval-ts 等）
 * 使用绿色背景，便于在多行正文中快速识别块类型与边界。
 * 返回的范围按类型合并嵌套/重叠区间（如 sql-query-object 内部的 sql-dialect），
 * 避免同一块区域被重复着色而颜色加深。起止标签行由适配层按整行着色。
 */
export function collectBlockBackgroundRanges(
  model: XProcDocumentModel,
): BlockBackgroundRange[] {
  if (!model.isXProc) {
    return [];
  }
  const catalog = getCatalog();
  const collected: BlockBackgroundRange[] = [];
  for (const element of model.scan.elements) {
    const kind = classifyBlock(element.name, catalog.isSqlBodyTag(element.name));
    if (!kind) {
      continue;
    }
    const body = bodyRange(element);
    if (!body || body.start >= body.end) {
      continue;
    }
    collected.push({ start: body.start, end: body.end, kind });
  }
  return mergeRanges(collected);
}

/** lang-eval* 优先按标签名前缀判定；其余按元数据中的 SQL 正文注入判定。 */
function classifyBlock(
  tagName: string,
  isSqlBody: boolean,
): BlockBackgroundKind | undefined {
  if (tagName.startsWith('lang-eval')) {
    return 'eval';
  }
  return isSqlBody ? 'sql' : undefined;
}

function mergeRanges(ranges: BlockBackgroundRange[]): BlockBackgroundRange[] {
  if (ranges.length <= 1) {
    return ranges;
  }
  const sorted = [...ranges].sort((a, b) => a.start - b.start || a.end - b.end);
  const merged: BlockBackgroundRange[] = [];
  for (const current of sorted) {
    const last = merged[merged.length - 1];
    if (last && last.kind === current.kind && current.start <= last.end) {
      last.end = Math.max(last.end, current.end);
      continue;
    }
    merged.push({ ...current });
  }
  return merged;
}
