import { BUILTIN_VARIABLES } from './builtin-variables';
import type {
  AttributeMeta,
  EnumBundle,
  FeatureMeta,
  InjectionBundle,
  InjectionMeta,
  LangDynamicTarget,
  MetadataBundle,
  SnippetDefinition,
  TagMeta,
} from './types';
import { METADATA } from '../../generated/metadata';

const ENUM_BY_ATTRIBUTE: Record<string, keyof EnumBundle> = {
  propagation: 'propagation',
  isolation: 'isolation',
  'time-unit': 'timeUnit',
  'jdbc-type': 'jdbcType',
  jdbcType: 'jdbcType',
  pattern: 'datePatterns',
  'date-format': 'datePatterns',
  format: 'datePatterns',
  method: 'methods',
  database: 'databases',
  databases: 'databases',
  datasource: 'datasources',
  datasources: 'datasources',
  'result-type': 'resultTypes',
  resultType: 'resultTypes',
  'parameter-type': 'resultTypes',
  parameterType: 'resultTypes',
  _lang: 'languageIds',
};

export class MetadataCatalog {
  private readonly tags = new Map<string, TagMeta>();
  private readonly attributes = new Map<string, AttributeMeta>();
  private readonly tagList: TagMeta[];
  private readonly attributeList: AttributeMeta[];
  private readonly features: FeatureMeta[];
  private readonly featureNames: Set<string>;
  private readonly bodyInjectionByTag = new Map<string, InjectionMeta>();
  private readonly dynamicTags: Set<string>;
  private readonly langDynamicByTag = new Map<string, Record<string, LangDynamicTarget>>();
  private readonly snippets: SnippetDefinition[];
  private readonly booleanAttributeSet: Set<string>;
  private readonly excludedBodyTagSet: Set<string>;

  constructor(private readonly bundle: MetadataBundle) {
    this.tagList = [...bundle.tags].sort((a, b) => a.name.localeCompare(b.name));
    this.attributeList = [...bundle.attributes].sort((a, b) => a.name.localeCompare(b.name));
    this.features = bundle.features;
    this.featureNames = new Set(bundle.features.map((feature) => feature.name));
    this.snippets = bundle.snippets;
    this.booleanAttributeSet = new Set(bundle.enums.booleanAttributes);
    this.excludedBodyTagSet = new Set(bundle.injections.excludedBodyTags);
    for (const tag of this.tagList) {
      this.tags.set(tag.name, tag);
    }
    for (const attribute of this.attributeList) {
      this.attributes.set(attribute.name, attribute);
    }
    const injections: InjectionBundle = bundle.injections;
    for (const rule of injections.body) {
      for (const tag of rule.tags) {
        this.bodyInjectionByTag.set(tag, rule);
      }
    }
    // 动态 SQL 标签以标签元数据为准，配置缺省时仍能正确识别。
    this.dynamicTags = new Set([
      ...(injections.dynamicSqlTags ?? []),
      ...this.tagList.filter((tag) => tag.source === 'dynamic').map((tag) => tag.name),
    ]);
    for (const tag of injections.langDynamic.tags) {
      this.langDynamicByTag.set(tag, injections.langDynamic.values);
    }
  }

  getTags(): readonly TagMeta[] {
    return this.tagList;
  }

  getAttributes(): readonly AttributeMeta[] {
    return this.attributeList;
  }

  getFeatures(): readonly FeatureMeta[] {
    return this.features;
  }

  getSnippets(): readonly SnippetDefinition[] {
    return this.snippets;
  }

  getBuiltinVariables(): readonly string[] {
    return BUILTIN_VARIABLES;
  }

  getTag(name: string): TagMeta | undefined {
    return this.tags.get(name);
  }

  getKnownAttribute(name: string): AttributeMeta | undefined {
    return this.attributes.get(name);
  }

  isKnownTag(name: string): boolean {
    return this.tags.has(name);
  }

  isKnownAttribute(name: string): boolean {
    return this.attributes.has(name);
  }

  isOpenTag(name: string): boolean {
    return this.tags.get(name)?.openAttributes ?? false;
  }

  isDynamicTag(name: string): boolean {
    return this.dynamicTags.has(name);
  }

  getDynamicTags(): ReadonlySet<string> {
    return this.dynamicTags;
  }

  isFeature(name: string): boolean {
    return this.featureNames.has(name);
  }

  getBodyInjection(tag: string): InjectionMeta | undefined {
    return this.bodyInjectionByTag.get(tag);
  }

  isSqlBodyTag(tag: string): boolean {
    return this.bodyInjectionByTag.get(tag)?.id === 'sql';
  }

  isExcludedBodyTag(tag: string): boolean {
    return this.excludedBodyTagSet.has(tag);
  }

  getLangDynamic(tag: string): Record<string, LangDynamicTarget> | undefined {
    return this.langDynamicByTag.get(tag);
  }

  getEnumsForAttribute(name: string): readonly string[] | undefined {
    if (this.booleanAttributeSet.has(name)) {
      return ['true', 'false'];
    }
    const kind = ENUM_BY_ATTRIBUTE[name];
    if (!kind) {
      return undefined;
    }
    const value = this.bundle.enums[kind];
    return Array.isArray(value) ? value : undefined;
  }

  getEnums(): EnumBundle {
    return this.bundle.enums;
  }

  getSqlKeywords(): readonly string[] {
    return this.bundle.keywords.sqlKeywords;
  }

  getSqlFunctions(): readonly string[] {
    return this.bundle.keywords.sqlFunctions;
  }

  getSeparatorsForAttribute(name: string): readonly string[] | undefined {
    if (name === 'databases') {
      return this.bundle.enums.multiValueSeparators.databases;
    }
    if (name === 'datasource' || name === 'datasources') {
      return this.bundle.enums.multiValueSeparators.datasources;
    }
    if (name === '_lang') {
      return this.bundle.enums.multiValueSeparators._lang;
    }
    return undefined;
  }
}

let cached: MetadataCatalog | undefined;

export function getCatalog(): MetadataCatalog {
  if (!cached) {
    cached = new MetadataCatalog(METADATA);
  }
  return cached;
}
