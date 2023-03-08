package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.nnulab.geoneo4jkgtr.Dao.BasicRelationDao;
import com.nnulab.geoneo4jkgtr.Dao.OntologyDao;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Vertex;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import com.nnulab.geoneo4jkgtr.Util.GeometryUtil;
import com.nnulab.geoneo4jkgtr.Util.Neo4jUtil;
import com.nnulab.geoneo4jkgtr.Util.StringUtil;
import org.neo4j.driver.v1.types.Path;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author : LiuXianYu
 * @date : 2022/11/29 20:39
 */
@Service
public class OntologyServiceImpl implements OntologyService {

    @Resource
    private OntologyDao ontologyDao;
    @Resource
    private Neo4jUtil neo4jUtil;

    @Resource
    private BasicRelationDao basicRelationDao;

    private static final String NODE_NAME = "nodeName";
    private static final String POSITION = "position";

    String[] StratigraphicChronology = {"高家边组", "坟头组", "茅山组", "五通组", "石炭系下统", "黄龙组", "船山组", "栖霞组", "孤峰组", "龙潭组", "下青龙组", "上青龙组", "薛家村组", "黄马青组", "范家塘组", "象山群", "浦口组"};

    Map<String, Integer> StratigraphicChronologyMap;

    public OntologyServiceImpl() {
        StratigraphicChronologyMap = new HashMap<>();
        int stratumId = 0;
        for (String stratumName :
                StratigraphicChronology) {
            StratigraphicChronologyMap.put(stratumName, stratumId++);
        }
    }

    @Override
    public void save(String name, String ontologyJson) {
        if (findByName(name) != null)
            return;
        //基于本体json创建本体
        KnowledgeGraph ontology = JSON.parseObject(ontologyJson, KnowledgeGraph.class);
        ontology.setName(name);
        ontology.setOntology(true);
        ontologyDao.save(ontology);
    }

    @Override
    public KnowledgeGraph findByName(String name) {
        return ontologyDao.findByName(name);
    }

    @Override
    public KnowledgeGraph findSymmetricRepetition(String name) {
        //获取地层链集合
//        List<Path> strataList = basicRelationDao.GetStrataList();
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match (b:Boundary)-[:BELONG]->(:Face)\n" +
                "match p=(b)-[:BELONG]->(:Face)\n" +
                "with count(p) as cp,b\n" +
                "where cp=1\n" +
                "with b\n" +
                "match (b)-[:BELONG]->(f1:Face)\n" +
                "with f1\n" +
                "match (b:Boundary)-[:BELONG]->(:Face)\n" +
                "match p=(b)-[:BELONG]->(:Face)\n" +
                "with count(p) as cp,p,b,f1\n" +
                "where cp=1\n" +
                "with b,f1\n" +
                "match (b)-[:BELONG]->(f2:Face)\n" +
                "with f1,f2\n" +
                "where f1.nodeName=f2.nodeName and f1.fid<>f2.fid and not exists((f1)-[:ADJACENT]-(f2))\n" +
                "match p=(f1:Face)-[:ADJACENT*..4]->(f2:Face) \n" +
                "with nodes(p) as nps,f1,f2,p\n" +
                "where SIZE(apoc.coll.toSet(nps)) = LENGTH(p) + 1\n" +
                "unwind nps as np1\n" +
                "with size(collect(distinct np1.nodeName)) as distinctNpName,f1,f2,nps,p\n" +
                "where (length(p)+1)/2+1=distinctNpName\n" +
//                "return p skip 0 limit 100";
                "return p";
        //待返回的值，与cql return后的值顺序对应
        List<Map<String, Object>> nodeList = new ArrayList<>();
        List<Map<String, Object>> edgeList = new ArrayList<>();
        List<List<Map<String, Object>>> pathList = neo4jUtil.getPathList(cql, nodeList, edgeList);
        retMap.put("nodeList", nodeList);
        retMap.put("edgeList", edgeList);
        int count = nodeList.size();
        int countOfSymmetricRepetition = 0;
        for (int i = 0; i < count; i++) {
            List<Map<String, Object>> nodesList = pathList.get(i);
            //核部最老或最新
            if (!oldestOrNewestNuclearDepartment(nodesList)) {
                continue;
            }
            //时代连续
            if (!continuousTimes(nodesList)) {
                continue;
            }
//            //邻接地层时代皆不相同
//            if (AdjacentStrataSameAge(nodesList)) {
//                continue;
//            }
            //地层链同线
            if (!StrataChainCollinear(nodesList)) {
                continue;
            }
            //地层链对称
            if (!StrataChainSymmetry(nodesList)) {
                continue;
            }
            //排除对称的两个地层为邻接地层的情况
//            if (symmetricalAdjacentStrata(nodesList)) {
//                continue;
//            }
            //记录对称重复地层链
            ++countOfSymmetricRepetition;
        }
        if (countOfSymmetricRepetition == 0)
            return null;
        //返回相关地层
        return null;
    }

