import type { TextRange, XProcDocumentModel } from '../types';

/**
 * 收集 XML 属性值的原文范围（不含引号）。
 *
 * 所有属性（已知、开放参数、未知属性）统一着色，因此不经过 catalog 过滤；
 * 无值布尔属性与空值属性不产生范围。返回偏移由适配层换算为编辑器位置。
 */
export function collectAttributeValueRanges(
  model: XProcDocumentModel,
): TextRange[] {
  if (!model.isXProc) {
    return [];
  }
  const ranges: TextRange[] = [];
  for (const element of model.scan.elements) {
    for (const attribute of element.attributes) {
      if (attribute.valueRange.end > attribute.valueRange.start) {
        ranges.push(attribute.valueRange);
      }
    }
  }
  return ranges;
}
