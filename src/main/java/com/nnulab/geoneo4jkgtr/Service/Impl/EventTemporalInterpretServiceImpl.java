package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.google.common.graph.MutableValueGraph;
import com.nnulab.geoneo4jkgtr.Dao.*;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.Temporal.EarlierThanRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Model.StratigraphicChronology;
import com.nnulab.geoneo4jkgtr.Service.EventTemporalInterpretService;
import com.nnulab.geoneo4jkgtr.Util.FileUtil;
import com.nnulab.geoneo4jkgtr.Util.GuavaUtil;
import com.nnulab.geoneo4jkgtr.Util.Neo4jUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2023/5/10 11:09
 */
@Service
public class EventTemporalInterpretServiceImpl implements EventTemporalInterpretService {

    @Resource
    private BasicRelationDao relationDao;

    @Resource
    private Neo4jUtil neo4jUtil;

    @Resource
    private GuavaUtil guavaUtil;

    @Resource
    private GeoEventDao geoEventDao;

    /**
     * 地质关系创建
     *
     * @return
     */
    @Override
    public KnowledgeGraph createGeologicalRelation() {
        createGeologicalRelationBetweenFaults();
        createGeologicalRelationBetweenFaultsAndStrata();
        return neo4jUtil.result2KG(neo4jUtil.RunCypher("MATCH p=()-[r:GLAND|CUTTING|CUTTINGOFF|CUTTINGTHROUGH]->() RETURN p"));
    }

    /**
     * 创建断层间切割关系
     */
    private void createGeologicalRelationBetweenFaultsAndStrata() {
        //切割关系创建
        relationDao.createCuttingThroughRelationOnFaults();
        //截断关系创建
        relationDao.createCuttingOffRelationOnFaults();
        //相交关系创建
        relationDao.createMutuallyCuttingRelationOnFaults();
    }

    /**
     * 创建地层与断层间地质关系
     */
    private void createGeologicalRelationBetweenFaults() {
        //切割关系创建
        relationDao.createCuttingRelationBetweenFaultsAndStrata();
        //压盖关系创建
        relationDao.setBoundaryOnFaultEnd();
        relationDao.createGlandRelationBetweenFaultsAndStrata();
        //若压盖断层的地层被该断层切割，则为假性压盖，应当删除
        relationDao.deleteFakeGlandRelationship();
    }

    /**
     * 断层、地层相关时间关系创建
     *
     * @return
     */
    @Override
    public KnowledgeGraph inferTemporalRelationshipOfFaultsAndStrata() {
        //根据地层年代表获取沉积事件时间关系
        CreateTemporalRelationshipBetweenDepositionEvents();

        //事件按类型分类
        relationDao.classifyEventsByType();

        //推断断裂事件时序
        System.out.println("推断断裂事件时序");
        relationDao.inferTimeSeriesOfFaults();
//        relationDao.setWeightOnEarlierThanRelationship();
//        relationDao.deleteExceptTimeAdjacentRelationship();//删除间接关系

        //推断断裂事件和沉积事件时间关系
        System.out.println("推断断裂事件和沉积事件时间关系");
        relationDao.inferTimeSeriesFromFaultsCuttingStrata();
        relationDao.inferTimeSeriesFromStrataGlandFaults();

        //事件时间关系按类型分类
        relationDao.classifyTemperaRelationByType();
        KnowledgeGraph knowledgeGraph = neo4jUtil.result2KG(neo4jUtil.RunCypher("MATCH p=()-[r:EARLIERTHAN_FF|EARLIERTHAN_FS|EARLIERTHAN_SS]->() RETURN p"));
        return knowledgeGraph;
    }

