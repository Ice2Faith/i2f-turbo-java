import type { XProcDocumentModel } from '../types';
import { LineIndex } from '../xml/line-index';

export interface FoldRegion {
  startLine: number;
  endLine: number;
  kind: 'region';
}

const MAX_REGIONS = 5000;

export function buildFoldingRegions(text: string, model: XProcDocumentModel): FoldRegion[] {
  const index = new LineIndex(text);
  const regions: FoldRegion[] = [];

  for (const element of model.scan.elements) {
    if (!element.closed || element.selfClosing || element.endTagStart < 0) {
      continue;
    }
    if (!element.startTagClosed || element.startTagEnd >= element.endTagStart) {
      continue;
    }
    const startLine = index.positionAt(element.startOffset).line;
    let endLine = index.positionAt(Math.max(element.startOffset, element.endOffset - 1)).line;
    if (endLine <= startLine) {
      continue;
    }

    const closeStart = index.positionAt(element.endTagStart);
    if (closeStart.line > startLine) {
      const lineStart = index.offsetAt({ line: closeStart.line, character: 0 });
      const lineEnd = index.offsetAt({ line: closeStart.line + 1, character: 0 });
      const before = text.slice(lineStart, element.endTagStart);
      const after = text.slice(Math.min(element.endOffset, lineEnd), lineEnd);
      if (!before.trim() && !after.trim()) {
        endLine = closeStart.line - 1;
      }
    }
    if (endLine > startLine) {
      regions.push({ startLine, endLine, kind: 'region' });
    }
    if (regions.length >= MAX_REGIONS) {
      break;
    }
  }
  return regions;
}
