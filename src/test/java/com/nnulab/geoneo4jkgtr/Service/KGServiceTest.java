package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Model.request.KGCreateRequest;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : LiuXianYu
 * @date : 2022/12/7 11:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KGServiceTest extends TestCase {

    @Autowired
    private KGService kgService;

    @Test
    public void testCreate() {

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
        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\qls_strata.shp",
                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\qls_boundaries2.shp",
                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Doc\\地层年代表.csv");

        //褶皱识别
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FoldIdentify\\strata.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\FoldIdentify\\boundaries.shp");

        //南京矢量地质图
//        kgService.create("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\strata3857.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\3857\\qls_boundaries2.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Doc\\地层年代表.csv");

        //南京知识图谱融合
//        kgService.create("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\NanjingSection\\拓塘镇\\相关地层.shp",
//                "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\NanjingSection\\拓塘镇\\相关地层边界.shp");
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
}