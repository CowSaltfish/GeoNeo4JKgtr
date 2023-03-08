package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Vertex;

import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2023/2/11 21:19
 */
public class GeometryUtil {


    /**
     * 计算三点形成的夹角大小
     *
     * @param v0
     * @param v1 顶角
     * @param v2
     */
    public static double calAngle(Vertex v0, Vertex v1, Vertex v2) {
        double x1 = v0.getX() - v1.getX();
        double y1 = v0.getY() - v1.getY();
        double x2 = v2.getX() - v1.getX();
        double y2 = v2.getY() - v1.getY();
        double cosv = (x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
        if (cosv - 1 > 0 && cosv - 1 < 10e-5)
            cosv = 1;
        else if (cosv + 1 < 0 && cosv + 1 > -10e-5)
            cosv = -1;
        return Math.acos(cosv);
    }

    /**
     * 计算向量夹角
     * @param v0 向量0
     * @param v1 向量1
     * @return 夹角
     */
    public static double calAngle(Vertex v0, Vertex v1) {
        double x1 = v0.getX();
        double y1 = v0.getY();
        double x2 = v1.getX();
        double y2 = v1.getY();
        double cosv = (x1 * x2 + y1 * y2) / (Math.sqrt(x1 * x1 + y1 * y1) * Math.sqrt(x2 * x2 + y2 * y2));
        if (cosv - 1 > 0 && cosv - 1 < 10e-5)
            cosv = 1;
        else if (cosv + 1 < 0 && cosv + 1 > -10e-5)
            cosv = -1;
        return Math.acos(cosv);
    }

    /**
     * 计算曲线走向
     *
     * @param vertices
     * @param directed 曲线是否有向
     * @return
     */
    public static double calLineStrike(double[][] vertices, boolean directed) {
        double[] k_b = lineFit(vertices);
        double strike;
        if (k_b[0] >= 0) {
            strike = (Math.PI / 2 - Math.atan(k_b[0])) / Math.PI * 180;
        } else {
            strike = (Math.atan(k_b[0]) + Math.PI) / Math.PI * 180;
        }
        if (directed) {
            if (vertices[0][0] > vertices[vertices.length - 1][0]) {
                strike += 180;
            }
        }
        return strike;
    }

    /**
     * 基于最小二乘法线性拟合
     *
     * @param vertices
     * @return
     */
    public static double[] lineFit(double[][] vertices) {
        double avgX = 0, avgY = 0;
        double Lxx = 0, Lyy = 0, Lxy = 0;

        //计算x,y平均值
        for (int i = 0; i < vertices.length; i++) {
            avgX += vertices[i][0] / vertices.length;
            avgY += vertices[i][1] / vertices.length;
        }

        //计算Lxx,Lyy,Lxy
        for (int i = 0; i < vertices.length; i++) {
            Lxy += (vertices[i][0] - avgX) * (vertices[i][1] - avgY);
            Lxx += (vertices[i][0] - avgX) * (vertices[i][0] - avgX);
            Lyy += (vertices[i][1] - avgY) * (vertices[i][1] - avgY);
        }

        //线性拟合结果
        double k = Lxy / Lxx;
        double b = avgY - k * avgX;
        System.out.println("相关系数r=" + Lxy / Math.sqrt(Lxx * Lyy));
        System.out.println("线性方程:" + "y = " + k + " + " + b + "* x");
        return new double[]{k, b};
    }

    public static Double[] azimuth2Vector(double azimuth) {
        return new Double[]{Math.sin(azimuth), Math.cos(azimuth)};
    }
}
