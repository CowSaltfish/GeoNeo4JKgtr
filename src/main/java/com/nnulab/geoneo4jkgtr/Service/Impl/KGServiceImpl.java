package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.nnulab.geoneo4jkgtr.Dao.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Enum.StratumType;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Model.GeoMap;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.KGService;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import com.nnulab.geoneo4jkgtr.Util.Neo4jUtil;
import com.nnulab.geoneo4jkgtr.Util.TopologyUtil;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 15:25
 */
@Service
public class KGServiceImpl implements KGService {

    @Resource
    private BasicNodeDao<BasicNode> basicNodeDao;

    @Resource
    private BasicRelationDao relationDao;

    @Resource
    private FaultDao faultDao;

    @Resource
    private FaceDao faceDao;

    @Resource
    private BoundaryDao boundaryDao;

    @Resource
    private GeoEventDao geoEventDao;

    @Resource
    private OntologyService ontologyService;

    @Resource
    private Neo4jUtil neo4jUtil;

    private GeoMap geoMap;


    @Override
    public <T extends BasicNode> T saveNode(T node) {
        basicNodeDao.save(node);
        return node;
    }

    @Override
    public <T extends BasicRelation> T saveRelation(T relation) {
        relationDao.save(relation);
        return relation;
    }

    @Override
    public void delete(long id) {
        basicNodeDao.delete(id);
    }

    @Override
    public List<Fault> findFaultByName(String name) {
        return faultDao.findByName(name);
    }

    @Override
    public List<Face> findFaceById(int id) {
        return faceDao.findByFid(id);
    }

    @Override
    public long getFaceCount() {
        return faceDao.count();
    }

    @Override
    public long getBoundaryCount() {
        return boundaryDao.count();
    }

    @Override
    public long getFaultCount() {
        return faultDao.count();
    }

    @Override
    public Boundary findBoundaryById(long id) {
        return boundaryDao.findOne(id);
    }

    @Override
    public List<Boundary> findBoundaryByFid(int fid) {
        return boundaryDao.findByFid(fid);
    }

    @Override
    public List<CuttingThroughRelation> inferCuttingThroughRelationOnFaults() {
        return relationDao.inferCuttingThroughRelationOnFaults();
    }

    @Override
    public List<CuttingOffRelation> inferCuttingOffRelationOnFaults() {
        return relationDao.inferCuttingOffRelationOnFaults();
    }

    @Override
    public List<MutuallyCuttingRelation> inferMutuallyCuttingRelationOnFaults() {
        return relationDao.inferMutuallyCuttingRelationOnFaults();
    }

    @Override
    public void inferTimeSeriesOfFaults() {
        relationDao.inferTimeSeriesOfFaults();
    }

    @Override
    public void inferContactRelationshipOnStrata() {
        relationDao.inferContactRelationshipOfSSOnStrata();//??????????????????????????????????????????????????????????????????????????????????????????
        relationDao.inferContactRelationshipOfIntrusionOnStrata();//???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        relationDao.inferContactRelationshipOfFaultOnStrata();//??????????????????????????????????????????
    }

    @Override
    public void inferSameTimeOnStrata() {
        relationDao.inferSameTimeOnStrata();
    }

    @Override
    public void inferCuttingBetweenFaultsAndFaces() {
        relationDao.inferCuttingBetweenFaultsAndFaces();
    }

    @Override
    public void inferTimeSeriesOfStrata() {
        relationDao.inferTimeSeriesOfStrata();
        relationDao.deleteExcpetTimeAdjacentRelationship();
    }

    @Override
    public List<GeoEvent> findGeoEventBySubjectName(String name) {
        return geoEventDao.findByName(name);
    }

    @Override
    public void inferTimeOfIntrusionByStrata() {
        relationDao.inferTimeOfIntrusionByStrata_0();
        relationDao.inferTimeOfIntrusionByStrata_1();
    }

    @Override
    public void inferTimeOfFaultsByStrata() {
        relationDao.inferTimeOfFaultsByStrata_0();
        relationDao.inferTimeOfFaultsByStrata_1();
    }

    @Override
    public void CreateRelationshipBetweenFaces() {
        //???????????????????????????
        if (0 != getBoundaryCount()) {
            //????????????????????????
            System.out.println("????????????????????????");
            createTopoOfBoundaries(geoMap.getBoundaryLayer());
        }
        //??????????????????????????????????????????????????????
        saveTimeRelationship();
        //??????????????????????????????????????????????????????
        saveSpatialRelationship();
    }

    @Override
    public void createNodes(String facePath, String boundaryPath) {
        geoMap = new GeoMap(facePath, boundaryPath);
        saveFaces();//?????????????????????????????????
        saveBoundaries();//???????????????????????????????????????????????????
    }

    private void saveTimeRelationship() {
        //?????????????????????????????????
        inferSameTimeOnStrata();
    }

