package com.nnulab.geoneo4jkgtr.Util;

import org.gdal.ogr.Geometry;

/**
 * 拓扑相关算法工具类
 *
 * @author : LiuXianYu
 * @date : 2022/5/25 16:06
 */
public class TopologyUtil {

    /**
     * 根据弧段指向左或者右，以及地层位于弧段的左或者右，判断地层与弧段的关系
     *
     * @param eGeometry 弧段
     * @param left      面是否在弧段的左边
     * @return 弧段在地层顶还是地层底
     */
    public static String JudgeEdgeAtTopOrBottomOfFace(Geometry eGeometry, Boolean left) {
        int pointsOnEdgeNum = eGeometry.GetPointCount();
        double diffBetweenStartAndEndPoint_X = eGeometry.GetX(0) - eGeometry.GetX(pointsOnEdgeNum - 1);

        if ((diffBetweenStartAndEndPoint_X < 0 && !left) || (diffBetweenStartAndEndPoint_X > 0 && left))
            return "top";
        else if ((diffBetweenStartAndEndPoint_X > 0 && !left) || (diffBetweenStartAndEndPoint_X < 0 && left))
            return "bottom";
        else {
            double diffBetweenStartAndEndPoint_Y = eGeometry.GetY(0) - eGeometry.GetY(pointsOnEdgeNum - 1);
            if (0 == diffBetweenStartAndEndPoint_Y)
                return TopologyUtil.JudgeFaceOutOrInEdge(eGeometry, left);
            else
                return "others";
        }

    }

    /**
     * 基于环状弧段顺时针或逆时针，判断面在弧段内还是外
     * @param eGeometry 弧段
     * @param left      面是否在弧段的左边
     * @return 弧段在地层内还是地层外
     */
    public static String JudgeFaceOutOrInEdge(Geometry eGeometry, Boolean left) {
        int pointsOnEdgeNum = eGeometry.GetPointCount();
        double maxX = -10e10, curX;
        int idx = -1, prei, nexti;

        for (int i = 0; i < pointsOnEdgeNum; i++) {
            if (maxX < eGeometry.GetX(i)) {
                maxX = eGeometry.GetX(i);
                idx = i;
            }
        }

        prei = idx == 0 ? pointsOnEdgeNum - 1 : idx - 1;
        nexti = (idx + 1) % pointsOnEdgeNum;

        double x1 = eGeometry.GetX(idx) - eGeometry.GetX(prei);
        double y1 = eGeometry.GetY(idx) - eGeometry.GetY(prei);
        double x2 = eGeometry.GetX(nexti) - eGeometry.GetX(idx);
        double y2 = eGeometry.GetY(nexti) - eGeometry.GetY(idx);

        if (x1 * y2 - x2 * y1 > 0) {
            if (left)
                return "inner";
            else
                return "outer";
        } else if (x1 * y2 - x2 * y1 < 0) {
            if (left)
                return "outer";
            else
                return "inner";
        }
        return "others";
    }

    /**
     * 由弧段构建面与面的拓扑关系——邻接、包含
     */
    public static void CreateTopoBetweenFacesFromBoundaries() {

    }

//    public static boolean is

}
