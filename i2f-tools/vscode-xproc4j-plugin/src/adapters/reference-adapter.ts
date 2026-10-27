import type * as vscode from 'vscode';
import { normalizeUriKey } from '../domain/index/uri-key';
import { findReferencesAt } from '../domain/navigation/navigation';
import type { NavigationDependencies } from './definition-adapter';
import { toLocation } from './vscode-types';

export class XProcReferenceProvider implements vscode.ReferenceProvider {
  constructor(private readonly deps: NavigationDependencies) {}

  async provideReferences(
    document: vscode.TextDocument,
    position: vscode.Position,
    context: vscode.ReferenceContext,
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
      const initial = findReferencesAt(text, offset, model, index, context.includeDeclaration);
      if (!initial) {
        return undefined;
      }

      let result = initial;
      if (config.indexEnabled) {
        await this.deps.readiness.whenReady(400, token);
        if (token.isCancellationRequested) {
          return undefined;
        }
        result = findReferencesAt(text, offset, model, index, context.includeDeclaration) ?? initial;
      }
      const locations = result.targets.map(toLocation);
      return locations.length ? locations : undefined;
    } catch (error) {
      this.deps.logger.error('查找引用失败', error);
      return undefined;
    }
  }
}
