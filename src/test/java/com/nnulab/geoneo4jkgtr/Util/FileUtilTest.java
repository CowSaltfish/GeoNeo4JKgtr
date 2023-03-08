package com.nnulab.geoneo4jkgtr.Util;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2023/3/4 23:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUtilTest extends TestCase {

    @Test
    public void testGetStratigraphicChronologyFromCSV() {
        Map<String, Integer> stratigraphicChronologyFromCSV = FileUtil.getStratigraphicChronologyFromCSV("E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Doc\\地层年代表.csv");
    }

}