package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import junit.framework.TestCase;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Layer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author : LiuXianYu
 * @date : 2022/12/7 11:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KGServiceTest extends TestCase {

    @Autowired
    private KGService kgService;

    @Autowired
    private EventTemporalInterpretService eventTemporalInterpretService;

    @Test
    public void testCreate() {

        GdalUtil.init();

        //南京主要断层
//        kgService.create("", "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\NanjingFaults\\fault.shp");

        //基金本子上的剖面图
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\stratas.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\boundary.shp");

        //夏邦栋剖面图
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Intrusion\\Test1\\strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Intrusion\\Test1\\boundary.shp");


        //五龙山剖面图
//        kgController.createKG("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\五龙山剖面图\\剖面地层.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\五龙山剖面图\\boundary.shp");

        //穹窿识别
//        kgController.createKG("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Dome\\strata1.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Dome\\boundary1.shp");

        //褶皱识别
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\qls_strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\qls_boundaries2.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Doc\\地层年代表.csv");

        //褶皱识别
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FoldIdentify\\strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FoldIdentify\\boundaries.shp");

        //南京矢量地质图
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\strata3857.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\3857\\qls_boundaries2.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Doc\\地层年代表.csv");
        //南京矢量地质图
//        kgService.create("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\strata3857.shp",
//                "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\qls_boundaries2.shp",
//                "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\地层年代表.csv");

        //南京知识图谱融合
        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\zhetangzhen_strata.shp",
                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\zhetangzhen_boundary.shp");

//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\TTII_strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\TTII_boundary.shp");
//
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\TTIIII_strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\TTIIII_boundary.shp");

        //6原型系统数据
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\NanjingSection\\system6\\strata3857.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\NanjingSection\\system6\\boundaries3857.shp");

//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\strata_merge.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\zhetangzhen\\boundaries_merge.shp");

        //断层组合数据
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\temp\\nanjing_voronoi.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\temp\\nanjing_voronoi_boundaries.shp");

        //栖霞山断层
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FaultTemporalInterpret\\qxs\\strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FaultTemporalInterpret\\qxs\\boundaries1.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Doc\\地层年代表2.csv");

        //内华达断层
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FaultTemporalInterpret\\nhd\\strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FaultTemporalInterpret\\nhd\\boundary1_dissolve1.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FaultTemporalInterpret\\nhd\\StratigraphicSequenceTable.csv");
    }

    /**
     * 创建要素间地质关系
     */
    @Test
    public void testCreateSpatialTopologyRelationBetweenFaults() {
        kgService.createGeologicalRelation();
    }

//    @Test
//    public void testCreateGeologicalRelationBetweenFaultsAndStrata(){
//        kgService.createGeologicalRelationBetweenFaultsAndStrata();
//    }

    /**
     * 推测事件时间关系
     */
    @Test
    public void inferTimeSeries() {
        eventTemporalInterpretService.inferTemporalRelationshipOfFaultsAndStrata();
    }

    @Test
    public void testSearchAllKG() {
    }

    @Test
    public void testSearch() {
    }

    @Test
    public void testSearchByOntology() {
        kgService.searchByOntology("testOntology1");
    }

    @Test
    public void testFindAllBoundaryMap() {
        kgService.findAllBoundaryMap();
    }

    /**
     * 清空知识图谱数据库
     *
     * @throws Exception
     */
    @Test
    public void clearAll() throws Exception {
        kgService.clearAll();
    }

    @Test
    public void testSaveRelation() {
        Boundary boundaryi = new Boundary();
        Boundary boundaryj = new Boundary();
        boundaryi.setId(129276L);
        boundaryj.setId(129328L);
        kgService.saveRelation(new AdjacentRelation(boundaryi, boundaryj));
    }

    /**
     * 环形构造查找
     * 模拟用户基于本系统进行环形构造查找
     */
    @Test
    public void testRingStructureQuery() {
        GdalUtil.init();

        //嵌套模式获取地层
//        KnowledgeGraph ringsKg = kgService.search(
//                "match (x:Face)-[:CONTAINS]->(y:Face) \n" +
//                        "with x,count(y)as cy\n" +
//                        "where cy <= 2 and x.ageIndex>4\n" +
//                        "return distinct x");

        //新增面积比排除
        KnowledgeGraph ringsKg = kgService.search(
                "match (x:Face)-[:CONTAINS]->(y:Face) \n" +
                        "where x.ageIndex>4\n" +
                        "with x.area/y.area as aratio,x,y\n" +
                        "where aratio>=1/5 and aratio<=5\n" +
                        "with x,count(y)as cy\n" +
                        "where cy <= 2  \n" +
                        "return distinct x");

        List<Integer> Ids = new ArrayList<>();
        for (Object face : ringsKg.getNodes()) {
            Long fid = ((HashMap<String, Long>) face).get("fid");
            Ids.add(fid.intValue());
        }
        GdalUtil.createNewShpByLayerWithId("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\strata3857.shp",
                "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\strata3857_rings_aratio.shp", Ids);
    }

    /**
     * 断层构造查找
     * 模拟用户基于本系统进行断层构造查找
     */
    @Test
    public void testFaultStructureQuery() {
        GdalUtil.init();

        //近直界线匹配
        KnowledgeGraph ringsKg = kgService.search(
                "match (b:Boundary)-[:BELONG]->(f:Face)\n" +
                        "with b,collect(f) as cf\n" +
                        "where size(cf)>1 and all(f1 in cf where f1.ageIndex>4)\n" +
                        "with collect(b) as cb\n" +
                        "match (b1:Boundary)-[r:ADJACENT]-(b2:Boundary)\n" +
                        "where b1 in cb and b2 in cb\n" +
                        "and (r.includedAngle>173 or r.includedAngle<7)\n" +
                        "return b1");

        List<Integer> Ids = ringsKg.getNodesFidList();

        GdalUtil.createNewShpByLayerWithId("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\boundaries.shp",
                "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\fault\\boundaries_straight.shp", Ids);
    }

    /**
     * 基于ArcGIS生成的断层与地层空间连接生成的shp建立断层切割地层关系（图5.10）
     */
    @Test
    public void createFaultSliceStrataRelationship() {
        //读取断层与地层空间连接生成的shp
        GdalUtil.init();
        Layer faultSliceStrataLayer = GdalUtil.getLayerByPath("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\NanjingFaults\\fault_slice_strata.shp");
        long featureCount = Objects.requireNonNull(faultSliceStrataLayer).GetFeatureCount();
        Map<Integer, Stratum> stratumMap = new HashMap<>();
        for (int i = 0; i < featureCount; i++) {
            Feature feature = faultSliceStrataLayer.GetFeature(i);
            int faceId = -1;
            String faceName;
            if (-1 != faultSliceStrataLayer.FindFieldIndex("id", 0)) {
                faceId = feature.GetFieldAsInteger("id");
            }
            Stratum stratum;
            if (!stratumMap.containsKey(faceId)) {
                //创建地层实体
                stratum = new Stratum();
                stratum.setFid(i);
                stratum.setArea(feature.GetGeometryRef().Area());
                if (-1 != faultSliceStrataLayer.FindFieldIndex("name_1", 0)) {
                    stratum.setNodeName(feature.GetFieldAsString("name_1"));
                }
                //地层节点存入库中
                kgService.saveNode(stratum);
                stratumMap.put(faceId, stratum);
            } else {
                stratum = stratumMap.get(faceId);
            }
            //建立切割关系
            if (-1 != faultSliceStrataLayer.FindFieldIndex("name", 0)) {
                String faultName = feature.GetFieldAsString("name");
                List<Fault> faults = kgService.findFaultByName(faultName);
                if (stratum != null && faults.size() == 1)//判断界线是否是地层上边界或下边界
                {
                    BasicRelation slice = new BasicRelation(faults.get(0), stratum);
                    slice.setType("slice");
                    kgService.saveRelation(slice);
                }
            }
        }
    }
}