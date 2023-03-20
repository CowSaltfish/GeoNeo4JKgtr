package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2023/3/6 19:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KnowledgeQueryServiceTest extends TestCase {

    @Autowired
    private KnowledgeQueryService knowledgeQueryService;

    @Test
    public void testFoldQuery() {
        knowledgeQueryService.foldQuery();
    }

}