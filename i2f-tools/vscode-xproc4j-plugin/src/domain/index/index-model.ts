import type { LineRange } from '../types';

export interface IndexedParam {
  name: string;
  features: string[];
}

export interface IndexedEntry {
  id: string;
  kind: 'procedure' | 'script-segment';
  uri: string;
  range: LineRange;
  idRange: LineRange;
  parentId?: string;
  params: IndexedParam[];
}

export interface IndexedRef {
  refid: string;
  uri: string;
  range: LineRange;
  tagName: string;
}

export interface FileIndex {
  uri: string;
  version: number;
  entries: IndexedEntry[];
  refs: IndexedRef[];
  rootTag: string | null;
  skippedReason?: 'too-large' | 'binary' | 'parse-error' | 'not-xproc';
}

export interface IndexStats {
  fileCount: number;
  scannedFiles: number;
  skippedFiles: number;
  entryCount: number;
  refCount: number;
  bytes: number;
  durationMs: number;
  truncatedFiles: boolean;
  truncatedEntries: boolean;
}

export interface IndexSnapshot {
  generation: number;
  files: Map<string, FileIndex>;
  byId: Map<string, IndexedEntry[]>;
  refsById: Map<string, IndexedRef[]>;
  allIds: Set<string>;
  truncated: boolean;
  builtAt: number;
  stats: IndexStats;
}

export function emptyStats(): IndexStats {
  return {
    fileCount: 0,
    scannedFiles: 0,
    skippedFiles: 0,
    entryCount: 0,
    refCount: 0,
    bytes: 0,
    durationMs: 0,
    truncatedFiles: false,
    truncatedEntries: false,
  };
}

export function emptySnapshot(generation = 0): IndexSnapshot {
  return {
    generation,
    files: new Map(),
    byId: new Map(),
    refsById: new Map(),
    allIds: new Set(),
    truncated: false,
    builtAt: Date.now(),
    stats: emptyStats(),
  };
}

function pushMap<K, V>(map: Map<K, V[]>, key: K, value: V): void {
  const list = map.get(key);
  if (list) {
    list.push(value);
  } else {
    map.set(key, [value]);
  }
}

function compareRange(a: IndexedEntry, b: IndexedEntry): number {
  return a.uri.localeCompare(b.uri) || a.range.start.line - b.range.start.line || a.range.start.character - b.range.start.character;
}

export function buildSnapshot(
  files: Iterable<FileIndex>,
  generation: number,
  stats: Partial<IndexStats> = {},
  truncated = false,
): IndexSnapshot {
  const fileMap = new Map<string, FileIndex>();
  const byId = new Map<string, IndexedEntry[]>();
  const refsById = new Map<string, IndexedRef[]>();
  const allIds = new Set<string>();
  let skippedFiles = 0;

  for (const file of files) {
    fileMap.set(file.uri, file);
    if (file.skippedReason) {
      skippedFiles++;
    }
    for (const entry of file.entries) {
      allIds.add(entry.id);
      pushMap(byId, entry.id, entry);
    }
    for (const ref of file.refs) {
      pushMap(refsById, ref.refid, ref);
    }
  }
  for (const list of byId.values()) {
    list.sort(compareRange);
  }
  for (const list of refsById.values()) {
    list.sort((a, b) => a.uri.localeCompare(b.uri) || a.range.start.line - b.range.start.line);
  }

  return {
    generation,
    files: fileMap,
    byId,
    refsById,
    allIds,
    truncated,
    builtAt: Date.now(),
    stats: {
      ...emptyStats(),
      fileCount: fileMap.size,
      skippedFiles,
      entryCount: [...byId.values()].reduce((sum, list) => sum + list.length, 0),
      refCount: [...refsById.values()].reduce((sum, list) => sum + list.length, 0),
      ...stats,
      truncatedFiles: stats.truncatedFiles ?? truncated,
    },
  };
}
