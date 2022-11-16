package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:42
 */
@Repository
public interface BasicNodeDao<T extends BasicNode> extends Neo4jRepository<T, Long> {

//    @Override
    @Query("start n=node({0}) detach delete n")
    void delete(Long id);

    @Query("match (n) detach delete n")
    void clearAll();
}
