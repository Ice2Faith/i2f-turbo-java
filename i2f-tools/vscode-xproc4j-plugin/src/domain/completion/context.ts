import type { MetadataCatalog } from '../metadata/catalog';
import type { TagMeta } from '../metadata/types';
import type { Quote, TextRange, XProcDocumentModel, XmlElementModel } from '../types';
import { findEnclosingElement } from '../types';
import { isOffsetProtected } from '../xml/scanner';
import { findStartTagOffset, parseStartTagPrefix } from '../xml/start-tag-prefix';
import type { PartialAttribute } from '../xml/start-tag-prefix';
import { currentWord, findVariablePrefix, valueSegment } from './text-edit';
import type { VariablePrefix } from './text-edit';

export type ValueKind =
  | 'refid'
  | 'propagation'
  | 'isolation'
  | 'boolean'
  | 'time-unit'
  | 'jdbc-type'
  | 'date-pattern'
  | 'method'
  | 'database'
  | 'datasource'
  | 'result-type'
  | 'language-id'
  | 'numeric'
  | 'eval-java'
  | 'eval-js'
  | 'eval-groovy'
  | 'variable'
  | 'plain';

export type CompletionContext =
  | { kind: 'none'; reason: string }
  | { kind: 'tag'; prefix: string; replaceRange: TextRange }
  | {
      kind: 'attribute';
      prefix: string;
      baseName: string;
      featurePrefix: string | null;
      replaceRange: TextRange;
      tag: TagMeta | undefined;
      tagName: string;
      elementIndex: number;
      partialAttributes: PartialAttribute[];
    }
  | {
      kind: 'value';
      attributeName: string;
      baseName: string;
      features: string[];
      prefix: string;
      replaceRange: TextRange;
      valueRange: TextRange;
      quote: Quote;
      tag: TagMeta | undefined;
      tagName: string;
      valueKind: ValueKind;
      element: XmlElementModel | undefined;
    }
  | {
      kind: 'text';
      prefix: string;
      replaceRange: TextRange;
      sqlRegion: boolean;
      variablePrefix: VariablePrefix | null;
      element: XmlElementModel | undefined;
    };

const NUMERIC_FEATURES = new Set(['int', 'long', 'short', 'byte', 'float', 'double', 'char']);
const SQL_LANG_SPLIT = /[,;|/]/;

function attributeFeatures(attributeName: string): { baseName: string; features: string[] } {
  const parts = attributeName.split('.');
  return { baseName: parts[0] ?? attributeName, features: parts.slice(1) };
}

export function resolveValueKind(
  attributeName: string,
  features: string[],
  catalog: MetadataCatalog,
): ValueKind {
  const { baseName } = attributeFeatures(attributeName);
  const lastFeature = features[features.length - 1];
  switch (baseName) {
    case 'refid':
      return 'refid';
    case 'propagation':
      return 'propagation';
    case 'isolation':
      return 'isolation';
    case 'time-unit':
      return 'time-unit';
    case 'jdbc-type':
    case 'jdbcType':
      return 'jdbc-type';
    case 'pattern':
    case 'date-format':
    case 'format':
      return 'date-pattern';
    case 'method':
      return 'method';
    case 'database':
    case 'databases':
      return 'database';
    case 'datasource':
    case 'datasources':
      return 'datasource';
    case 'result-type':
    case 'resultType':
    case 'parameter-type':
    case 'parameterType':
      return 'result-type';
    case '_lang':
      return 'language-id';
    default:
      break;
  }
  if (catalog.getEnums().booleanAttributes.includes(baseName) || lastFeature === 'boolean') {
    return 'boolean';
  }
  if (lastFeature === 'eval-java') {
    return 'eval-java';
  }
  if (lastFeature === 'eval-js') {
    return 'eval-js';
  }
  if (lastFeature === 'eval-groovy') {
    return 'eval-groovy';
  }
  if (lastFeature && NUMERIC_FEATURES.has(lastFeature)) {
    return 'numeric';
  }
  return 'variable';
}

function hasSqlLang(element: XmlElementModel): boolean {
  const lang = element.attributes.find((attribute) => attribute.baseName === '_lang');
  if (!lang) {
    return false;
  }
  return lang.value
    .split(SQL_LANG_SPLIT)
    .some((value) => value.trim().toLowerCase() === 'sql');
}

