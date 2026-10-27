import type {
  ProtectedKind,
  ProtectedRange,
  ScanIssue,
  ScanIssueCode,
  ScanResult,
  TextRange,
  XmlElementModel,
} from '../types';
import type { ParsedStartTag } from './types';
import { parseEndTag, parseStartTag } from './start-tag';

export interface ScanOptions {
  maxElements: number;
  maxDepth: number;
  maxIssues: number;
}

const DEFAULT_OPTIONS: ScanOptions = {
  maxElements: 50000,
  maxDepth: 200,
  maxIssues: 100,
};

interface IssueCollector {
  issues: ScanIssue[];
  count: number;
  max: number;
}

function addIssue(collector: IssueCollector, offset: number, code: ScanIssueCode, message: string): void {
  collector.count++;
  if (collector.issues.length < collector.max) {
    collector.issues.push({ offset, code, message });
  }
}

function findCloseTag(text: string, from: number, token: string): number {
  const index = text.indexOf(token, from);
  return index === -1 ? -1 : index + token.length;
}

/** DOCTYPE 可能包含内部子集 [...]，需要跳过引号与方括号。 */
function scanDoctype(text: string, start: number): number {
  let i = start;
  let quote: string | null = null;
  let brackets = 0;
  while (i < text.length) {
    const ch = text[i]!;
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
      return i + 1;
    }
    i++;
  }
  return text.length;
}

function protectedRange(
  text: string,
  start: number,
  kind: ProtectedKind,
  closeToken: string,
  issues: IssueCollector,
): { range: ProtectedRange; end: number } {
  const close = findCloseTag(text, start + 2, closeToken);
  if (close === -1) {
    addIssue(issues, start, `unclosed-${kind === 'declaration' ? 'doctype' : kind}` as ScanIssueCode, `未闭合的 ${kind}`);
    return { range: { start, end: text.length, kind }, end: text.length };
  }
  return { range: { start, end: close, kind }, end: close };
}

/**
 * 容错 XML 扫描：只关心元素结构、属性与保护区间，不做 DTD 校验。
 * 任何输入都不会抛异常，最多返回 issues。
 */
