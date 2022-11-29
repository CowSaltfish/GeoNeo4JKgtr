package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ONgDBDao<T extends BasicNode>  extends Neo4jRepository<T, Long> {

    @Query("WITH \'{{0}}\' AS json RETURN olab.schema.auto.cycher(json, 0, 100, false) as cypher")
    String search(String json);

}
