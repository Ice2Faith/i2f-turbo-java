import type { IndexQuery } from '../../index/query';
import type { MetadataCatalog } from '../../metadata/catalog';

export interface CompletionRuntime {
  catalog: MetadataCatalog;
  index?: IndexQuery;
  builtinVariables: boolean;
  sqlKeywords: boolean;
}

export function displayUri(uri: string): string {
  const withoutScheme = uri.replace(/^[a-z][a-z0-9+.-]*:\/\//i, '');
  const parts = withoutScheme.split('/').filter(Boolean);
  return parts.length > 2 ? parts.slice(-2).join('/') : withoutScheme;
}
