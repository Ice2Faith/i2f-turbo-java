import { decodeXmlEntities } from './entities';
import type { ParsedStartTag } from './types';
import type { Quote, TextRange, XmlAttributeModel } from '../types';

const NAME_START = /[A-Za-z_:]/;
const NAME_CHAR = /[A-Za-z0-9_.:-]/;

function skipWhitespace(text: string, index: number): number {
  let i = index;
  while (i < text.length && /\s/.test(text[i]!)) {
    i++;
  }
  return i;
}

function readName(text: string, index: number): { name: string; end: number } | null {
  if (index >= text.length || !NAME_START.test(text[index]!)) {
    return null;
  }
  let i = index + 1;
  while (i < text.length && NAME_CHAR.test(text[i]!)) {
    i++;
  }
  return { name: text.slice(index, i), end: i };
}

/**
 * 解析从 offset 开始的开始标签。允许未闭合与缺失引号，供扫描器与补全共用。
 * offset 必须指向 '<'。
 */
export function parseStartTag(text: string, offset: number): ParsedStartTag | null {
  if (text[offset] !== '<') {
    return null;
  }
  const nameResult = readName(text, offset + 1);
  if (!nameResult) {
    return null;
  }
  const attributes: XmlAttributeModel[] = [];
  let i = nameResult.end;
  let closed = false;
  let selfClosing = false;

  while (i < text.length) {
    i = skipWhitespace(text, i);
    if (i >= text.length) {
      break;
    }
    if (text[i] === '>') {
      i++;
      closed = true;
      break;
    }
    if (text[i] === '/' && text[i + 1] === '>') {
      i += 2;
      closed = true;
      selfClosing = true;
      break;
    }
    if (text[i] === '<') {
      // 非法的新开始标签：停止，避免把后续内容吞入。
      break;
    }

    const attrNameStart = i;
    while (i < text.length && !/[\s=/>]/.test(text[i]!)) {
      i++;
    }
    const rawName = text.slice(attrNameStart, i);
    if (!rawName) {
      i++;
      continue;
    }
    const attrNameEnd = i;
    const afterName = skipWhitespace(text, i);
    let quote: Quote = null;
    let valueStart = afterName;
    let valueEnd = afterName;
    let valueRange: TextRange = { start: afterName, end: afterName };
    let hasValue = false;

    if (text[afterName] === '=') {
      hasValue = true;
      i = skipWhitespace(text, afterName + 1);
      valueStart = i;
      if (text[i] === '"' || text[i] === "'") {
        const quoteChar = text[i]!;
        quote = quoteChar as Quote;
        const close = text.indexOf(quoteChar, i + 1);
        valueStart = i + 1;
        if (close === -1) {
          valueEnd = text.length;
          valueRange = { start: valueStart, end: valueEnd };
          i = text.length;
        } else {
          valueEnd = close;
          valueRange = { start: valueStart, end: valueEnd };
          i = close + 1;
        }
      } else {
        while (i < text.length && !/[\s>]/.test(text[i]!)) {
          i++;
        }
        valueEnd = i;
        valueRange = { start: valueStart, end: valueEnd };
      }
    } else {
      // 无值的布尔属性；引号位置仍记录在 name 之后。
      i = attrNameEnd;
    }

    const rawValue = hasValue ? text.slice(valueRange.start, valueRange.end) : '';
    const dot = rawName.indexOf('.');
    attributes.push({
      name: rawName,
      baseName: dot > 0 ? rawName.slice(0, dot) : rawName,
      features: dot > 0 ? rawName.slice(dot + 1).split('.').filter(Boolean) : [],
      rawValue,
      value: decodeXmlEntities(rawValue),
      nameRange: { start: attrNameStart, end: attrNameEnd },
      valueRange,
      valueStart,
      valueEnd,
      quote,
    });
  }

  return {
    name: nameResult.name,
    nameRange: { start: offset + 1, end: nameResult.end },
    attributes,
    startTagEnd: closed ? i : text.length,
    closed,
    selfClosing,
    endOffset: closed ? i : text.length,
  };
}

/** 解析 `</name>`，返回名称与结束偏移。 */
export function parseEndTag(
  text: string,
  offset: number,
): { name: string; nameRange: TextRange; end: number } | null {
  if (text[offset] !== '<' || text[offset + 1] !== '/') {
    return null;
  }
  let i = offset + 2;
  const start = i;
  while (i < text.length && /[A-Za-z0-9_.:-]/.test(text[i]!)) {
    i++;
  }
  if (i === start) {
    return null;
  }
  const name = text.slice(start, i);
  const close = text.indexOf('>', i);
  return {
    name,
    nameRange: { start, end: i },
    end: close === -1 ? text.length : close + 1,
  };
}