    private void saveSpatialRelationship() {
        relationDao.CreateAdjacentRelationBetweenFacesFromBoundaries();
        relationDao.CreateContainsRelationBetweenFacesFromBoundaries();
    }

    @Override
    public KnowledgeGraph searchAllKG() {
        KnowledgeGraph knowledgeGraph = new KnowledgeGraph();
        List<ScenarioRelation> relationships = relationDao.searchAllRelationships();
        List<Object> nodes = relationDao.searchAllNodes();
        if (relationships != null && nodes != null) {
            knowledgeGraph.setRelationships(relationships);
            knowledgeGraph.setNodes(nodes);
            System.out.println(nodes);
            lightenKnowledgeGraph(knowledgeGraph);
            return knowledgeGraph;
        }
        return null;
    }

    @Override
    public KnowledgeGraph search(String cypher) {
        return neo4jUtil.result2Path(neo4jUtil.RunCypher(cypher));
    }

    @Override
    public KnowledgeGraph searchByOntology(String ontologyName) {
        //??????????????????????????????
        KnowledgeGraph ontology = ontologyService.findByName(ontologyName);
        if(ontology==null)
            return null;
        //????????????cypher
        String cypher = neo4jUtil.ontologyJson2Cypher(JSON.toJSONString(ontology));
        //??????????????????
        return neo4jUtil.result2Path(neo4jUtil.RunCypher(cypher));
    }

    private void lightenKnowledgeGraph(KnowledgeGraph knowledgeGraph) {
        List<ScenarioRelation> relationships = knowledgeGraph.getRelationships();
        List<Object> nodes = knowledgeGraph.getNodes();
        for (ScenarioRelation sr :
                relationships) {
            sr.setStartNode(sr.getSource().getId());
            sr.setEndNode(sr.getTarget().getId());
            sr.setSource(null);
            sr.setTarget(null);
        }
        for (Object node :
                nodes) {
            if (node instanceof Face) {
                Face face = (Face) node;
                face.getAdjacent().clear();
                face.getBelong().clear();
            } else if (node instanceof Boundary) {
                Boundary boundary = (Boundary) node;
                boundary.getAdjacent().clear();
                boundary.getBelong().clear();
            } else if (node instanceof Fault) {
                Fault fault = (Fault) node;
                fault.getBelong().clear();
            }
        }
    }

    @Override
    public void clearAll() {
        basicNodeDao.clearAll();
    }

    private void saveFaces() {
        Layer faceLayer = geoMap.getFaceLayer();
        if (null == faceLayer) {
            System.out.println("???????????????");
            return;
        }
        long numFeature = faceLayer.GetFeatureCount(0);
        Feature feature;
        double[] env = new double[4];

        //??????????????????
        for (int i = 0; i < numFeature; ++i) {
            feature = faceLayer.GetFeature(i);
            Face face = new Face();
            face.setFid(i);
            face.setArea(feature.GetGeometryRef().Area());
            feature.GetGeometryRef().GetEnvelope(env);
            face.setEnvelope(env);

            if (-1 != faceLayer.FindFieldIndex("Type", 0)) {
                String featureType = feature.GetFieldAsString("Type");
                if (Objects.equals(featureType, "Sedimentary")) {
                    face.setStratumType(StratumType.Sedimentary);
//                    face.setCenter_y(feature.GetGeometryRef().Centroid().GetY());
                } else if ((Objects.equals(featureType, "Magmatic")))
                    face.setStratumType(StratumType.Magmatic);
            }

            if (-1 != faceLayer.FindFieldIndex("name", 0)) {
                face.setNodeName(feature.GetFieldAsString("name"));
            } else if (-1 != faceLayer.FindFieldIndex("S_Name", 0)) {
                face.setNodeName(feature.GetFieldAsString("S_Name"));
            } else {
                face.setNodeName(Integer.toString(i));
            }

//            Geometry pointsGeometry = feature.GetGeometryRef().GetGeometryRef(0);
//            for (int j = 0; j < pointsGeometry.GetPointCount(); ++j) {
//                face.addPoint(new Vertex(pointsGeometry.GetX(j), pointsGeometry.GetY(j), pointsGeometry.GetZ(j)));
//            }

            //????????????????????????
            saveNode(face);
            geoMap.addFace(face);

            //??????????????????????????????????????????-?????????????????????
            GeoEvent geoEvent;
            List<GeoEvent> geoEvents;
            if (geoMap.getEvents().containsKey(face.getNodeName()))
                saveRelation(new SubjectRelation(geoMap.getEvents().get(face.getNodeName()), face));
            else if (!(geoEvents = findGeoEventBySubjectName(face.getNodeName())).isEmpty()) {
                saveRelation(new SubjectRelation(geoEvents.get(0), face));
            } else {
                geoEvent = new GeoEvent(face);
                geoMap.addEvent(geoEvent);
                saveNode(geoEvent);
                saveRelation(new SubjectRelation(geoEvent, face));
            }
        }
    }

