package com.nnulab.geoneo4jkgtr.Util;

import junit.framework.TestCase;
import org.gdal.ogr.Layer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GdalUtilTest extends TestCase {
    /**
     * 面转线
     */
    @Test
    public void ConvertPolygonToPolylineEx() {
        GdalUtil.init();
        GdalUtil.ConvertPolygonToPolylineEx("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\boundary1.shp",
                GdalUtil.getLayerByPath("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\stratas.shp"),
                new double[4],
                null,
                null);
    }

    /**
     * 检查面与面之间的拓扑问题
     */
    @Test
    public void CheckTopologyBetweenFaces() {
        GdalUtil.init();
        GdalUtil.CheckTopologyBetweenFaces("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\boundary1.shp",
                GdalUtil.getLayerByPath("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\stratas.shp"),
                new double[4],
                null,
                null);
    }

    /**
     * 检查面与面之间的拓扑问题
     */
    @Test
    public void CalAreaOfFace() {
        GdalUtil.init();
        GdalUtil.CalAreaOfFace(Objects.requireNonNull(GdalUtil.getLayerByPath("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Dome\\strata3857.shp")));
    }

    @Test
    public void testShp2GeoJson() {
        GdalUtil.init();
//        GdalUtil.Shp2GeoJson("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\stratas.shp",
//                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\geojson\\strata.json");
        GdalUtil.Shp2GeoJson("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\Dome\\nanjing3857.shp",
                "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\geojson\\nanjing3857.json");

    }

//    @Test
//    public void testCalFiledValue() {
//        GdalUtil.init();
//        Layer layer = GdalUtil.getLayerByPath("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\KGQuery\\Nanjing\\zjs\\zjs_boundaries.shp");
//        if (layer != null) {
//            GdalUtil.calFiledValue(layer, "strike");
//        }
//    }
}