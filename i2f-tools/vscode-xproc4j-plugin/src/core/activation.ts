import * as vscode from 'vscode';
import { XProcBlockBackgroundDecorator } from '../adapters/block-background-adapter';
import { COMPLETION_TRIGGER_CHARACTERS, XProcCompletionProvider } from '../adapters/completion-adapter';
import { XProcDefinitionProvider } from '../adapters/definition-adapter';
import { DocumentGate } from '../adapters/document-gate';
import { XProcFoldingProvider } from '../adapters/folding-adapter';
import { createProcedureFile } from '../adapters/new-file-command';
import { RawVariableHighlightDecorator } from '../adapters/raw-variable-highlight-adapter';
import { XProcReferenceProvider } from '../adapters/reference-adapter';
import { SEMANTIC_LEGEND, XProcSemanticTokensProvider } from '../adapters/semantic-token-adapter';
import { XProcSymbolProvider } from '../adapters/symbol-adapter';
import { normalizeUriKey } from '../domain/index/uri-key';
import { DocumentModelCache } from '../domain/xml/document-model';
import { IndexService } from '../infrastructure/index-service';
import { readConfig } from './config';
import { Logger } from './logger';
import { StatusBar } from './status';

const XML_SELECTOR: vscode.DocumentSelector = { language: 'xml' };

export async function activateExtension(context: vscode.ExtensionContext): Promise<void> {
  const started = Date.now();
  const logger = new Logger();
  context.subscriptions.push(logger);
  const status = new StatusBar();
  context.subscriptions.push(status);

  const getConfig = (): ReturnType<typeof readConfig> => readConfig();
  const models = new DocumentModelCache();
  const gate = new DocumentGate(getConfig);
  const index = new IndexService({ getConfig, logger, status });
  context.subscriptions.push(index);

  const blockBackground = new XProcBlockBackgroundDecorator({ gate, models, getConfig });
  context.subscriptions.push(blockBackground);

  const rawVariables = new RawVariableHighlightDecorator({ gate, getConfig });
  context.subscriptions.push(rawVariables);

  const semantic = new XProcSemanticTokensProvider({
    gate,
    models,
    index: index.store,
    logger,
    getConfig,
  });
  context.subscriptions.push(semantic);
  context.subscriptions.push(index.onDidChange(() => semantic.refresh()));

  const completion = new XProcCompletionProvider({
    gate,
    models,
    index: index.store,
    ensureIndex: () => index.ensureStarted(),
    logger,
    getConfig,
  });
  const definition = new XProcDefinitionProvider({
    gate,
    models,
    index: index.store,
    readiness: index,
    logger,
    getConfig,
  });
  const reference = new XProcReferenceProvider({
    gate,
    models,
    index: index.store,
    readiness: index,
    logger,
    getConfig,
  });
  const symbols = new XProcSymbolProvider({ gate, models, logger, getConfig });
  const folding = new XProcFoldingProvider({ gate, models, logger, getConfig });

  context.subscriptions.push(
    vscode.languages.registerCompletionItemProvider(
      XML_SELECTOR,
      completion,
      ...COMPLETION_TRIGGER_CHARACTERS,
    ),
    vscode.languages.registerDefinitionProvider(XML_SELECTOR, definition),
    vscode.languages.registerReferenceProvider(XML_SELECTOR, reference),
    vscode.languages.registerDocumentSymbolProvider(XML_SELECTOR, symbols),
    vscode.languages.registerFoldingRangeProvider(XML_SELECTOR, folding),
    vscode.languages.registerDocumentSemanticTokensProvider(XML_SELECTOR, semantic, SEMANTIC_LEGEND),
  );

  registerDocumentListeners(context, { models, gate, index });
  blockBackground.refreshAll();
  rawVariables.refreshAll();

  const configSubscription = registerConfigurationListener({
    gate,
    models,
    index,
    semantic,
    blockBackground,
    rawVariables,
    getConfig,
  });
  context.subscriptions.push(configSubscription);

  context.subscriptions.push(
    vscode.workspace.onDidChangeWorkspaceFolders(() => index.workspaceFoldersChanged()),
  );

  registerCommands(context, { logger, index, getConfig });
  logger.info(`XProc4J 已激活（${Date.now() - started} ms）`);
}

interface ListenerDependencies {
  models: DocumentModelCache;
  gate: DocumentGate;
  index: IndexService;
}

function invalidateCaches(document: vscode.TextDocument, deps: ListenerDependencies): void {
  deps.models.invalidate(normalizeUriKey(document.uri.toString()));
  deps.gate.invalidate(document.uri);
}

function registerDocumentListeners(
  context: vscode.ExtensionContext,
  deps: ListenerDependencies,
): void {
  context.subscriptions.push(
    vscode.workspace.onDidOpenTextDocument((document) => {
      if (document.languageId === 'xml') {
        deps.index.updateOverlay(document);
      }
    }),
    vscode.workspace.onDidChangeTextDocument((event) => {
      invalidateCaches(event.document, deps);
      if (event.document.languageId === 'xml') {
        deps.index.scheduleOverlay(event.document);
      }
    }),
    vscode.workspace.onDidSaveTextDocument((document) => {
      invalidateCaches(document, deps);
      if (document.languageId === 'xml') {
        deps.index.updateOverlay(document);
      }
    }),
    vscode.workspace.onDidCloseTextDocument((document) => {
      invalidateCaches(document, deps);
      if (document.languageId === 'xml') {
        deps.index.removeOverlay(document.uri);
      }
    }),
  );
}