export function isSqlRegion(element: XmlElementModel | undefined, model: XProcDocumentModel, catalog: MetadataCatalog): boolean {
  let current = element;
  while (current) {
    if (catalog.isExcludedBodyTag(current.name)) {
      return false;
    }
    if (catalog.isSqlBodyTag(current.name)) {
      return true;
    }
    if (catalog.isDynamicTag(current.name)) {
      current = current.parentIndex >= 0 ? model.scan.elements[current.parentIndex] : undefined;
      continue;
    }
    if (hasSqlLang(current)) {
      return true;
    }
    current = current.parentIndex >= 0 ? model.scan.elements[current.parentIndex] : undefined;
  }
  return false;
}

function isInsideClosingTag(element: XmlElementModel | undefined, offset: number): boolean {
  // 光标正好停在结束标签的 `<` 之前时仍属于正文，只有进入结束标签内部才禁用补全。
  return Boolean(element && element.endTagStart >= 0 && offset > element.endTagStart && offset <= element.endOffset);
}

export function resolveCompletionContext(
  text: string,
  offset: number,
  model: XProcDocumentModel,
  catalog: MetadataCatalog,
): CompletionContext {
  const clamped = Math.max(0, Math.min(offset, text.length));
  if (isOffsetProtected(model.scan.protectedRanges, clamped)) {
    return { kind: 'none', reason: 'protected' };
  }

  const startTagOffset = findStartTagOffset(text, clamped);
  if (startTagOffset !== undefined) {
    const prefix = parseStartTagPrefix(text, startTagOffset, clamped);
    const tagName = prefix.tagName;
    const tag = catalog.getTag(tagName);
    const elementIndex = model.scan.elements.findIndex(
      (element) => element.startOffset === startTagOffset,
    );
    const element = elementIndex >= 0 ? model.scan.elements[elementIndex] : undefined;

    if (prefix.state === 'invalid') {
      return { kind: 'none', reason: 'invalid-start-tag' };
    }
    if (prefix.state === 'tag-name') {
      return {
        kind: 'tag',
        prefix: text.slice(prefix.tagNameRange.start, Math.min(clamped, prefix.tagNameRange.end)),
        replaceRange: {
          start: prefix.tagNameRange.start,
          end: Math.max(clamped, prefix.tagNameRange.end),
        },
      };
    }
    if (prefix.state === 'closed') {
      return textContext(text, clamped, model, catalog);
    }

    if (prefix.state === 'after-value') {
      return {
        kind: 'attribute',
        prefix: '',
        baseName: '',
        featurePrefix: null,
        replaceRange: { start: clamped, end: clamped },
        tag,
        tagName,
        elementIndex,
        partialAttributes: prefix.attributes,
      };
    }

    if (prefix.state === 'attr-name' || prefix.state === 'after-attr-name') {
      const attributeName = prefix.currentAttributeName;
      const dot = attributeName.lastIndexOf('.');
      const baseName = dot >= 0 ? attributeName.slice(0, dot) : attributeName;
      const featurePrefix = dot >= 0 ? attributeName.slice(dot + 1) : null;
      const range = prefix.currentAttributeRange;
      return {
        kind: 'attribute',
        prefix: attributeName,
        baseName,
        featurePrefix,
        replaceRange: {
          start: range.start,
          end: Math.max(range.end, clamped),
        },
        tag,
        tagName,
        elementIndex,
        partialAttributes: prefix.attributes,
      };
    }

    const attributeName = prefix.currentAttributeName;
    const { baseName, features } = attributeFeatures(attributeName);
    const valueRange = prefix.currentValueRange;
    const valueKind = resolveValueKind(attributeName, features, catalog);
    const separators = catalog.getSeparatorsForAttribute(baseName);
    const replaceRange =
      separators && separators.length
        ? valueSegment(text, valueRange, clamped, separators)
        : valueRange;
    const prefixText = text.slice(replaceRange.start, clamped);
    return {
      kind: 'value',
      attributeName,
      baseName,
      features,
      prefix: prefixText,
      replaceRange,
      valueRange,
      quote: prefix.currentQuote,
      tag,
      tagName,
      valueKind,
      element,
    };
  }

  return textContext(text, clamped, model, catalog);
}

function textContext(
  text: string,
  offset: number,
  model: XProcDocumentModel,
  catalog: MetadataCatalog,
): CompletionContext {
  const element = findEnclosingElement(model, offset);
  if (isInsideClosingTag(element, offset)) {
    return { kind: 'none', reason: 'closing-tag' };
  }
  const word = currentWord(text, offset);
  return {
    kind: 'text',
    prefix: word.word,
    replaceRange: word.range,
    sqlRegion: isSqlRegion(element, model, catalog),
    variablePrefix: findVariablePrefix(text, offset),
    element,
  };
}
