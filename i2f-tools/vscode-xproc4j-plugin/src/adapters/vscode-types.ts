import * as vscode from 'vscode';
import type { CompletionEntry } from '../domain/completion/text-edit';
import type { NavigationTarget } from '../domain/navigation/navigation';
import type { TextRange } from '../domain/types';

export function toRange(document: vscode.TextDocument, range: TextRange): vscode.Range {
  return new vscode.Range(document.positionAt(range.start), document.positionAt(range.end));
}

export function toLocation(target: NavigationTarget): vscode.Location {
  return new vscode.Location(
    vscode.Uri.parse(target.uri),
    new vscode.Range(
      target.range.start.line,
      target.range.start.character,
      target.range.end.line,
      target.range.end.character,
    ),
  );
}

const COMPLETION_KINDS: Record<CompletionEntry['kind'], vscode.CompletionItemKind> = {
  class: vscode.CompletionItemKind.Class,
  property: vscode.CompletionItemKind.Property,
  snippet: vscode.CompletionItemKind.Snippet,
  reference: vscode.CompletionItemKind.Reference,
  enum: vscode.CompletionItemKind.EnumMember,
  constant: vscode.CompletionItemKind.Constant,
  value: vscode.CompletionItemKind.Value,
  function: vscode.CompletionItemKind.Function,
  variable: vscode.CompletionItemKind.Variable,
  keyword: vscode.CompletionItemKind.Keyword,
  text: vscode.CompletionItemKind.Text,
};

export function toCompletionItem(
  entry: CompletionEntry,
  replaceRange: TextRange,
  document: vscode.TextDocument,
): vscode.CompletionItem {
  const item = new vscode.CompletionItem(entry.label, COMPLETION_KINDS[entry.kind]);
  item.insertText =
    entry.insertTextFormat === 'snippet'
      ? new vscode.SnippetString(entry.insertText)
      : entry.insertText;
  item.filterText = entry.filterText ?? entry.label;
  item.sortText = entry.sortText;
  item.detail = entry.detail;
  item.range = toRange(document, entry.range ?? replaceRange);
  if (entry.documentation) {
    item.documentation = new vscode.MarkdownString(entry.documentation, false);
  }
  return item;
}
