package i2f.algo.text.test;

import i2f.algo.text.TextEditDistance;

/**
 * @author Ice2Faith
 * @date 2024/11/15 11:06
 */
public class TestTextEditDistance {
    public static void main(String[] args) {
        String str1 = "when str1[i-1]=str2[j-1] then d[i-1][j-1]";
        String str2 = "when str1[i-1]!=str2[j-1] then min(d[i-1][j],d[i][j-1],d[i-1][j-1])+1";
        int dis = TextEditDistance.editDistance(str1, str2);
        System.out.println(dis);
    }
}
