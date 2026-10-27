import * as vscode from 'vscode';
import type { XProcConfig } from '../core/config';
import { normalizeUriKey } from '../domain/index/uri-key';
import { collectAttributeValueRanges } from '../domain/semantics/attribute-value-background';
import {
  collectBlockBackgroundRanges,
  type BlockBackgroundKind,
} from '../domain/semantics/block-background';
import { collectEvalKeywordRanges } from '../domain/semantics/eval-keywords';
import { collectEvalLiteralRanges } from '../domain/semantics/eval-literals';
import type { TextRange, XProcDocumentModel } from '../domain/types';
import { LineIndex } from '../domain/xml/line-index';
import type { DocumentModelCache } from '../domain/xml/document-model';
import type { DocumentGate } from './document-gate';

/**
 * 各嵌入块的整行叠加色。白底上分别接近 #fff7e4 与 #e3f3e4，
 * 透明度足够浅，不会压过正文；深色主题下也能保留轻微色差。
 */
const BLOCK_BACKGROUND_COLORS: Record<BlockBackgroundKind, string> = {
  sql: 'rgba(255, 213, 79, 0.18)',
  eval: 'rgba(76, 175, 80, 0.18)',
};
/** 属性值背景比 eval 块更浅，避免与整行块背景抢视觉层级。 */
const ATTRIBUTE_VALUE_BACKGROUND = 'rgba(129, 199, 132, 0.16)';
/**
 * lang-eval* 正文关键字的固定前景色。主题无法保证 dodgerblue，
 * 因此用装饰强制覆盖 token 颜色；只染关键字本身，不改背景。
 */
const EVAL_KEYWORD_COLOR = '#1E90FF';
/**
 * 数值字面量的前景色。Light 用较深的钴蓝，与 dodgerblue 关键字明显区分；
 * Dark 用较亮的浅蓝，保证深色背景可读且仍属蓝色系。
 */
const LIGHT_NUMBER_COLOR = '#1565C0';
const DARK_NUMBER_COLOR = '#90CAF9';
/**
 * 字符串字面量的前景色。颜色刻意与 eval 整行浅绿背景拉开明度：
 * Light 用深绿（背景是极浅绿），Dark 用亮绿（背景是深色微绿），
 * 避免“绿字压在绿底上”分不清。
 */
const LIGHT_STRING_COLOR = '#2E7D32';
const DARK_STRING_COLOR = '#81C784';
const REFRESH_DELAY_MS = 200;

export interface BlockBackgroundDependencies {
  gate: DocumentGate;
  models: DocumentModelCache;
  getConfig: () => XProcConfig;
}

/**
 * 为嵌入块正文、XML 属性值与 lang-eval* 关键字/字面量叠加装饰。
 *
 * TextMate 的 token 规则在 VS Code 中不渲染 background，因此不能用注入语法实现；
 * 这里按文档模型计算范围，再按类型用编辑器装饰绘制。SQL/lang-eval* 使用整行
 * 着色，让起止标签所在行也包含在色块内；属性值只覆盖值本身（不含引号）；
 * 关键字使用装饰强制前景色，保证在任意主题下都呈现 dodgerblue；数值与字符串
 * 字面量按主题选择明度，保持“数值蓝色、字符串绿色”且与块背景可区分。
 */
export class XProcBlockBackgroundDecorator implements vscode.Disposable {
  private readonly decorations = new Map<
    BlockBackgroundKind,
    vscode.TextEditorDecorationType
  >();
  private readonly attributeValueDecoration =
    vscode.window.createTextEditorDecorationType({
      backgroundColor: ATTRIBUTE_VALUE_BACKGROUND,
      rangeBehavior: vscode.DecorationRangeBehavior.ClosedClosed,
    });
  private readonly evalKeywordDecoration =
    vscode.window.createTextEditorDecorationType({
      color: EVAL_KEYWORD_COLOR,
      rangeBehavior: vscode.DecorationRangeBehavior.ClosedClosed,
    });
  private literalDecorations = this.createLiteralDecorations();
  private readonly timers = new Map<string, NodeJS.Timeout>();
  private readonly disposables: vscode.Disposable[] = [];

