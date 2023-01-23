package com.nnulab.geoneo4jkgtr.Controller;


import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Model.request.KGCreateRequest;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 20:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KGControllerTest {

    @Autowired
    private KGController kgController;

    /**
     * 输入面数据和边界数据，构建知识图谱
     *
     * @throws Exception
     */
    @Test
    public void create() throws Exception {
        GdalUtil.init();

        //南京主要断层
//        KGCreateRequest kgCreateRequest = new KGCreateRequest();
//        kgCreateRequest.setBoundaryPath("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\NanjingFaults\\fault.shp");
//        kgController.create(kgCreateRequest);

        //基金本子上的剖面图
//        KGCreateRequest kgCreateRequest = new KGCreateRequest();
////        kgCreateRequest.setFacePath("C:\\Users\\lab\\Desktop\\知识图谱地质事件时序推理\\Data\\StudyData\\StudyData\\stratas.shp");
////        kgCreateRequest.setBoundaryPath("C:\\Users\\lab\\Desktop\\知识图谱地质事件时序推理\\Data\\StudyData\\StudyData\\boundary.shp");
//        kgCreateRequest.setFacePath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\StudyData\\StudyData\\stratas.shp");
//        kgCreateRequest.setBoundaryPath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\StudyData\\StudyData\\boundary.shp");
//        kgController.create(kgCreateRequest);

        //五龙山剖面图
//        kgController.createKG("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\五龙山剖面图\\剖面地层.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\五龙山剖面图\\boundary.shp");

        //穹窿识别
//        kgController.createKG("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Dome\\strata1.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Dome\\boundary1.shp");

        //褶皱识别
//        KGCreateRequest kgCreateRequest = new KGCreateRequest();
//        kgCreateRequest.setFacePath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\qinglongshan\\QingLongShanWithoutQuaternary.shp");
//        kgCreateRequest.setBoundaryPath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\qinglongshan\\boundaries1015.shp");
//        kgController.create(kgCreateRequest);

        //褶皱识别
//        KGCreateRequest kgCreateRequest = new KGCreateRequest();
//        kgCreateRequest.setFacePath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\FoldIdentify\\strata.shp");
//        kgCreateRequest.setBoundaryPath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\FoldIdentify\\boundaries.shp");
//        kgController.create(kgCreateRequest);

        //南京矢量地质图
        KGCreateRequest kgCreateRequest = new KGCreateRequest();
        kgCreateRequest.setFacePath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\Nanjing\\strata1.shp");
        kgCreateRequest.setBoundaryPath("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\Nanjing\\boundaries.shp");
        kgController.create(kgCreateRequest);

    }

    //纯断层实验

    /**
     * 推断断层减切割关系
     *
     * @throws Exception
     */
    @Test
    public void inferCuttingThroughRelationOnFaults() throws Exception {
        kgController.inferCuttingThroughRelationOnFaults();
    }

    /**
     * 推断断层间截断关系
     *
     * @throws Exception
     */
    @Test
    public void inferCuttingOffRelationOnFaults() throws Exception {
        kgController.inferCuttingOffRelationOnFaults();
    }

    /**
     * 推断断层间相交关系
     *
     * @throws Exception
     */
    @Test
    public void inferMutuallyCuttingRelationOnFaults() throws Exception {
        kgController.inferMutuallyCuttingRelationOnFaults();
    }

    /**
     * 基于断层间地质关系，推断断层发育时间序列
     *
     * @throws Exception
     */
    @Test
    public void inferTimeSeriesOfFaults() throws Exception {
        kgController.inferTimeSeriesOfFaults();
    }


    //地层+断层实验

    /**
     * 基于地质界线与地层拓扑关系推断，地层间地质接触关系
     *
     * @throws Exception
     */
    @Test
    public void inferGeoRelationshipBetweenStrata() throws Exception {
        kgController.inferGeoRelationshipBetweenStrata();
    }

    /**
     * 基于地层间地质接触关系，推断沉积岩生成序列
     * 基于断层与地层接触关系，推断断层发育时间
     * 基于侵入岩与地层接触关系，推断侵入岩发育时间
     *
     * @throws Exception
     */
    @Test
    public void inferTimeSeriesOfStrata() throws Exception {
        kgController.inferTimeSeriesOfStrata();
        kgController.inferTimeOfIntrusionByStrata();
        kgController.inferTimeOfFaultsByStrata();
    }

    @Test
    public void testSearchAllKG() throws Exception {
        KnowledgeGraph knowledgeGraph = kgController.searchAllKG();
    }

    /**
     * 清空知识图谱数据库
     *
     * @throws Exception
     */
    @Test
    public void clearAll() throws Exception {
        kgController.clearAll();
    }


}
