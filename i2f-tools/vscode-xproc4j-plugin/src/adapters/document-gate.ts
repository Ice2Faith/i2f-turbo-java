import type * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import { detectRoot } from '../domain/recognition/detector';
import type { RootInfo } from '../domain/recognition/detector';

export type GatePurpose = 'completion' | 'navigation' | 'symbols' | 'semantic' | 'decoration';

export interface GateDecision {
  allowed: boolean;
  reason?: 'disabled' | 'not-xml' | 'not-xproc' | 'too-large' | 'unsupported-scheme';
  root?: RootInfo;
}

const UNSUPPORTED_SCHEMES = new Set(['output', 'debug', 'git', 'vscode-scm']);

export class DocumentGate {
  private readonly roots = new Map<string, RootInfo | null>();
  private readonly maxCache = 50;

  constructor(private readonly getConfig: () => XProcConfig) {}

  decide(document: vscode.TextDocument, purpose: GatePurpose): GateDecision {
    const config = this.getConfig();
    if (document.languageId !== 'xml') {
      return { allowed: false, reason: 'not-xml' };
    }
    if (!config.enable) {
      return { allowed: false, reason: 'disabled' };
    }
    if (UNSUPPORTED_SCHEMES.has(document.uri.scheme)) {
      return { allowed: false, reason: 'unsupported-scheme' };
    }
    if (document.getText().length > config.maxDocumentSizeKB * 1024) {
      return { allowed: false, reason: 'too-large' };
    }
    const root = this.rootOf(document);
    if (!root?.isProcedure) {
      return { allowed: false, reason: 'not-xproc', root: root ?? undefined };
    }
    void purpose;
    return { allowed: true, root };
  }

  /**
   * 非 XML 文件的轻量门控：只检查总开关、scheme 与文档大小。
   *
   * 后缀筛选由调用方完成；这里不要求 procedure 根节点，也不构建 XML 模型。
   */
  decideRaw(document: vscode.TextDocument): GateDecision {
    const config = this.getConfig();
    if (!config.enable) {
      return { allowed: false, reason: 'disabled' };
    }
    if (UNSUPPORTED_SCHEMES.has(document.uri.scheme)) {
      return { allowed: false, reason: 'unsupported-scheme' };
    }
    if (document.getText().length > config.maxDocumentSizeKB * 1024) {
      return { allowed: false, reason: 'too-large' };
    }
    return { allowed: true };
  }

  private rootOf(document: vscode.TextDocument): RootInfo | null {
    const key = `${document.uri.toString()}@${document.version}`;
    if (this.roots.has(key)) {
      return this.roots.get(key) ?? null;
    }
    const root = detectRoot(document.getText());
    if (this.roots.size >= this.maxCache) {
      const oldest = this.roots.keys().next().value;
      if (oldest !== undefined) {
        this.roots.delete(oldest);
      }
    }
    this.roots.set(key, root);
    return root;
  }

  invalidate(uri: vscode.Uri): void {
    const prefix = `${uri.toString()}@`;
    for (const key of [...this.roots.keys()]) {
      if (key.startsWith(prefix)) {
        this.roots.delete(key);
      }
    }
  }

  clear(): void {
    this.roots.clear();
  }
}
