import type { Quote, TextRange } from '../types';

export type StartTagState =
  | 'tag-name'
  | 'attr-name'
  | 'after-attr-name'
  | 'before-value'
  | 'in-value'
  | 'after-value'
  | 'closed'
  | 'invalid';

export interface PartialAttribute {
  name: string;
  nameRange: TextRange;
  value: string;
  valueRange: TextRange;
  quote: Quote;
  hasEquals: boolean;
  valueClosed: boolean;
}

export interface StartTagContext {
  startOffset: number;
  tagName: string;
  tagNameRange: TextRange;
  state: StartTagState;
  attributes: PartialAttribute[];
  currentAttributeName: string;
  currentAttributeRange: TextRange;
  currentValue: string;
  currentValueRange: TextRange;
  currentQuote: Quote;
  closed: boolean;
  selfClosing: boolean;
}

const NAME_START = /[A-Za-z_:]/;
const NAME_CHAR = /[A-Za-z0-9_.:-]/;
const BACKWARD_LIMIT = 4096;

function emptyRange(offset: number): TextRange {
  return { start: offset, end: offset };
}

function invalidContext(startOffset: number): StartTagContext {
  return {
    startOffset,
    tagName: '',
    tagNameRange: emptyRange(startOffset + 1),
    state: 'invalid',
    attributes: [],
    currentAttributeName: '',
    currentAttributeRange: emptyRange(startOffset + 1),
    currentValue: '',
    currentValueRange: emptyRange(startOffset + 1),
    currentQuote: null,
    closed: false,
    selfClosing: false,
  };
}

/**
 * 解析开始标签到光标位置为止的前缀。与扫描器共享字符规则，但允许未闭合引号/标签。
 */
