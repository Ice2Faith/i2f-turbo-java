/** URI 键只做字符串归一化，避免不同类型 URI 在 Map 中重复。 */
export function normalizeUriKey(uri: string): string {
  const trimmed = uri.trim();
  if (!trimmed) {
    return '';
  }
  // Windows 盘符大小写不敏感；其余部分保持原样，避免误伤大小写敏感的远程 URI。
  return trimmed.replace(/^file:\/\/([a-zA-Z]):/, (_all, drive: string) => `file://${drive.toLowerCase()}:`);
}
