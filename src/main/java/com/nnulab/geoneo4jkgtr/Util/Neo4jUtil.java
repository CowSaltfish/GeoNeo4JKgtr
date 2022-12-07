package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Neo4jUtil {
    @Value("${spring.data.neo4j.bolt}")
    private String uri;
    @Value("${spring.data.neo4j.username}")
    private String username;
    @Value("${spring.data.neo4j.password}")
    private String password;

    private Driver driver;

    public Neo4jUtil() {
    }

    private void GetDriver() {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }

    private void close() {
        driver.close();
    }

    /**
     * 获取节点的信息
     *
     * @param cypher
     */
    public StatementResult RunCypher(String cypher) {
        GetDriver();
        try (Session session = driver.session()) {
            return session.run(cypher);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    /**
     * 本体json转cypher
     *
     * @param ontologyJson
     * @return
     */
    public String ontologyJson2Cypher(String ontologyJson) {
//        if (!ontologyJson.matches("graph") || !ontologyJson.matches("nodes") || !ontologyJson.matches("relationships")) {
//            return null;
//        }
        GetDriver();
        try (Session session = driver.session()) {
            StatementResult result = session.run("WITH \'{\"graph\":\n" + ontologyJson + "}\' AS graphData " +
                    "RETURN olab.schema.auto.cypher(graphData, 0, " + 10000 + ", false) AS cypher");
            if (!result.hasNext()) {
                return null;
            }
            Record record = result.next();
            return record.values().get(0).asString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public KnowledgeGraph result2Path(StatementResult result) {
        KnowledgeGraph knowledgeGraph = new KnowledgeGraph();
        List<ScenarioRelation> relationships = new ArrayList<ScenarioRelation>();
        List<Object> nodes = new ArrayList<Object>();
        while (result.hasNext()) {
            Record record = result.next();
            for (org.neo4j.driver.v1.Value i : record.values()) {
                Path path = i.get("graph").get(0).asPath();
                //处理路径中的关系
                for (Relationship relationship : path.relationships()) {
                    relationships.add(Neo4jRelationship2ScenarioRelation(relationship));
                }
                //处理路径中的节点
                for (Node node : path.nodes()) {
                    nodes.add(node);
                }
            }
        }
        knowledgeGraph.setRelationships(relationships);
        knowledgeGraph.setNodes(nodes);
        return knowledgeGraph;
    }

    private ScenarioRelation Neo4jRelationship2ScenarioRelation(Relationship relationship) {
        ScenarioRelation sr = new ScenarioRelation();
        sr.setType(relationship.type());
        sr.setStartNode(relationship.startNodeId());
        sr.setEndNode(relationship.endNodeId());
        //处理关系属性
        for (String relKey : relationship.keys()) {
            sr.getProperties().put(relKey, relationship.get(relKey).asObject());
            String relValue = relationship.get(relKey).asObject().toString();
            System.out.println(relKey + "-----" + relValue);
        }
        return sr;
    }
}
