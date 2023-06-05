package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Stratum;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 19:34
 */
public interface StratumDao extends Neo4jRepository<Stratum, Long> {

    @Query("MATCH (n:Stratum) WHERE n.fid = $fid RETURN n")
    List<Stratum> findByFid(@Param("fid")int fid) ;

    /**
     * 查找沉积岩，且任一沉积岩所在连续基岩区域内不止一个沉积岩
     * @return
     */
    @Query("match (m:Stratum)-[:ADJACENT|CONTAINS]-(n:Stratum)\n" +
            "where n.ageIndex>4 and m.ageIndex>4\n" +
            "and n.fid<>m.fid\n" +
            "return distinct n")
    List<Stratum> findSedimentaryRockArea() ;
}