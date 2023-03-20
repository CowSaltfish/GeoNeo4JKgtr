package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.nnulab.geoneo4jkgtr.Dao.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Enum.StratumType;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Model.GeoMap;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.KGService;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import com.nnulab.geoneo4jkgtr.Util.*;
import org.gdal.ogr.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 知识图谱基本功能
 *
 * @author : LiuXianYu
 * @date : 2022/4/1 15:25
 */
@Service
public class KGServiceImpl implements KGService {

    @Resource
    private BasicNodeDao<BasicNode> basicNodeDao;

    @Resource
    private ScenarioNodeDao scenarioNodeDao;

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

//    @Resource
//    private FileUtil fileUtil;

    private GeoMap geoMap;

    private Map<String, Integer> stratigraphicChronology;


    @Override
    public <T extends BasicNode> T saveNode(T node) {
        basicNodeDao.save(node);
        if (node instanceof ScenarioNode) {
            saveAttributes((ScenarioNode) node);
        }
        return node;
    }

    private void saveAttributes(ScenarioNode node) {
        Map<String, Object> attributes = node.getAttribute();
        if (attributes == null) {
            System.out.println();
            return;
        }
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            if (StringUtil.isBlank(entry.getKey())) {
                continue;
            }
            scenarioNodeDao.putAttribute(node.getId(), "A_" + entry.getKey(), entry.getValue());
        }
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
    public Map<Integer, Boundary> findAllBoundaryMap() {
        Map<Integer, Boundary> boundaryMap = new HashMap<>();
        long boundaryCount = boundaryDao.count();

        for (Boundary boundary : boundaryDao.findAll(0)) {
            boundaryMap.put(boundary.getFid(), boundary);
        }
        return boundaryMap;
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
    public void create(String facePath, String boundaryPath) {
        geoMap = new GeoMap(facePath, boundaryPath);
        //节点生成
        createNodes(facePath, boundaryPath);
        //构建要素时空关系(邻接、方向、距离)、角度
        CreateRelationshipBetweenFaces();
    }

    @Override
    public void create(String facePath, String boundaryPath, String stratigraphicChronologyPath) {
        if (!StringUtil.isBlank(stratigraphicChronologyPath)) {
            stratigraphicChronology = FileUtil.getStratigraphicChronologyFromCSV(stratigraphicChronologyPath);
        }
        create(facePath, boundaryPath);
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
        //生成边界间拓扑关系
        if (0 != getBoundaryCount()) {
            //添加边界邻接关系
            System.out.println("添加边界邻接关系");
            createTopoOfBoundaries(geoMap.getBoundaryLayer());
        }
        //获取地质要素间时间关系，并存入数据库
        saveTimeRelationship();
        //获取地质要素间空间关系，并存入数据库
        saveSpatialRelationship();
    }

    @Override
    public void createNodes(String facePath, String boundaryPath) {
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
        return neo4jUtil.result2KG(neo4jUtil.RunCypher(cypher));
    }

    @Override
    public KnowledgeGraph searchByOntology(String ontologyName) {
        //基于本体名称获取本体
        KnowledgeGraph ontology = ontologyService.findByName(ontologyName);
        if (ontology == null)
            return null;
        //本体转为cypher
        String cypher = neo4jUtil.ontologyJson2Cypher(JSON.toJSONString(ontology));
        //查询返回子图
        return neo4jUtil.result2KG(neo4jUtil.RunCypher(cypher));
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
            System.out.println("无面图层！");
            return;
        }
        FeatureDefn featureDefn = faceLayer.GetLayerDefn();

        long numFeature = faceLayer.GetFeatureCount(0);
        Feature feature;
        double[] env = new double[4];

        //创建地层节点
        for (int i = 0; i < numFeature; ++i) {
            feature = faceLayer.GetFeature(i);
            Face face = new Face();
            face.setFid(i);
            face.setArea(feature.GetGeometryRef().Area());
            feature.GetGeometryRef().GetEnvelope(env);
            face.setEnvelope(env);
            face.setPosition(new double[]{(env[0] + (env[0] - env[1]) / 2), (env[2] + (env[2] - env[3]) / 2)});

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

            //获取要素属性
            readFeatureAttribute(featureDefn, feature, face);
            if (-1 != faceLayer.FindFieldIndex("name", 0)) {
                String stratumType = feature.GetFieldAsString("name");
                //岩浆岩类型并不在年代表中，因此记为负数
                face.setAgeIndex(stratigraphicChronology.getOrDefault(stratumType, -1));
            }

            //地层节点存入库中
            saveNode(face);
            geoMap.addFace(face);

            //形成事件存入库中，并建立地层-形成事件的关系
//            GeoEvent geoEvent;
//            List<GeoEvent> geoEvents;
//            if (geoMap.getEvents().containsKey(face.getNodeName()))
//                saveRelation(new SubjectRelation(geoMap.getEvents().get(face.getNodeName()), face));
//            else if (!(geoEvents = findGeoEventBySubjectName(face.getNodeName())).isEmpty()) {
//                saveRelation(new SubjectRelation(geoEvents.get(0), face));
//            } else {
//                geoEvent = new GeoEvent(face);
//                geoMap.addEvent(geoEvent);
//                saveNode(geoEvent);
//                saveRelation(new SubjectRelation(geoEvent, face));
//            }
        }
    }

    /**
     * 设置地层年代序号
     *
     * @param face
     */
    private void SetAgeIndex(Face face) {

    }

