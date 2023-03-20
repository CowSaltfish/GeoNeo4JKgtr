package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.internal.value.ListValue;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

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
     * 执行cypher返回结果
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

    /**
     * 解析结果返回知识图谱
     *
     * @param result
     * @return
     */
    public KnowledgeGraph result2KG(StatementResult result) {
        KnowledgeGraph knowledgeGraph = new KnowledgeGraph();
        List<ScenarioRelation> relationships = new ArrayList<>();
        List<Object> nodes = new ArrayList<>();
        while (result.hasNext()) {
            Record record = result.next();
            for (org.neo4j.driver.v1.Value value : record.values()) {
                if(value instanceof NodeValue){
                    Map<String, Object> nodeMap = value.asMap();
                    Map<String, Object> nodeHashMap = new HashMap<>(nodeMap);
                    nodeHashMap.put("id", value.asNode().id());
                    nodes.add(nodeHashMap);
                }
                else if(value instanceof ListValue){
                    Path path = value.get("graph").get(0).asPath();
                    //处理路径中的关系
                    for (Relationship relationship : path.relationships()) {
                        relationships.add(Neo4jRelationship2ScenarioRelation(relationship));
                    }
                    //处理路径中的节点
                    for (Node node : path.nodes()) {
                        Map<String, Object> nodeMap = node.asMap();
                        Map<String, Object> nodeHashMap = new HashMap<>(nodeMap);
                        nodeHashMap.put("id", node.id());
                        nodes.add(nodeHashMap);
                    }
                }
            }
        }
        knowledgeGraph.setRelationships(relationships);
        knowledgeGraph.setNodes(nodes);
        return knowledgeGraph;
    }

    /**
     * 解析结果返回路径节点
     *
     * @param result
     * @return
     */
    public List<Object> result2Nodes(StatementResult result) {
        List<Object> nodes = new ArrayList<>();
        while (result.hasNext()) {
            Record record = result.next();
            for (org.neo4j.driver.v1.Value i : record.values()) {
                if(i instanceof ListValue){
                    List<Object> innerO = new ArrayList<>(((ListValue) i).asList());
                    nodes.add(innerO);
                }
                else{
                    nodes.add(i);
                }
            }
        }
        return nodes;
    }

    /**
     * 将Neo4j的关系转为地理场景关系
     *
     * @param relationship
     * @return
     */
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

    /**
     * cql 路径查询 返回节点和关系
     *
     * @param cql      查询语句
     * @param nodeList 节点
     * @param edgeList 关系
     * @return List<Map < String, Object>>
     */
    public <T> List<List<Map<String, Object>>> getPathList(String cql, List<T> nodeList, List<T> edgeList) {
        GetDriver();
        List<List<Map<String, Object>>> nodesOfPathList = new ArrayList<>();
        try {
            Session session = driver.session();
            StatementResult result = session.run(cql);
            List<Record> list = result.list();
            for (Record r : list) {
                for (String index : r.keys()) {
                    Path path = r.get(index).asPath();
                    //节点
                    Iterable<Node> nodes = path.nodes();
                    Map<Long, Map<String, Object>> nodesMap = new HashMap<>();
                    List<Map<String, Object>> nodesList = new ArrayList<>();
                    for (Iterator iter = nodes.iterator(); iter.hasNext(); ) {
                        InternalNode nodeInter = (InternalNode) iter.next();
                        Map<String, Object> nodeMap = new HashMap<>();
                        //节点上设置的属性
                        nodeMap.putAll(nodeInter.asMap());
                        //外加一个固定属性
                        nodeMap.put("nodeId", nodeInter.id());
                        nodesMap.put(nodeInter.id(), nodeMap);
                        nodesList.add(nodeMap);
                    }
                    nodesOfPathList.add(nodesList);
                    nodeList.add((T) nodesMap);
                    //关系
                    Iterable<Relationship> edges = path.relationships();
                    Map<Long, Map<String, Object>> relationsMap = new HashMap<>();
                    for (Iterator iter = edges.iterator(); iter.hasNext(); ) {
                        InternalRelationship relationInter = (InternalRelationship) iter.next();
                        Map<String, Object> relationMap = new HashMap<>();
                        relationMap.putAll(relationInter.asMap());
                        //关系上设置的属性
                        relationMap.put("edgeId", relationInter.id());
                        relationMap.put("edgeFrom", relationInter.startNodeId());
                        relationMap.put("edgeTo", relationInter.endNodeId());
                        relationsMap.put(relationInter.id(), relationMap);
                    }
                    edgeList.add((T) relationsMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodesOfPathList;
    }


}