export function parseStartTagPrefix(text: string, startOffset: number, upto: number): StartTagContext {
  if (startOffset < 0 || startOffset >= text.length || text[startOffset] !== '<') {
    return invalidContext(Math.max(0, startOffset));
  }
  const limit = Math.max(startOffset + 1, Math.min(upto, text.length));
  let i = startOffset + 1;
  const nameStart = i;
  if (text[i] === '/' || text[i] === '!' || text[i] === '?') {
    return invalidContext(startOffset);
  }
  while (i < text.length && NAME_CHAR.test(text[i]!)) {
    i++;
  }
  const tagNameEnd = i;
  const context = invalidContext(startOffset);
  context.tagName = text.slice(nameStart, tagNameEnd);
  context.tagNameRange = { start: nameStart, end: tagNameEnd };
  context.state = 'tag-name';

  if (context.tagName && !NAME_START.test(context.tagName[0]!)) {
    return context;
  }
  if (limit <= tagNameEnd) {
    return context;
  }

  const attributes: PartialAttribute[] = [];
  let state: StartTagState = 'attr-name';
  let currentAttributeName = '';
  let currentAttributeRange = emptyRange(limit);
  let currentValue = '';
  let currentValueRange = emptyRange(limit);
  let currentQuote: Quote = null;
  let closed = false;
  let selfClosing = false;

  const setCurrent = (attribute: PartialAttribute): void => {
    currentAttributeName = attribute.name;
    currentAttributeRange = attribute.nameRange;
    currentValue = attribute.value;
    currentValueRange = attribute.valueRange;
    currentQuote = attribute.quote;
  };

  while (i < limit) {
    while (i < limit && /\s/.test(text[i]!)) {
      i++;
    }
    if (i >= limit) {
      state = 'attr-name';
      currentAttributeName = '';
      currentAttributeRange = emptyRange(limit);
      currentValue = '';
      currentValueRange = emptyRange(limit);
      currentQuote = null;
      break;
    }
    if (text[i] === '>') {
      closed = true;
      i++;
      state = 'closed';
      break;
    }
    if (text[i] === '/' && text[i + 1] === '>') {
      closed = true;
      selfClosing = true;
      i += 2;
      state = 'closed';
      break;
    }
    if (text[i] === '<') {
      state = 'invalid';
      break;
    }

    const attrNameStart = i;
    while (i < limit && !/[\s=/>]/.test(text[i]!)) {
      i++;
    }
    const attrNameEnd = i;
    const fullName = text.slice(attrNameStart, attrNameEnd);
    if (!fullName) {
      state = 'invalid';
      break;
    }

    if (limit <= attrNameEnd) {
      const attribute: PartialAttribute = {
        name: text.slice(attrNameStart, limit),
        nameRange: { start: attrNameStart, end: attrNameEnd },
        value: '',
        valueRange: emptyRange(limit),
        quote: null,
        hasEquals: false,
        valueClosed: false,
      };
      attributes.push(attribute);
      setCurrent(attribute);
      state = 'attr-name';
      break;
    }

    let cursor = i;
    while (cursor < limit && /\s/.test(text[cursor]!)) {
      cursor++;
    }
    if (cursor >= limit) {
      const attribute: PartialAttribute = {
        name: fullName,
        nameRange: { start: attrNameStart, end: attrNameEnd },
        value: '',
        valueRange: emptyRange(limit),
        quote: null,
        hasEquals: false,
        valueClosed: false,
      };
      attributes.push(attribute);
      setCurrent(attribute);
      state = 'after-attr-name';
      break;
    }

    if (text[cursor] !== '=') {
      const attribute: PartialAttribute = {
        name: fullName,
        nameRange: { start: attrNameStart, end: attrNameEnd },
        value: '',
        valueRange: emptyRange(attrNameEnd),
        quote: null,
        hasEquals: false,
        valueClosed: false,
      };
      attributes.push(attribute);
      setCurrent(attribute);
      state = 'after-attr-name';
      i = cursor;
      // 没有等号时，下一个 token 仍是属性名；只有光标正好落在分隔符后才结束。
      if (cursor >= limit) {
        break;
      }
      i = cursor;
      continue;
    }

    cursor++;
    while (cursor < limit && /\s/.test(text[cursor]!)) {
      cursor++;
    }
    if (cursor >= limit) {
      const attribute: PartialAttribute = {
        name: fullName,
        nameRange: { start: attrNameStart, end: attrNameEnd },
        value: '',
        valueRange: emptyRange(limit),
        quote: null,
        hasEquals: true,
        valueClosed: false,
      };
      attributes.push(attribute);
      setCurrent(attribute);
      state = 'before-value';
      break;
    }

    const quoteChar = text[cursor];
    if (quoteChar === '"' || quoteChar === "'") {
      const valueStart = cursor + 1;
      const close = text.indexOf(quoteChar, valueStart);
      const visibleEnd = close === -1 ? limit : Math.min(close, limit);
      const attribute: PartialAttribute = {
        name: fullName,
        nameRange: { start: attrNameStart, end: attrNameEnd },
        value: text.slice(valueStart, visibleEnd),
        valueRange: { start: valueStart, end: close === -1 ? limit : close },
        quote: quoteChar as Quote,
        hasEquals: true,
        valueClosed: close !== -1 && limit > close,
      };
      attributes.push(attribute);
      setCurrent(attribute);
      if (close === -1 || limit <= close) {
        state = 'in-value';
        break;
      }
      i = close + 1;
      if (i >= limit) {
        state = 'after-value';
        break;
      }
      state = 'after-value';
      continue;
    }

    let valueEnd = cursor;
    while (valueEnd < limit && !/[\s/>]/.test(text[valueEnd]!)) {
      valueEnd++;
    }
    const attribute: PartialAttribute = {
      name: fullName,
      nameRange: { start: attrNameStart, end: attrNameEnd },
      value: text.slice(cursor, valueEnd),
      valueRange: { start: cursor, end: valueEnd },
      quote: null,
      hasEquals: true,
      valueClosed: valueEnd < limit,
    };
    attributes.push(attribute);
    setCurrent(attribute);
    if (valueEnd >= limit) {
      state = 'in-value';
      break;
    }
    i = valueEnd;
    state = 'after-value';
  }

  context.state = state;
  context.attributes = attributes;
  context.currentAttributeName = currentAttributeName;
  context.currentAttributeRange = currentAttributeRange;
  context.currentValue = currentValue;
  context.currentValueRange = currentValueRange;
  context.currentQuote = currentQuote;
  context.closed = closed;
  context.selfClosing = selfClosing;
  return context;
}

/**
 * 从光标向前定位仍在其中的开始标签 `<`；遇到未加引号的 `>` 说明已经离开标签。
 */
export function findStartTagOffset(text: string, offset: number): number | undefined {
  const floor = Math.max(0, offset - BACKWARD_LIMIT);
  for (let start = offset - 1; start >= floor; start--) {
    if (text[start] !== '<') {
      continue;
    }
    const next = text[start + 1];
    if (next === '/') {
      continue;
    }
    // 刚输入 `<` 时没有标签名，仍应进入标签补全。
    if (next !== undefined && (next === '!' || next === '?')) {
      continue;
    }
    if (next !== undefined && !NAME_START.test(next)) {
      // 下一字符是换行/空白时，仅接受独占行首的 `<`，避免把 SQL 的 `<` 比较符当成标签。
      if (!/\s/.test(next)) {
        continue;
      }
      const lineStart = text.lastIndexOf('\n', start - 1) + 1;
      if (text.slice(lineStart, start).trim() !== '') {
        continue;
      }
    }
    let quote: string | null = null;
    let valid = true;
    for (let i = start + 1; i < offset; i++) {
      const ch = text[i]!;
      if (quote) {
        if (ch === quote) {
          quote = null;
        }
      } else if (ch === '"' || ch === "'") {
        quote = ch;
      } else if (ch === '>') {
        valid = false;
        break;
      } else if (ch === '<') {
        valid = false;
        break;
      }
    }
    if (valid) {
      return start;
    }
  }
  return undefined;
}
