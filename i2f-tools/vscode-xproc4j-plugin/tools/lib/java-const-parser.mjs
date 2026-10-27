/** 解析 Java interface 常量：`String NAME = "value";`。 */
export function parseJavaStringConstants(text) {
  const result = [];
  const re = /\bString\s+([A-Z][A-Z0-9_]*)\s*=\s*"((?:[^"\\]|\\.)*)"\s*;/g;
  let m;
  while ((m = re.exec(text)) !== null) {
    result.push({ name: m[1], value: m[2].replace(/\\"/g, '"') });
  }
  return result;
}

/**
 * 按声明顺序返回去重后的字符串值。
 * @param {string} text
 */
export function parseJavaStringValues(text) {
  const seen = new Set();
  const values = [];
  for (const item of parseJavaStringConstants(text)) {
    if (!seen.has(item.value)) {
      seen.add(item.value);
      values.push(item.value);
    }
  }
  return values;
}
