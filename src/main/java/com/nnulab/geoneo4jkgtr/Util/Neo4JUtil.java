package com.nnulab.geoneo4jkgtr.Util;

import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.Session;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Neo4JUtil {
    private static Driver driver;

    public static void close() {
        driver.close();
    }

    // 获取节点的信息
    public static void getNodesInfo(String cypher) {
        MetaData metaData = new MetaData("");
        try {
            Neo4jSession session = new Neo4jSession(metaData, driver);
            Result result = session.query(cypher, new HashMap<>());
            Iterator<Map<String, Object>> resultIterator = result.iterator();
            while (resultIterator.hasNext()) {
                Map<String, Object> record = resultIterator.next();
                Object value = record.values();
//                for(Value i:value){
//                    Node node = i.asNode();
//                    Iterator nodeTypes = node.labels().iterator(); // 获取节点标签
//                    String nodeType = nodeTypes.next().toString();  // 这里由于本地数据库节点只有一个标签，所以并没有遍历
//                    Iterator keys = node.keys().iterator(); // 获取节点类型迭代器
//                    System.out.println("节点类型："+nodeType);
//                    System.out.println("节点属性如下：");
//                    while(keys.hasNext()){
//                        String attrKey = (String)keys.next(); // 获取节点属性名称
//                        String attrValue = node.get(attrKey).asString(); // 获取节点属性值
//                        System.out.println(attrKey+":"+attrValue);
//                    }
//                    System.out.println("-----这里是分隔符---------");
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }
//    // 测试 --获取neo4j中所有标签为Person的节点的信息
//    public static void main(String... args){
//
//        String cypher = "match (n:Person) return n";;
//        getNodesInfo(cypher);
//    }

}
