import * as path from 'node:path';
import * as vscode from 'vscode';
import { resolveEol } from '../core/config';
import type { Logger } from '../core/logger';
import { formatLocalDate, formatLocalTime, resolveAuthor } from '../core/system-info';
import { normalizeFileName, validateFileName, validateProcedureId } from '../domain/templates/new-file';
import { renderProcedureTemplate } from '../domain/templates/render';
import { PROCEDURE_TEMPLATE } from '../generated/procedure-template';

export interface NewFileDependencies {
  logger: Logger;
}

async function resolveDirectory(arg?: vscode.Uri): Promise<vscode.Uri | undefined> {
  if (arg) {
    let stat: vscode.FileStat | undefined;
    try {
      stat = await vscode.workspace.fs.stat(arg);
    } catch {
      stat = undefined;
    }
    if (stat?.type === vscode.FileType.Directory) {
      return arg;
    }
    if (stat?.type === vscode.FileType.File) {
      return vscode.Uri.joinPath(arg, '..');
    }
  }
  const folders = vscode.workspace.workspaceFolders ?? [];
  if (folders.length === 1) {
    return folders[0]!.uri;
  }
  if (folders.length > 1) {
    const picked = await vscode.window.showQuickPick(
      folders.map((folder) => ({
        label: folder.name,
        description: folder.uri.fsPath,
        uri: folder.uri,
      })),
      { placeHolder: '选择目标工作区文件夹' },
    );
    return picked?.uri;
  }
  const picked = await vscode.window.showOpenDialog({
    canSelectFiles: false,
    canSelectFolders: true,
    canSelectMany: false,
    openLabel: '选择目标目录',
  });
  return picked?.[0];
}

async function exists(uri: vscode.Uri): Promise<boolean> {
  try {
    await vscode.workspace.fs.stat(uri);
    return true;
  } catch {
    return false;
  }
}

function validateNameInput(raw: string): string | vscode.InputBoxValidationMessage | undefined {
  const check = validateFileName(raw);
  if (!check.valid) {
    return check.message;
  }
  const normalized = normalizeFileName(raw);
  if (normalized !== raw.trim()) {
    return {
      message: `将保存为 ${normalized}`,
      severity: vscode.InputBoxValidationSeverity.Info,
    };
  }
  return undefined;
}

/**
 * 新建 Procedure 文件：所有输入均可取消；只有最终 WorkspaceEdit 会写盘。
 * 覆盖需要用户显式确认，不静默覆盖。
 */
export async function createProcedureFile(
  arg: vscode.Uri | undefined,
  deps: NewFileDependencies,
): Promise<void> {
  const directory = await resolveDirectory(arg);
  if (!directory) {
    return;
  }

  const id = await vscode.window.showInputBox({
    prompt: '输入 procedure id',
    placeHolder: '例如：user-query',
    value: 'procedure',
    ignoreFocusOut: true,
    validateInput: (value) => {
      const result = validateProcedureId(value);
      return result.valid ? undefined : result.message;
    },
  });
  if (id === undefined) {
    return;
  }
  const trimmedId = id.trim();

  const fileName = await vscode.window.showInputBox({
    prompt: '输入文件名（默认 .xml 扩展名）',
    placeHolder: '例如：user-query.xml',
    value: `${trimmedId}.xml`,
    ignoreFocusOut: true,
    validateInput: validateNameInput,
  });
  if (fileName === undefined) {
    return;
  }

  const normalizedName = normalizeFileName(fileName);
  // 双重校验：即使 UI 校验被绕过，也不允许目录穿越。
  if (
    path.posix.basename(normalizedName) !== normalizedName ||
    path.win32.basename(normalizedName) !== normalizedName
  ) {
    throw new Error('文件名无效：不能包含路径片段');
  }
  const target = vscode.Uri.joinPath(directory, normalizedName);

  let overwrite = false;
  if (await exists(target)) {
    const choice = await vscode.window.showWarningMessage(
      `文件已存在：${normalizedName}，是否覆盖？`,
      { modal: true },
      '覆盖',
    );
    if (choice !== '覆盖') {
      return;
    }
    overwrite = true;
  }

  const now = new Date();
  const content = renderProcedureTemplate(PROCEDURE_TEMPLATE, {
    id: trimmedId,
    author: resolveAuthor(),
    date: formatLocalDate(now),
    time: formatLocalTime(now),
    eol: resolveEol(target),
  });

  // 竞态保护：用户确认期间文件可能被其他进程创建。
  if (!overwrite && (await exists(target))) {
    await vscode.window.showWarningMessage('目标文件在操作期间被创建，已取消写入。');
    return;
  }

  const edit = new vscode.WorkspaceEdit();
  edit.createFile(target, { overwrite: true, ignoreIfExists: false });
  edit.insert(target, new vscode.Position(0, 0), content);
  const applied = await vscode.workspace.applyEdit(edit);
  if (!applied) {
    deps.logger.error(`写入文件失败（${target.toString()}）`);
    throw new Error('无法写入文件：工作区文件系统拒绝编辑，请检查目录权限');
  }

  deps.logger.info(`已创建 XProc4J 文件：${target.toString()}`);
  await vscode.window.showTextDocument(target, { preview: false });
}
