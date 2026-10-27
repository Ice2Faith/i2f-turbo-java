import type * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import type { Logger } from '../core/logger';
import type { IndexQuery } from '../domain/index/query';
import { normalizeUriKey } from '../domain/index/uri-key';
import { findDefinitionsAt } from '../domain/navigation/navigation';
import type { DocumentModelCache } from '../domain/xml/document-model';
import type { DocumentGate } from './document-gate';
import { toLocation } from './vscode-types';

export interface NavigationReadiness {
  whenReady(timeoutMs: number, token?: vscode.CancellationToken): Promise<boolean>;
}

export interface NavigationDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  index: IndexQuery;
  readiness: NavigationReadiness;
  logger: Logger;
  getConfig: () => XProcConfig;
}

export class XProcDefinitionProvider implements vscode.DefinitionProvider {
  constructor(private readonly deps: NavigationDependencies) {}

  async provideDefinition(
    document: vscode.TextDocument,
    position: vscode.Position,
    token: vscode.CancellationToken,
  ): Promise<vscode.Location[] | undefined> {
    const config = this.deps.getConfig();
    if (!config.refidEnabled) {
      return undefined;
    }
    if (!this.deps.gate.decide(document, 'navigation').allowed) {
      return undefined;
    }

    try {
      const text = document.getText();
      const offset = document.offsetAt(position);
      const model = this.deps.models.get(
        normalizeUriKey(document.uri.toString()),
        document.version,
        text,
      );
      const index = config.indexEnabled ? this.deps.index : undefined;
      const initial = findDefinitionsAt(text, offset, model, index);
      if (!initial) {
        return undefined;
      }

      let result = initial;
      if (config.indexEnabled) {
        await this.deps.readiness.whenReady(400, token);
        if (token.isCancellationRequested) {
          return undefined;
        }
        // 索引可能刚就绪，用同一快照重新合并一次。
        result = findDefinitionsAt(text, offset, model, index) ?? initial;
      }
      const locations = result.targets.map(toLocation);
      return locations.length ? locations : undefined;
    } catch (error) {
      this.deps.logger.error('查找定义失败', error);
      return undefined;
    }
  }
}
