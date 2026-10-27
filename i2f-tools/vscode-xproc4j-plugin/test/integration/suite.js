const assert = require('node:assert');
const fs = require('node:fs');
const path = require('node:path');
const vscode = require('vscode');

const EXTENSION_ID = 'ice2faith.xproc4j';
const TIMEOUT_MS = 20_000;

function labelsOf(result) {
  const items = Array.isArray(result) ? result : (result?.items ?? []);
  return items.map((item) => (typeof item.label === 'string' ? item.label : item.label.label));
}

async function waitFor(label, action) {
  const started = Date.now();
  while (Date.now() - started < TIMEOUT_MS) {
    const value = await action();
    if (value) {
      return value;
    }
    await new Promise((resolve) => setTimeout(resolve, 200));
  }
  throw new Error(`等待超时：${label}`);
}

async function completionLabels(uri, position, triggerCharacter) {
  const result = await vscode.commands.executeCommand(
    'vscode.executeCompletionItemProvider',
    uri,
    position,
    triggerCharacter,
  );
  return labelsOf(result);
}

async function completionItems(uri, position, triggerCharacter) {
  const result = await vscode.commands.executeCommand(
    'vscode.executeCompletionItemProvider',
    uri,
    position,
    triggerCharacter,
  );
  return Array.isArray(result) ? result : (result?.items ?? []);
}

async function hasSemanticTagCompletion(uri, position, triggerCharacter, label) {
  const items = await completionItems(uri, position, triggerCharacter);
  return items.some((item) => {
    const itemLabel = typeof item.label === 'string' ? item.label : item.label.label;
    return (
      itemLabel === label &&
      typeof item.detail === 'string' &&
      item.detail.startsWith('标签 ·')
    );
  });
}

async function setConfig(key, value) {
  await vscode.workspace
    .getConfiguration('xproc4j')
    .update(key, value, vscode.ConfigurationTarget.Global);
  // 配置监听器会清理 Gate/模型缓存，给事件循环一个回合再继续断言。
  await new Promise((resolve) => setTimeout(resolve, 50));
}

async function symbolsOf(uri) {
  const result = await vscode.commands.executeCommand(
    'vscode.executeDocumentSymbolProvider',
    uri,
  );
  return Array.isArray(result) ? result : [];
}

async function foldingOf(uri) {
  const result = await vscode.commands.executeCommand(
    'vscode.executeFoldingRangeProvider',
    uri,
  );
  return Array.isArray(result) ? result : [];
}

async function testActivation() {
  const extension = vscode.extensions.getExtension(EXTENSION_ID);
  if (!extension) {
    console.log(
      '[integration] 已加载扩展：',
      vscode.extensions.all.filter((item) => item.id.includes('xproc')).map((item) => item.id),
    );
  }
  assert.ok(extension, '扩展应已安装');
  await extension.activate();
  assert.ok(extension.isActive, '扩展应已激活');
  const commands = await vscode.commands.getCommands(true);
  for (const command of [
    'xproc4j.createProcedureFile',
    'xproc4j.rebuildIndex',
    'xproc4j.showOutput',
  ]) {
    assert.ok(commands.includes(command), `应注册命令 ${command}`);
  }
}

async function testTagCompletion() {
  const document = await vscode.workspace.openTextDocument({
    language: 'xml',
    content: '<procedure id="X">\n    <\n</procedure>\n',
  });
  await vscode.window.showTextDocument(document);
  const position = new vscode.Position(1, 5);
  const labels = await completionLabels(document.uri, position, '<');
  assert.ok(labels.includes('procedure-call'), '标签补全应包含 procedure-call');
  assert.ok(labels.includes('script-segment'), '标签补全应包含 script-segment');
}

async function testVariableCompletion() {
  const content = '<procedure id="X">\n    <script-segment id="S">SELECT ${co</script-segment>\n</procedure>\n';
  const document = await vscode.workspace.openTextDocument({ language: 'xml', content });
  await vscode.window.showTextDocument(document);
  const offset = content.indexOf('${co') + '${co'.length;
  const labels = await completionLabels(document.uri, document.positionAt(offset), '{');
  assert.ok(labels.includes('context'), '变量补全应包含内置变量 context');
}

