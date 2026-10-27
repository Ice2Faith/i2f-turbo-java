import type { ScanResult, TextRange, VariableOccurrence, XmlElementModel } from '../types';
import { isOffsetProtected } from '../xml/scanner';

const BRACED = /([#$])!?\{([^{}]*)\}/g;
const SHORT = /\$([A-Za-z0-9_]+)/g;

export interface RawVariableOccurrence {
  name: string;
  range: TextRange;
  empty: boolean;
}

/**
 * 从原始文本中提取变量，不依赖 XML 结构。
 *
 * 用于非 XML 文件；XML 文件仍由 {@link extractVariables} 按已知标签/属性过滤。
 */
export function extractRawVariables(text: string): RawVariableOccurrence[] {
  const result: RawVariableOccurrence[] = [];
  for (const match of text.matchAll(BRACED)) {
    const start = match.index ?? 0;
    result.push({
      name: match[2] ?? '',
      range: { start, end: start + match[0].length },
      empty: (match[2] ?? '').trim() === '',
    });
  }
  for (const match of text.matchAll(SHORT)) {
    const start = match.index ?? 0;
    // 跳过 ${...} / $!{...} 中的 $ 前缀，避免重复。
    const next = text[start + 1];
    if (next === '{' || next === '!') {
      continue;
    }
    result.push({
      name: match[1] ?? '',
      range: { start, end: start + match[0].length },
      empty: false,
    });
  }
  result.sort((a, b) => a.range.start - b.range.start);
  return result;
}

function attributeAt(scan: ScanResult, offset: number) {
  for (const element of scan.elements) {
    if (offset < element.startOffset || offset > element.endOffset) {
      continue;
    }
    for (const attribute of element.attributes) {
      if (offset >= attribute.valueRange.start && offset <= attribute.valueRange.end) {
        return { element, attribute };
      }
    }
  }
  return undefined;
}

function elementAt(scan: ScanResult, offset: number): XmlElementModel | undefined {
  let best: XmlElementModel | undefined;
  for (const element of scan.elements) {
    if (offset >= element.startOffset && offset <= element.endOffset) {
      if (!best || element.depth >= best.depth) {
        best = element;
      }
    }
  }
  return best;
}

/**
 * 提取变量出现位置。D-005：只保留已知标签内、且（属性场景）已知属性或开放属性内的变量。
 *
 * 只依赖扫描结果而不是完整文档模型，避免构建模型时产生循环依赖。
 */
export function extractVariables(
  text: string,
  scan: ScanResult,
  isKnownTag: (name: string) => boolean,
  isKnownAttribute: (name: string) => boolean,
  isOpenTag: (name: string) => boolean,
): VariableOccurrence[] {
  const result: VariableOccurrence[] = [];
  const ranges = scan.protectedRanges;

  for (const occurrence of extractRawVariables(text)) {
    if (isOffsetProtected(ranges, occurrence.range.start)) {
      continue;
    }
    const attr = attributeAt(scan, occurrence.range.start);
    if (attr) {
      if (!isKnownTag(attr.element.name)) {
        continue;
      }
      if (!isKnownAttribute(attr.attribute.baseName) && !isOpenTag(attr.element.name)) {
        continue;
      }
      result.push({ ...occurrence, attribute: true });
      continue;
    }
    const element = elementAt(scan, occurrence.range.start);
    if (!element || !isKnownTag(element.name)) {
      continue;
    }
    result.push({ ...occurrence, attribute: false });
  }

  return result;
}

export function collectVariableNames(occurrences: VariableOccurrence[]): string[] {
  return [...new Set(occurrences.map((item) => item.name).filter(Boolean))].sort();
}
