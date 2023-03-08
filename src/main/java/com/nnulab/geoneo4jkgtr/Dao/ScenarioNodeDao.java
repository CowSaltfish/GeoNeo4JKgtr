package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author : LiuXianYu
 * @date : 2023/1/23 17:20
 */
public interface ScenarioNodeDao extends Neo4jRepository<ScenarioNode, Long> {

    @Query("match (n) " +
            "where id(n) = $id " +
            "with n " +
            "CALL apoc.create.setProperty(n, $key, $value) " +
            "YIELD node " +
            "RETURN node")
    void putAttribute(@Param("id") long id, @Param("key") String key, @Param("value") Object value);
}
