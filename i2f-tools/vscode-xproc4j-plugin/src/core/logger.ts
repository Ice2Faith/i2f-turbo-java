import * as vscode from 'vscode';
import { isTraceVerbose } from './config';

/** 输出通道只记录元信息；禁止写入文档内容、变量值或凭据。 */
export class Logger implements vscode.Disposable {
  private readonly channel: vscode.OutputChannel;

  constructor() {
    this.channel = vscode.window.createOutputChannel('XProc4J');
  }

  debug(message: string): void {
    if (isTraceVerbose()) {
      this.channel.appendLine(`[debug] ${message}`);
    }
  }

  info(message: string): void {
    this.channel.appendLine(`[info] ${message}`);
  }

  warn(message: string): void {
    this.channel.appendLine(`[warn] ${message}`);
  }

  error(message: string, error?: unknown): void {
    const suffix = error instanceof Error ? `: ${error.message}` : error === undefined ? '' : `: ${String(error)}`;
    this.channel.appendLine(`[error] ${message}${suffix}`);
  }

  show(preserveFocus = true): void {
    this.channel.show(preserveFocus);
  }

  dispose(): void {
    this.channel.dispose();
  }
}