    /**
     * 排除对称的两个地层为邻接地层的情况
     *
     * @param nodesList
     * @return
     */
    private boolean symmetricalAdjacentStrata(List<Map<String, Object>> nodesList) {
        return false;
    }

    /**
     * 地层链对称
     *
     * @param nodesList
     * @return
     */
    private boolean StrataChainSymmetry(List<Map<String, Object>> nodesList) {
        List<String> stratumNameList0 = new ArrayList<>(), stratumNameList1;
        for (Map<String, Object> node : nodesList) {
            stratumNameList0.add((String) node.get(NODE_NAME));
        }
        stratumNameList1 = new ArrayList<>(stratumNameList0);
        Collections.reverse(stratumNameList1);

        //动态规划获取最长公共子串
        List<String> lcslist = StringUtil.LCSList(stratumNameList0, stratumNameList1);

        return false;
    }

    /**
     * 地层链同线
     *
     * @param nodesList
     * @return
     */
    private boolean StrataChainCollinear(List<Map<String, Object>> nodesList) {
        int i, count = nodesList.size();
        for (i = 0; i < count - 2; ++i) {
            Vertex v0 = new Vertex((double) ((List<?>) nodesList.get(i).get(POSITION)).get(0), (double) ((List<?>) nodesList.get(i).get(POSITION)).get(1));
            Vertex v1 = new Vertex((double) ((List<?>) nodesList.get(i + 1).get(POSITION)).get(0), (double) ((List<?>) nodesList.get(i + 1).get(POSITION)).get(1));
            Vertex v2 = new Vertex((double) ((List<?>) nodesList.get(i + 2).get(POSITION)).get(0), (double) ((List<?>) nodesList.get(i + 2).get(POSITION)).get(1));
            double angle = GeometryUtil.calAngle(v0, v1, v2);
            if (!(angle > 150.0 / 180 * Math.PI && angle < 210.0 / 180 * Math.PI)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 邻接地层时代相同
     *
     * @param nodesList
     * @return
     */
    private boolean AdjacentStrataSameAge(List<Map<String, Object>> nodesList) {
        return false;
    }

    /**
     * 时代连续
     *
     * @param nodesList
     * @return
     */
    private boolean continuousTimes(List<Map<String, Object>> nodesList) {
        int i, count = nodesList.size();
        for (i = 0; i < count; ++i) {
            int strataDiff = Math.abs(StratigraphicChronologyMap.get(nodesList.get(i).get(NODE_NAME)) - StratigraphicChronologyMap.get(nodesList.get((i + 1) % count).get(NODE_NAME)));
            if (strataDiff != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 核部最老或最新
     *
     * @param nodesList
     * @return
     */
    private boolean oldestOrNewestNuclearDepartment(List<Map<String, Object>> nodesList) {
        String name = (String) nodesList.get(nodesList.size() / 2).get(NODE_NAME);
        if (!Objects.equals(name, StratigraphicChronology[0]) && !Objects.equals(name, StratigraphicChronology[StratigraphicChronology.length - 1]))
            return false;
        return true;
    }

    @Override
    public KnowledgeGraph findAsymmetricDuplication(String name) {
        return null;
    }

    @Override
    public long updateOntology(String ontologyName, String ontologyJson) {
        KnowledgeGraph ontology = JSON.parseObject(ontologyJson, KnowledgeGraph.class);
        return ontologyDao.updateOntology(ontologyName, ontology);
    }

    @Override
    public void deleteOntologyByName(String name) {
        ontologyDao.deleteOntologyByName(name);
    }
}
