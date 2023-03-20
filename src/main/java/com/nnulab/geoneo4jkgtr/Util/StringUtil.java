package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

public class StringUtil {
    /*
     * 是否为空字符串
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 连接方法 类似于javascript
     *
     * @param join   连接字符串
     * @param strAry 需要连接的集合
     * @return
     */
    public static String join(String join, String[] strAry) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = strAry.length; i < len; i++) {
            if (i == (len - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return sb.toString();
    }

    /**
     * 将结果集中的一列用指定字符连接起来
     *
     * @param join    指定字符
     * @param cols    结果集
     * @param colName 列名
     * @return
     */
    public static String join(String join, List<Map> cols, String colName) {
        List<String> aColCons = new ArrayList<String>();
        for (Map map :
                cols) {
            aColCons.add(ObjectUtils.toString(map.get(colName)));
        }
        return join(join, aColCons);
    }

    public static String join(String join, List<String> listStr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = listStr.size(); i < len; i++) {
            if (i == (len - 1)) {
                sb.append(listStr.get(i));
            } else {
                sb.append(listStr.get(i)).append(join);
            }
        }
        return sb.toString();
    }

    /**
     * 动态规划获取最长公共子List
     *
     * @param stratumNameList0 List0
     * @param stratumNameList1 List1
     * @return 子List
     */
    public static List<String> LCSList(List<String> stratumNameList0, List<String> stratumNameList1) {
        int size0 = stratumNameList0.size(), size1 = stratumNameList1.size();
        //endIndex是最长公共子串在str1中的结束位置
        int endIndex = -1, maxSize = 0;
        //二维数组dp，其内的值默认为0。维度为(len1+1)*(len2+1)
        //其中dp[0][?]和dp[?][0]为辅助的单元，用于递推式的起始条件，相比于上述递推式有所变化。
//        List<List<Integer>> dp (size0 + 1, Vector < Integer>(size1 + 1));
        int[][] dp = new int[size0 + 1][size1 + 1];
        for (int i = 1; i <= size0; i++) {
            for (int j = 1; j <= size1; j++) {
                //相等则更新dp[i][j],否则仍然为0
                if (stratumNameList0.get(i - 1).equals(stratumNameList0.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    //更新maxSize和endIndex
                    if (dp[i][j] > maxSize) {
                        maxSize = dp[i][j];
                        endIndex = i - 1;
                    }
                }
            }
        }
        int startIndex = endIndex - maxSize + 1;
        return stratumNameList0.subList(startIndex, maxSize);
    }

    public static String getWhereFid(List<?> faces) {
        Set<Integer> fidSet = new HashSet<>();

        for (int i = 0; i < faces.size(); ++i) {
            Object o = faces.get(i);
            if (o instanceof Face) {
                fidSet.add(((Face) faces.get(i)).getFid());
            } else if (o instanceof List<?>) {
                for (int j = 0; j < ((List<?>) o).size(); ++j) {
                    fidSet.add(((Face) ((List<?>) o).get(i)).getFid());
                }
            }
        }
        return getWhereFidFromFidSet(fidSet);
    }

    private static String getWhereFidFromFidSet(Set<Integer> fidSet) {
        StringBuilder where = new StringBuilder();

        int size = fidSet.size(), count = 0;
        for (Integer fid : fidSet) {
            where.append("\"FID\"=");
            where.append(fid);
            ++count;
            if (count < size) {
                where.append(" or ");
            }
        }
        return where.toString();
    }
}
