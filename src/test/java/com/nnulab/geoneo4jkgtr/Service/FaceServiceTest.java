package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import com.nnulab.geoneo4jkgtr.Util.StringUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2023/3/15 15:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FaceServiceTest extends TestCase {

    @Autowired
    private FaceService faceService;

    @Test
    public void testFindSedimentaryRockArea() {
        GdalUtil.init();
        List<Face> sedimentaryRockArea = faceService.findSedimentaryRockArea();
        List<Integer> Ids = new ArrayList<>();
//        Ids.add(1);
        for (Face face : sedimentaryRockArea) {
            Ids.add(face.getFid());
        }
//        String where = StringUtil.getWhereFid(sedimentaryRockArea);

        GdalUtil.createNewShpByLayerWithId("D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\strata3857.shp",
                "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\Nanjing\\3857\\strata3857_1.shp", Ids);
    }
}