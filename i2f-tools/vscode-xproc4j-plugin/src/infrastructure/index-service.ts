import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import type { Logger } from '../core/logger';
import type { StatusBar } from '../core/status';
import type { FileIndex, IndexSnapshot, IndexStats } from '../domain/index/index-model';
import { buildSnapshot, emptySnapshot, emptyStats } from '../domain/index/index-model';
import { indexFile } from '../domain/index/index-scan';
import { IndexStore } from '../domain/index/query';
import { normalizeUriKey } from '../domain/index/uri-key';
import { matchesAny } from './glob';

const BATCH_SIZE = 64;
const MAX_INDEX_ENTRIES = 500_000;
const DEBOUNCE_MS = 300;
const RETRY_DELAY_MS = 30_000;

export interface IndexServiceOptions {
  getConfig: () => XProcConfig;
  logger: Logger;
  status: StatusBar;
}

type ProgressReporter = (scanned: number, total: number) => void;

function yieldToEventLoop(): Promise<void> {
  return new Promise((resolve) => setImmediate(resolve));
}

function skipped(uri: string, reason: FileIndex['skippedReason']): FileIndex {
  return { uri, version: 0, entries: [], refs: [], rootTag: null, skippedReason: reason };
}

/**
 * 工作区索引服务：批量读盘、覆盖层、文件监听与原子快照替换。
 * 文档内容只在内存中扫描，不写入日志。
 */
export class IndexService implements vscode.Disposable {
  readonly store = new IndexStore();
  private files = new Map<string, FileIndex>();
  private stats: IndexStats = emptyStats();
  private generation = 0;
  private cancellation: vscode.CancellationTokenSource | undefined;
  private waiters: Array<(ready: boolean) => void> = [];
  private watchers: vscode.FileSystemWatcher[] = [];
  private readonly disposables: vscode.Disposable[] = [];
  private readonly overlayTimers = new Map<string, NodeJS.Timeout>();
  private readonly pendingDisk = new Set<string>();
  private diskTimer: NodeJS.Timeout | undefined;
  private retryAfter = 0;
  private disposed = false;
  private readonly changedEmitter = new vscode.EventEmitter<void>();
  readonly onDidChange = this.changedEmitter.event;

  constructor(private readonly options: IndexServiceOptions) {
    this.store.setState('idle');
    this.disposables.push(
      vscode.workspace.onDidRenameFiles((event) => {
        if (this.store.getState() === 'disabled') {
          return;
        }
        for (const file of event.files) {
          this.handleDelete(file.oldUri);
          this.onDiskEvent(file.newUri);
        }
      }),
    );
  }

  /** 惰性启动首次构建；重复调用无副作用。 */
  ensureStarted(): void {
    if (this.disposed) {
      return;
    }
    const config = this.options.getConfig();
    if (!config.indexEnabled) {
      if (this.store.getState() !== 'disabled') {
        this.disable();
      }
      return;
    }
    const state = this.store.getState();
    if (state === 'building' || state === 'ready') {
      return;
    }
    if (Date.now() < this.retryAfter) {
      return;
    }
    void this.startBuild();
  }

  async rebuild(
    progress?: vscode.Progress<{ message?: string }>,
    token?: vscode.CancellationToken,
  ): Promise<IndexSnapshot | undefined> {
    if (!this.options.getConfig().indexEnabled) {
      this.disable();
      return undefined;
    }
    this.retryAfter = 0;
    const subscription = token?.onCancellationRequested(() => this.cancelCurrentBuild());
    try {
      await this.startBuild((scanned, total) => {
        progress?.report({ message: `已扫描 ${scanned} / ${total}` });
      });
    } finally {
      subscription?.dispose();
    }
    if (token?.isCancellationRequested) {
      return undefined;
    }
    return this.store.getState() === 'ready' ? this.store.getBase() : undefined;
  }

  workspaceFoldersChanged(): void {
    if (this.disposed) {
      return;
    }
    this.disposeWatchers();
    if (!this.options.getConfig().indexEnabled) {
      this.disable();
      return;
    }
    if (this.store.getState() === 'disabled') {
      this.store.setState('idle');
    }
    void this.rebuild();
  }

  async whenReady(timeoutMs: number, token?: vscode.CancellationToken): Promise<boolean> {
    const state = this.store.getState();
    if (state === 'ready' || state === 'disabled') {
      return true;
    }
    if (state === 'failed') {
      return false;
    }
    this.ensureStarted();
    const current = this.store.getState();
    if (current === 'ready' || current === 'disabled') {
      return true;
    }
    if (current === 'failed') {
      return false;
    }
    if (token?.isCancellationRequested) {
      return false;
    }

    return new Promise<boolean>((resolve) => {
      let settled = false;
      const finish = (ready: boolean): void => {
        if (settled) {
          return;
        }
        settled = true;
        clearTimeout(timer);
        const index = this.waiters.indexOf(finish);
        if (index >= 0) {
          this.waiters.splice(index, 1);
        }
        subscription?.dispose();
        resolve(ready);
      };
      const timer = setTimeout(() => finish(false), Math.max(0, timeoutMs));
      const subscription = token?.onCancellationRequested(() => finish(false));
      this.waiters.push(finish);
    });
  }