  constructor(private readonly deps: BlockBackgroundDependencies) {
    for (const kind of Object.keys(BLOCK_BACKGROUND_COLORS) as BlockBackgroundKind[]) {
      this.decorations.set(
        kind,
        vscode.window.createTextEditorDecorationType({
          backgroundColor: BLOCK_BACKGROUND_COLORS[kind],
          isWholeLine: true,
          rangeBehavior: vscode.DecorationRangeBehavior.ClosedClosed,
        }),
      );
    }
    this.disposables.push(
      vscode.window.onDidChangeVisibleTextEditors(() => this.refreshAll()),
      vscode.window.onDidChangeActiveTextEditor(() => this.refreshAll()),
      // 数值/字符串颜色按主题选择明度，切换主题时重建对应装饰并重绘。
      vscode.window.onDidChangeActiveColorTheme(() => {
        this.literalDecorations.number.dispose();
        this.literalDecorations.string.dispose();
        this.literalDecorations = this.createLiteralDecorations();
        this.refreshAll();
      }),
      vscode.workspace.onDidChangeTextDocument((event) => {
        if (event.document.languageId === 'xml') {
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
    for (const decoration of this.decorations.values()) {
      decoration.dispose();
    }
    this.decorations.clear();
    this.attributeValueDecoration.dispose();
    this.evalKeywordDecoration.dispose();
    this.literalDecorations.number.dispose();
    this.literalDecorations.string.dispose();
    for (const disposable of this.disposables) {
      disposable.dispose();
    }
  }

  private createLiteralDecorations(): {
    number: vscode.TextEditorDecorationType;
    string: vscode.TextEditorDecorationType;
  } {
    const kind = vscode.window.activeColorTheme.kind;
    const dark =
      kind === vscode.ColorThemeKind.Dark || kind === vscode.ColorThemeKind.HighContrast;
    return {
      number: vscode.window.createTextEditorDecorationType({
        color: dark ? DARK_NUMBER_COLOR : LIGHT_NUMBER_COLOR,
        rangeBehavior: vscode.DecorationRangeBehavior.ClosedClosed,
      }),
      string: vscode.window.createTextEditorDecorationType({
        color: dark ? DARK_STRING_COLOR : LIGHT_STRING_COLOR,
        rangeBehavior: vscode.DecorationRangeBehavior.ClosedClosed,
      }),
    };
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
      !config.blockBackgroundEnabled &&
      !config.attributeValueBackgroundEnabled &&
      !config.evalKeywordHighlightEnabled &&
      !config.evalLiteralHighlightEnabled
    ) {
      this.clear(editor);
      return;
    }
    const decision = this.deps.gate.decide(document, 'decoration');
    if (!decision.allowed) {
      this.clear(editor);
      return;
    }
    const text = document.getText();
    const model = this.deps.models.get(
      normalizeUriKey(document.uri.toString()),
      document.version,
      text,
    );
    if (!model.isXProc) {
      this.clear(editor);
      return;
    }
    const lineIndex = new LineIndex(text);
    if (config.blockBackgroundEnabled) {
      this.applyBlocks(editor, model, lineIndex);
    } else {
      this.clearBlocks(editor);
    }
    if (config.attributeValueBackgroundEnabled) {
      editor.setDecorations(
        this.attributeValueDecoration,
        this.toEditorRanges(collectAttributeValueRanges(model), lineIndex),
      );
    } else {
      editor.setDecorations(this.attributeValueDecoration, []);
    }
    if (config.evalKeywordHighlightEnabled) {
      editor.setDecorations(
        this.evalKeywordDecoration,
        this.toEditorRanges(collectEvalKeywordRanges(model, text), lineIndex),
      );
    } else {
      editor.setDecorations(this.evalKeywordDecoration, []);
    }
    if (config.evalLiteralHighlightEnabled) {
      const literals = collectEvalLiteralRanges(model, text);
      editor.setDecorations(
        this.literalDecorations.number,
        this.toEditorRanges(literals.numbers, lineIndex),
      );
      editor.setDecorations(
        this.literalDecorations.string,
        this.toEditorRanges(literals.strings, lineIndex),
      );
    } else {
      editor.setDecorations(this.literalDecorations.number, []);
      editor.setDecorations(this.literalDecorations.string, []);
    }
  }

  private toEditorRanges(
    ranges: readonly TextRange[],
    lineIndex: LineIndex,
  ): vscode.Range[] {
    return ranges.map((range) => {
      const start = lineIndex.positionAt(range.start);
      const end = lineIndex.positionAt(range.end);
      return new vscode.Range(start.line, start.character, end.line, end.character);
    });
  }

  private applyBlocks(
    editor: vscode.TextEditor,
    model: XProcDocumentModel,
    lineIndex: LineIndex,
  ): void {
    const byKind = new Map<BlockBackgroundKind, vscode.Range[]>();
    for (const range of collectBlockBackgroundRanges(model)) {
      const start = lineIndex.positionAt(range.start).line;
      const end = lineIndex.positionAt(range.end).line;
      // isWholeLine 会忽略列，但结束行必须包含在区间内。
      const lineRange = new vscode.Range(start, 0, end, 1);
      const bucket = byKind.get(range.kind);
      if (bucket) {
        bucket.push(lineRange);
      } else {
        byKind.set(range.kind, [lineRange]);
      }
    }
    for (const [kind, decoration] of this.decorations) {
      editor.setDecorations(decoration, byKind.get(kind) ?? []);
    }
  }

  private clearBlocks(editor: vscode.TextEditor): void {
    for (const decoration of this.decorations.values()) {
      editor.setDecorations(decoration, []);
    }
  }

  private clear(editor: vscode.TextEditor): void {
    this.clearBlocks(editor);
    editor.setDecorations(this.attributeValueDecoration, []);
    editor.setDecorations(this.evalKeywordDecoration, []);
    editor.setDecorations(this.literalDecorations.number, []);
    editor.setDecorations(this.literalDecorations.string, []);
  }
}
