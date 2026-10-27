export class TemplatePlaceholderError extends Error {
  constructor(public readonly placeholder: string) {
    super(`模板包含未知占位符：${placeholder}`);
    this.name = 'TemplatePlaceholderError';
  }
}

export interface ProcedureTemplateParams {
  id: string;
  author: string;
  date: string;
  time: string;
  eol: '\n' | '\r\n';
}

export function escapeXmlAttribute(value: string): string {
  return value
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&apos;');
}

export function sanitizeComment(value: string, fallback = 'unknown'): string {
  const cleaned = value
    .replace(/-->/g, '- -')
    .replace(/--/g, '- -')
    .replace(/[\r\n\u0000-\u001f\u007f]/g, ' ')
    .trim()
    .slice(0, 64);
  return cleaned || fallback;
}

export function renderProcedureTemplate(template: string, params: ProcedureTemplateParams): string {
  const values: Record<string, string> = {
    id: escapeXmlAttribute(params.id),
    author: sanitizeComment(params.author),
    date: params.date,
    time: params.time,
  };
  const rendered = template.replace(/\{\{(\w+)\}\}/g, (_all, key: string) => {
    const value = values[key];
    if (value === undefined) {
      throw new TemplatePlaceholderError(key);
    }
    return value;
  });
  const normalized = rendered.replace(/\r\n/g, '\n').replace(/\n+$/, '');
  return `${normalized}${params.eol}`;
}