async function testRefidCompletionAndNavigation() {
  const folder = vscode.workspace.workspaceFolders?.[0];
  assert.ok(folder, '测试工作区应已打开');
  const aUri = vscode.Uri.joinPath(folder.uri, 'a.xml');
  const document = await vscode.workspace.openTextDocument(aUri);
  await vscode.window.showTextDocument(document);
  const text = document.getText();

  const refidOffset = text.indexOf('refid="B"') + 'refid="'.length;
  const positions = {
    refid: document.positionAt(refidOffset),
    declaration: document.positionAt(text.indexOf('id="A"') + 'id="'.length),
  };

  const refidLabels = await waitFor('refid 索引补全', async () => {
    const labels = await completionLabels(document.uri, positions.refid, '"');
    return labels.includes('B') ? labels : undefined;
  });
  assert.ok(refidLabels.includes('B'));

  const definitions = await waitFor('跨文件定义跳转', async () => {
    const result = await vscode.commands.executeCommand(
      'vscode.executeDefinitionProvider',
      document.uri,
      positions.refid,
    );
    return Array.isArray(result) && result.length ? result : undefined;
  });
  assert.ok(definitions.every((location) => location.uri instanceof vscode.Uri));
  assert.ok(
    definitions.some((location) => location.uri.path.endsWith('/b.xml')),
    '定义应指向 b.xml',
  );

  const references = await waitFor('跨文件引用查找', async () => {
    const result = await vscode.commands.executeCommand(
      'vscode.executeReferenceProvider',
      document.uri,
      positions.declaration,
    );
    return Array.isArray(result) && result.length ? result : undefined;
  });
  assert.ok(
    references.some((location) => location.uri.path.endsWith('/b.xml')),
    '引用应包含 b.xml 中的调用',
  );

  const symbols = await vscode.commands.executeCommand(
    'vscode.executeDocumentSymbolProvider',
    document.uri,
  );
  assert.ok(Array.isArray(symbols) && symbols.length > 0, '应返回文档符号');
  assert.ok(
    symbols.some((symbol) => symbol.name.startsWith('procedure id=A')),
    '顶层符号应包含 procedure id=A',
  );

  const folding = await vscode.commands.executeCommand(
    'vscode.executeFoldingRangeProvider',
    document.uri,
  );
  assert.ok(Array.isArray(folding) && folding.length > 0, '应返回折叠范围');
}

async function testNonProcedureGate() {
  const content = `<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    <
</project>
`;
  const document = await vscode.workspace.openTextDocument({ language: 'xml', content });
  await vscode.window.showTextDocument(document);
  const position = document.positionAt(content.lastIndexOf('<'));
  const items = await completionItems(document.uri, position, '<');
  const suspicious = items.filter((item) =>
    ['procedure-call', 'script-segment', 'sql-query-list', 'context'].includes(
      typeof item.label === 'string' ? item.label : item.label.label,
    ),
  );
  console.log(
    '[integration] 非 procedure 候选：',
    suspicious.map((item) => ({
      label: typeof item.label === 'string' ? item.label : item.label.label,
      kind: item.kind,
      detail: item.detail,
    })),
  );
  // 原生 Snippets 在 VS Code 中按语言全局生效，无法按根节点门控；
  // FR-07 明确允许 POM 中出现片段候选（可由 Snippet 管理关闭）。
  // 这里只断言语义 Provider 不提供 XProc4J 候选：语义候选带有“标签 · / 变量 ·”detail。
  for (const label of ['procedure-call', 'script-segment', 'sql-query-list', 'context']) {
    assert.ok(
      !items.some((item) => {
        const itemLabel = typeof item.label === 'string' ? item.label : item.label.label;
        return (
          itemLabel === label &&
          typeof item.detail === 'string' &&
          (item.detail.startsWith('标签 ·') || item.detail.startsWith('变量 ·'))
        );
      }),
      `非 procedure XML 不应出现 XProc4J 语义候选 ${label}`,
    );
  }

  const symbols = await symbolsOf(document.uri);
  assert.equal(symbols.length, 0, '非 procedure XML 不应返回 XProc4J 大纲');
  const folding = await foldingOf(document.uri);
  assert.ok(
    folding.every((range) => !range.kind || range.kind !== vscode.FoldingRangeKind.Region),
    '非 procedure XML 不应返回 XProc4J 折叠区域',
  );
}

