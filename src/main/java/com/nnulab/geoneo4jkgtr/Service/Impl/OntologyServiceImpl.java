package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.nnulab.geoneo4jkgtr.Dao.BasicRelationDao;
import com.nnulab.geoneo4jkgtr.Dao.OntologyDao;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import com.nnulab.geoneo4jkgtr.Util.Neo4jUtil;
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
                "where f1.nodeName=f2.nodeName\n" +
                "match p=(f1:Face)-[:ADJACENT*4]-(f2:Face) \n" +
                "with nodes(p) as nps,f1,f2,p\n" +
                "where SIZE(apoc.coll.toSet(nps)) = LENGTH(p) + 1\n" +
                "unwind nps as np1\n" +
                "with size(collect(distinct np1.nodeName)) as distinctNpName,f1,f2,nps,p\n" +
                "where (length(p)+1)/2+1=distinctNpName\n" +
                "return p skip 0 limit 100";
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
            if(!oldestOrNewestNuclearDepartment(nodesList)){
                continue;
            }
            //时代连续
            if(!continuousTimes(nodesList)){
                continue;
            }
            //邻接地层时代皆不相同
            if(AdjacentStrataSameAge(nodesList)){
                continue;
            }
            //地层链同线
            if(!StrataChainCollinear(nodesList)){
                continue;
            }
            //地层链对称
            if(!StrataChainSymmetry(nodesList)){
                continue;
            }
            //排除对称地层邻接的
            if(symmetricalAdjacentStrata(nodesList)){
                continue;
            }
            //记录对称重复地层链
            ++countOfSymmetricRepetition;
        }
        if (countOfSymmetricRepetition == 0)
            return null;
        //返回相关地层
        return null;
    }

    private boolean symmetricalAdjacentStrata(List<Map<String, Object>> nodesList) {
        return false;
    }

    private boolean StrataChainSymmetry(List<Map<String, Object>> nodesList) {
        return false;
    }

    private boolean StrataChainCollinear(List<Map<String, Object>> nodesList) {
        return false;
    }

    private boolean AdjacentStrataSameAge(List<Map<String, Object>> nodesList) {
        return false;
    }

    private boolean continuousTimes(List<Map<String, Object>> nodesList) {
        return false;
    }

    private boolean oldestOrNewestNuclearDepartment(List<Map<String, Object>> nodesList) {
        return false;
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
