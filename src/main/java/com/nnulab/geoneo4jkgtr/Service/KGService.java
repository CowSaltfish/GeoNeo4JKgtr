package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.CuttingOffRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.CuttingThroughRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.MutuallyCuttingRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

import java.util.List;
import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 15:23
 */
public interface KGService {

    public <T extends BasicNode> T saveNode(T node);

    public <T extends BasicRelation> T saveRelation(T relation);

    public void delete(long id);

    List<Fault> findFaultByName(String name);

    List<Face> findFaceById(int id);

    long getFaceCount();

    long getBoundaryCount();

    long getFaultCount();

    Boundary findBoundaryById(long id);

    Map<Integer, Boundary> findAllBoundaryMap();

    List<Boundary> findBoundaryByFid(int fid);

    List<CuttingThroughRelation> inferCuttingThroughRelationOnFaults();

    List<CuttingOffRelation> inferCuttingOffRelationOnFaults();

    List<MutuallyCuttingRelation> inferMutuallyCuttingRelationOnFaults();

    void clearAll();

    void inferTimeSeriesOfFaults();

    void inferContactRelationshipOnStrata();

    void inferSameTimeOnStrata();

    void inferCuttingBetweenFaultsAndFaces();

    List<GeoEvent> findGeoEventBySubjectName(String nodeName);

    void inferTimeSeriesOfStrata();

    void inferTimeOfIntrusionByStrata();

    void inferTimeOfFaultsByStrata();

    void CreateRelationshipBetweenFaces();

    void createNodes(String facePath, String boundaryPath);

    KnowledgeGraph searchAllKG();

    KnowledgeGraph search(String cypher);

    KnowledgeGraph searchByOntology(String ontologyName);
}
