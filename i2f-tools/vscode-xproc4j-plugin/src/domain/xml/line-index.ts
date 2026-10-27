import type { LinePosition, TextRange } from '../types';

/** 偏移与行列互转；text 变化时必须重建。 */
export class LineIndex {
  private readonly lineStarts: number[];

  constructor(private readonly text: string) {
    const starts = [0];
    for (let i = 0; i < text.length; i++) {
      if (text.charCodeAt(i) === 10) {
        starts.push(i + 1);
      }
    }
    this.lineStarts = starts;
  }

  get lineCount(): number {
    return this.lineStarts.length;
  }

  positionAt(offset: number): LinePosition {
    const clamped = Math.max(0, Math.min(offset, this.text.length));
    let low = 0;
    let high = this.lineStarts.length - 1;
    while (low <= high) {
      const mid = (low + high) >> 1;
      const start = this.lineStarts[mid]!;
      const next = mid + 1 < this.lineStarts.length ? this.lineStarts[mid + 1]! : this.text.length + 1;
      if (clamped < start) {
        high = mid - 1;
      } else if (clamped >= next) {
        low = mid + 1;
      } else {
        return { line: mid, character: clamped - start };
      }
    }
    const last = this.lineStarts.length - 1;
    return { line: last, character: clamped - this.lineStarts[last]! };
  }

  offsetAt(position: LinePosition): number {
    const line = Math.max(0, Math.min(position.line, this.lineStarts.length - 1));
    const start = this.lineStarts[line]!;
    const end = line + 1 < this.lineStarts.length ? this.lineStarts[line + 1]! - 1 : this.text.length;
    return Math.max(start, Math.min(start + Math.max(0, position.character), end));
  }

  lineRange(range: TextRange) {
    return { start: this.positionAt(range.start), end: this.positionAt(range.end) };
  }
}
