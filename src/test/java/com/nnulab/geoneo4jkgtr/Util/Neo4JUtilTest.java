package com.nnulab.geoneo4jkgtr.Util;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Neo4JUtilTest extends TestCase {

    @Resource
    private Neo4jUtil neo4jUtil;

    @Test
    public void testGetNodesInfo() {
        Neo4jUtil.getNodesInfo("match (n:Face) return n");
    }

    @Test
    public void testOntologyJson2Cypher() {
        System.out.println(neo4jUtil.ontologyJson2Cypher("{\n" +
                "    \"graph\":{\n" +
                "        \"nodes\":[\n" +
                "            {\n" +
                "                \"id\":\"0\",\n" +
                "                \"labels\":[\n" +
                "                    \"Face\"\n" +
                "                ],\n" +
                "                \"properties_filter\":[{\n" +
                "                    \"type\":\"{var}.type=\\'Sedimentary\\'\"\n" +
                "                     }]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\":\"1\",\n" +
                "                \"labels\":[\n" +
                "                    \"Face\"\n" +
                "                ],\n" +
                "                \"properties_filter\": [{\n" +
                "                    \"type\":\"{var}.type=\\'Sedimentary\\'\"\n" +
                "                     }]\n" +
                "            },\n" +
                "            {\n" +
                "                \"id\":\"2\",\n" +
                "                \"labels\":[\n" +
                "                    \"Boundary\"\n" +
                "                ]\n" +
                "            }\n" +
                "\n" +
                "        ],\n" +
                "        \"relationships\":[\n" +
                "            {\n" +
                "                \"id\":\"100\",\n" +
                "                \"type\":\"BELONG\",\n" +
                "                \"startNode\":\"2\",\n" +
                "                \"endNode\":\"0\"\n" +
                "            },{\n" +
                "                \"id\":\"101\",\n" +
                "                \"type\":\"BELONG\",\n" +
                "                \"startNode\":\"2\",\n" +
                "                \"endNode\":\"1\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}"));
    }
}