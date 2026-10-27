import type { TextRange } from '../types';

export type CompletionKind =
  | 'class'
  | 'property'
  | 'snippet'
  | 'reference'
  | 'enum'
  | 'constant'
  | 'value'
  | 'function'
  | 'variable'
  | 'keyword'
  | 'text';

export interface TextEdit {
  range: TextRange;
  newText: string;
}

export interface CompletionEntry {
  label: string;
  kind: CompletionKind;
  insertText: string;
  filterText?: string;
  sortText: string;
  detail?: string;
  documentation?: string;
  insertTextFormat?: 'plain' | 'snippet';
  /** 候选自带替换范围；缺省使用上下文的 replaceRange。 */
  range?: TextRange;
}

export type VariablePrefix =
  | {
      form: 'placeholder';
      opener: '${' | '$!{' | '#{' | '#!{';
      namePrefix: string;
      replaceStart: number;
      needsClosingBrace: boolean;
    }
  | { form: 'short'; namePrefix: string; replaceStart: number };

const PLACEHOLDER = /([#$]!?\{)([A-Za-z0-9_$.-]*)$/;
const SHORT_VARIABLE = /\$([A-Za-z0-9_]*)$/;

export function findVariablePrefix(text: string, offset: number, lowerBound = 0): VariablePrefix | null {
  const from = Math.max(lowerBound, offset - 200);
  const slice = text.slice(from, offset);
  const placeholder = PLACEHOLDER.exec(slice);
  if (placeholder) {
    const openerStart = from + placeholder.index;
    return {
      form: 'placeholder',
      opener: placeholder[1] as '${' | '$!{' | '#{' | '#!{',
      namePrefix: placeholder[2] ?? '',
      replaceStart: openerStart + placeholder[1]!.length,
      needsClosingBrace: text[offset] !== '}',
    };
  }
  const short = SHORT_VARIABLE.exec(slice);
  if (short) {
    return {
      form: 'short',
      namePrefix: short[1] ?? '',
      replaceStart: offset - (short[1]?.length ?? 0),
    };
  }
  return null;
}

/** 多值属性的当前段；光标位于分隔符上时视为前一段末尾。 */
export function valueSegment(
  text: string,
  valueRange: TextRange,
  offset: number,
  separators: readonly string[],
): TextRange {
  const clamped = Math.max(valueRange.start, Math.min(offset, valueRange.end));
  let start = valueRange.start;
  let end = valueRange.end;
  for (let i = clamped - 1; i >= valueRange.start; i--) {
    if (separators.includes(text[i]!)) {
      start = i + 1;
      break;
    }
  }
  for (let i = clamped; i < valueRange.end; i++) {
    if (separators.includes(text[i]!)) {
      end = i;
      break;
    }
  }
  while (start < end && /\s/.test(text[start]!)) {
    start++;
  }
  while (end > start && /\s/.test(text[end - 1]!)) {
    end--;
  }
  return { start, end };
}

export function currentWord(text: string, offset: number, lowerBound = 0): { word: string; range: TextRange } {
  const from = Math.max(lowerBound, offset - 200);
  const slice = text.slice(from, offset);
  const match = /[A-Za-z_][A-Za-z0-9_]*$/.exec(slice);
  if (!match) {
    return { word: '', range: { start: offset, end: offset } };
  }
  return {
    word: match[0],
    range: { start: offset - match[0].length, end: offset },
  };
}
