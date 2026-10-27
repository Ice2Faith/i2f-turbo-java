/**
 * 把 IDEA Live Template 转换为 VS Code Snippet 文本。
 *
 * 关键规则：
 * - `$NAME$` → `${n}` / `${n:default}`，编号按首次出现顺序；
 * - `$END$` → `$0`；
 * - 4 个已知宏转换为 VS Code 变量；
 * - 先拆分占位符再转义字面量，避免把用户文本中的 `$`、`\`、`}` 当成 snippet 语法。
 */

const MACROS = new Map([
  ['concat(fileName(),":",lineNumber())', '$TM_FILENAME:$TM_LINE_NUMBER'],
  ['fileNameWithoutExtension()', '$TM_FILENAME_BASE'],
  ['concat("error at ",fileName(),":",lineNumber())', 'error at $TM_FILENAME:$TM_LINE_NUMBER'],
]);

function escapeSnippetLiteral(text) {
  return text.replace(/\\/g, '\\\\').replace(/\$/g, '\\$');
}

function escapePlaceholderDefault(text) {
  return text.replace(/\\/g, '\\\\').replace(/\$/g, '\\$').replace(/}/g, '\\}');
}

const PLACEHOLDER_RE = /\$([A-Z][A-Z0-9_]*)\$|\$END\$/g;

/**
 * @param {string} templateValue 已经过 XML 解码
 * @param {Array<{name: string, expression: string, defaultValue: string}>} declarations
 */
export function convertTemplateValue(templateValue, declarations) {
  const byName = new Map(declarations.map((v) => [v.name, v]));
  const order = [];
  for (const match of templateValue.matchAll(PLACEHOLDER_RE)) {
    const name = match[1];
    if (name && !order.includes(name)) {
      order.push(name);
    }
  }

  let out = '';
  let last = 0;
  for (const match of templateValue.matchAll(PLACEHOLDER_RE)) {
    out += escapeSnippetLiteral(templateValue.slice(last, match.index));
    last = match.index + match[0].length;
    if (match[0] === '$END$') {
      out += '$0';
      continue;
    }
    const name = match[1];
    const index = order.indexOf(name) + 1;
    const decl = byName.get(name);
    if (!decl) {
      out += `\${${index}}`;
      continue;
    }
    const macro = decl.expression ? MACROS.get(decl.expression.trim()) : undefined;
    const fallback = macro ?? decl.defaultValue;
    if (macro) {
      // 宏写入的是 VS Code 变量，必须保持 `$TM_...` 原样，不能被转义。
      out += `\${${index}:${macro.replace(/}/g, '\\}')}}`;
    } else if (fallback) {
      out += `\${${index}:${escapePlaceholderDefault(fallback)}}`;
    } else {
      out += `\${${index}}`;
    }
  }
  out += escapeSnippetLiteral(templateValue.slice(last));
  return { body: out, order };
}

export const PROCEDURE_OVERRIDE_BODY = [
  '<?xml version="1.0" encoding="UTF-8"?>',
  '<!DOCTYPE procedure SYSTEM "procedure.dtd">',
  '',
  '<procedure id="${1:procedure}">',
  '    <!--',
  '',
  '    @author ${2}',
  '    @date $CURRENT_YEAR-$CURRENT_MONTH-$CURRENT_DATE',
  '    @time $CURRENT_HOUR:$CURRENT_MINUTE:$CURRENT_SECOND',
  '    -->',
  '',
  '    $0',
  '</procedure>',
].join('\n');
