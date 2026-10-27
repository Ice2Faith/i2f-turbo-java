/**
 * 极简 glob 匹配：仅用于文件监听事件的 include/exclude 过滤。
 * 支持 `**`、`*`、`?` 与字符类，不追求与 VS Code 完全一致。
 */
export function globToRegExp(pattern: string): RegExp {
  const normalized = pattern.replace(/\\/g, '/');
  let source = '';
  for (let i = 0; i < normalized.length; i++) {
    const char = normalized[i]!;
    if (char === '*') {
      const next = normalized[i + 1];
      if (next === '*') {
        const after = normalized[i + 2];
        if (after === '/') {
          source += '(?:.*/)?';
          i += 2;
        } else {
          source += '.*';
          i += 1;
        }
      } else {
        source += '[^/]*';
      }
      continue;
    }
    if (char === '?') {
      source += '[^/]';
      continue;
    }
    if (char === '{') {
      const end = normalized.indexOf('}', i + 1);
      if (end > i) {
        const alternatives = normalized
          .slice(i + 1, end)
          .split(',')
          .map((item) => item.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'));
        source += `(?:${alternatives.join('|')})`;
        i = end;
        continue;
      }
    }
    if ('\\^$.|+()[]{}'.includes(char)) {
      source += `\\${char}`;
      continue;
    }
    source += char;
  }
  return new RegExp(`^${source}$`, 'i');
}

export function globMatch(pattern: string, relativePath: string): boolean {
  return globToRegExp(pattern).test(relativePath.replace(/\\/g, '/'));
}

export function matchesAny(patterns: readonly string[], relativePath: string): boolean {
  return patterns.some((pattern) => globMatch(pattern, relativePath));
}