interface ConfigDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  index: IndexService;
  semantic: XProcSemanticTokensProvider;
  blockBackground: XProcBlockBackgroundDecorator;
  rawVariables: RawVariableHighlightDecorator;
  getConfig: () => ReturnType<typeof readConfig>;
}

function registerConfigurationListener(deps: ConfigDependencies): vscode.Disposable {
  let rebuildTimer: NodeJS.Timeout | undefined;
  const subscription = vscode.workspace.onDidChangeConfiguration((event) => {
    if (!event.affectsConfiguration('xproc4j')) {
      return;
    }
    const config = deps.getConfig();
    if (
      event.affectsConfiguration('xproc4j.enable') ||
      event.affectsConfiguration('xproc4j.maxDocumentSizeKB')
    ) {
      deps.gate.clear();
      deps.models.clear();
      deps.semantic.refresh();
      deps.blockBackground.refreshAll();
      deps.rawVariables.refreshAll();
    }
    if (
      event.affectsConfiguration('xproc4j.blockBackground.enabled') ||
      event.affectsConfiguration('xproc4j.attributeValueBackground.enabled') ||
      event.affectsConfiguration('xproc4j.evalKeywordHighlight.enabled') ||
      event.affectsConfiguration('xproc4j.evalLiteralHighlight.enabled')
    ) {
      deps.blockBackground.refreshAll();
    }
    if (event.affectsConfiguration('xproc4j.refid.enabled')) {
      deps.semantic.refresh();
    }
    if (event.affectsConfiguration('xproc4j.variableHighlight.extensions')) {
      deps.semantic.refresh();
      deps.rawVariables.refreshAll();
    }
    if (event.affectsConfiguration('xproc4j.index.enabled')) {
      if (config.indexEnabled) {
        for (const document of vscode.workspace.textDocuments) {
          if (document.languageId === 'xml') {
            deps.index.updateOverlay(document);
          }
        }
        deps.index.ensureStarted();
      } else {
        deps.index.disable();
      }
    }

    const scanChanged =
      event.affectsConfiguration('xproc4j.projectScan.include') ||
      event.affectsConfiguration('xproc4j.projectScan.exclude') ||
      event.affectsConfiguration('xproc4j.projectScan.maxFiles');
    if (!scanChanged) {
      return;
    }
    if (rebuildTimer) {
      clearTimeout(rebuildTimer);
    }
    rebuildTimer = setTimeout(() => {
      rebuildTimer = undefined;
      if (!deps.getConfig().indexEnabled) {
        deps.index.disable();
        return;
      }
      void deps.index.rebuild();
    }, 300);
  });

  return {
    dispose: () => {
      if (rebuildTimer) {
        clearTimeout(rebuildTimer);
      }
      subscription.dispose();
    },
  };
}

interface CommandDependencies {
  logger: Logger;
  index: IndexService;
  getConfig: () => ReturnType<typeof readConfig>;
}

async function runCommand(
  logger: Logger,
  label: string,
  action: () => Promise<void>,
): Promise<void> {
  try {
    await action();
  } catch (error) {
    logger.error(`${label}失败`, error);
    const message = error instanceof Error ? error.message : String(error);
    const choice = await vscode.window.showErrorMessage(`XProc4J: ${label}失败：${message}`, '显示输出');
    if (choice === '显示输出') {
      logger.show(false);
    }
  }
}

function registerCommands(context: vscode.ExtensionContext, deps: CommandDependencies): void {
  context.subscriptions.push(
    vscode.commands.registerCommand('xproc4j.createProcedureFile', (arg?: vscode.Uri) =>
      runCommand(deps.logger, '新建 Procedure 文件', () =>
        createProcedureFile(arg, { logger: deps.logger }),
      ),
    ),
    vscode.commands.registerCommand('xproc4j.rebuildIndex', () =>
      runCommand(deps.logger, '重建工程索引', async () => {
        if (!deps.getConfig().indexEnabled) {
          const choice = await vscode.window.showInformationMessage(
            'XProc4J 工程索引已禁用。',
            '打开设置',
          );
          if (choice === '打开设置') {
            await vscode.commands.executeCommand(
              'workbench.action.openSettings',
              'xproc4j.index.enabled',
            );
          }
          return;
        }
        if (!vscode.workspace.workspaceFolders?.length) {
          await vscode.window.showWarningMessage('请先打开一个工作区文件夹。');
          return;
        }
        const snapshot = await vscode.window.withProgress(
          {
            location: vscode.ProgressLocation.Notification,
            title: 'XProc4J: 重建工程索引',
            cancellable: true,
          },
          (progress, token) => deps.index.rebuild(progress, token),
        );
        if (snapshot) {
          let message = `索引完成：${snapshot.stats.fileCount} 个文件，${snapshot.allIds.size} 个 id，耗时 ${(snapshot.stats.durationMs / 1000).toFixed(1)} s`;
          if (snapshot.stats.truncatedFiles) {
            message += `（已达文件上限 ${deps.getConfig().maxFiles}，结果不完整）`;
          }
          if (snapshot.stats.truncatedEntries) {
            message += '（条目数已达上限，结果不完整）';
          }
          await vscode.window.showInformationMessage(message);
          return;
        }
        if (deps.index.store.getState() === 'failed') {
          throw new Error('索引构建失败，请查看输出通道');
        }
        await vscode.window.showInformationMessage('已取消索引重建，保留旧快照。');
      }),
    ),
    vscode.commands.registerCommand('xproc4j.showOutput', () => deps.logger.show(false)),
  );
}
