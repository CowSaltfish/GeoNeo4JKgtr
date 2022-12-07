package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.Impl.OntologyServiceImpl;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2022/12/6 16:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OntologyServiceTest extends TestCase {

    @Autowired
    private OntologyService ontologyService;

    @Test
    public void testSave() {
//        ontologyService.save("testOntology",
//                "{\"nodes\":" +
//                "[" +
//                "{\"id\":\"0\",\"labels\":[\"公司\"]}," +
//                "{\"id\":\"1\",\"labels\":[\"公司\"]}]," +
//                "\"relationships\":" +
//                "[{\"id\":\"10001\",\"type\":\"持股\",\"startNode\":\"0\",\"endNode\":\"1\"}]" +
//                "}");
        ontologyService.save("testOntology",
                "{\"nodes\":" +
                        "[" +
                        "{\"id\":\"0\",\"labels\":[\"Face\"]}," +
                        "{\"id\":\"1\",\"labels\":[\"Face\"]}]," +
                        "\"relationships\":" +
                        "[{\"id\":\"10001\",\"type\":\"ADJACENT\",\"startNode\":\"0\",\"endNode\":\"1\"}]" +
                        "}");
//        ontologyService.save("testOntology", "{\"nodes\":{\"id\":\"0\",\"labels\":\"公司\"},\"relationships\":{\"id\":\"314\",\"type\":\"持股\",\"startNode\":\"0\",\"endNode\":\"1\"}}");
    }

    @Test
    public void testFindByName() {
        KnowledgeGraph testOntology = ontologyService.findByName("testOntology");
        System.out.println(testOntology);
    }

    @Test
    public void testUpdateOntology() {

    }

    @Test
    public void testDeleteOntologyByName() {
        ontologyService.deleteOntologyByName("testOntology");
    }
}