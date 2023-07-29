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
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.Temporal.EarlierThanRelation;
import com.nnulab.geoneo4jkgtr.Model.GeoMap;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Model.StratigraphicChronology;
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
    private StratumDao stratumDao;

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

    private StratigraphicChronology stratigraphicChronology;

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
    public List<Stratum> findFaceById(int id) {
        return stratumDao.findByFid(id);
    }

    @Override
    public long getFaceCount() {
        return stratumDao.count();
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
    public Optional<Boundary> findBoundaryById(long id) {
        return boundaryDao.findById(id);
    }

    @Override
    public Map<Integer, Boundary> findAllBoundaryMap() {
        Map<Integer, Boundary> boundaryMap = new HashMap<>();

        for (Boundary boundary : boundaryDao.findAll()) {
            boundaryMap.put(boundary.getFid(), boundary);
        }
        return boundaryMap;
    }

    @Override
    public List<Boundary> findBoundaryByFid(int fid) {
        return boundaryDao.findByFid(fid);
    }

    @Override
    public List<CuttingThroughRelation> createCuttingThroughRelationOnFaults() {
        return relationDao.createCuttingThroughRelationOnFaults();
    }

    @Override
    public List<CuttingOffRelation> createCuttingOffRelationOnFaults() {
        return relationDao.createCuttingOffRelationOnFaults();
    }

    @Override
    public List<MutuallyCuttingRelation> createMutuallyCuttingRelationOnFaults() {
        return relationDao.createMutuallyCuttingRelationOnFaults();
    }

    @Override
    public void create(String stratumPath, String boundaryPath) {
//        String stratigraphicChronologyPath = "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Project\\GeoNeo4jKgtr\\src\\main\\resources\\static\\StratigraphicTimeTable.csv";
        String stratigraphicChronologyPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\StratigraphicTimeTable.csv";
        stratigraphicChronology = FileUtil.getStratigraphicChronologyFromCSV(stratigraphicChronologyPath);
        geoMap = new GeoMap(stratumPath, boundaryPath);
        //节点生成
        createNodes();
        //构建要素空间关系、几何关系(邻接、方向、距离)、角度
        CreateSpatialGeometryRelationship();
//        //根据地层年代表获取沉积事件时间关系
//        CreateTemporalRelationshipBetweenDepositionEvents();
    }

    @Override
    public void create(String facePath, String boundaryPath, String stratigraphicChronologyPath) {
        if (!StringUtil.isBlank(stratigraphicChronologyPath)) {
            stratigraphicChronology = FileUtil.getStratigraphicChronologyFromCSV(stratigraphicChronologyPath);
        }
        create(facePath, boundaryPath);
    }

    @Override
    public void inferContactRelationshipOnStrata() {
        relationDao.inferContactRelationshipOfSSOnStrata();//判断地层之间关系（角度不整合接触、平行不整合接触、整合接触）
        relationDao.inferContactRelationshipOfIntrusionOnStrata();//判断侵入岩之间关系（切割）、侵入岩与围岩（侵入接触、沉积接触、包裹关系、穿切关系）
        relationDao.inferContactRelationshipOfFaultOnStrata();//判断地层之间关系（断层接触）
    }

    @Override
    public void inferSameTypeOnStrata() {
        relationDao.inferSameTypeOnStrata();
    }

    @Override
    public void inferCuttingBetweenFaultsAndFaces() {
        relationDao.inferCuttingBetweenFaultsAndFaces();
    }

    @Override
    public void inferTimeSeriesOfStrata() {
        relationDao.inferTimeSeriesOfStrata();
//        relationDao.addFoldEvent();
        relationDao.deleteExceptTimeAdjacentRelationship();
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

    private void CreateSpatialGeometryRelationship() {
        //生成边界间拓扑关系
        if (0 != getBoundaryCount()) {
            //添加边界邻接关系
            System.out.println("添加地质界线邻接关系");
            createTopoOfBoundaries(geoMap.getBoundaryLayer());
        }
        //获取地质要素间空间关系，并存入数据库
        System.out.println("建立面与面邻接关系");
        relationDao.CreateAdjacentRelationBetweenFaces();
        System.out.println("建立面与面包含关系");
        relationDao.CreateContainsRelationBetweenFaces();
        System.out.println("建立断层间邻接关系");
        relationDao.CreateAdjacentRelationBetweenFaults();
        System.out.println("建立断层与面之间邻接关系");
        relationDao.CreateAdjacentRelationBetweenFaultsAndFaces();
    }

    private void createNodes() {
        System.out.println("建立地层要素");
        saveStrata();//读取地层要素并存入数据库
        System.out.println("建立地质界线要素、断层要素");
        saveBoundaries();//读取地层界线及断层数据并存入数据库
        relationDao.setAllNodesId();
    }

    private void createTimeRelationship() {
        //添加同时期地层间的关系
        inferSameTypeOnStrata();
    }

    @Override
    public KnowledgeGraph searchAllKG() {
        KnowledgeGraph knowledgeGraph = new KnowledgeGraph();
        List<ScenarioRelation> relationships = relationDao.searchAllRelationships();
        List<Object> nodes = relationDao.searchAllNodes();
        if (relationships != null && nodes != null) {
            knowledgeGraph.setRelationships(relationships);
            knowledgeGraph.setNodes(nodes);
//            System.out.println(nodes);
            lightenKnowledgeGraph(knowledgeGraph);
            return knowledgeGraph;
        }
        return null;
    }

    @Override
    public KnowledgeGraph search(String cypher) {
        System.out.println("查询Cypher：" + cypher);
        KnowledgeGraph result = neo4jUtil.result2KG(neo4jUtil.RunCypher(cypher));
        if (result.getNodes().size() != 0) {
            System.out.println("Cypher查询成功！");
        }
        return result;
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

    /**
     * 创建断层间切割关系
     */
    @Override
    public void createGeologicalRelationBetweenFaults() {
        createCuttingThroughRelationOnFaults();
        createCuttingOffRelationOnFaults();
        createMutuallyCuttingRelationOnFaults();
    }

    @Override
    public void createGeologicalRelationBetweenFaultsAndStrata() {
        relationDao.createCuttingRelationBetweenFaultsAndStrata();
        relationDao.setBoundaryOnFaultEnd();
        relationDao.createGlandRelationBetweenFaultsAndStrata();
        //若压盖断层的地层被该断层切割，则为假性压盖，应当删除
        relationDao.deleteFakeGlandRelationship();
    }

    @Override
    public void createGeologicalRelation() {
        createGeologicalRelationBetweenFaults();
        createGeologicalRelationBetweenFaultsAndStrata();
    }

    private void lightenKnowledgeGraph(KnowledgeGraph knowledgeGraph) {
        List<ScenarioRelation> relationships = knowledgeGraph.getRelationships();
        List<Object> nodes = knowledgeGraph.getNodes();
        for (ScenarioRelation sr : relationships) {
            sr.setStartNode(sr.getSource().getId());
            sr.setEndNode(sr.getTarget().getId());
            sr.setSource(null);
            sr.setTarget(null);
        }
//        int index = 0;
        for (Object node : nodes) {
//            if(node instanceof ScenarioNode){
//                ScenarioNode scenarioNode = (ScenarioNode) node;
//                scenarioNode.setId((Long) index++);
//            }
            if (node instanceof Stratum) {
                Stratum stratum = (Stratum) node;
                stratum.getAdjacent().clear();
                stratum.getBelong().clear();
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

    private void saveStrata() {
        Layer stratumLayer = geoMap.getStratumLayer();

        if (null == stratumLayer) {
            System.out.println("无面图层！");
            return;
        }
        FeatureDefn featureDefn = stratumLayer.GetLayerDefn();

        long numFeature = stratumLayer.GetFeatureCount(0);
        Feature feature;
        double[] env = new double[4];

        //创建地层节点
        for (int i = 0; i < numFeature; ++i) {
            feature = stratumLayer.GetFeature(i);
            Stratum stratum = new Stratum();
            stratum.setFid(i);
            stratum.setArea(feature.GetGeometryRef().Area());
            feature.GetGeometryRef().GetEnvelope(env);
            stratum.setEnvelope(env);
            stratum.setPosition(new double[]{(env[0] + (env[1] - env[0]) / 2.0), (env[2] + (env[3] - env[2]) / 2.0)});

            if (-1 != stratumLayer.FindFieldIndex("Type", 0)) {
                String featureType = feature.GetFieldAsString("Type");
                if (Objects.equals(featureType, "Sedimentary")) {
                    stratum.setStratumType(StratumType.Sedimentary);
//                    face.setCenter_y(feature.GetGeometryRef().Centroid().GetY());
                } else if ((Objects.equals(featureType, "Magmatic")))
                    stratum.setStratumType(StratumType.Magmatic);
            }

            if (-1 != stratumLayer.FindFieldIndex("name", 0)) {
                stratum.setNodeName(feature.GetFieldAsString("name"));
            } else if (-1 != stratumLayer.FindFieldIndex("S_Name", 0)) {
                stratum.setNodeName(feature.GetFieldAsString("S_Name"));
            } else {
                stratum.setNodeName(Integer.toString(i));
            }

//            Geometry pointsGeometry = feature.GetGeometryRef().GetGeometryRef(0);
//            for (int j = 0; j < pointsGeometry.GetPointCount(); ++j) {
//                face.addPoint(new Vertex(pointsGeometry.GetX(j), pointsGeometry.GetY(j), pointsGeometry.GetZ(j)));
//            }

            //获取要素属性
            readFeatureAttribute(featureDefn, feature, stratum);
            if (-1 != stratumLayer.FindFieldIndex("name", 0)) {
                String stratumType = feature.GetFieldAsString("name");
                //岩浆岩类型并不在年代表中，因此记为负数
                if (stratigraphicChronology == null) {
                    System.out.println("无地层年代表");
                } else {
                    Map<String, Integer> scm = stratigraphicChronology.getStratigraphicChronologyMap();
                    if (scm != null && scm.size() != 0) {
                        stratum.setAgeIndex(scm.getOrDefault(stratumType, -1));
                    }
                }
            }

            //地层节点存入库中
            System.out.println("创建地层节点：" + i + "/" + numFeature + "[" + stratum + "]");
            saveNode(stratum);
            geoMap.addStratum(stratum);

            //todo 地质事件节点，及事件节点与地质元素、构造节点的关系的建立从本方法中独立出来
            //形成事件存入库中，并建立地层-形成事件的关系
            GeoEvent geoEvent;
            List<GeoEvent> geoEvents;
            if (geoMap.getEvents().containsKey(stratum.getNodeName()))
                saveRelation(new SubjectRelation(geoMap.getEvents().get(stratum.getNodeName()), stratum));
            else if (!(geoEvents = findGeoEventBySubjectName(stratum.getNodeName())).isEmpty()) {
                saveRelation(new SubjectRelation(geoEvents.get(0), stratum));
            } else {
                geoEvent = new GeoEvent(stratum);
                geoMap.addEvent(geoEvent);
                saveNode(geoEvent);
                saveRelation(new SubjectRelation(geoEvent, stratum));
            }
        }
    }

    /**
     * 设置地层年代序号
     *
     * @param stratum
     */
    private void SetAgeIndex(Stratum stratum) {

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
            double[][] vertices = new double[0][];
            if (feature.GetGeometryRef().GetPointCount() != 0) {
                vertices = feature.GetGeometryRef().GetPoints();
            } else if (feature.GetGeometryRef().GetGeometryRef(0).GetPointCount() != 0) {
                vertices = feature.GetGeometryRef().GetGeometryRef(0).GetPoints();
            }
            boundary.setStrike(GeometryUtil.calLineStrike(vertices, false));
            boundary.setLength(feature.GetGeometryRef().Length());

            //断层
            if ((-1 != boundaryLayer.FindFieldIndex("type", 0) && "fault".equals(feature.GetFieldAsString("type")))
                    || (-1 != boundaryLayer.FindFieldIndex("Type", 0) && "fault".equals(feature.GetFieldAsString("Type")))) {
//                List<Fault> faults = findFaultByName(feature.GetFieldAsString("name"));
                String code = feature.GetFieldAsString("code");
                List<Fault> faults = findFaultByName(code);
                if (!faults.isEmpty()) {//数据库中存在这个断层
                    //添加界线属于断层关系
                    saveRelation(new BelongRelation(boundary, faults.get(0)));
                    //增加该断层的长度
                    neo4jUtil.RunCypher("MATCH (n:Fault) where n.nodeName=" + code + " set n.length+=" + boundary.getLength());
                } else {
                    Fault fault = new Fault();
//                    fault.setName(feature.GetFieldAsString("name"));
                    fault.setName(feature.GetFieldAsString("code"));
                    fault.setCode(feature.GetFieldAsString("code"));
                    fault.setLength(boundary.getLength());
                    //加入新断层
                    System.out.println("创建断层节点" + "[" + fault + "]");
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
            System.out.println("创建地质界线：" + i + "/" + numFeature);
            saveNode(boundary);
            geoMap.addBoundary(boundary);

            //生成线面拓扑关系
            int lfid = -1, rfid = -1;
            Stratum stratum;
            if (-1 != boundaryLayer.FindFieldIndex("LEFT_FID", 0) && -1 != feature.GetFieldAsInteger("LEFT_FID")) {
                lfid = feature.GetFieldAsInteger("LEFT_FID");
                if (geoMap.getStrata().size() > lfid)
                    stratum = geoMap.getStrata().get(lfid);
                else
                    stratum = findFaceById(lfid).get(0);
                if (stratum != null)//判断界线是否是地层上边界或下边界
                    saveRelation(new BelongRelation(boundary, stratum).setBelongType(TopologyUtil.JudgeEdgeAtTopOrBottomOfFace(feature.GetGeometryRef(), true)));
            }
            if (-1 != boundaryLayer.FindFieldIndex("RIGHT_FID", 0) && -1 != feature.GetFieldAsInteger("RIGHT_FID")) {
                rfid = feature.GetFieldAsInteger("RIGHT_FID");
                if (lfid != rfid) {
                    if (geoMap.getStrata().size() > rfid)
                        stratum = geoMap.getStrata().get(rfid);
                    else
                        stratum = findFaceById(rfid).get(0);
                    if (stratum != null)//判断界线是否是地层上边界或下边界
                        saveRelation(new BelongRelation(boundary, stratum).setBelongType(TopologyUtil.JudgeEdgeAtTopOrBottomOfFace(feature.GetGeometryRef(), false)));
                }
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

                    //建立边界邻接关系
                    if (geoMap.getBoundaries().size() > i)
                        boundaryi = geoMap.getBoundaries().get(i);
                    else if (boundaryMap.containsKey(i))
                        boundaryi = boundaryMap.get(i);
                    if (geoMap.getBoundaries().size() > j)
                        boundaryj = geoMap.getBoundaries().get(j);
                    else if (boundaryMap.containsKey(j))
                        boundaryj = boundaryMap.get(j);
                    if (boundaryi != null && boundaryj != null) {
                        System.out.println("添加地质界线" + i + "/" + numFeature + "和" + j + "之间的邻接关系:");
                        saveRelation(new AdjacentRelation(boundaryi, boundaryj));
                        saveRelation(new AdjacentRelation(boundaryj, boundaryi));
                        //计算夹角
//                        basicNodeDao.setBoundariesAngle();
                    }
                }
            }
        }
    }

    /**
     * 根据地层年代表获取沉积事件时间关系
     */
//    private void CreateTemporalRelationshipBetweenDepositionEvents() {
//        if (stratigraphicChronology == null) {
//            System.out.println("无地层年代表");
//            return;
//        }
//        List<String> scl = stratigraphicChronology.getStratigraphicChronologyList();
//        if (scl == null || scl.size() == 0) {
//            return;
//        }
//        String stratumName0;
//        List<GeoEvent> events0 = null;
//        int index = 0;
//        int size = scl.size();
//        for (; index < size; ++index) {
//            stratumName0 = scl.get(index);
//            events0 = geoEventDao.findByName(stratumName0);
//            if (events0 != null && events0.size() != 0) {
//                break;
//            }
//        }
//        index++;
//        if (index >= size) {
//            return;
//        }
//        for (; index < size; ++index) {
//            String stratumName1 = scl.get(index);
//            List<GeoEvent> events1 = geoEventDao.findByName(stratumName1);
//            if (events0 != null && events0.size() != 0 && events1 != null && events1.size() != 0) {
//                GeoEvent event0 = events0.get(0);
//                GeoEvent event1 = events1.get(0);
//                relationDao.save(new EarlierThanRelation(event1, event0));
//                stratumName0 = stratumName1;
//                events0 = geoEventDao.findByName(stratumName0);
//            }
//        }
//        System.out.println("根据地层年代表获取沉积事件时间关系成功");
//    }

}
