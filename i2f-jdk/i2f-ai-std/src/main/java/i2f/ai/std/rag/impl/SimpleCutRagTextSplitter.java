package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagTextSplitter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/3/31 10:17
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleCutRagTextSplitter implements RagTextSplitter {
    protected int maxSegmentSizeInChars = 1024;
    protected double minSegmentSizeRate = 0.6;

    public SimpleCutRagTextSplitter maxSegmentSizeInChars(int maxSegmentSizeInChars) {
        this.maxSegmentSizeInChars = maxSegmentSizeInChars;
        return this;
    }

    @Override
    public List<String> split(String text) {
        Set<String> ret = new LinkedHashSet<>();
        double minSegmentSize = maxSegmentSizeInChars * minSegmentSizeRate;
        int halfSegmentSize = maxSegmentSizeInChars / 2;
        String[] lines = text.split("\n");
        StringBuilder curr = new StringBuilder();
        for (String line : lines) {
            int sumLen = curr.length() + line.length();
            if (sumLen + 1 < maxSegmentSizeInChars) {
                curr.append(line).append("\n");
            } else {
                // 前后两部分如果满足大小限定，都存入
                String last = curr.toString();
                curr.setLength(0);

                String trimLast = last.trim();
                if (trimLast.length() <= maxSegmentSizeInChars && trimLast.length() > minSegmentSize) {
                    ret.add(trimLast);
                }

                String trimLine = line.trim();
                if (trimLine.length() <= maxSegmentSizeInChars && trimLine.length() > minSegmentSize) {
                    ret.add(trimLine.trim());
                }

                String full = (last + line).trim();
                // 强制按块分割
                int idx = 0;
                while (idx + maxSegmentSizeInChars < full.length()) {
                    String next = full.substring(idx, idx + maxSegmentSizeInChars);
                    int irn = next.lastIndexOf("\n");
                    boolean adjustIndex = false;
                    if (irn > 0 && irn > minSegmentSize) {
                        idx += irn;
                        next = next.substring(0, irn);
                        adjustIndex = true;
                    }
                    String nextTrim = next.trim();
                    if (!nextTrim.isEmpty()) {
                        ret.add(nextTrim);
                    }
                    if (!adjustIndex) {
                        idx += maxSegmentSizeInChars;
                    }
                }
                if (idx < full.length()) {
                    curr.append(full.substring(idx)).append("\n");
                }
                // 错位分割，尽量保持关联性
                idx = halfSegmentSize;
                int fln = full.indexOf("\n");
                if (fln <= idx) {
                    idx = fln + 1;
                }
                while (idx + maxSegmentSizeInChars < full.length()) {
                    String next = full.substring(idx, idx + maxSegmentSizeInChars);
                    int irn = next.lastIndexOf("\n");
                    boolean adjustIndex = false;
                    if (irn > 0 && irn > minSegmentSize) {
                        idx += irn;
                        next = next.substring(0, irn);
                        adjustIndex = true;
                    }
                    String nextTrim = next.trim();
                    if (!nextTrim.isEmpty()) {
                        ret.add(nextTrim);
                    }
                    if (!adjustIndex) {
                        idx += maxSegmentSizeInChars;
                    }
                }

            }
        }
        if (curr.length() > 0) {
            String trimLeft = curr.toString().trim();
            if (!trimLeft.isEmpty()) {
                ret.add(trimLeft);
            }
        }

        return new ArrayList<>(ret);
    }
}
