package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicNode;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:42
 */
@Component
public interface BasicNodeDao<T extends BasicNode> extends Neo4jRepository<T, Long> {

    @Query("start n=node($id) detach delete n")
    void delete(@Param("id") Long id);

    @Query("match (n) detach delete n")
    void clearAll();
}
