import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import type { Logger } from '../core/logger';
import { getCatalog } from '../domain/metadata/catalog';
import type { OutlineSymbol, OutlineSymbolKind } from '../domain/symbols/document-symbols';
import { buildDocumentSymbols } from '../domain/symbols/document-symbols';
import { normalizeUriKey } from '../domain/index/uri-key';
import type { DocumentModelCache } from '../domain/xml/document-model';
import type { DocumentGate } from './document-gate';
import { toRange } from './vscode-types';

export interface SymbolDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  logger: Logger;
  getConfig: () => XProcConfig;
}

const SYMBOL_KINDS: Record<OutlineSymbolKind, vscode.SymbolKind> = {
  class: vscode.SymbolKind.Class,
  function: vscode.SymbolKind.Function,
  method: vscode.SymbolKind.Method,
  operator: vscode.SymbolKind.Operator,
  event: vscode.SymbolKind.Event,
  namespace: vscode.SymbolKind.Namespace,
  object: vscode.SymbolKind.Object,
};

function convert(document: vscode.TextDocument, symbol: OutlineSymbol): vscode.DocumentSymbol {
  const converted = new vscode.DocumentSymbol(
    symbol.name,
    symbol.detail,
    SYMBOL_KINDS[symbol.kind],
    toRange(document, symbol.range),
    toRange(document, symbol.selectionRange),
  );
  converted.children = symbol.children.map((child) => convert(document, child));
  return converted;
}

export class XProcSymbolProvider implements vscode.DocumentSymbolProvider {
  constructor(private readonly deps: SymbolDependencies) {}

  provideDocumentSymbols(
    document: vscode.TextDocument,
    token: vscode.CancellationToken,
  ): vscode.DocumentSymbol[] | undefined {
    const config = this.deps.getConfig();
    if (!config.symbolsEnabled) {
      return undefined;
    }
    if (!this.deps.gate.decide(document, 'symbols').allowed || token.isCancellationRequested) {
      return undefined;
    }
    try {
      const model = this.deps.models.get(
        normalizeUriKey(document.uri.toString()),
        document.version,
        document.getText(),
      );
      return buildDocumentSymbols(model, getCatalog()).map((symbol) => convert(document, symbol));
    } catch (error) {
      this.deps.logger.error('生成大纲失败', error);
      return undefined;
    }
  }
}
