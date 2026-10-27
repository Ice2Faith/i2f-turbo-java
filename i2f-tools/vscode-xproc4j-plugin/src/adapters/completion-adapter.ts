import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import type { Logger } from '../core/logger';
import { assembleCompletions } from '../domain/completion/orchestrator';
import { resolveCompletionContext } from '../domain/completion/context';
import type { IndexQuery } from '../domain/index/query';
import { normalizeUriKey } from '../domain/index/uri-key';
import { getCatalog } from '../domain/metadata/catalog';
import type { DocumentModelCache } from '../domain/xml/document-model';
import type { DocumentGate } from './document-gate';
import { toCompletionItem } from './vscode-types';

export const COMPLETION_TRIGGER_CHARACTERS = ['<', '.', '"', "'", '{', '$', '#', ' ', '='] as const;

export interface CompletionDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  index: IndexQuery;
  ensureIndex: () => void;
  logger: Logger;
  getConfig: () => XProcConfig;
}

export class XProcCompletionProvider implements vscode.CompletionItemProvider {
  constructor(private readonly deps: CompletionDependencies) {}

  provideCompletionItems(
    document: vscode.TextDocument,
    position: vscode.Position,
    token: vscode.CancellationToken,
  ): vscode.CompletionList | undefined {
    const config = this.deps.getConfig();
    if (!config.completionEnabled) {
      return undefined;
    }
    const decision = this.deps.gate.decide(document, 'completion');
    if (!decision.allowed || token.isCancellationRequested) {
      return undefined;
    }

    try {
      const text = document.getText();
      const offset = document.offsetAt(position);
      const catalog = getCatalog();
      const model = this.deps.models.get(
        normalizeUriKey(document.uri.toString()),
        document.version,
        text,
      );
      const context = resolveCompletionContext(text, offset, model, catalog);
      if (context.kind === 'none') {
        return undefined;
      }

      // 参数/refid 补全依赖工程索引；首次请求惰性启动，不阻塞本次返回。
      this.deps.ensureIndex();

      const entries = assembleCompletions(
        context,
        text,
        offset,
        model,
        {
          catalog,
          index: config.indexEnabled ? this.deps.index : undefined,
          builtinVariables: config.builtinVariables,
          sqlKeywords: config.sqlKeywords,
        },
        {
          isCancelled: () => token.isCancellationRequested,
          onError: (assembler, error) => {
            this.deps.logger.error(`补全候选生成失败（${assembler}）`, error);
          },
        },
      );
      if (!entries.length || token.isCancellationRequested) {
        return undefined;
      }
      return new vscode.CompletionList(
        entries.map((entry) => toCompletionItem(entry, context.replaceRange, document)),
        false,
      );
    } catch (error) {
      this.deps.logger.error('补全请求失败', error);
      return undefined;
    }
  }
}
