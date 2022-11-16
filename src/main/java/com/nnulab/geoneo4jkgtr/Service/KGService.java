package com.nnulab.geoneo4jkgtr.Service;

import com.alibaba.fastjson.JSONObject;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Boundary;
import com.nnulab.geoneo4jkgtr.Entity.Face;
import com.nnulab.geoneo4jkgtr.Entity.Fault;
import com.nnulab.geoneo4jkgtr.Entity.GeoEvent;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.CuttingOffRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.CuttingThroughRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.MutuallyCuttingRelation;
import com.nnulab.geoneo4jkgtr.Model.GeoMap;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

import java.util.List;

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

    void CreateTopoBetweenFacesFromBoundaries();

    void createKG(String facePath, String boundaryPath);

    KnowledgeGraph searchAllKG();
}
