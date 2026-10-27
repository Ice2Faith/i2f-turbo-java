import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import type { Logger } from '../core/logger';
import { normalizeUriKey } from '../domain/index/uri-key';
import { buildFoldingRegions } from '../domain/symbols/folding';
import type { DocumentModelCache } from '../domain/xml/document-model';
import type { DocumentGate } from './document-gate';

export interface FoldingDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  logger: Logger;
  getConfig: () => XProcConfig;
}

export class XProcFoldingProvider implements vscode.FoldingRangeProvider {
  constructor(private readonly deps: FoldingDependencies) {}

  provideFoldingRanges(
    document: vscode.TextDocument,
    _context: vscode.FoldingContext,
    token: vscode.CancellationToken,
  ): vscode.FoldingRange[] | undefined {
    const config = this.deps.getConfig();
    if (!config.foldingEnabled) {
      return undefined;
    }
    if (!this.deps.gate.decide(document, 'symbols').allowed || token.isCancellationRequested) {
      return undefined;
    }
    try {
      const text = document.getText();
      const model = this.deps.models.get(
        normalizeUriKey(document.uri.toString()),
        document.version,
        text,
      );
      return buildFoldingRegions(text, model).map(
        (region) => new vscode.FoldingRange(region.startLine, region.endLine, vscode.FoldingRangeKind.Region),
      );
    } catch (error) {
      this.deps.logger.error('生成折叠范围失败', error);
      return undefined;
    }
  }
}
