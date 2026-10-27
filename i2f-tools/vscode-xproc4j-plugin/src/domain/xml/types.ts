import type { TextRange, XmlAttributeModel } from '../types';

export interface ParsedStartTag {
  name: string;
  nameRange: TextRange;
  attributes: XmlAttributeModel[];
  startTagEnd: number;
  closed: boolean;
  selfClosing: boolean;
  endOffset: number;
}
