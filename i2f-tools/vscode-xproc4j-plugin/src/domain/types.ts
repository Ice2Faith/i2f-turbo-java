export interface TextRange {
  start: number;
  end: number;
}

export interface LinePosition {
  line: number;
  character: number;
}

export interface LineRange {
  start: LinePosition;
  end: LinePosition;
}

export type Quote = '"' | "'" | null;

export interface XmlAttributeModel {
  name: string;
  baseName: string;
  features: string[];
  rawValue: string;
  value: string;
  nameRange: TextRange;
  valueRange: TextRange;
  valueStart: number;
  valueEnd: number;
  quote: Quote;
}

export interface XmlElementModel {
  name: string;
  nameRange: TextRange;
  startOffset: number;
  startTagEnd: number;
  startTagClosed: boolean;
  selfClosing: boolean;
  endTagStart: number;
  endOffset: number;
  closed: boolean;
  parentIndex: number;
  depth: number;
  attributes: XmlAttributeModel[];
}

export type ProtectedKind = 'comment' | 'cdata' | 'pi' | 'doctype' | 'declaration';

export interface ProtectedRange extends TextRange {
  kind: ProtectedKind;
}

export type ScanIssueCode =
  | 'unclosed-comment'
  | 'unclosed-cdata'
  | 'unclosed-doctype'
  | 'unclosed-pi'
  | 'unclosed-start-tag'
  | 'unclosed-attribute-value'
  | 'unclosed-element'
  | 'unexpected-end-tag'
  | 'mismatched-end-tag'
  | 'max-elements'
  | 'max-depth';

export interface ScanIssue {
  offset: number;
  code: ScanIssueCode;
  message: string;
}

export interface ScanResult {
  elements: XmlElementModel[];
  rootIndex: number;
  protectedRanges: ProtectedRange[];
  issues: ScanIssue[];
  issueCount: number;
  truncated: boolean;
  truncatedReason?: 'max-elements' | 'max-depth';
}

export interface XProcDocumentModel {
  uri: string;
  version: number;
  textLength: number;
  isXProc: boolean;
  rootTag: string | null;
  rootTagOffset: number;
  scan: ScanResult;
  procedureIndexes: number[];
  variableNames: string[];
  refidValues: Array<{ value: string; range: TextRange }>;
  builtAt: number;
}

export interface VariableOccurrence {
  name: string;
  range: TextRange;
  empty: boolean;
  attribute: boolean;
}

export function containsRange(range: TextRange, offset: number): boolean {
  return offset >= range.start && offset <= range.end;
}

export function findEnclosingElement(
  model: XProcDocumentModel,
  offset: number,
): XmlElementModel | undefined {
  let best: XmlElementModel | undefined;
  for (const element of model.scan.elements) {
    if (element.startOffset <= offset && offset <= element.endOffset) {
      if (!best || element.depth >= best.depth) {
        best = element;
      }
    }
  }
  return best;
}
