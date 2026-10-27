import fs from 'node:fs';
import path from 'node:path';
import * as oniguruma from 'vscode-oniguruma';
import {
  INITIAL,
  Registry,
  parseRawGrammar,
  type IGrammar,
  type StateStack,
} from 'vscode-textmate';

const root = path.resolve(__dirname, '../..');
const vendor = path.join(root, 'test/grammar/vendor');

const wasmBin = fs.readFileSync(
  path.join(root, 'node_modules/vscode-oniguruma/release/onig.wasm'),
);
const onigLib = oniguruma.loadWASM(
  wasmBin.buffer.slice(wasmBin.byteOffset, wasmBin.byteOffset + wasmBin.byteLength),
).then(() => ({
  createOnigScanner(patterns: string[]) {
    return new oniguruma.OnigScanner(patterns);
  },
  createOnigString(text: string) {
    return new oniguruma.OnigString(text);
  },
}));

/**
 * vscode-textmate 不支持跨语法注入（README 明确说明），VS Code 的注入由
 * tokenizer service 完成。测试中把注入语法的 patterns/repository 合并进
 * text.xml 根规则，等价于 VS Code 的根级注入，用于验证规则本身。
 */
function mergeInjection(hostRaw: any, injectionRaw: any): any {
  return {
    ...hostRaw,
    patterns: [...injectionRaw.patterns, ...(hostRaw.patterns ?? [])],
    repository: {
      ...(hostRaw.repository ?? {}),
      ...(injectionRaw.repository ?? {}),
    },
  };
}

function readRaw(scopeName: string): any {
  const files: Record<string, string> = {
    'text.xml': 'xml.tmLanguage.json',
    'source.sql': 'sql.tmLanguage.json',
    'source.java': 'java.tmLanguage.json',
    'source.js': 'JavaScript.tmLanguage.json',
  };
  const file = files[scopeName];
  if (!file) {
    return null;
  }
  return JSON.parse(fs.readFileSync(path.join(vendor, file), 'utf8'));
}

export interface Token {
  text: string;
  scopes: string[];
}

export async function loadXProcGrammar(): Promise<IGrammar> {
  const injection = JSON.parse(
    fs.readFileSync(path.join(root, 'syntaxes/xproc4j.injection.tmLanguage.json'), 'utf8'),
  );
  const host = readRaw('text.xml');
  const merged = mergeInjection(host, injection);
  const registry = new Registry({
    onigLib,
    loadGrammar: async (scopeName: string) => {
      if (scopeName === 'text.xml') {
        return parseRawGrammar(JSON.stringify(merged), 'merged.xml.tmLanguage.json');
      }
      const raw = readRaw(scopeName);
      return raw ? parseRawGrammar(JSON.stringify(raw), `${scopeName}.json`) : null;
    },
  });
  const grammar = await registry.loadGrammar('text.xml');
  if (!grammar) {
    throw new Error('failed to load grammar');
  }
  return grammar;
}

export function tokenize(grammar: IGrammar, text: string): Token[][] {
  let stack: StateStack = INITIAL;
  const lines = text.split('\n');
  return lines.map((line) => {
    const result = grammar.tokenizeLine(line, stack);
    stack = result.ruleStack;
    return result.tokens.map((token) => ({
      text: line.slice(token.startIndex, token.endIndex),
      scopes: token.scopes,
    }));
  });
}

export function flatten(lines: Token[][]): Token[] {
  return lines.flat();
}

export function hasScope(token: Token, scope: string): boolean {
  return token.scopes.some((s) => s === scope || s.startsWith(`${scope}.`) || s.startsWith(`${scope} `));
}

export function findToken(tokens: Token[], text: string, occurrence = 0): Token | undefined {
  return tokens.filter((t) => t.text === text)[occurrence];
}
