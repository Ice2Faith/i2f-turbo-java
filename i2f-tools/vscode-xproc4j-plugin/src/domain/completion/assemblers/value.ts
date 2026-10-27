import type { XProcDocumentModel } from '../../types';
import type { CompletionContext } from '../context';
import type { CompletionEntry, CompletionKind } from '../text-edit';
import type { CompletionRuntime } from './runtime';
import { displayUri } from './runtime';

interface EnumPresentation {
  kind: CompletionKind;
  detail: string;
}

function enumPresentation(context: Extract<CompletionContext, { kind: 'value' }>): EnumPresentation {
  switch (context.valueKind) {
    case 'propagation':
      return { kind: 'enum', detail: '枚举 · propagation' };
    case 'isolation':
      return { kind: 'enum', detail: '枚举 · isolation' };
    case 'time-unit':
      return { kind: 'enum', detail: '枚举 · TimeUnit' };
    case 'jdbc-type':
      return { kind: 'enum', detail: '枚举 · JDBCType' };
    case 'database':
      return { kind: 'enum', detail: '枚举 · 数据库' };
    case 'datasource':
      return { kind: 'enum', detail: '枚举 · 数据源' };
    case 'result-type':
      return { kind: 'enum', detail: '枚举 · 结果类型' };
    case 'language-id':
      return { kind: 'enum', detail: '语言 ID' };
    case 'boolean':
      return { kind: 'constant', detail: '布尔值' };
    case 'date-pattern':
      return { kind: 'value', detail: '模板 · 日期格式' };
    case 'method':
      return { kind: 'function', detail: '方法' };
    default:
      return { kind: 'value', detail: '值' };
  }
}

function enumValues(context: Extract<CompletionContext, { kind: 'value' }>, runtime: CompletionRuntime): string[] {
  if (context.valueKind === 'boolean') {
    return ['true', 'false'];
  }
  return [...(runtime.catalog.getEnumsForAttribute(context.baseName) ?? [])];
}

export function assembleValues(
  context: Extract<CompletionContext, { kind: 'value' }>,
  model: XProcDocumentModel,
  runtime: CompletionRuntime,
): CompletionEntry[] {
  if (context.valueKind === 'refid') {
    if (!runtime.index) {
      return [];
    }
    return runtime.index.getAllIds(500, model.uri).map((candidate) => {
      const sameFile = candidate.uri === model.uri;
      const paths = runtime.index!.findDefinitions(candidate.id).map((entry) => displayUri(entry.uri));
      return {
        label: candidate.id,
        kind: 'reference' as const,
        insertText: candidate.id,
        filterText: candidate.id,
        sortText: `3_${sameFile ? '0' : '1'}_${candidate.id}`,
        detail:
          candidate.count > 1
            ? `定义 · ${displayUri(candidate.uri)} · 共 ${candidate.count} 处`
            : `定义 · ${displayUri(candidate.uri)}`,
        documentation: paths.slice(0, 10).join('\n'),
      };
    });
  }

  const values = enumValues(context, runtime);
  if (values.length === 0) {
    return [];
  }
  const presentation = enumPresentation(context);
  return values.map((value, index) => ({
    label: value,
    kind: presentation.kind,
    insertText: value,
    filterText: value,
    sortText: `3_0_${String(index).padStart(3, '0')}`,
    detail: presentation.detail,
    documentation:
      context.valueKind === 'language-id'
        ? `语言 ID ${value}；需安装对应语法扩展才能获得高亮。`
        : `${presentation.detail}：${value}`,
  }));
}
