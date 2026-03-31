package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagTextSplitter;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexFindPartMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/31 10:17
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleRecursiveRagTextSplitter implements RagTextSplitter {

    public int maxSegmentSizeInChars = 128;
    public double maxOverlapRate = 0.15;
    public String[] separatorsRegex = {
            "(\\s*\n){2,}",
            "\n",
            "[。！？]",
            "[：；]",
            "[，]",
            "[、]",
            "[\\.\\!\\?]",
            "[:;]",
            "[,]",
            "[\\s]+",
            ""
    };

    public SimpleRecursiveRagTextSplitter maxSegmentSizeInChars(int maxSegmentSizeInChars) {
        this.maxSegmentSizeInChars = maxSegmentSizeInChars;
        return this;
    }

    public SimpleRecursiveRagTextSplitter maxOverlapRate(double maxOverlapRate) {
        this.maxOverlapRate = maxOverlapRate;
        return this;
    }

    public SimpleRecursiveRagTextSplitter separatorsRegex(String[] separatorsRegex) {
        this.separatorsRegex = separatorsRegex;
        return this;
    }

    @Override
    public List<String> split(String text) {
        List<String> ret = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return ret;
        }
        splitRecursive(text, 0, ret);
        return ret;
    }

    public void splitOnly(String text, int separatorIndex, int maxSegmentSize, List<String> ret) {

        if (separatorIndex < 0 || separatorIndex > separatorsRegex.length) {
            ret.add(text);
        }
        String splitRegex = separatorsRegex[separatorIndex];
        if (splitRegex.isEmpty()) {
            for (int i = 0; i < text.length(); i++) {
                String item = text.substring(i, i + 1);
                ret.add(item);
            }
        } else {
            List<RegexFindPartMeta> list = RegexUtil.regexFindParts(text, splitRegex);
            for (RegexFindPartMeta meta : list) {
                String part = meta.getPart();
                if (part.length() <= maxSegmentSize) {
                    ret.add(part);
                } else {
                    splitOnly(part, separatorIndex + 1, maxSegmentSize, ret);
                }
            }
        }
    }

    public void splitRecursive(String text, int separatorIndex, List<String> ret) {
        if (separatorIndex >= separatorsRegex.length) {
            ret.add(text);
        }

        int overlapSize = (int) (maxSegmentSizeInChars * maxOverlapRate);

        List<String> candidateList = new ArrayList<>();
        splitOnly(text, separatorIndex, (overlapSize > 0 ? overlapSize : maxSegmentSizeInChars), candidateList);

        List<String> currList = new ArrayList<>();
        int currLen = 0;
        List<String> overlapList = new ArrayList<>();
        int overlapLen = 0;

        for (String item : candidateList) {
            int len = currLen + item.length();
            if (len < maxSegmentSizeInChars) {
                currList.add(item);
                currLen += item.length();
            } else {
                if (!currList.isEmpty()) {
                    ret.add(String.join("", currList));

                    overlapList.clear();
                    overlapLen = 0;
                    for (int i = currList.size() - 1; i >= 0; i--) {
                        String str = currList.get(i);
                        if (overlapLen + str.length() > overlapSize) {
                            break;
                        }
                        overlapList.add(str);
                        overlapLen += str.length();
                    }
                    currLen = overlapLen;
                    currList.clear();
                    currList.addAll(overlapList);
                }

                currList.add(item);
                currLen += item.length();
            }
        }

        if (!currList.isEmpty()) {
            ret.add(String.join("", currList));
            currLen = 0;
            currList.clear();
        }

    }
}
