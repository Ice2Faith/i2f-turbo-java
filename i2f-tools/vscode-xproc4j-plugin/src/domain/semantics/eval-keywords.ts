import type { TextRange, XProcDocumentModel } from '../types';
import { bodyRange } from '../xml/scanner';
import { EVAL_STRING_PATTERN } from './eval-literals';

/**
 * lang-eval* 正文的联合关键字表：Java、JavaScript 与 SQL99 常用关键字/字面量。
 *
 * 只收录关键字与字面量，不收录函数名，避免把业务标识符大面积着色。
 * 小写存储，匹配时使用 i 标志；foreach 等脚本常见写法也一并保留。
 */
export const EVAL_KEYWORDS: readonly string[] = [
  'abstract',
  'add',
  'all',
  'alter',
  'and',
  'any',
  'as',
  'asc',
  'assert',
  'async',
  'await',
  'begin',
  'between',
  'boolean',
  'break',
  'byte',
  'by',
  'call',
  'cascade',
  'case',
  'cast',
  'catch',
  'char',
  'check',
  'class',
  'close',
  'column',
  'commit',
  'const',
  'constraint',
  'continue',
  'create',
  'cross',
  'cursor',
  'declare',
  'default',
  'delete',
  'desc',
  'distinct',
  'do',
  'double',
  'drop',
  'else',
  'elseif',
  'end',
  'enum',
  'except',
  'exists',
  'export',
  'extends',
  'false',
  'fetch',
  'final',
  'finally',
  'float',
  'for',
  'foreach',
  'foreign',
  'from',
  'full',
  'function',
  'grant',
  'group',
  'having',
  'if',
  'implements',
  'import',
  'in',
  'index',
  'inner',
  'insert',
  'instanceof',
  'int',
  'interface',
  'intersect',
  'into',
  'is',
  'join',
  'key',
  'left',
  'let',
  'like',
  'limit',
  'long',
  'natural',
  'new',
  'no',
  'not',
  'null',
  'of',
  'offset',
  'on',
  'open',
  'or',
  'order',
  'outer',
  'package',
  'permits',
  'primary',
  'private',
  'procedure',
  'protected',
  'public',
  'record',
  'recursive',
  'references',
  'restrict',
  'return',
  'returns',
  'revoke',
  'right',
  'rollback',
  'sealed',
  'select',
  'set',
  'short',
  'some',
  'static',
  'strictfp',
  'super',
  'switch',
  'synchronized',
  'table',
  'then',
  'this',
  'throw',
  'throws',
  'transaction',
  'transient',
  'trigger',
  'true',
  'try',
  'typeof',
  'undefined',
  'union',
  'unique',
  'update',
  'using',
  'values',
  'var',
  'view',
  'void',
  'volatile',
  'when',
  'where',
  'while',
  'with',
  'yield',
];

const KEYWORD_SOURCE = `\\b(?:${EVAL_KEYWORDS.join('|')})\\b`;

export interface EvalKeywordRange extends TextRange {
  keyword: string;
}

/**
 * 在 lang-eval* 正文内按词边界匹配联合关键字。
 *
 * 按要求使用正则语义，不做 Java/JavaScript/SQL 词法分析；因此注释中出现的
 * 关键字也会被匹配。字符串字面量内部的关键字被跳过，避免与字符串装饰叠色，
 * 让字符串整体保持字面量配色。范围只包含关键字本身，不含两侧空白与标点。
 */
export function collectEvalKeywordRanges(
  model: XProcDocumentModel,
  text: string,
): EvalKeywordRange[] {
  if (!model.isXProc) {
    return [];
  }
  const ranges: EvalKeywordRange[] = [];
  const pattern = new RegExp(KEYWORD_SOURCE, 'gi');
  for (const element of model.scan.elements) {
    if (!element.name.startsWith('lang-eval')) {
      continue;
    }
    const body = bodyRange(element);
    if (!body || body.start >= body.end) {
      continue;
    }
    const segment = text.slice(body.start, body.end);
    // 字符串字面量范围是关键字保护区：`"select"` 中的 select 不再按关键字着色。
    const stringRanges: TextRange[] = [];
    for (const match of segment.matchAll(EVAL_STRING_PATTERN)) {
      const literal = match[0] ?? '';
      const start = body.start + (match.index ?? 0);
      stringRanges.push({ start, end: start + literal.length });
    }
    for (const match of segment.matchAll(pattern)) {
      const keyword = match[0];
      if (!keyword) {
        continue;
      }
      const start = body.start + (match.index ?? 0);
      const end = start + keyword.length;
      if (stringRanges.some((range) => start >= range.start && end <= range.end)) {
        continue;
      }
      ranges.push({ start, end, keyword });
    }
  }
  return ranges;
}
