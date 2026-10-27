/**
 * 容错的 DTD 子集解析器。
 *
 * 只解析 procedure.dtd 实际使用的 ELEMENT / ATTLIST 声明，不实现完整 DTD。
 * 上游 DTD 存在 19 条异常声明（缺类型或非法类型），这里按 CDATA 容错并记录，
 * 供构建期报告使用；不能把解析结果当作严格校验依据。
 */

const VALID_TYPES = new Set([
  'CDATA',
  'ID',
  'IDREF',
  'IDREFS',
  'NMTOKEN',
  'NMTOKENS',
  'ENTITY',
  'ENTITIES',
  'NOTATION',
]);

const DEFAULT_KEYWORDS = new Set(['#REQUIRED', '#IMPLIED', '#FIXED']);

/** 去掉 DTD 注释，注释内的声明不参与解析。 */
export function stripComments(text) {
  return text.replace(/<!--[\s\S]*?-->/g, '');
}

/**
 * 读取从 start 开始、以 '>' 结束的声明，忽略引号内的 '>'。
 * @returns {{ body: string, end: number }}
 */
function readDeclaration(text, start) {
  let i = start;
  let quote = null;
  while (i < text.length) {
    const ch = text[i];
    if (quote) {
      if (ch === quote) {
        quote = null;
      }
    } else if (ch === '"' || ch === "'") {
      quote = ch;
    } else if (ch === '>') {
      return { body: text.slice(start, i), end: i + 1 };
    }
    i++;
  }
  return { body: text.slice(start), end: text.length };
}

/**
 * 把 ATTLIST 主体拆成 token：普通 token、带引号的默认值、括号括起的枚举类型。
 * @returns {string[]}
 */
export function tokenizeAttlist(body) {
  const tokens = [];
  let i = 0;
  while (i < body.length) {
    while (i < body.length && /\s/.test(body[i])) {
      i++;
    }
    if (i >= body.length) {
      break;
    }
    const ch = body[i];
    if (ch === '"' || ch === "'") {
      const quote = ch;
      let j = i + 1;
      while (j < body.length && body[j] !== quote) {
        j++;
      }
      tokens.push(body.slice(i, Math.min(j + 1, body.length)));
      i = j + 1;
      continue;
    }
    if (ch === '(') {
      let depth = 0;
      let j = i;
      while (j < body.length) {
        if (body[j] === '(') {
          depth++;
        } else if (body[j] === ')') {
          depth--;
          if (depth === 0) {
            j++;
            break;
          }
        }
        j++;
      }
      tokens.push(body.slice(i, j));
      i = j;
      continue;
    }
    let j = i;
    while (j < body.length && !/\s/.test(body[j])) {
      j++;
    }
    tokens.push(body.slice(i, j));
    i = j;
  }
  return tokens;
}

function stripQuotes(token) {
  if (token.length >= 2) {
    const first = token[0];
    if ((first === '"' || first === "'") && token.at(-1) === first) {
      return token.slice(1, -1);
    }
  }
  return token;
}

/**
 * @param {string} text
 */
export function parseDtd(text) {
  const clean = stripComments(text);
  const elements = [];
  const elementSet = new Set();

  const elementRe = /<!ELEMENT\s+([^\s>]+)/g;
  let m;
  while ((m = elementRe.exec(clean)) !== null) {
    const name = m[1];
    if (!elementSet.has(name)) {
      elementSet.add(name);
      elements.push({ name });
    }
  }

  /** @type {Map<string, Array<{name: string, type: string, required: boolean, defaultValue: string|null}>>} */
  const attributesByTag = new Map();
  /** @type {Array<{tag: string, name: string, rawType: string, reason: 'missing-type'|'invalid-type'}>} */
  const anomalies = [];
  let attlistCount = 0;
  const attlistTags = new Set();
  const attlistCountByTag = new Map();

  let index = 0;
  while ((index = clean.indexOf('<!ATTLIST', index)) !== -1) {
    const { body, end } = readDeclaration(clean, index + '<!ATTLIST'.length);
    index = end;
    attlistCount++;
    const tokens = tokenizeAttlist(body);
    if (tokens.length === 0) {
      continue;
    }
    const tag = tokens[0];
    attlistTags.add(tag);
    attlistCountByTag.set(tag, (attlistCountByTag.get(tag) ?? 0) + 1);
    if (!attributesByTag.has(tag)) {
      attributesByTag.set(tag, []);
    }
    const list = attributesByTag.get(tag);

    let i = 1;
    while (i < tokens.length) {
      const name = tokens[i];
      i++;
      if (i >= tokens.length) {
        break;
      }
      let rawType = tokens[i];
      i++;
      let type;
      let defaultValue = null;
      let required = false;

      if (rawType === '#REQUIRED' || rawType === '#IMPLIED') {
        // 缺类型声明：如 <!ATTLIST lang-set value.double #IMPLIED>
        anomalies.push({ tag, name, rawType, reason: 'missing-type' });
        type = 'CDATA';
        required = rawType === '#REQUIRED';
        i--; // 该 token 是默认值而不是类型
        rawType = 'CDATA';
      } else if (rawType === '#FIXED') {
        // 极少见：#FIXED "值"，这里把下一个 token 当默认值。
        defaultValue = i < tokens.length ? stripQuotes(tokens[i]) : null;
        i++;
        type = 'CDATA';
        anomalies.push({ tag, name, rawType, reason: 'missing-type' });
      } else if (
        VALID_TYPES.has(rawType) ||
        rawType.startsWith('(') ||
        rawType.startsWith('NOTATION')
      ) {
        type = rawType;
        if (i < tokens.length) {
          defaultValue = tokens[i];
          i++;
        }
      } else {
        // 非法类型：如 <!ATTLIST lang-shell result trim #IMPLIED>
        // `trim` 不是合法的 DTD 类型；把它当默认值，类型按 CDATA 处理。
        anomalies.push({ tag, name, rawType, reason: 'invalid-type' });
        type = 'CDATA';
        defaultValue = rawType;
      }

      if (defaultValue === '#REQUIRED') {
        required = true;
        defaultValue = null;
      } else if (defaultValue === '#IMPLIED') {
        defaultValue = null;
      } else if (defaultValue !== null && defaultValue.startsWith('#')) {
        defaultValue = null;
      } else if (defaultValue !== null) {
        defaultValue = stripQuotes(defaultValue);
      }

      list.push({ name, type, required, defaultValue });
    }
  }

  const noAttributes = [...elementSet].filter((tag) => !attributesByTag.has(tag)).sort();
  const multiAttlist = [...attlistCountByTag.entries()]
    .filter(([, count]) => count > 1)
    .map(([tag, count]) => ({ tag, count }))
    .sort((a, b) => a.tag.localeCompare(b.tag));

  return {
    elements,
    elementSet,
    attributesByTag,
    noAttributes,
    attlistCount,
    attlistTags: [...attlistTags].sort(),
    multiAttlist,
    anomalies,
  };
}
