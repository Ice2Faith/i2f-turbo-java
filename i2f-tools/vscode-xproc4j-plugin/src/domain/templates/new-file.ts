const INVALID_ID_CHARS = /[<>&"'/\\\s\u0000-\u001f\u007f]/;
const INVALID_FILE_CHARS = /[/\\:*?"<>|]/;

export interface ValidationResult {
  valid: boolean;
  message?: string;
}

export function validateProcedureId(raw: string): ValidationResult {
  const value = raw.trim();
  if (!value) {
    return { valid: false, message: '请输入 procedure id' };
  }
  if (value.length > 128) {
    return { valid: false, message: 'id 不能超过 128 个字符' };
  }
  if (INVALID_ID_CHARS.test(value)) {
    return { valid: false, message: 'id 不能包含空白、控制字符或 < > & " \' / \\' };
  }
  if (value.startsWith('.')) {
    return { valid: false, message: 'id 不能以点开头' };
  }
  return { valid: true };
}

export function normalizeFileName(raw: string): string {
  const value = raw.trim();
  if (!value) {
    return value;
  }
  return value.toLowerCase().endsWith('.xml') ? value : `${value}.xml`;
}

export function validateFileName(raw: string): ValidationResult {
  const value = raw.trim();
  if (!value) {
    return { valid: false, message: '请输入文件名' };
  }
  if (value === '.' || value === '..') {
    return { valid: false, message: '文件名无效' };
  }
  if (INVALID_FILE_CHARS.test(value)) {
    return { valid: false, message: '文件名不能包含路径分隔符或非法字符' };
  }
  if (/[. ]$/.test(value)) {
    return { valid: false, message: '文件名不能以空格或点结尾' };
  }
  const extension = /\.[^.]+$/.exec(value);
  if (extension && extension[0].toLowerCase() !== '.xml') {
    return { valid: false, message: 'XProc4J 文件必须使用 .xml 扩展名' };
  }
  return { valid: true };
}