  /** 打开文档时同步覆盖层（通常在大纲/补全前调用）。 */
  updateOverlay(document: vscode.TextDocument): void {
    this.writeOverlay(document);
  }

  /** 编辑时 300ms 防抖更新覆盖层。 */
  scheduleOverlay(document: vscode.TextDocument): void {
    if (this.disposed || document.languageId !== 'xml' || !this.options.getConfig().indexEnabled) {
      return;
    }
    const key = normalizeUriKey(document.uri.toString());
    const existing = this.overlayTimers.get(key);
    if (existing) {
      clearTimeout(existing);
    }
    this.overlayTimers.set(
      key,
      setTimeout(() => {
        this.overlayTimers.delete(key);
        this.writeOverlay(document);
      }, DEBOUNCE_MS),
    );
  }

  removeOverlay(uri: vscode.Uri): void {
    const key = normalizeUriKey(uri.toString());
    const timer = this.overlayTimers.get(key);
    if (timer) {
      clearTimeout(timer);
      this.overlayTimers.delete(key);
    }
    this.store.removeOverlay(key);
    this.changedEmitter.fire();
    if (this.store.getState() === 'ready') {
      this.scheduleDisk(uri);
    }
  }

  clearOverlays(): void {
    for (const timer of this.overlayTimers.values()) {
      clearTimeout(timer);
    }
    this.overlayTimers.clear();
    this.store.clearOverlays();
    this.changedEmitter.fire();
  }

  /** 配置关闭或释放资源时调用。 */
  disable(): void {
    this.cancelCurrentBuild();
    this.disposeWatchers();
    this.clearDiskTimers();
    this.files.clear();
    this.stats = emptyStats();
    this.store.setBase(emptySnapshot(++this.generation));
    this.store.clearOverlays();
    this.store.setState('disabled');
    this.options.status.set('disabled');
    this.resolveWaiters(true);
    this.changedEmitter.fire();
  }

  dispose(): void {
    if (this.disposed) {
      return;
    }
    this.disposed = true;
    this.cancelCurrentBuild();
    this.disposeWatchers();
    this.clearDiskTimers();
    for (const timer of this.overlayTimers.values()) {
      clearTimeout(timer);
    }
    this.overlayTimers.clear();
    for (const disposable of this.disposables) {
      disposable.dispose();
    }
    this.resolveWaiters(false);
    this.changedEmitter.dispose();
  }

  private writeOverlay(document: vscode.TextDocument): void {
    if (this.disposed || document.languageId !== 'xml') {
      return;
    }
    const key = normalizeUriKey(document.uri.toString());
    const config = this.options.getConfig();
    if (!config.indexEnabled) {
      return;
    }
    try {
      const text = document.getText();
      const file =
        text.length > config.maxDocumentSizeKB * 1024
          ? { ...skipped(key, 'too-large'), version: document.version }
          : indexFile(key, document.version, text);
      this.store.updateOverlay(file);
      this.changedEmitter.fire();
    } catch (error) {
      // 保留上一次覆盖层，避免编辑过程中补全瞬间失效。
      this.options.logger.warn(`更新文档覆盖层失败（${key}）：${String(error)}`);
    }
  }

  private startBuild(onProgress?: ProgressReporter): Promise<void> {
    this.cancelCurrentBuild();
    const cts = new vscode.CancellationTokenSource();
    this.cancellation = cts;
    this.store.setState('building');
    this.options.status.set('building');
    const promise = this.runBuild(cts, onProgress)
      .catch((error: unknown) => {
        if (cts.token.isCancellationRequested || this.cancellation !== cts) {
          return;
        }
        this.options.logger.error('构建工程索引失败', error);
        this.store.setState('failed');
        this.options.status.set('failed');
        this.retryAfter = Date.now() + RETRY_DELAY_MS;
        this.resolveWaiters(false);
      })
      .finally(() => {
        cts.dispose();
        if (this.cancellation === cts) {
          this.cancellation = undefined;
          if (cts.token.isCancellationRequested && this.store.getState() === 'building') {
            if (this.store.getBase().files.size) {
              this.store.setState('ready');
              this.options.status.set('ready', '已取消构建，保留旧索引');
            } else {
              this.store.setState('idle');
              this.options.status.set('idle');
            }
          }
        }
      });
    return promise;
  }

