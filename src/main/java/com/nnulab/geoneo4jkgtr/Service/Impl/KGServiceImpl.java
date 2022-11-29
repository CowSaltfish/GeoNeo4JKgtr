package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.nnulab.geoneo4jkgtr.Dao.*;
import com.nnulab.geoneo4jkgtr.Entity.*;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Enum.StratumType;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.*;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Model.GeoMap;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.KGService;
import com.nnulab.geoneo4jkgtr.Util.Neo4jUtil;
import com.nnulab.geoneo4jkgtr.Util.TopologyUtil;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
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
        relationDao.inferContactRelationshipOfSSOnStrata();//判断地层之间关系（角度不整合接触、平行不整合接触、整合接触）
        relationDao.inferContactRelationshipOfIntrusionOnStrata();//判断侵入岩之间关系（切割）、侵入岩与围岩（侵入接触、沉积接触、包裹关系、穿切关系）
        relationDao.inferContactRelationshipOfFaultOnStrata();//判断地层之间关系（断层接触）
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
        saveTimeRelationship();//获取地质要素间时间关系，并存入数据库
        saveSpatialRelationship();//获取地质要素间空间关系，并存入数据库
    }

    @Override
    public void createNodes(String facePath, String boundaryPath) {
        geoMap = new GeoMap(facePath, boundaryPath);
        saveFaces();//读取面要素并存入数据库
        saveBoundaries();//读取地层界线及断层数据并存入数据库
    }

    private void saveTimeRelationship() {
        //添加同时期地层间的关系
        inferSameTimeOnStrata();
    }

    private void saveSpatialRelationship() {
        relationDao.CreateAdjacentRelationBetweenFacesFromBoundaries();
        relationDao.CreateContainsRelationBetweenFacesFromBoundaries();
    }

    @Override
    public KnowledgeGraph searchAllKG() {
        KnowledgeGraph knowledgeGraph = new KnowledgeGraph();
        List<BasicRelation> relationships = relationDao.searchAllRelationships();
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
    public KnowledgeGraph search(String ontologyJson) {
//        String cypher = Neo4jUtil.ontologyJson2Cypher(ontologyJson);



        return null;
    }

    private void lightenKnowledgeGraph(KnowledgeGraph knowledgeGraph) {
        List<BasicRelation> relationships = knowledgeGraph.getRelationships();
        List<Object> nodes = knowledgeGraph.getNodes();
        for (BasicRelation br :
                relationships) {
            br.setStartNode(br.getSource().getId());
            br.setEndNode(br.getTarget().getId());
            br.setSource(null);
            br.setTarget(null);
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
            System.out.println("无面图层！");
            return;
        }
        long numFeature = faceLayer.GetFeatureCount(0);
        Feature feature;
        double[] env = new double[4];

        //创建地层面对象
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
                    face.setType(StratumType.Sedimentary);
//                    face.setCenter_y(feature.GetGeometryRef().Centroid().GetY());
                } else if ((Objects.equals(featureType, "Magmatic")))
                    face.setType(StratumType.Magmatic);
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

            //添加面要素
            saveNode(face);
            geoMap.addFace(face);

            //添加形成事件
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
            System.out.println("无边界图层！");
            return;
        }
        long numFeature = boundaryLayer.GetFeatureCount(0);

        for (int i = 0; i < numFeature; ++i) {
            feature = boundaryLayer.GetFeature(i);

            Boundary boundary = new Boundary();
            boundary.setFid(i);

            //断层
            if ((-1 != boundaryLayer.FindFieldIndex("type", 0) && "fault".equals(feature.GetFieldAsString("type")))
                    || (-1 != boundaryLayer.FindFieldIndex("Type", 0) && "fault".equals(feature.GetFieldAsString("Type")))) {
                List<Fault> faults = findFaultByName(feature.GetFieldAsString("name"));
//                List<Fault> faults = findFaultByName(feature.GetFieldAsString("code"));
                if (!faults.isEmpty()) {//数据库中存在这个断层
                    //添加界线属于断层关系
                    saveRelation(new BelongRelation(boundary, faults.get(0)));
                } else {
                    Fault fault = new Fault();
                    fault.setName(feature.GetFieldAsString("name"));
//                    fault.setName(feature.GetFieldAsString("code"));
                    fault.setCode(feature.GetFieldAsString("code"));
                    //加入新断层
                    saveNode(fault);
                    saveRelation(new BelongRelation(boundary, fault));
                    //加入新断层事件
                    GeoEvent geoEvent = new GeoEvent(fault);
                    saveNode(geoEvent);
                    saveRelation(new SubjectRelation(geoEvent, fault));
                }
            }

            //添加新的界线
            saveNode(boundary);
            geoMap.addBoundary(boundary);

            //生成线面拓扑关系
            int fid;
            Face face;
            if (-1 != boundaryLayer.FindFieldIndex("LEFT_FID", 0) && -1 != feature.GetFieldAsInteger("LEFT_FID")) {
                fid = feature.GetFieldAsInteger("LEFT_FID");
                if (geoMap.getFaces().size() > fid)
                    face = geoMap.getFaces().get(fid);
                else
                    face = findFaceById(fid).get(0);
                if (face != null)//判断界线是否是地层上边界或下边界
                    saveRelation(new BelongRelation(boundary, face).setBelongType(TopologyUtil.JudgeEdgeAtTopOrBottomOfFace(feature.GetGeometryRef(), true)));
            }
            if (-1 != boundaryLayer.FindFieldIndex("RIGHT_FID", 0) && -1 != feature.GetFieldAsInteger("RIGHT_FID")) {
                fid = feature.GetFieldAsInteger("RIGHT_FID");
                if (geoMap.getFaces().size() > fid)
                    face = geoMap.getFaces().get(fid);
                else
                    face = findFaceById(fid).get(0);
                if (face != null)//判断界线是否是地层上边界或下边界
                    saveRelation(new BelongRelation(boundary, face).setBelongType(TopologyUtil.JudgeEdgeAtTopOrBottomOfFace(feature.GetGeometryRef(), false)));
            }
        }

        //生成边界间拓扑关系
        if (0 != getBoundaryCount()) {
            //添加边界邻接关系
            System.out.println("添加边界邻接关系");
            createTopoOfBoundaries(boundaryLayer);
        }
    }

    /**
     * 添加边界邻接关系
     *
     * @param boundaryLayer 弧段图层
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
                        System.out.print("添加边界邻接关系:");
                        saveRelation(new AdjacentRelation(boundaryi, boundaryj));
                        saveRelation(new AdjacentRelation(boundaryj, boundaryi));
                    }
                }
            }
        }
    }

}
