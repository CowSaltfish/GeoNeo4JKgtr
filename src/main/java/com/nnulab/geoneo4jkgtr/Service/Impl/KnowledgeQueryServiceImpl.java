package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.nnulab.geoneo4jkgtr.Dao.FoldDao;
import com.nnulab.geoneo4jkgtr.Dao.KnowledgeQueryDao;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Vertex;
import com.nnulab.geoneo4jkgtr.Service.KnowledgeQueryService;
import com.nnulab.geoneo4jkgtr.Util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 知识查询
 *
 * @author : LiuXianYu
 * @date : 2023/3/6 11:00
 */
@Service
public class KnowledgeQueryServiceImpl implements KnowledgeQueryService {

    @Resource
    FoldDao foldDao;

    @Resource
    DaoUtil daoUtil;

    @Resource
    KnowledgeQueryDao knowledgeQueryDao;

    /**
     * 褶皱构造查找
     * 模拟用户基于本系统进行褶皱构造查找
     */
    @Override
    public void foldQuery() {
        String where;//用于获取结果节点的fid的where子句，方便展示

        //核部模式匹配
        List<Face> cores = foldDao.matchCorePattern();
        where = StringUtil.getWhereFid(cores);

        //翼部模式匹配
        List<List<Face>> swingPaths = new ArrayList<>();
        for (int i = 0; i < cores.size(); ++i) {
            Face core = cores.get(i);
            int fid = core.getFid(), length = 5;

//            if (fid != 5024) {
//                continue;
//            }

            System.out.println("翼步路径查找：" + i + "/" + cores.size() + "，当前核部fid为：" + core.getFid());
            if (fid == 5024) {//青龙山路径长度设为15，其他为5，但由于路径长度传参怎么都不成功，这里先这么写
                length = 15;
                swingPaths.addAll(foldDao.matchSwingPattern15(fid, length));
                continue;
            }
            swingPaths.addAll(foldDao.matchSwingPattern(fid, length));
//            swingPaths.addAll(daoUtil.matchSwingPattern(fid, length));//非springboot方法
        }
        where = StringUtil.getWhereFid(swingPaths);

        //基于Louvain社区发现算法的两翼地层划分
//        foldDao.divide2Wings();
        List<List<List<Face>>> twoSwings = divide2Wings(swingPaths);

        //褶皱构造化简
        simplifyFoldStructure(twoSwings);

        //对称重复模式匹配
        foldDao.matchSymmetricalRepeatPattern();
    }


    /**
     * 硬编码划分褶皱构造两翼
     *
     * @param wings
     * @return
     */
    private List<List<List<Face>>> divide2Wings(List<List<Face>> wings) {
        List<Double[]> dipDirectionOfWingPath = new ArrayList<>();
        //获取倾向向量集合
        for (List<Face> wing : wings) {
            double[][] vertices = new double[wing.size()][];
            for (int j = 0; j < wing.size(); ++j) {
                Face stratum = wing.get(j);
                vertices[j][0] = (stratum.getMinX() + stratum.getMaxX()) / 2;
                vertices[j][1] = (stratum.getMinY() + stratum.getMaxY()) / 2;
            }
            dipDirectionOfWingPath.add(GeometryUtil.azimuth2Vector(GeometryUtil.calLineStrike(vertices, true)));
        }
        //向量二分聚类
        Double[][] center = new Double[2][2];//内部向量平均值
        double maxAngle = -1.0;
        int maxAngleI = 0, maxAngleJ = 1;
        //获取夹角最大的两个向量
        for (int i = 0; i < dipDirectionOfWingPath.size(); ++i) {
            for (int j = 0; j < dipDirectionOfWingPath.size(); ++j) {
                if (i == j) {
                    continue;
                }
                double angle = GeometryUtil.calAngle(new Vertex(dipDirectionOfWingPath.get(i)[0], dipDirectionOfWingPath.get(i)[1]),
                        new Vertex(dipDirectionOfWingPath.get(j)[0], dipDirectionOfWingPath.get(j)[1]));
                if (angle > maxAngle) {
                    maxAngle = angle;
                    maxAngleI = i;
                    maxAngleJ = j;
                }
            }
        }
        center[0] = dipDirectionOfWingPath.get(maxAngleI);
        center[1] = dipDirectionOfWingPath.get(maxAngleJ);

        //k-mean聚类
        Set<Integer> cluster0Indexes = new HashSet<>(), cluster1Indexes = new HashSet<>(), oldCluster0Indexes = new HashSet<>(), oldCluster1Indexes = new HashSet<>();
        while (true) {
            List<Double[]> cluster0 = new ArrayList<>(), cluster1 = new ArrayList<>();
            for (int i = 0; i < dipDirectionOfWingPath.size(); ++i) {
                Double[] vector = dipDirectionOfWingPath.get(i);
                double angle0 = GeometryUtil.calAngle(new Vertex(vector[0], vector[1]), new Vertex(center[0][0], center[0][1]));
                double angle1 = GeometryUtil.calAngle(new Vertex(vector[0], vector[1]), new Vertex(center[1][0], center[1][1]));
                if (angle0 < angle1) {
                    cluster0.add(vector);
                    cluster0Indexes.add(i);
                } else {
                    cluster1.add(vector);
                    cluster1Indexes.add(i);
                }
            }
            double distance;
            for (Double[] vecter : cluster0) {
                center[0][0] += vecter[0];
                center[0][1] += vecter[1];
            }
            distance = Math.sqrt(Math.pow(center[0][0], 2) + Math.pow(center[0][1], 2));
            center[0][0] /= distance;
            center[0][1] /= distance;

            for (Double[] vecter : cluster1) {
                center[1][0] += vecter[0];
                center[1][1] += vecter[1];
            }
            distance = Math.sqrt(Math.pow(center[1][0], 2) + Math.pow(center[1][1], 2));
            center[1][0] /= distance;
            center[1][1] /= distance;

            if (!oldCluster0Indexes.isEmpty() && !oldCluster1Indexes.isEmpty()) {
                if (oldCluster0Indexes.containsAll(cluster0Indexes)) {
                    break;
                }
            }
            oldCluster0Indexes.clear();
            oldCluster1Indexes.clear();
            oldCluster0Indexes.addAll(cluster0Indexes);
            oldCluster1Indexes.addAll(cluster1Indexes);
        }
        List<List<List<Face>>> twoWing = new ArrayList<>();
        List<List<Face>> wing0 = new ArrayList<>(), wing1 = new ArrayList<>();
        for (int i = 0; i < wings.size(); ++i) {
            if (cluster0Indexes.contains(i)) {
                wing0.add(wings.get(i));
            } else {
                wing1.add(wings.get(i));
            }
        }
        twoWing.add(wing0);
        twoWing.add(wing1);

        return twoWing;
    }

    private void simplifyFoldStructure(List<List<List<Face>>> twoSwings) {
        //纵向化简
        //位于同一翼部路径，接触且同时代

        //横向化简
        //接触且同时代

        //同时代，且与同一节点接触

    }


}
