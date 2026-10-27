import * as vscode from 'vscode';
import {
  DEFAULT_VARIABLE_HIGHLIGHT_EXTENSIONS,
  normalizeFileExtensions,
} from '../domain/semantics/variable-file-extensions';

export interface XProcConfig {
  enable: boolean;
  indexEnabled: boolean;
  include: string[];
  exclude: string[];
  maxFiles: number;
  completionEnabled: boolean;
  sqlKeywords: boolean;
  builtinVariables: boolean;
  refidEnabled: boolean;
  symbolsEnabled: boolean;
  foldingEnabled: boolean;
  blockBackgroundEnabled: boolean;
  attributeValueBackgroundEnabled: boolean;
  evalKeywordHighlightEnabled: boolean;
  evalLiteralHighlightEnabled: boolean;
  variableHighlightExtensions: string[];
  maxDocumentSizeKB: number;
}

const DEFAULT_INCLUDE = ['**/*.xml'];
const DEFAULT_EXCLUDE = [
  '**/node_modules/**',
  '**/.git/**',
  '**/target/**',
  '**/build/**',
  '**/out/**',
  '**/dist/**',
  '**/logs/**',
];

function readBoolean(value: unknown, fallback: boolean): boolean {
  return typeof value === 'boolean' ? value : fallback;
}

function readPatterns(value: unknown, fallback: string[]): string[] {
  if (!Array.isArray(value)) {
    return fallback;
  }
  const cleaned = [
    ...new Set(
      value
        .filter((item): item is string => typeof item === 'string')
        .map((item) => item.trim())
        .filter(Boolean),
    ),
  ];
  return cleaned.length ? cleaned : fallback;
}

function readNumber(value: unknown, fallback: number, min: number, max: number): number {
  if (typeof value !== 'number' || !Number.isFinite(value)) {
    return fallback;
  }
  return Math.min(max, Math.max(min, Math.round(value)));
}

export function readConfig(scope?: vscode.ConfigurationScope): XProcConfig {
  const config = vscode.workspace.getConfiguration('xproc4j', scope ?? null);
  return {
    enable: readBoolean(config.get('enable'), true),
    indexEnabled: readBoolean(config.get('index.enabled'), true),
    include: readPatterns(config.get('projectScan.include'), DEFAULT_INCLUDE),
    exclude: readPatterns(config.get('projectScan.exclude'), DEFAULT_EXCLUDE),
    maxFiles: readNumber(config.get('projectScan.maxFiles'), 20000, 1, 100000),
    completionEnabled: readBoolean(config.get('completion.enabled'), true),
    sqlKeywords: readBoolean(config.get('completion.sqlKeywords'), true),
    builtinVariables: readBoolean(config.get('completion.builtinVariables'), true),
    refidEnabled: readBoolean(config.get('refid.enabled'), true),
    symbolsEnabled: readBoolean(config.get('symbols.enabled'), true),
    foldingEnabled: readBoolean(config.get('folding.enabled'), true),
    blockBackgroundEnabled: readBoolean(config.get('blockBackground.enabled'), true),
    attributeValueBackgroundEnabled: readBoolean(
      config.get('attributeValueBackground.enabled'),
      true,
    ),
    evalKeywordHighlightEnabled: readBoolean(
      config.get('evalKeywordHighlight.enabled'),
      true,
    ),
    evalLiteralHighlightEnabled: readBoolean(config.get('evalLiteralHighlight.enabled'), true),
    variableHighlightExtensions: normalizeFileExtensions(
      config.get('variableHighlight.extensions'),
      DEFAULT_VARIABLE_HIGHLIGHT_EXTENSIONS,
    ),
    maxDocumentSizeKB: readNumber(config.get('maxDocumentSizeKB'), 2048, 1, 102400),
  };
}

export function resolveEol(target?: vscode.Uri): '\n' | '\r\n' {
  const value = vscode.workspace.getConfiguration('files', target ?? null).get<string>('eol', 'auto');
  if (value === '\r\n') {
    return '\r\n';
  }
  if (value === '\n') {
    return '\n';
  }
  return process.platform === 'win32' ? '\r\n' : '\n';
}

export function isTraceVerbose(): boolean {
  return vscode.workspace.getConfiguration('xproc4j').get<string>('trace.server', 'off') === 'verbose';
}
