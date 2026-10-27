/** 解析 IntelliJ Live Template XML（XProc4j.xml 的有限子集）。 */

export function decodeXmlEntities(text) {
  return text
    .replace(/&#x([0-9a-fA-F]+);/g, (_, hex) => String.fromCodePoint(parseInt(hex, 16)))
    .replace(/&#(\d+);/g, (_, dec) => String.fromCodePoint(Number(dec)))
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&quot;/g, '"')
    .replace(/&apos;/g, "'")
    .replace(/&amp;/g, '&');
}

function readAttr(attrs, name) {
  const re = new RegExp(`\\b${name}\\s*=\\s*"([^"]*)"`);
  const m = attrs.match(re);
  return m ? decodeXmlEntities(m[1]) : '';
}

/** IDEA 把默认值存成带引号的字面量，例如 &quot;true&quot; → true。 */
function normalizeDefault(raw) {
  const value = raw ?? '';
  if (value.length >= 2) {
    const first = value[0];
    if ((first === '"' || first === "'") && value.at(-1) === first) {
      return value.slice(1, -1);
    }
  }
  return value;
}

/**
 * @param {string} xml
 * @returns {Array<{name: string, value: string, description: string,
 *   variables: Array<{name: string, expression: string, defaultValue: string, alwaysStopAt: boolean}>,
 *   xmlContext: boolean}>}
 */
export function parseIdeaTemplates(xml) {
  const templates = [];
  const templateRe = /<template\b([^>]*)>([\s\S]*?)<\/template>/g;
  let m;
  while ((m = templateRe.exec(xml)) !== null) {
    const attrs = m[1];
    const body = m[2];
    const variables = [];
    const varRe = /<variable\b([^>]*)\/>/g;
    let vm;
    while ((vm = varRe.exec(body)) !== null) {
      variables.push({
        name: readAttr(vm[1], 'name'),
        expression: readAttr(vm[1], 'expression'),
        defaultValue: normalizeDefault(readAttr(vm[1], 'defaultValue')),
        alwaysStopAt: readAttr(vm[1], 'alwaysStopAt') === 'true',
      });
    }
    const contextMatch = body.match(/<context\b[^>]*>([\s\S]*?)<\/context>/);
    const xmlContext = Boolean(
      contextMatch && /name\s*=\s*"XML"\s+value\s*=\s*"true"/.test(contextMatch[1]),
    );
    templates.push({
      name: readAttr(attrs, 'name'),
      value: readAttr(attrs, 'value'),
      description: readAttr(attrs, 'description'),
      variables,
      xmlContext,
    });
  }
  return templates;
}