  private cancelCurrentBuild(): void {
    this.cancellation?.cancel();
  }

  private async runBuild(cts: vscode.CancellationTokenSource, onProgress?: ProgressReporter): Promise<void> {
    const token = cts.token;
    const started = Date.now();
    const config = this.options.getConfig();
    if (!config.indexEnabled) {
      this.disable();
      return;
    }
    this.ensureWatcher();

    const folders = vscode.workspace.workspaceFolders ?? [];
    if (!folders.length) {
      if (this.cancellation !== cts || token.isCancellationRequested) {
        return;
      }
      this.files = new Map();
      this.stats = emptyStats();
      this.replaceSnapshot(0);
      return;
    }

    const include = config.include.length ? config.include : ['**/*.xml'];
    const exclude = config.exclude.length
      ? config.exclude.length === 1
        ? config.exclude[0]
        : `{${config.exclude.join(',')}}`
      : undefined;
    const maxFiles = Math.min(100_000, Math.max(1, Math.floor(config.maxFiles)));

    const found = new Map<string, vscode.Uri>();
    for (const pattern of include) {
      if (token.isCancellationRequested) {
        return;
      }
      const batch = await vscode.workspace.findFiles(pattern, exclude, maxFiles + 1);
      for (const uri of batch) {
        found.set(uri.toString(), uri);
        if (found.size > maxFiles) {
          break;
        }
      }
      if (found.size > maxFiles) {
        break;
      }
    }

    const uris = [...found.values()].sort((a, b) => a.toString().localeCompare(b.toString()));
    const truncatedFiles = uris.length > maxFiles;
    const limited = uris.slice(0, maxFiles);
    const nextFiles = new Map<string, FileIndex>();
    let scanned = 0;
    let bytes = 0;
    let entries = 0;
    let refs = 0;
    let truncatedEntries = false;

    for (let index = 0; index < limited.length; index += BATCH_SIZE) {
      if (token.isCancellationRequested || this.cancellation !== cts) {
        return;
      }
      const batch = limited.slice(index, index + BATCH_SIZE);
      const results = await Promise.all(
        batch.map((uri) => this.readAndIndex(uri, config.maxDocumentSizeKB)),
      );
      for (const result of results) {
        scanned++;
        bytes += result.bytes;
        nextFiles.set(result.file.uri, result.file);
        entries += result.file.entries.length;
        refs += result.file.refs.length;
        if (entries + refs >= MAX_INDEX_ENTRIES) {
          truncatedEntries = true;
          break;
        }
      }
      const done = Math.min(index + batch.length, limited.length);
      onProgress?.(done, limited.length);
      this.options.status.set('building', `${done}/${limited.length}`);
      if (truncatedEntries) {
        break;
      }
      await yieldToEventLoop();
    }

    if (token.isCancellationRequested || this.cancellation !== cts) {
      return;
    }
    this.files = nextFiles;
    this.stats = {
      ...emptyStats(),
      scannedFiles: scanned,
      bytes,
      durationMs: Date.now() - started,
      truncatedFiles,
      truncatedEntries,
    };
    this.replaceSnapshot(Date.now() - started);
  }

  private async readAndIndex(
    uri: vscode.Uri,
    maxDocumentSizeKB: number,
  ): Promise<{ file: FileIndex; bytes: number }> {
    const data = await vscode.workspace.fs.readFile(uri);
    const bytes = data.byteLength;
    const key = normalizeUriKey(uri.toString());
    if (bytes > maxDocumentSizeKB * 1024) {
      return { file: skipped(key, 'too-large'), bytes };
    }
    if (data.subarray(0, 1024).includes(0)) {
      return { file: skipped(key, 'binary'), bytes };
    }
    let text = new TextDecoder('utf-8', { fatal: false }).decode(data);
    if (text.charCodeAt(0) === 0xfeff) {
      text = text.slice(1);
    }
    if (!text.includes('<procedure') && !text.includes('<script-segment')) {
      return { file: skipped(key, 'not-xproc'), bytes };
    }
    return { file: indexFile(key, 0, text), bytes };
  }

  private replaceSnapshot(durationMs: number, announce = true): void {
    const truncated = this.stats.truncatedFiles || this.stats.truncatedEntries;
    const snapshot = buildSnapshot(
      this.files.values(),
      ++this.generation,
      {
        scannedFiles: this.stats.scannedFiles,
        bytes: this.stats.bytes,
        durationMs,
        truncatedFiles: this.stats.truncatedFiles,
        truncatedEntries: this.stats.truncatedEntries,
      },
      truncated,
    );
    this.stats = snapshot.stats;
    this.store.setBase(snapshot);
    this.store.setState('ready');
    if (announce) {
      const summary = this.summary(snapshot);
      this.options.status.set('ready', summary);
      this.options.logger.info(summary);
    }
    this.resolveWaiters(true);
    this.changedEmitter.fire();
  }