async function testConfigToggles() {
  const completionText =
    '<procedure id="X">\n    <script-segment id="S">select 1</script-segment>\n    <\n</procedure>\n';
  const document = await vscode.workspace.openTextDocument({
    language: 'xml',
    content: completionText,
  });
  await vscode.window.showTextDocument(document);
  const position = new vscode.Position(2, 5);

  const labels = () => completionLabels(document.uri, position, '<');
  const assertTagEnabled = async (expected) => {
    const enabled = await hasSemanticTagCompletion(
      document.uri,
      position,
      '<',
      'procedure-call',
    );
    assert.equal(
      enabled,
      expected,
      `procedure-call 语义补全应${expected ? '启用' : '禁用'}`,
    );
  };
  const openProcedure = async () => {
    const opened = await vscode.workspace.openTextDocument({
      language: 'xml',
      content: '<procedure id="X">\n    <script-segment id="S">select 1</script-segment>\n</procedure>\n',
    });
    await vscode.window.showTextDocument(opened);
    return opened;
  };
  const assertStructureEnabled = async (expected) => {
    // VS Code 会按文档版本缓存大纲/折叠结果；每次配置变化后换新文档，
    // 避免把命令缓存误判为 Provider 行为。
    const opened = await openProcedure();
    assert.equal(
      (await symbolsOf(opened.uri)).length > 0,
      expected,
      `大纲应${expected ? '启用' : '禁用'}`,
    );
    const hasRegion = (await foldingOf(opened.uri)).some(
      (range) => range.kind === vscode.FoldingRangeKind.Region,
    );
    assert.equal(hasRegion, expected, `XProc4J 折叠应${expected ? '启用' : '禁用'}`);
  };

  await assertTagEnabled(true);
  assert.ok((await labels()).includes('procedure-call'), '默认应提供 procedure-call 候选');
  await assertStructureEnabled(true);
  try {
    await setConfig('completion.enabled', false);
    await assertTagEnabled(false);
    await setConfig('completion.enabled', true);
    await assertTagEnabled(true);

    await setConfig('enable', false);
    await assertTagEnabled(false);
    await assertStructureEnabled(false);
    await setConfig('enable', true);
    await assertTagEnabled(true);
    await assertStructureEnabled(true);

    await setConfig('symbols.enabled', false);
    const noSymbols = await openProcedure();
    assert.equal((await symbolsOf(noSymbols.uri)).length, 0, 'symbols.enabled=false 时不应返回大纲');
    await setConfig('symbols.enabled', true);
    const withSymbols = await openProcedure();
    assert.ok((await symbolsOf(withSymbols.uri)).length > 0, '恢复后应返回大纲');

    await setConfig('folding.enabled', false);
    const noFolding = await openProcedure();
    assert.ok(
      !(await foldingOf(noFolding.uri)).some(
        (range) => range.kind === vscode.FoldingRangeKind.Region,
      ),
      'folding.enabled=false 时不应返回 XProc4J 折叠',
    );
    await setConfig('folding.enabled', true);
    const withFolding = await openProcedure();
    assert.ok(
      (await foldingOf(withFolding.uri)).some(
        (range) => range.kind === vscode.FoldingRangeKind.Region,
      ),
      '恢复后应返回 XProc4J 折叠',
    );

    const bigText =
      '<procedure id="X">\n' + 'x'.repeat(2048) + '\n<\n</procedure>\n';
    const bigDocument = await vscode.workspace.openTextDocument({
      language: 'xml',
      content: bigText,
    });
    await vscode.window.showTextDocument(bigDocument);
    const bigPosition = new vscode.Position(2, 1);
    await setConfig('maxDocumentSizeKB', 1);
    assert.ok(
      !(await hasSemanticTagCompletion(
        bigDocument.uri,
        bigPosition,
        '<',
        'procedure-call',
      )),
      'maxDocumentSizeKB 超限时不应返回语义补全',
    );
    await setConfig('maxDocumentSizeKB', 2048);
    assert.ok(
      await hasSemanticTagCompletion(
        bigDocument.uri,
        bigPosition,
        '<',
        'procedure-call',
      ),
      '恢复大小上限后应返回语义补全',
    );

    const folder = vscode.workspace.workspaceFolders?.[0];
    assert.ok(folder, '测试工作区应已打开');
    const aDocument = await vscode.workspace.openTextDocument(
      vscode.Uri.joinPath(folder.uri, 'a.xml'),
    );
    await vscode.window.showTextDocument(aDocument);
    const refidOffset = aDocument.getText().indexOf('refid="B"') + 'refid="'.length;
    const refidPosition = aDocument.positionAt(refidOffset);
    await setConfig('refid.enabled', false);
    const definitionsOff = await vscode.commands.executeCommand(
      'vscode.executeDefinitionProvider',
      aDocument.uri,
      refidPosition,
    );
    assert.ok(!definitionsOff || definitionsOff.length === 0, 'refid.enabled=false 时不应跳转');
    await setConfig('refid.enabled', true);
    const definitionsOn = await waitFor('恢复 refid 跳转', async () => {
      const result = await vscode.commands.executeCommand(
        'vscode.executeDefinitionProvider',
        aDocument.uri,
        refidPosition,
      );
      return Array.isArray(result) && result.length ? result : undefined;
    });
    assert.ok(
      definitionsOn.some((location) => location.uri.path.endsWith('/b.xml')),
      '恢复后定义应指向 b.xml',
    );
  } finally {
    await setConfig('completion.enabled', true);
    await setConfig('enable', true);
    await setConfig('symbols.enabled', true);
    await setConfig('folding.enabled', true);
    await setConfig('maxDocumentSizeKB', 2048);
    await setConfig('refid.enabled', true);
  }
}

