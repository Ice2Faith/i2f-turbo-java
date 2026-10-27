import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import { normalizeUriKey } from '../domain/index/uri-key';
import { extractRawVariables } from '../domain/semantics/variable-extractor';
import { fileMatchesExtensions } from '../domain/semantics/variable-file-extensions';
import { LineIndex } from '../domain/xml/line-index';
import type { DocumentGate } from './document-gate';

/**
 * 非 XML 文件的变量前景色。Light 下与 XML 变量一致的蓝色，
 * Dark 下使用更亮的蓝，避免深色背景上对比度不足。
 */
const LIGHT_VARIABLE_COLOR = '#0550AE';
const DARK_VARIABLE_COLOR = '#9CDCFE';
const REFRESH_DELAY_MS = 200;

export interface RawVariableHighlightDependencies {
  gate: DocumentGate;
  getConfig: () => XProcConfig;
}

/**
 * 为配置后缀的非 XML 文件绘制变量高亮。
 *
 * VS Code 同一语言只使用一个评分最高的语义 token Provider；若为 yaml/java 等
 * 语言注册 Provider，会顶掉该语言扩展自己的语义 token。因此这里改用编辑器
 * 装饰，只覆盖变量范围，不参与语义 token 竞争。XML 仍由语义 token 处理。
 */
export class RawVariableHighlightDecorator implements vscode.Disposable {
  private decoration = this.createDecoration();
  private readonly timers = new Map<string, NodeJS.Timeout>();
  private readonly disposables: vscode.Disposable[] = [];

  constructor(private readonly deps: RawVariableHighlightDependencies) {
    this.disposables.push(
      vscode.window.onDidChangeVisibleTextEditors(() => this.refreshAll()),
      vscode.window.onDidChangeActiveTextEditor(() => this.refreshAll()),
      vscode.window.onDidChangeActiveColorTheme(() => {
        this.decoration.dispose();
        this.decoration = this.createDecoration();
        this.refreshAll();
      }),
      vscode.workspace.onDidChangeTextDocument((event) => {
        if (event.document.languageId !== 'xml') {
          this.schedule(event.document);
        }
      }),
      vscode.workspace.onDidCloseTextDocument((document) => {
        this.clearTimer(document.uri);
      }),
    );
  }

  refreshAll(): void {
    for (const editor of vscode.window.visibleTextEditors) {
      this.apply(editor);
    }
  }

  dispose(): void {
    for (const timer of this.timers.values()) {
      clearTimeout(timer);
    }
    this.timers.clear();
    this.decoration.dispose();
    for (const disposable of this.disposables) {
      disposable.dispose();
    }
  }

  private createDecoration(): vscode.TextEditorDecorationType {
    const kind = vscode.window.activeColorTheme.kind;
    const dark =
      kind === vscode.ColorThemeKind.Dark || kind === vscode.ColorThemeKind.HighContrast;
    return vscode.window.createTextEditorDecorationType({
      color: dark ? DARK_VARIABLE_COLOR : LIGHT_VARIABLE_COLOR,
      rangeBehavior: vscode.DecorationRangeBehavior.ClosedClosed,
    });
  }

  private schedule(document: vscode.TextDocument): void {
    const key = normalizeUriKey(document.uri.toString());
    const existing = this.timers.get(key);
    if (existing) {
      clearTimeout(existing);
    }
    this.timers.set(
      key,
      setTimeout(() => {
        this.timers.delete(key);
        for (const editor of vscode.window.visibleTextEditors) {
          if (normalizeUriKey(editor.document.uri.toString()) === key) {
            this.apply(editor);
          }
        }
      }, REFRESH_DELAY_MS),
    );
  }

  private clearTimer(uri: vscode.Uri): void {
    const key = normalizeUriKey(uri.toString());
    const timer = this.timers.get(key);
    if (timer) {
      clearTimeout(timer);
      this.timers.delete(key);
    }
  }

  private apply(editor: vscode.TextEditor): void {
    const document = editor.document;
    const config = this.deps.getConfig();
    if (
      !config.enable ||
      document.languageId === 'xml' ||
      !fileMatchesExtensions(document.uri.path, config.variableHighlightExtensions)
    ) {
      editor.setDecorations(this.decoration, []);
      return;
    }
    if (!this.deps.gate.decideRaw(document).allowed) {
      editor.setDecorations(this.decoration, []);
      return;
    }

    const text = document.getText();
    const lineIndex = new LineIndex(text);
    const ranges = extractRawVariables(text).map((range) => {
      const start = lineIndex.positionAt(range.range.start);
      const end = lineIndex.positionAt(range.range.end);
      return new vscode.Range(start.line, start.character, end.line, end.character);
    });
    editor.setDecorations(this.decoration, ranges);
  }
}
