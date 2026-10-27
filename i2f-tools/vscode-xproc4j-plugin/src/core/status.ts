import * as vscode from 'vscode';

export type IndexStatus = 'idle' | 'building' | 'ready' | 'failed' | 'disabled';

export class StatusBar implements vscode.Disposable {
  private readonly item: vscode.StatusBarItem;
  private hideTimer: NodeJS.Timeout | undefined;

  constructor() {
    this.item = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Right, 90);
    this.item.command = 'xproc4j.showOutput';
    this.set('idle');
  }

  set(state: IndexStatus, detail = ''): void {
    if (this.hideTimer) {
      clearTimeout(this.hideTimer);
      this.hideTimer = undefined;
    }
    switch (state) {
      case 'building':
        this.item.text = `$(sync~spin) XProc4J 索引${detail ? ` ${detail}` : ''}`;
        this.item.tooltip = 'XProc4J 正在构建工程索引';
        this.item.show();
        break;
      case 'ready':
        this.item.text = '$(check) XProc4J 索引就绪';
        this.item.tooltip = detail || 'XProc4J 工程索引已就绪';
        this.item.show();
        this.hideTimer = setTimeout(() => this.item.hide(), 3000);
        break;
      case 'failed':
        this.item.text = '$(warning) XProc4J 索引失败';
        this.item.tooltip = '点击查看 XProc4J 输出';
        this.item.show();
        break;
      case 'disabled':
        this.item.text = '$(circle-slash) XProc4J 索引已禁用';
        this.item.tooltip = 'xproc4j.index.enabled = false';
        this.item.show();
        break;
      default:
        this.item.hide();
        break;
    }
  }

  dispose(): void {
    if (this.hideTimer) {
      clearTimeout(this.hideTimer);
    }
    this.item.dispose();
  }
}