export function scanXml(text: string, options: Partial<ScanOptions> = {}): ScanResult {
  const opts = { ...DEFAULT_OPTIONS, ...options };
  const elements: XmlElementModel[] = [];
  const protectedRanges: ProtectedRange[] = [];
  const stack: number[] = [];
  const issues: IssueCollector = { issues: [], count: 0, max: opts.maxIssues };
  let rootIndex = -1;
  let truncated = false;
  let truncatedReason: 'max-elements' | 'max-depth' | undefined;
  let i = 0;

  const pushElement = (parsed: ParsedStartTag): number | null => {
    if (elements.length >= opts.maxElements) {
      if (!truncated) {
        truncated = true;
        truncatedReason = 'max-elements';
        addIssue(issues, parsed.nameRange.start, 'max-elements', '元素数量超过上限');
      }
      return null;
    }
    if (stack.length >= opts.maxDepth) {
      if (!truncated) {
        truncated = true;
        truncatedReason = 'max-depth';
        addIssue(issues, parsed.nameRange.start, 'max-depth', '元素嵌套超过上限');
      }
      return null;
    }
    const parentIndex = stack.length ? stack[stack.length - 1]! : -1;
    const element: XmlElementModel = {
      name: parsed.name,
      nameRange: parsed.nameRange,
      startOffset: parsed.startTagEnd - (parsed.closed ? 0 : 0),
      startTagEnd: parsed.startTagEnd,
      startTagClosed: parsed.closed,
      selfClosing: parsed.selfClosing,
      endTagStart: -1,
      endOffset: parsed.selfClosing ? parsed.startTagEnd : text.length,
      closed: parsed.selfClosing,
      parentIndex,
      depth: parentIndex === -1 ? 0 : elements[parentIndex]!.depth + 1,
      attributes: parsed.attributes,
    };
    element.startOffset = parsed.nameRange.start - 1;
    const index = elements.length;
    elements.push(element);
    if (parentIndex === -1 && rootIndex === -1) {
      rootIndex = index;
    }
    if (!parsed.closed) {
      addIssue(issues, element.startOffset, 'unclosed-start-tag', `开始标签 <${parsed.name}> 未闭合`);
      element.endOffset = text.length;
    } else if (!parsed.selfClosing) {
      stack.push(index);
    }
    return index;
  };

  const closeTop = (endTagStart: number, endOffset: number, name: string) => {
    const index = stack.pop();
    if (index === undefined) {
      return;
    }
    const element = elements[index]!;
    if (element.name !== name) {
      addIssue(issues, endTagStart, 'mismatched-end-tag', `结束标签 </${name}> 与 <${element.name}> 不匹配`);
    }
    element.endTagStart = endTagStart;
    element.endOffset = endOffset;
    element.closed = true;
  };

  while (i < text.length) {
    if (text[i] !== '<') {
      i++;
      continue;
    }

    if (text.startsWith('<!--', i)) {
      const result = protectedRange(text, i, 'comment', '-->', issues);
      protectedRanges.push(result.range);
      i = result.end;
      continue;
    }
    if (text.startsWith('<![CDATA[', i)) {
      const close = findCloseTag(text, i + 9, ']]>');
      if (close === -1) {
        addIssue(issues, i, 'unclosed-cdata', '未闭合的 CDATA');
        protectedRanges.push({ start: i, end: text.length, kind: 'cdata' });
        i = text.length;
      } else {
        protectedRanges.push({ start: i, end: close, kind: 'cdata' });
        i = close;
      }
      continue;
    }
    if (text.startsWith('<?', i)) {
      const result = protectedRange(text, i, 'pi', '?>', issues);
      protectedRanges.push(result.range);
      i = result.end;
      continue;
    }
    if (text.startsWith('<!DOCTYPE', i) || text.startsWith('<!doctype', i)) {
      const end = scanDoctype(text, i);
      protectedRanges.push({ start: i, end, kind: 'doctype' });
      if (end === text.length && !text.endsWith('>')) {
        addIssue(issues, i, 'unclosed-doctype', '未闭合的 DOCTYPE');
      }
      i = end;
      continue;
    }
    if (text.startsWith('<!', i)) {
      const end = scanDoctype(text, i);
      protectedRanges.push({ start: i, end, kind: 'declaration' });
      i = end;
      continue;
    }

    if (text[i + 1] === '/') {
      const endTag = parseEndTag(text, i);
      if (!endTag) {
        i++;
        continue;
      }
      if (stack.length === 0) {
        addIssue(issues, i, 'unexpected-end-tag', `多余的结束标签 </${endTag.name}>`);
        i = endTag.end;
        continue;
      }
      const topIndex = stack[stack.length - 1]!;
      const top = elements[topIndex]!;
      if (top.name === endTag.name) {
        closeTop(i, endTag.end, endTag.name);
        i = endTag.end;
        continue;
      }
      // 向上寻找匹配项；中间未闭合的元素按当前位置结束。
      let match = -1;
      for (let s = stack.length - 1; s >= 0; s--) {
        if (elements[stack[s]!]!.name === endTag.name) {
          match = s;
          break;
        }
      }
      if (match === -1) {
        addIssue(issues, i, 'unexpected-end-tag', `多余的结束标签 </${endTag.name}>`);
        i = endTag.end;
        continue;
      }
      while (stack.length - 1 > match) {
        const index = stack.pop()!;
        const element = elements[index]!;
        element.endOffset = i;
      }
      closeTop(i, endTag.end, endTag.name);
      i = endTag.end;
      continue;
    }

    const parsed = parseStartTag(text, i);
    if (!parsed) {
      i++;
      continue;
    }
    const index = pushElement(parsed);
    if (index === null) {
      // 达到上限后跳过当前开始标签，但继续扫描结束标签以便尽量收敛。
      i = Math.max(parsed.startTagEnd, i + 1);
      continue;
    }
    i = Math.max(parsed.startTagEnd, i + 1);
  }

  while (stack.length) {
    const index = stack.pop()!;
    const element = elements[index]!;
    element.endOffset = text.length;
    addIssue(issues, element.startOffset, 'unclosed-element', `元素 <${element.name}> 未闭合`);
  }

  return {
    elements,
    rootIndex,
    protectedRanges,
    issues: issues.issues,
    issueCount: issues.count,
    truncated,
    truncatedReason,
  };
}

export function isOffsetProtected(ranges: ProtectedRange[], offset: number): boolean {
  let low = 0;
  let high = ranges.length - 1;
  while (low <= high) {
    const mid = (low + high) >> 1;
    const range = ranges[mid]!;
    if (offset < range.start) {
      high = mid - 1;
    } else if (offset >= range.end) {
      low = mid + 1;
    } else {
      return true;
    }
  }
  return false;
}

export function protectedRangeAt(ranges: ProtectedRange[], offset: number): ProtectedRange | undefined {
  return ranges.find((range) => offset >= range.start && offset < range.end);
}

export function bodyRange(element: XmlElementModel): TextRange | undefined {
  if (!element.startTagClosed || element.selfClosing || !element.closed || element.endTagStart < 0) {
    return undefined;
  }
  return { start: element.startTagEnd, end: element.endTagStart };
}
