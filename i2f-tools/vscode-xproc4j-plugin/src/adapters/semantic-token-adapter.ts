import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import type { Logger } from '../core/logger';
import type { IndexQuery } from '../domain/index/query';
import { normalizeUriKey } from '../domain/index/uri-key';
import { getCatalog } from '../domain/metadata/catalog';
import { buildVariableTokens, TOKEN_MODIFIERS, TOKEN_TYPES } from '../domain/semantics/variable-tokens';
import type { DocumentModelCache } from '../domain/xml/document-model';
import type { DocumentGate } from './document-gate';
import { toRange } from './vscode-types';

const MAX_TOKENS = 10_000;

export const SEMANTIC_LEGEND = new vscode.SemanticTokensLegend(
  [...TOKEN_TYPES],
  [...TOKEN_MODIFIERS],
);

export interface SemanticTokenDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  index: IndexQuery;
  logger: Logger;
  getConfig: () => XProcConfig;
}

export class XProcSemanticTokensProvider implements vscode.DocumentSemanticTokensProvider {
  private readonly changed = new vscode.EventEmitter<void>();
  readonly onDidChangeSemanticTokens = this.changed.event;

  constructor(private readonly deps: SemanticTokenDependencies) {}

  refresh(): void {
    this.changed.fire();
  }

  provideDocumentSemanticTokens(
    document: vscode.TextDocument,
    token: vscode.CancellationToken,
  ): vscode.SemanticTokens | undefined {
    if (!this.deps.gate.decide(document, 'semantic').allowed || token.isCancellationRequested) {
      return undefined;
    }
    const config = this.deps.getConfig();
    try {
      const text = document.getText();
      const model = this.deps.models.get(
        normalizeUriKey(document.uri.toString()),
        document.version,
        text,
      );
      const resolved = new Set<string>();
      if (config.refidEnabled && config.indexEnabled) {
        for (const refid of model.refidValues) {
          if (this.deps.index.peekId(refid.value)) {
            resolved.add(refid.value);
          }
        }
      }
      const entries = buildVariableTokens(
        model,
        text,
        getCatalog(),
        resolved,
        config.variableHighlightExtensions.includes('xml'),
      );
      if (!entries.length || token.isCancellationRequested) {
        return undefined;
      }
      const builder = new vscode.SemanticTokensBuilder(SEMANTIC_LEGEND);
      for (const entry of entries.slice(0, MAX_TOKENS)) {
        const type = TOKEN_TYPES[entry.tokenType] ?? TOKEN_TYPES[0];
        const modifiers = entry.tokenModifiers & 1 ? [TOKEN_MODIFIERS[0]!] : [];
        builder.push(toRange(document, entry.range), type, modifiers);
      }
      return builder.build();
    } catch (error) {
      this.deps.logger.error('生成语义 token 失败', error);
      return undefined;
    }
  }

  dispose(): void {
    this.changed.dispose();
  }
}