async function testSnippetArtifact() {
  const extension = vscode.extensions.getExtension(EXTENSION_ID);
  assert.ok(extension, '扩展应已安装');
  const file = path.join(extension.extensionPath, 'snippets/xproc4j.code-snippets');
  const snippets = JSON.parse(fs.readFileSync(file, 'utf8'));
  const prefixes = Object.keys(snippets);
  assert.equal(prefixes.length, 90, '原生片段应包含 90 个模板');
  assert.ok(prefixes.every((prefix) => prefix.startsWith('<')), '片段前缀应以 < 开头');
  assert.ok(
    prefixes.every((prefix) => snippets[prefix].scope === 'xml'),
    '片段作用域应为 xml',
  );
  const procedureBody = snippets['<procedure'].body.join('\n');
  assert.ok(procedureBody.includes('<!DOCTYPE procedure'), '<procedure> 应包含 DOCTYPE');
  assert.ok(procedureBody.includes('$0'), '<procedure> 应包含最终光标占位符');
}

exports.run = async function run() {
  const results = [];
  const tests = [
    ['激活与命令注册', testActivation],
    ['标签补全', testTagCompletion],
    ['变量补全', testVariableCompletion],
    ['refid 补全、定义、引用、大纲与折叠', testRefidCompletionAndNavigation],
    ['非 procedure XML 门控', testNonProcedureGate],
    ['配置开关', testConfigToggles],
    ['原生片段产物', testSnippetArtifact],
  ];
  for (const [name, run] of tests) {
    await run();
    results.push(name);
    console.log(`[integration] PASS ${name}`);
  }
  console.log(`[integration] 全部通过：${results.length} 项`);
};
