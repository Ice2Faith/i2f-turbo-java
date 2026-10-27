import type { TextRange } from '../types';

export interface RootInfo {
  rootTag: string | null;
  rootTagOffset: number;
  rootTagNameRange: TextRange;
  hasDoctype: boolean;
  isProcedure: boolean;
}

const MAX_SCAN = 65_536;

function skipWhitespace(text: string, index: number, limit: number): number {
  let i = index;
  while (i < limit && /\s/.test(text[i]!)) {
    i++;
  }
  return i;
}

/**
 * 只跳过声明区并读取根节点名，不解析完整文档；用于 Provider Gate。
 */
export function detectRoot(text: string, maxScan = MAX_SCAN): RootInfo | null {
  const limit = Math.min(text.length, maxScan);
  let i = 0;
  if (text.charCodeAt(0) === 0xfeff) {
    i = 1;
  }
  let hasDoctype = false;

  while (i < limit) {
    i = skipWhitespace(text, i, limit);
    if (i >= limit) {
      return null;
    }
    if (text.startsWith('<?', i)) {
      const close = text.indexOf('?>', i + 2);
      if (close === -1 || close >= limit) {
        return null;
      }
      i = close + 2;
      continue;
    }
    if (text.startsWith('<!--', i)) {
      const close = text.indexOf('-->', i + 4);
      if (close === -1 || close >= limit) {
        return null;
      }
      i = close + 3;
      continue;
    }
    if (text.startsWith('<!DOCTYPE', i) || text.startsWith('<!doctype', i)) {
      hasDoctype = true;
      let quote: string | null = null;
      let brackets = 0;
      let j = i + 9;
      while (j < limit) {
        const ch = text[j]!;
        if (quote) {
          if (ch === quote) {
            quote = null;
          }
        } else if (ch === '"' || ch === "'") {
          quote = ch;
        } else if (ch === '[') {
          brackets++;
        } else if (ch === ']') {
          brackets = Math.max(0, brackets - 1);
        } else if (ch === '>' && brackets === 0) {
          break;
        }
        j++;
      }
      if (j >= limit) {
        return null;
      }
      i = j + 1;
      continue;
    }
    if (text.startsWith('<!', i)) {
      const close = text.indexOf('>', i + 2);
      if (close === -1 || close >= limit) {
        return null;
      }
      i = close + 1;
      continue;
    }
    break;
  }

  if (text[i] !== '<') {
    return null;
  }
  const nameStart = i + 1;
  let j = nameStart;
  while (j < limit && /[A-Za-z0-9_.:-]/.test(text[j]!)) {
    j++;
  }
  if (j === nameStart) {
    return null;
  }
  const rootTag = text.slice(nameStart, j);
  return {
    rootTag,
    rootTagOffset: i,
    rootTagNameRange: { start: nameStart, end: j },
    hasDoctype,
    isProcedure: rootTag === 'procedure',
  };
}
