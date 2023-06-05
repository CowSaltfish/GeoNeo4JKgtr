package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Stratum;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2023/3/6 19:23
 */
//@SuppressWarnings({"all"})
public interface FoldDao extends Neo4jRepository<Stratum, Long> {

    @Query("match (x:Stratum)-[:ADJACENT]->(y:Stratum)\n" +
            "where x.ageIndex>4 and y.ageIndex>4//沉积岩\n" +
            "with x,collect(y) as cy//与x一起，可自动group\n" +
            "where all(y1 in cy where x.ageIndex > y1.ageIndex)\n" +
            "return x\n" +
            "union\n" +
            "match (x:Stratum)-[:ADJACENT]->(y:Stratum)\n" +
            "where x.ageIndex>4 and y.ageIndex>4//沉积岩\n" +
            "with x,collect(y) as cy//与x一起，可自动group\n" +
            "where all(y1 in cy where x.ageIndex < y1.ageIndex)\n" +
            "return x\n"
//            +"union\n" +
//            "MATCH (y:Stratum)-[:CONTAINS]->(x:Stratum) \n" +
//            "where x.ageIndex>4 and y.ageIndex>4 and x.ageIndex<>y.ageIndex\n" +
//            "and not exists ((x)-[:CONTAINS]->())\n" +
//            "RETURN x"
    )
    List<Stratum> matchCorePattern();

    @Query("match (x:Stratum)-[r:ADJACENT]->(y:Stratum) " +
            "where x.ageIndex >= y.ageIndex " +
            "match (x)<-[:BELONG]-(b: Boundary)-[:BELONG]->(y) " +
            "where (abs(b.strike-toFloat(x.A_地层走))<45 or abs(b.strike-toFloat(x.A_地层走)+180)<45) " +
            "and (abs(b.strike-toFloat(y.A_地层走))<45 or abs(b.strike-toFloat(y.A_地层走)+180)<45) " +
            "with collect(r) as cr " +
            "match (:Stratum{fid: $fid })-[:ADJACENT]->(s1:Stratum) " +
            "match p=(s1)-[:ADJACENT*..5 ]->(:Stratum) " +
            "with nodes(p) as ns, relationships(p) as rs,p " +
            "where SIZE(apoc.coll.toSet(ns)) = LENGTH(p) + 1 " +
            "and all(n in ns where n.ageIndex > 4) " +
            "and all(r1 in rs where r1 in cr) " +
            "return [n in ns|n]")
    List<List<Stratum>> matchSwingPattern(@Param("fid") Integer fid, @Param("length") Integer length);

    @Query("match (x:Stratum)-[r:ADJACENT]->(y:Stratum) " +
            "where x.ageIndex >= y.ageIndex " +
            "match (x)<-[:BELONG]-(b: Boundary)-[:BELONG]->(y) " +
            "where (abs(b.strike-toFloat(x.A_地层走))<45 or abs(b.strike-toFloat(x.A_地层走)+180)<45) " +
            "and (abs(b.strike-toFloat(y.A_地层走))<45 or abs(b.strike-toFloat(y.A_地层走)+180)<45) " +
            "with collect(r) as cr " +
            "match (:Stratum{fid: $fid })-[:ADJACENT]->(s1:Stratum) " +
            "match p=(s1)-[:ADJACENT*..15 ]->(:Stratum) " +
            "with nodes(p) as ns, relationships(p) as rs,p " +
            "where SIZE(apoc.coll.toSet(ns)) = LENGTH(p) + 1 " +
            "and all(n in ns where n.ageIndex > 4) " +
            "and all(r1 in rs where r1 in cr) " +
            "return [n in ns|n]")
    List<List<Stratum>> matchSwingPattern15(@Param("fid") Integer fid, @Param("length") Integer length);


    /**
     * todo
     * neo4j调用社区发现算法，在3.5版本之前用的是neo4j graph algorithm库，之后用的是neo4j graph science库
     * 但老版本很难找，现在先用硬编码的方式解决两翼划分问题
     */
    @Query("CALL gds.graph.create(\n" +
            "    'myGraph',\n" +
            "    'User',\n" +
            "    {\n" +
            "        LINK: {\n" +
            "            orientation: 'UNDIRECTED'\n" +
            "        }\n" +
            "    },\n" +
            "    {\n" +
            "        nodeProperties: 'seed',\n" +
            "        relationshipProperties: 'weight'\n" +
            "    }\n")
    void divide2Wings();

//    @Query("")
//    void matchSymmetricalRepeatPattern();

}
