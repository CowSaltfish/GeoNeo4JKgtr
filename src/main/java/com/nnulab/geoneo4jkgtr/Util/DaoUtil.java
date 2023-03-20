package com.nnulab.geoneo4jkgtr.Util;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Neo4j数据连接工具
 * Dao运行失败的语句都在这里处理
 *
 * @author : LiuXianYu
 * @date : 2023/3/13 22:13
 */
@Component
public class DaoUtil {

    @Resource
    private Neo4jUtil neo4jUtil;

    public List<List<Face>> matchSwingPattern(int fid, int length) {
        String cypher = "match (x:Face)-[r:ADJACENT]->(y:Face) " +
                "where x.ageIndex >= y.ageIndex " +
                "match (x)<-[:BELONG]-(b: Boundary)-[:BELONG]->(y) " +
                "where (abs(b.strike-toFloat(x.A_地层走))<45 or abs(b.strike-toFloat(x.A_地层走)+180)<45) " +
                "and (abs(b.strike-toFloat(y.A_地层走))<45 or abs(b.strike-toFloat(y.A_地层走)+180)<45) " +
                "with collect(r) as cr " +
                "match (:Face{fid: 5024 })-[:ADJACENT]->(s1:Face) " +
                "match p=(s1)-[:ADJACENT*.." + length + " ]->(:Face) " +
                "with nodes(p) as ns, relationships(p) as rs,p " +
                "where SIZE(apoc.coll.toSet(ns)) = LENGTH(p) + 1 " +
                "and all(n in ns where n.ageIndex > 4) " +
                "and all(r1 in rs where r1 in cr) " +
                "return [n in ns|n] limit 5";
//        List<List<Face>> swing = neo4jUtil.result2Nodes(neo4jUtil.RunCypher(cypher));

        return null;
    }
}