    /**
     * 获取要素属性表
     *
     * @param featureDefn
     * @param feature
     * @param node
     */
    private void readFeatureAttribute(FeatureDefn featureDefn, Feature feature, ScenarioNode node) {
        int fieldCount = featureDefn.GetFieldCount();
        Map<String, Object> attributeMap = new HashMap<>();
        for (int j = 0; j < fieldCount; j++) {
            FieldDefn fieldDefn = featureDefn.GetFieldDefn(j);
            int fieldTypeCode = fieldDefn.GetFieldType();
            switch (fieldTypeCode) {
                case 2:
                    attributeMap.put(fieldDefn.GetName(), feature.GetFieldAsDouble(j));
                case 12:
                case 0:
                    attributeMap.put(fieldDefn.GetName(), feature.GetFieldAsInteger(j));
                case 4:
                default:
                    attributeMap.put(fieldDefn.GetName(), feature.GetFieldAsString(j));
            }
        }
        node.setAttribute(attributeMap);
    }

    private void saveBoundaries() {
        Layer boundaryLayer = geoMap.getBoundaryLayer();
        Feature feature;
        if (null == boundaryLayer) {
            System.out.println("无边界图层！");
            return;
        }
        FeatureDefn featureDefn = boundaryLayer.GetLayerDefn();
        long numFeature = boundaryLayer.GetFeatureCount(0);

        for (int i = 0; i < numFeature; ++i) {
            feature = boundaryLayer.GetFeature(i);

            Boundary boundary = new Boundary();
            boundary.setFid(i);

            //抽取界线几何信息
            double[][] vertices = feature.GetGeometryRef().GetPoints();
            boundary.setStrike(GeometryUtil.calLineStrike(vertices, false));

            //断层
            if ((-1 != boundaryLayer.FindFieldIndex("type", 0) && "fault".equals(feature.GetFieldAsString("type")))
                    || (-1 != boundaryLayer.FindFieldIndex("Type", 0) && "fault".equals(feature.GetFieldAsString("Type")))) {
//                List<Fault> faults = findFaultByName(feature.GetFieldAsString("name"));
                List<Fault> faults = findFaultByName(feature.GetFieldAsString("code"));
                if (!faults.isEmpty()) {//数据库中存在这个断层
                    //添加界线属于断层关系
                    saveRelation(new BelongRelation(boundary, faults.get(0)));
                } else {
                    Fault fault = new Fault();
//                    fault.setName(feature.GetFieldAsString("name"));
                    fault.setName(feature.GetFieldAsString("code"));
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
            readFeatureAttribute(featureDefn, feature, boundary);

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
    }

    /**
     * 添加边界邻接关系
     *
     * @param boundaryLayer 弧段图层
     */
    private void createTopoOfBoundaries(Layer boundaryLayer) {
        Feature feature;
        Geometry boundaryGeoI, boundaryGeoJ;
        Boundary boundaryi = null, boundaryj = null;
        List<Boundary> boundariesi, boundariesj;
        long numFeature = boundaryLayer.GetFeatureCount(0);
        Map<Integer, Boundary> boundaryMap = findAllBoundaryMap();

        for (int i = 0; i < numFeature - 1; ++i) {
            feature = boundaryLayer.GetFeature(i);
            boundaryGeoI = feature.GetGeometryRef();
            for (int j = i + 1; j < numFeature; ++j) {
                boundaryGeoJ = boundaryLayer.GetFeature(j).GetGeometryRef();
                if ((Math.abs(boundaryGeoI.GetX(0) - boundaryGeoJ.GetX(0)) <= 0.001
                        && Math.abs(boundaryGeoI.GetY(0) - boundaryGeoJ.GetY(0)) <= 0.001)
                        || (Math.abs(boundaryGeoI.GetX(boundaryGeoI.GetPointCount() - 1) - boundaryGeoJ.GetX(boundaryGeoJ.GetPointCount() - 1)) <= 0.001
                        && Math.abs(boundaryGeoI.GetY(boundaryGeoI.GetPointCount() - 1) - boundaryGeoJ.GetY(boundaryGeoJ.GetPointCount() - 1)) <= 0.001)
                        || (Math.abs(boundaryGeoI.GetX(0) - boundaryGeoJ.GetX(boundaryGeoJ.GetPointCount() - 1)) <= 0.001
                        && Math.abs(boundaryGeoI.GetY(0) - boundaryGeoJ.GetY(boundaryGeoJ.GetPointCount() - 1)) <= 0.001)
                        || (Math.abs(boundaryGeoI.GetX(boundaryGeoI.GetPointCount() - 1) - boundaryGeoJ.GetX(0)) <= 0.001
                        && Math.abs(boundaryGeoI.GetY(boundaryGeoI.GetPointCount() - 1) - boundaryGeoJ.GetY(0)) <= 0.001)) {
//                if (boundaryGeoI.Buffer(0.001).Intersect(boundaryLayer.GetFeature(j).GetGeometryRef().Buffer(0.001))) {

                    //建立边界邻接关系
                    if (geoMap.getBoundaries().size() > i)
                        boundaryi = geoMap.getBoundaries().get(i);
//                    else if (!(boundariesi = findBoundaryByFid(i)).isEmpty())
                    else if (boundaryMap.containsKey(i))
                        boundaryi = boundaryMap.get(i);
                    if (geoMap.getBoundaries().size() > j)
                        boundaryj = geoMap.getBoundaries().get(j);
                    else if (boundaryMap.containsKey(j))
                        boundaryj = boundaryMap.get(j);
                    if (boundaryi != null && boundaryj != null) {
//                        System.out.print("添加边界邻接关系:");
                        saveRelation(new AdjacentRelation(boundaryi, boundaryj));
                        saveRelation(new AdjacentRelation(boundaryj, boundaryi));
                        //计算夹角
//                        basicNodeDao.setBoundariesAngle();
                    }
                }
            }
        }
    }

}
