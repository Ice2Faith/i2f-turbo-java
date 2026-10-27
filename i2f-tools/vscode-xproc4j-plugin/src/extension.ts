import * as vscode from 'vscode';
import { activateExtension } from './core/activation';

export async function activate(context: vscode.ExtensionContext): Promise<void> {
  await activateExtension(context);
}

export function deactivate(): void {
  // 所有资源通过 context.subscriptions 释放。
}
