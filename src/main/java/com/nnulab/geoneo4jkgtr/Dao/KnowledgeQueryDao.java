package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2023/3/16 22:21
 */
public interface KnowledgeQueryDao<T extends ScenarioNode> extends Neo4jRepository<T, Long> {

    @Query("$cypher")
    List<Face> matchFaces(@Param("cypher")String cypher);
}
