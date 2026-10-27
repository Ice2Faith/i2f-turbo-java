/**
 * 变量高亮默认覆盖的文件后缀。
 *
 * XML 仍沿用 procedure 根节点与已知标签/属性门控；其余后缀只做原始文本扫描。
 */
export const DEFAULT_VARIABLE_HIGHLIGHT_EXTENSIONS: readonly string[] = [
  'xml',
  'yaml',
  'yml',
  'java',
];

/**
 * 规范化用户填写的后缀列表：允许 `xml`、`.xml`、`*.xml` 三种写法，大小写不敏感。
 *
 * 返回空数组表示显式关闭变量高亮；传入非数组时使用兜底默认值。
 */
export function normalizeFileExtensions(
  value: unknown,
  fallback: readonly string[] = DEFAULT_VARIABLE_HIGHLIGHT_EXTENSIONS,
): string[] {
  if (!Array.isArray(value)) {
    return [...fallback];
  }
  const result = new Set<string>();
  for (const item of value) {
    if (typeof item !== 'string') {
      continue;
    }
    const normalized = item
      .trim()
      .toLowerCase()
      .replace(/^\*\./, '')
      .replace(/^\./, '');
    if (!/^[a-z0-9][a-z0-9._+-]*$/.test(normalized) || normalized.endsWith('.')) {
      continue;
    }
    result.add(normalized);
  }
  return [...result];
}

/**
 * 判断路径是否以配置中的某个后缀结尾。
 *
 * 使用 `.suffix` 结尾匹配而不是只取最后一个点，因此 `d.ts` 这类复合后缀也能生效；
 * 同时不会把 `notxml` 误判为 `xml`。
 */
export function fileMatchesExtensions(
  path: string,
  extensions: readonly string[],
): boolean {
  const lower = path.toLowerCase();
  return extensions.some((extension) => lower.endsWith(`.${extension}`));
}
