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
//        ontologyService.save("testOntology",
//                "{\"nodes\":" +
//                        "[" +
//                        "{\"id\":\"0\",\"labels\":[\"Face\"]}," +
//                        "{\"id\":\"1\",\"labels\":[\"Face\"]}]," +
//                        "\"relationships\":" +
//                        "[{\"id\":\"10001\",\"type\":\"ADJACENT\",\"startNode\":\"0\",\"endNode\":\"1\"}]" +
//                        "}");
        ontologyService.save("testOntology1",
                "{\n" +
                        "  \"_id\": {\n" +
                        "    \"$oid\": \"63b563ca7fefd7c830db19f1\"\n" +
                        "  },\n" +
                        "  \"_class\": \"com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph\",\n" +
                        "  \"name\": \"onto1\",\n" +
                        "  \"isOntology\": true,\n" +
                        "  \"nodes\": [\n" +
                        "    {\n" +
                        "      \"x\": -50,\n" +
                        "      \"y\": -20,\n" +
                        "      \"id\": \"0\",\n" +
                        "      \"labels\": [\n" +
                        "        \"Face\"\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"x\": 57,\n" +
                        "      \"y\": 22,\n" +
                        "      \"id\": \"1\",\n" +
                        "      \"labels\": [\n" +
                        "        \"Face\"\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"relationships\": [\n" +
                        "    {\n" +
                        "      \"_id\": \"2\",\n" +
                        "      \"direction\": 0,\n" +
                        "      \"distance\": 0,\n" +
                        "      \"directionOfTime\": 0,\n" +
                        "      \"distanceOfTime\": 0,\n" +
                        "      \"properties\": {},\n" +
                        "      \"startNode\": \"0\"\n" +
                        "      ,\n" +
                        "      \"endNode\": \"1\"\n" +
                        "      ,\n" +
                        "      \"type\": \"ADJACENT\",\n" +
                        "      \"added\": \"1672831946484\"\n" +
                        "      \n" +
                        "    }\n" +
                        "  ]\n" +
                        "}");
//        ontologyService.save("testOntology", "{\"nodes\":{\"id\":\"0\",\"labels\":\"公司\"},\"relationships\":{\"id\":\"314\",\"type\":\"持股\",\"startNode\":\"0\",\"endNode\":\"1\"}}");
    }

    @Test
    public void testFindByName() {
        KnowledgeGraph testOntology = ontologyService.findByName("testOntology");
        System.out.println(testOntology);
    }

    @Test
    public void testFindSymmetricRepetition() {
        ontologyService.findSymmetricRepetition("");
    }

    @Test
    public void testUpdateOntology() {

    }

    @Test
    public void testDeleteOntologyByName() {
        ontologyService.deleteOntologyByName("testOntology");
    }
}