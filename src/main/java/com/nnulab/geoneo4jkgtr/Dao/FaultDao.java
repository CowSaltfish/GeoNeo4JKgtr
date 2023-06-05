package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 19:34
 */
public interface FaultDao extends Neo4jRepository<Fault, Long> {

    @Query("MATCH (n:Fault) WHERE n.name = $name RETURN n")
    List<Fault> findByName(@Param("name")String name) ;

}
