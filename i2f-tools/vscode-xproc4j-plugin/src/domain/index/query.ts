import type {
  FileIndex,
  IndexSnapshot,
  IndexStats,
  IndexedEntry,
  IndexedParam,
  IndexedRef,
} from './index-model';
import { buildSnapshot, emptySnapshot } from './index-model';
import { normalizeUriKey } from './uri-key';

export type IndexState = 'idle' | 'building' | 'ready' | 'failed' | 'disabled';

export interface IdCandidate {
  id: string;
  uri: string;
  count: number;
}

export interface IndexQuery {
  findDefinitions(id: string, currentUri?: string): IndexedEntry[];
  findReferences(id: string): IndexedRef[];
  getEntry(uri: string, id: string): IndexedEntry | undefined;
  getParams(refid: string, currentUri?: string): { entry: IndexedEntry; params: IndexedParam[] } | undefined;
  getAllIds(limit: number, currentUri?: string): IdCandidate[];
  getAllParams(limit: number): Array<{ entry: IndexedEntry; params: IndexedParam[] }>;
  peekId(id: string): boolean;
  getStats(): IndexStats;
  getState(): IndexState;
}

function sortEntries(entries: IndexedEntry[], currentUri?: string): IndexedEntry[] {
  const current = currentUri ? normalizeUriKey(currentUri) : undefined;
  return entries.sort((a, b) => {
    const aCurrent = a.uri === current ? 0 : 1;
    const bCurrent = b.uri === current ? 0 : 1;
    return (
      aCurrent - bCurrent ||
      a.uri.localeCompare(b.uri) ||
      a.range.start.line - b.range.start.line ||
      a.range.start.character - b.range.start.character
    );
  });
}

/**
 * 只读查询视图：磁盘快照 + 打开文档覆盖层。
 * 覆盖层始终优先，并且即使覆盖层为空也会屏蔽同 URI 的旧磁盘结果。
 */
export class IndexStore implements IndexQuery {
  private base: IndexSnapshot = emptySnapshot();
  private readonly overlays = new Map<string, FileIndex>();
  private merged: { byId: Map<string, IndexedEntry[]>; refsById: Map<string, IndexedRef[]>; allIds: Set<string> } | undefined;
  private stateValue: IndexState = 'idle';

  setBase(snapshot: IndexSnapshot): void {
    this.base = snapshot;
    this.merged = undefined;
  }

  getBase(): IndexSnapshot {
    return this.base;
  }

  updateOverlay(file: FileIndex): void {
    this.overlays.set(normalizeUriKey(file.uri), file);
    this.merged = undefined;
  }

  removeOverlay(uri: string): void {
    this.overlays.delete(normalizeUriKey(uri));
    this.merged = undefined;
  }

  getOverlay(uri: string): FileIndex | undefined {
    return this.overlays.get(normalizeUriKey(uri));
  }

  clearOverlays(): void {
    this.overlays.clear();
    this.merged = undefined;
  }

  setState(state: IndexState): void {
    this.stateValue = state;
  }

  getState(): IndexState {
    return this.stateValue;
  }

  private computeMerged() {
    if (this.merged) {
      return this.merged;
    }
    const files: FileIndex[] = [];
    for (const [uri, file] of this.base.files) {
      if (!this.overlays.has(uri)) {
        files.push(file);
      }
    }
    for (const file of this.overlays.values()) {
      files.push(file);
    }
    const snapshot = buildSnapshot(files, this.base.generation);
    this.merged = {
      byId: snapshot.byId,
      refsById: snapshot.refsById,
      allIds: snapshot.allIds,
    };
    return this.merged;
  }

  findDefinitions(id: string, currentUri?: string): IndexedEntry[] {
    if (!id) {
      return [];
    }
    const list = this.computeMerged().byId.get(id) ?? [];
    return sortEntries([...list], currentUri);
  }

  findReferences(id: string): IndexedRef[] {
    if (!id) {
      return [];
    }
    return [...(this.computeMerged().refsById.get(id) ?? [])];
  }

  getEntry(uri: string, id: string): IndexedEntry | undefined {
    const key = normalizeUriKey(uri);
    const overlay = this.overlays.get(key);
    if (overlay) {
      const found = overlay.entries.find((entry) => entry.id === id);
      if (found) {
        return found;
      }
    }
    return (this.computeMerged().byId.get(id) ?? []).find((entry) => entry.uri === key);
  }

  getParams(refid: string, currentUri?: string): { entry: IndexedEntry; params: IndexedParam[] } | undefined {
    const entry = this.findDefinitions(refid, currentUri)[0];
    return entry ? { entry, params: entry.params } : undefined;
  }

  getAllParams(limit: number): Array<{ entry: IndexedEntry; params: IndexedParam[] }> {
    const result: Array<{ entry: IndexedEntry; params: IndexedParam[] }> = [];
    for (const entries of this.computeMerged().byId.values()) {
      for (const entry of entries) {
        if (!entry.params.length) {
          continue;
        }
        result.push({ entry, params: entry.params });
        if (result.length >= limit) {
          return result;
        }
      }
    }
    return result;
  }

  getAllIds(limit: number, currentUri?: string): IdCandidate[] {
    const current = currentUri ? normalizeUriKey(currentUri) : undefined;
    const byId = this.computeMerged().byId;
    const result: IdCandidate[] = [];
    for (const [id, entries] of byId) {
      if (!entries.length) {
        continue;
      }
      const preferred = entries.find((entry) => entry.uri === current) ?? entries[0]!;
      result.push({ id, uri: preferred.uri, count: entries.length });
    }
    result.sort((a, b) => {
      const aCurrent = a.uri === current ? 0 : 1;
      const bCurrent = b.uri === current ? 0 : 1;
      return aCurrent - bCurrent || a.id.localeCompare(b.id);
    });
    return result.slice(0, Math.max(0, limit));
  }

  peekId(id: string): boolean {
    return this.computeMerged().allIds.has(id);
  }

  getStats(): IndexStats {
    const baseStats = this.base.stats;
    if (this.overlays.size === 0) {
      return baseStats;
    }
    // 覆盖层替换同名文件贡献，而不是简单相加。
    let fileCount = 0;
    let entryCount = 0;
    let refCount = 0;
    let skippedFiles = 0;
    for (const [uri, file] of this.base.files) {
      if (this.overlays.has(uri)) {
        continue;
      }
      fileCount++;
      entryCount += file.entries.length;
      refCount += file.refs.length;
      if (file.skippedReason) {
        skippedFiles++;
      }
    }
    for (const file of this.overlays.values()) {
      fileCount++;
      entryCount += file.entries.length;
      refCount += file.refs.length;
      if (file.skippedReason) {
        skippedFiles++;
      }
    }
    return {
      ...baseStats,
      fileCount,
      scannedFiles: baseStats.scannedFiles + this.overlays.size,
      skippedFiles,
      entryCount,
      refCount,
    };
  }
}
