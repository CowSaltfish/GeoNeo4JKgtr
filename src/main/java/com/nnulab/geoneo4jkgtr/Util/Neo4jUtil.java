package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Config.Neo4jConfig;
import com.nnulab.geoneo4jkgtr.Dao.ONgDBDao;
import lombok.experimental.UtilityClass;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    public void init() {
        driver.close();
    }

    public void close() {
        driver.close();
    }

    // 获取节点的信息
    public static void getNodesInfo(String cypher) {

    }

    public String ontologyJson2Cypher(String ontologyJson) {

//        if (!ontologyJson.matches("graph") || !ontologyJson.matches("nodes") || !ontologyJson.matches("relationships")) {
//            return null;
//        }
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        Session session = driver.session();

        try {

//        session.run( "CREATE (a:Person {name: {name}, title: {title}})",
//                parameters( "name", "Arthur001", "title", "King001" ) );
//        StatementResult result = session.run( "MATCH (n:Face) WHERE n.nodeName = {nodeName} RETURN n LIMIT 25",
//                parameters( "nodeName", "T" ) );
//        StatementResult result = session.run("WITH \'{json}\' AS graphData " +
//                        "RETURN olab.schema.auto.cypher(graphData, 0, 100, false) AS cypher",
//                parameters("json", ontologyJson));
            StatementResult result = session.run("WITH \'" + ontologyJson + "\' AS graphData " +
                    "RETURN olab.schema.auto.cypher(graphData, 0, 100, false) AS cypher");

            if (!result.hasNext()) {
                return null;
            }
            String cypher = result.next().values().get(0).toString();
            return cypher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
            driver.close();
        }
    }

}
