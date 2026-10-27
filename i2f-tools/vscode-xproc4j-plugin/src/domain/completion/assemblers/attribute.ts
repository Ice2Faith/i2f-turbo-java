import type { IndexedEntry, IndexedParam } from '../../index/index-model';
import { BUILTIN_ATTRIBUTES, RESERVED_PARAM_NAMES } from '../../metadata/builtin-attributes';
import type { XProcDocumentModel } from '../../types';
import type { CompletionContext } from '../context';
import type { CompletionEntry } from '../text-edit';
import { assembleFeatures } from './feature';
import type { CompletionRuntime } from './runtime';
import { displayUri } from './runtime';

const PARAM_TAGS = new Set(['procedure', 'script-segment']);
const CALL_TAGS = new Set(['procedure-call', 'function-call', 'script-include']);
const MAX_TEMPLATE_PARAMS = 30;

interface ParamAggregate {
  name: string;
  features: Set<string>;
  source: string;
}

function mergeParam(map: Map<string, ParamAggregate>, name: string, features: string[], source: string): void {
  if (!name || RESERVED_PARAM_NAMES.has(name)) {
    return;
  }
  const existing = map.get(name) ?? { name, features: new Set<string>(), source };
  for (const feature of features) {
    existing.features.add(feature);
  }
  map.set(name, existing);
}

function currentDocumentParams(model: XProcDocumentModel): Map<string, ParamAggregate> {
  const result = new Map<string, ParamAggregate>();
  for (const element of model.scan.elements) {
    if (!PARAM_TAGS.has(element.name)) {
      continue;
    }
    for (const attribute of element.attributes) {
      mergeParam(result, attribute.baseName, attribute.features, '当前文件');
    }
  }
  return result;
}

function indexParams(runtime: CompletionRuntime): Map<string, ParamAggregate> {
  const result = new Map<string, ParamAggregate>();
  if (!runtime.index) {
    return result;
  }
  for (const item of runtime.index.getAllParams(500)) {
    for (const param of item.params) {
      mergeParam(result, param.name, param.features, `工程索引 · ${displayUri(item.entry.uri)}`);
    }
  }
  return result;
}

function paramEntries(params: Map<string, ParamAggregate>, group: string): CompletionEntry[] {
  const entries: CompletionEntry[] = [];
  for (const param of params.values()) {
    const featureChains = new Set<string>();
    if (param.features.size) {
      featureChains.add([...param.features].sort().join('.'));
    }
    entries.push({
      label: param.name,
      kind: 'property',
      insertText: param.name,
      filterText: param.name,
      sortText: `${group}_0_${param.name}`,
      detail: `参数 · ${param.source}`,
      documentation: `业务参数 ${param.name}`,
    });
    let added = 0;
    for (const chain of featureChains) {
      if (added >= 3) {
        break;
      }
      const name = `${param.name}.${chain}`;
      entries.push({
        label: name,
        kind: 'property',
        insertText: name,
        filterText: name,
        sortText: `${group}_1_${name}`,
        detail: `参数 · ${param.source}`,
        documentation: `业务参数 ${param.name} 的修饰组合`,
      });
      added++;
    }
  }
  return entries;
}

function templateEntry(params: IndexedParam[], label: string, group: string): CompletionEntry | undefined {
  if (params.length < 1 || params.length > MAX_TEMPLATE_PARAMS) {
    return undefined;
  }
  const lines = params.map((param, index) => `${param.name}="$${index + 1}"`);
  return {
    label,
    kind: 'snippet',
    insertText: `${lines.join('\n')}\n$0`,
    filterText: 'params',
    sortText: `${group}_9_template`,
    detail: `参数模板 · ${params.length} 项`,
    documentation: params.map((param) => param.name).join(', '),
    insertTextFormat: 'snippet',
  };
}

function findRefid(context: Extract<CompletionContext, { kind: 'attribute' }>): string {
  const attribute = context.partialAttributes.find((item) => item.name === 'refid');
  const value = attribute?.value.trim() ?? '';
  if (!value || /[$#{]/.test(value)) {
    return '';
  }
  return value;
}

function builtinEntries(sortGroup: string): CompletionEntry[] {
  return BUILTIN_ATTRIBUTES.map((name) => ({
    label: name,
    kind: 'property' as const,
    insertText: name,
    filterText: name,
    sortText: `${sortGroup}_${name}`,
    detail: '通用属性',
    documentation: 'XProc4J 通用属性',
  }));
}

export function assembleAttributes(
  context: Extract<CompletionContext, { kind: 'attribute' }>,
  model: XProcDocumentModel,
  runtime: CompletionRuntime,
): CompletionEntry[] {
  if (context.featurePrefix !== null) {
    return assembleFeatures(context, runtime);
  }

  const existing = new Set(
    context.partialAttributes
      .map((attribute) => attribute.name.split('.')[0] ?? attribute.name)
      .filter((name) => name !== context.baseName),
  );
  const entries: CompletionEntry[] = [];

  if (!context.tag) {
    return builtinEntries('8_0');
  }

  for (const attribute of context.tag.attributes) {
    if (existing.has(attribute.name)) {
      continue;
    }
    const known = runtime.catalog.getKnownAttribute(attribute.name);
    const features = (known?.features ?? attribute.features ?? []).slice(0, 5);
    entries.push({
      label: attribute.name,
      kind: 'property',
      insertText: attribute.name,
      filterText: attribute.name,
      sortText: `0_0_${attribute.name}`,
      detail: `属性 · ${context.tagName}`,
      documentation: features.length
        ? `常见修饰：${features.join(', ')}`
        : `XProc4J 属性 ${attribute.name}`,
    });
  }

  for (const entry of builtinEntries('0_8')) {
    if (!existing.has(entry.label) && !entries.some((item) => item.label === entry.label)) {
      entries.push(entry);
    }
  }

  if (PARAM_TAGS.has(context.tagName) || context.tag.openAttributes) {
    const params = currentDocumentParams(model);
    for (const [name, value] of indexParams(runtime)) {
      if (!params.has(name)) {
        params.set(name, value);
      }
    }
    entries.push(...paramEntries(params, '1_2'));
    const template = templateEntry(
      [...params.values()].map((param) => ({ name: param.name, features: [...param.features] })),
      `参数模板（${params.size} 项）`,
      '1_2',
    );
    if (template) {
      entries.push(template);
    }
  }

  if (CALL_TAGS.has(context.tagName) && runtime.index) {
    const refid = findRefid(context);
    if (refid) {
      const target = runtime.index.getParams(refid, model.uri);
      if (target) {
        for (const entry of paramEntries(
          new Map(target.params.map((param) => [
            param.name,
            { name: param.name, features: new Set(param.features), source: `目标 ${refid}` },
          ])),
          '1_0',
        )) {
          entries.push(entry);
        }
        const template = templateEntry(target.params, `目标参数模板（${target.params.length} 项）`, '1_0');
        if (template) {
          entries.push(template);
        }
      }
    }
  }

  if (entries.length === 0) {
    return builtinEntries('8_0');
  }
  return entries;
}

export function indexedEntrySummary(entry: IndexedEntry): string {
  return `${entry.kind} ${entry.id} · ${displayUri(entry.uri)}`;
}
