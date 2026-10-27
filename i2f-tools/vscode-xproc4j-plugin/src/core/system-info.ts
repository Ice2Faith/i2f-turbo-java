import * as os from 'node:os';

/** 作者名只来自扩展宿主环境，不读取工作区内容。 */
export function resolveAuthor(fallback = 'unknown'): string {
  try {
    return os.userInfo().username || fallback;
  } catch {
    return process.env.USER || process.env.USERNAME || fallback;
  }
}

function pad(value: number): string {
  return String(value).padStart(2, '0');
}

export function formatLocalDate(date: Date): string {
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}`;
}

export function formatLocalTime(date: Date): string {
  return `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}