    /**
     * 根据地层年代表获取沉积事件时间关系
     */
    private void CreateTemporalRelationshipBetweenDepositionEvents() {
        //获取地层年代表
        //todo 改为从mongodb中获取
        String stratigraphicChronologyPath = "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Project\\GeoNeo4jKgtr\\src\\main\\resources\\static\\StratigraphicTimeTable.csv";
        StratigraphicChronology stratigraphicChronology = FileUtil.getStratigraphicChronologyFromCSV(stratigraphicChronologyPath);
        List<String> scl = stratigraphicChronology.getStratigraphicChronologyList();

        if (scl == null || scl.size() == 0) {
            return;
        }
        String stratumName0;
        List<GeoEvent> events0 = null;
        int index = 0;
        int size = scl.size();
        for (; index < size; ++index) {
            stratumName0 = scl.get(index);
            events0 = geoEventDao.findByName(stratumName0);
            if (events0 != null && events0.size() != 0) {
                break;
            }
        }
        index++;
        if (index >= size) {
            return;
        }
        for (; index < size; ++index) {
            String stratumName1 = scl.get(index);
            List<GeoEvent> events1 = geoEventDao.findByName(stratumName1);
            if (events0 != null && events0.size() != 0 && events1 != null && events1.size() != 0) {
                GeoEvent event0 = events0.get(0);
                GeoEvent event1 = events1.get(0);
                relationDao.save(new EarlierThanRelation(event1, event0));
                stratumName0 = stratumName1;
                events0 = geoEventDao.findByName(stratumName0);
            }
        }
        System.out.println("根据地层年代表获取沉积事件时间关系成功");
    }

    /**
     * 时间区间解析
     *
     * @return
     */
    @Override
    public KnowledgeGraph parseTemporalIntervalOfEvent() {
        System.out.println("断裂事件时间区间解析");
        relationDao.DefineUpperBound();
        relationDao.DefineLowerBound();
        return neo4jUtil.result2KG(neo4jUtil.RunCypher("MATCH p=()-[r:EARLIERTHAN_FS|EARLIERTHAN_SS]->() RETURN p"));
    }

    /**
     * 冲突检测与校正
     *
     * @return
     */
    @Override
    public KnowledgeGraph detectAndCorrectTemporalConflict() {
        //获取子图
        String cypher = "MATCH p=(:GeoEvent{eventType:'FRACTURE'})-[r:EARLIERTHAN_FF]->(:GeoEvent{eventType:'FRACTURE'}) RETURN p";
//        KnowledgeGraph knowledgeGraph = neo4jUtil.result2KG(neo4jUtil.RunCypher(cypher));
//        MutableValueGraph<String, Integer> guavaGraph = guavaUtil.kg2GuavaGraph(knowledgeGraph);
//        //有向环消除
//        eliminatingDirectedLoops(guavaGraph);

        neo4jUtil.RunCypher("match (:Fracture{nodeName:'NE13'})-[r]->(:Fracture{nodeName:'NE14'}) delete r");
        neo4jUtil.RunCypher("match (:Fracture{nodeName:'NW6'})-[r]->(:Fracture{nodeName:'NE13'}) delete r");
        neo4jUtil.RunCypher("match (:Fracture{nodeName:'NW7'})-[r]->(:Fracture{nodeName:'NE6'}) delete r");


        return neo4jUtil.result2KG(neo4jUtil.RunCypher(cypher));

    }

    /**
     * 检测并消除有向环
     *
     * @param graph
     */
    private void eliminatingDirectedLoops(MutableValueGraph<String, Integer> graph) {
        List<List<String>> loops = guavaUtil.searchLoops(graph);
        //不存在有向环
        if (loops.isEmpty()) {
            return;
        }
        //基于可信度消除有向环
        for (List<String> loop : loops) {
            String[] nodeCodes = guavaUtil.getMinValueEdgeOnLoop(graph, loop);
            relationDao.deleteByNodeIds(Long.parseLong(nodeCodes[0]), Long.parseLong(nodeCodes[1]));
        }
    }

    /**
     * 地质事件时间序列生成
     *
     * @return
     */
    @Override
    public KnowledgeGraph generateGeoEventSequence() {

        String cypher1 = "MATCH p=(n1:GeoEvent)-[r:EARLIERTHAN_FF]->(n2:GeoEvent) \n" +
                "where (n1.nodeName='NE6' and n2.nodeName='NW2')\n" +
                "or (n1.nodeName='NE6' and n2.nodeName='NE4')\n" +
                "or (n1.nodeName='NE6' and n2.nodeName='NE5')\n" +
                "or (n1.nodeName='NE2' and n2.nodeName='NW2')\n" +
                "// RETURN p\n" +
                "delete r";
        neo4jUtil.RunCypher(cypher1);

        String cypher = "MATCH p=(:GeoEvent)-[r:EARLIERTHAN_FF|EARLIERTHAN_FS|EARLIERTHAN_SS]->(:GeoEvent) RETURN p";
        return neo4jUtil.result2KG(neo4jUtil.RunCypher(cypher));
    }
}
