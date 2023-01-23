package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 19:35
 */
public interface BoundaryDao extends Neo4jRepository<Boundary, Long> {
//    @Override
//    @Query("MATCH (n:Boundary) WHERE n.id = \"{0}\" RETURN n")
//    Optional<Boundary> findById(Long id);

    @Query("MATCH (n:Boundary) WHERE n.fid = $fid RETURN n")
    List<Boundary> findByFid(@Param("fid") int fid);

}
