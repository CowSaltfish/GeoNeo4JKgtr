package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.CuttingOffRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.CuttingThroughRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.MutuallyCuttingRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 15:23
 */
public interface KGService {

    <T extends BasicNode> T saveNode(T node);

    <T extends BasicRelation> T saveRelation(T relation);

    void delete(long id);

    List<Fault> findFaultByName(String name);

    List<Stratum> findFaceById(int id);

    long getFaceCount();

    long getBoundaryCount();

    long getFaultCount();

    Optional<Boundary> findBoundaryById(long id);

    Map<Integer, Boundary> findAllBoundaryMap();

    List<Boundary> findBoundaryByFid(int fid);

    List<CuttingThroughRelation> createCuttingThroughRelationOnFaults();

    List<CuttingOffRelation> createCuttingOffRelationOnFaults();

    List<MutuallyCuttingRelation> createMutuallyCuttingRelationOnFaults();

    void create(String stratumPath, String boundaryPath);

    void create(String facePath, String boundaryPath, String stratigraphicChronologyPath);

    void clearAll();

    void inferContactRelationshipOnStrata();

    void inferSameTypeOnStrata();

    void inferCuttingBetweenFaultsAndFaces();

    List<GeoEvent> findGeoEventBySubjectName(String nodeName);

    void inferTimeSeriesOfStrata();

    void inferTimeOfIntrusionByStrata();

    void inferTimeOfFaultsByStrata();

    KnowledgeGraph searchAllKG();

    KnowledgeGraph search(String cypher);

    KnowledgeGraph searchByOntology(String ontologyName);

    void createGeologicalRelationBetweenFaults();

    void createGeologicalRelationBetweenFaultsAndStrata();

    void createGeologicalRelation();
}
