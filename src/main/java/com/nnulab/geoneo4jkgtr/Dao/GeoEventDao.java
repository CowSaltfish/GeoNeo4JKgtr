package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/4/15 16:37
 */
public interface GeoEventDao  extends Neo4jRepository<Fault, Long> {

    @Query("MATCH (n:GeoEvent) WHERE n.nodeName = $name RETURN n")
    List<GeoEvent> findByName(@Param("name")String name) ;
}
