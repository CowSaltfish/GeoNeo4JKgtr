package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Entity.Face;
import com.nnulab.geoneo4jkgtr.Entity.Fault;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 19:34
 */
public interface FaceDao extends Neo4jRepository<Face, Long> {

    @Query("MATCH (n:Face) WHERE n.fid = $fid RETURN n")
    List<Face> findByFid(@Param("fid")int fid) ;
}