    private void saveBoundaries() {
        Layer boundaryLayer = geoMap.getBoundaryLayer();
        Feature feature;
        if (null == boundaryLayer) {
            System.out.println("??????????????????");
            return;
        }
        long numFeature = boundaryLayer.GetFeatureCount(0);

        for (int i = 0; i < numFeature; ++i) {
            feature = boundaryLayer.GetFeature(i);

            Boundary boundary = new Boundary();
            boundary.setFid(i);

            //??????
            if ((-1 != boundaryLayer.FindFieldIndex("type", 0) && "fault".equals(feature.GetFieldAsString("type")))
                    || (-1 != boundaryLayer.FindFieldIndex("Type", 0) && "fault".equals(feature.GetFieldAsString("Type")))) {
//                List<Fault> faults = findFaultByName(feature.GetFieldAsString("name"));
                List<Fault> faults = findFaultByName(feature.GetFieldAsString("code"));
                if (!faults.isEmpty()) {//??????????????????????????????
                    //??????????????????????????????
                    saveRelation(new BelongRelation(boundary, faults.get(0)));
                } else {
                    Fault fault = new Fault();
//                    fault.setName(feature.GetFieldAsString("name"));
                    fault.setName(feature.GetFieldAsString("code"));
                    fault.setCode(feature.GetFieldAsString("code"));
                    //???????????????
                    saveNode(fault);
                    saveRelation(new BelongRelation(boundary, fault));
                    //?????????????????????
                    GeoEvent geoEvent = new GeoEvent(fault);
                    saveNode(geoEvent);
                    saveRelation(new SubjectRelation(geoEvent, fault));
                }
            }

            //??????????????????
            saveNode(boundary);
            geoMap.addBoundary(boundary);

            //????????????????????????
            int fid;
            Face face;
            if (-1 != boundaryLayer.FindFieldIndex("LEFT_FID", 0) && -1 != feature.GetFieldAsInteger("LEFT_FID")) {
                fid = feature.GetFieldAsInteger("LEFT_FID");
                if (geoMap.getFaces().size() > fid)
                    face = geoMap.getFaces().get(fid);
                else
                    face = findFaceById(fid).get(0);
                if (face != null)//????????????????????????????????????????????????
                    saveRelation(new BelongRelation(boundary, face).setBelongType(TopologyUtil.JudgeEdgeAtTopOrBottomOfFace(feature.GetGeometryRef(), true)));
            }
            if (-1 != boundaryLayer.FindFieldIndex("RIGHT_FID", 0) && -1 != feature.GetFieldAsInteger("RIGHT_FID")) {
                fid = feature.GetFieldAsInteger("RIGHT_FID");
                if (geoMap.getFaces().size() > fid)
                    face = geoMap.getFaces().get(fid);
                else
                    face = findFaceById(fid).get(0);
                if (face != null)//????????????????????????????????????????????????
                    saveRelation(new BelongRelation(boundary, face).setBelongType(TopologyUtil.JudgeEdgeAtTopOrBottomOfFace(feature.GetGeometryRef(), false)));
            }
        }
    }

    /**
     * ????????????????????????
     *
     * @param boundaryLayer ????????????
     */
    private void createTopoOfBoundaries(Layer boundaryLayer) {
        Feature feature;
        Geometry boundaryGeo;
        Boundary boundaryi = null, boundaryj = null;
        List<Boundary> boundariesi, boundariesj;
        long numFeature = boundaryLayer.GetFeatureCount(0);

        for (int i = 0; i < numFeature - 1; ++i) {
            feature = boundaryLayer.GetFeature(i);
            boundaryGeo = feature.GetGeometryRef();
            for (int j = i + 1; j < numFeature; ++j) {
                if (boundaryGeo.Buffer(0.001).Intersect(boundaryLayer.GetFeature(j).GetGeometryRef().Buffer(0.001))) {
                    if (geoMap.getBoundaries().size() > i)
                        boundaryi = geoMap.getBoundaries().get(i);
                    else if (!(boundariesi = findBoundaryByFid(i)).isEmpty())
                        boundaryi = boundariesi.get(0);
                    if (geoMap.getBoundaries().size() > j)
                        boundaryj = geoMap.getBoundaries().get(j);
                    else if (!(boundariesj = findBoundaryByFid(j)).isEmpty())
                        boundaryj = boundariesj.get(0);
                    if (boundaryi != null && boundaryj != null) {
                        System.out.print("????????????????????????:");
                        saveRelation(new AdjacentRelation(boundaryi, boundaryj));
                        saveRelation(new AdjacentRelation(boundaryj, boundaryi));
                    }
                }
            }
        }
    }

}
