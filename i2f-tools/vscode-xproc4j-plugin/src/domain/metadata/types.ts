export type TagCategory =
  | 'root'
  | 'fragment'
  | 'call'
  | 'flow'
  | 'sql'
  | 'etl'
  | 'java'
  | 'script'
  | 'concurrent'
  | 'io'
  | 'string'
  | 'assign'
  | 'output'
  | 'log'
  | 'context'
  | 'event'
  | 'dynamic'
  | 'unknown';

export interface TagAttributeMeta {
  name: string;
  type: string;
  required?: boolean;
  features?: string[];
  rawNames?: string[];
}

export interface TagMeta {
  name: string;
  category: TagCategory;
  openAttributes: boolean;
  source: 'dtd' | 'consts' | 'dynamic' | 'builtin';
  attributes: TagAttributeMeta[];
  description?: string;
}

export interface AttributeMeta {
  name: string;
  type: string;
  features: string[];
  tags: string[];
  source: 'dtd' | 'consts' | 'builtin';
}

export interface FeatureMeta {
  name: string;
  group: string;
  description: string;
}

export interface InjectionMeta {
  id: string;
  tags: string[];
  contentName: string;
  scope?: string;
  languageId?: string;
  variables: boolean;
  dynamicSql?: boolean;
  childTags?: string[];
}

export interface LangDynamicTarget {
  scope?: string;
  contentName: string;
  languageId?: string;
}

export interface InjectionBundle {
  body: InjectionMeta[];
  langDynamic: { tags: string[]; values: Record<string, LangDynamicTarget> };
  attribute: Array<{ suffix: string; scope: string; contentName: string }>;
  excludedBodyTags: string[];
  varsOnlyBodyTags: string[];
  dynamicSqlTags?: string[];
}

export interface SnippetDefinition {
  prefix: string;
  description: string;
  body: string;
  sourceName: string;
}

export interface KeywordBundle {
  sqlKeywords: string[];
  sqlFunctions: string[];
}

export interface EnumBundle {
  propagation: string[];
  isolation: string[];
  booleanAttributes: string[];
  timeUnit: string[];
  jdbcType: string[];
  datePatterns: string[];
  methods: string[];
  databases: string[];
  datasources: string[];
  resultTypes: string[];
  languageIds: string[];
  multiValueSeparators: Record<string, string[]>;
}

export interface MetadataBundle {
  tags: TagMeta[];
  attributes: AttributeMeta[];
  features: FeatureMeta[];
  injections: InjectionBundle;
  enums: EnumBundle;
  keywords: KeywordBundle;
  snippets: SnippetDefinition[];
}
