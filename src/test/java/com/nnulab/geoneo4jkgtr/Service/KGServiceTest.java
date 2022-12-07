package com.nnulab.geoneo4jkgtr.Service;

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
    public void testSearchAllKG() {
    }

    @Test
    public void testSearch() {
    }

    @Test
    public void testSearchByOntology() {
        kgService.searchByOntology("testOntology");
    }
}