  private summary(snapshot: IndexSnapshot): string {
    const seconds = (snapshot.stats.durationMs / 1000).toFixed(1);
    let text = `索引完成：${snapshot.stats.fileCount.toLocaleString('zh-CN')} 个文件，${snapshot.allIds.size.toLocaleString('zh-CN')} 个 id，耗时 ${seconds} s`;
    if (snapshot.stats.truncatedFiles) {
      text += `（文件数已达上限，结果不完整）`;
    }
    if (snapshot.stats.truncatedEntries) {
      text += `（条目数已达上限，结果不完整）`;
    }
    return text;
  }

  private ensureWatcher(): void {
    if (this.watchers.length) {
      return;
    }
    const folders = vscode.workspace.workspaceFolders ?? [];
    for (const folder of folders) {
      try {
        const watcher = vscode.workspace.createFileSystemWatcher(
          new vscode.RelativePattern(folder, '**/*'),
        );
        watcher.onDidCreate((uri) => this.onDiskEvent(uri));
        watcher.onDidChange((uri) => this.onDiskEvent(uri));
        watcher.onDidDelete((uri) => this.handleDelete(uri));
        this.watchers.push(watcher);
      } catch (error) {
        this.options.logger.warn(`无法监听工作区文件变化：${String(error)}`);
      }
    }
  }

  private disposeWatchers(): void {
    for (const watcher of this.watchers) {
      watcher.dispose();
    }
    this.watchers = [];
  }

  private onDiskEvent(uri: vscode.Uri): void {
    if (this.disposed || this.store.getState() !== 'ready') {
      return;
    }
    const config = this.options.getConfig();
    if (!config.indexEnabled) {
      return;
    }
    if (!this.isWatchedFile(uri, config)) {
      this.handleDelete(uri);
      return;
    }
    this.scheduleDisk(uri);
  }

  private isWatchedFile(uri: vscode.Uri, config: XProcConfig): boolean {
    if (!vscode.workspace.getWorkspaceFolder(uri)) {
      return false;
    }
    const relative = vscode.workspace.asRelativePath(uri, false);
    if (config.exclude.length && matchesAny(config.exclude, relative)) {
      return false;
    }
    if (uri.path.toLowerCase().endsWith('.xml')) {
      return true;
    }
    return config.include.length > 0 && matchesAny(config.include, relative);
  }

  private scheduleDisk(uri: vscode.Uri): void {
    if (this.store.getState() !== 'ready') {
      return;
    }
    this.pendingDisk.add(uri.toString());
    if (this.diskTimer) {
      clearTimeout(this.diskTimer);
    }
    this.diskTimer = setTimeout(() => {
      this.diskTimer = undefined;
      void this.flushDisk();
    }, DEBOUNCE_MS);
  }

  private async flushDisk(): Promise<void> {
    const keys = [...this.pendingDisk];
    this.pendingDisk.clear();
    if (!keys.length || this.disposed || this.store.getState() !== 'ready') {
      return;
    }
    const config = this.options.getConfig();
    const started = Date.now();
    let changed = false;
    for (const raw of keys) {
      const uri = vscode.Uri.parse(raw);
      if (!this.isWatchedFile(uri, config)) {
        if (this.files.delete(normalizeUriKey(raw))) {
          changed = true;
        }
        continue;
      }
      try {
        const result = await this.readAndIndex(uri, config.maxDocumentSizeKB);
        this.files.set(result.file.uri, result.file);
        changed = true;
      } catch {
        if (this.files.delete(normalizeUriKey(raw))) {
          changed = true;
        }
      }
    }
    if (!changed || this.disposed || this.store.getState() !== 'ready') {
      return;
    }
    this.replaceSnapshot(Date.now() - started, false);
  }

  private handleDelete(uri: vscode.Uri): void {
    const key = normalizeUriKey(uri.toString());
    let changed = this.files.delete(key);
    if (this.store.getOverlay(key)) {
      this.store.removeOverlay(key);
      changed = true;
    }
    this.pendingDisk.delete(uri.toString());
    if (!changed || this.disposed || this.store.getState() !== 'ready') {
      return;
    }
    this.replaceSnapshot(0, false);
  }

  private clearDiskTimers(): void {
    if (this.diskTimer) {
      clearTimeout(this.diskTimer);
      this.diskTimer = undefined;
    }
    this.pendingDisk.clear();
  }

  private resolveWaiters(ready: boolean): void {
    const waiters = this.waiters;
    this.waiters = [];
    for (const waiter of waiters) {
      waiter(ready);
    }
  }
}
