import type { TextRange, XProcDocumentModel } from '../types';
import { bodyRange } from '../xml/scanner';

/**
 * lang-eval* 正文的字符串字面量：单引号或双引号包裹，不含跨行转义语义。
 * 按用户要求使用 `'[^']*'` 与 `"[^"]*"` 的原始正则语义，不解析转义；
 * 因此未闭合引号会吞到下一个同类型引号，包括跨行的情况（已知限制）。
 */
const STRING_SOURCE = /'[^']*'|"[^"]*"/g;
/** 数值字面量：整数或小数。按词边界匹配，避免命中标识符中的数字。 */
const NUMBER_SOURCE = /\b\d+(?:\.\d+)?\b/g;

/**
 * 字符串字面量正则（供关键字收集器复用，避免关键字与字符串装饰叠色）。
 * 带 g 标志；matchAll 不受 lastIndex 影响，可安全共享。
 */
export const EVAL_STRING_PATTERN = STRING_SOURCE;

export interface EvalStringLiteralRange extends TextRange {
  /** 该字面量使用的引号类型。 */
  quote: '"' | "'";
}

export interface EvalLiteralRanges {
  /** 数值字面量范围；已排除位于字符串字面量内部的数字。 */
  numbers: TextRange[];
  /** 字符串字面量范围（含两侧引号）。 */
  strings: EvalStringLiteralRange[];
}

/**
 * 在 lang-eval* 正文内收集数值与字符串字面量。
 *
 * 与关键字一样按正则语义扫描，不做 Java/JavaScript/SQL 词法分析；
 * 注释中的数字与字符串同样会被匹配（已知限制）。
 */
export function collectEvalLiteralRanges(
  model: XProcDocumentModel,
  text: string,
): EvalLiteralRanges {
  if (!model.isXProc) {
    return { numbers: [], strings: [] };
  }
  const numbers: TextRange[] = [];
  const strings: EvalStringLiteralRange[] = [];
  for (const element of model.scan.elements) {
    if (!element.name.startsWith('lang-eval')) {
      continue;
    }
    const body = bodyRange(element);
    if (!body || body.start >= body.end) {
      continue;
    }
    const segment = text.slice(body.start, body.end);

    // 先收集字符串范围，作为数字的保护区，避免 `"12"` 中的 12 被染成数值色。
    const protectedRanges: TextRange[] = [];
    for (const match of segment.matchAll(STRING_SOURCE)) {
      const literal = match[0] ?? '';
      const start = body.start + (match.index ?? 0);
      protectedRanges.push({ start, end: start + literal.length });
      strings.push({ start, end: start + literal.length, quote: literal[0] as '"' | "'" });
    }
    for (const match of segment.matchAll(NUMBER_SOURCE)) {
      const literal = match[0] ?? '';
      const start = body.start + (match.index ?? 0);
      const end = start + literal.length;
      if (protectedRanges.some((range) => start >= range.start && end <= range.end)) {
        continue;
      }
      numbers.push({ start, end });
    }
  }
  return { numbers, strings };
}
