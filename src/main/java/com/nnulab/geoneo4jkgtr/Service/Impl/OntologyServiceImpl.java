package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.alibaba.fastjson.JSON;
import com.nnulab.geoneo4jkgtr.Dao.OntologyDao;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2022/11/29 20:39
 */
@Service
public class OntologyServiceImpl implements OntologyService {

    @Resource
    private OntologyDao ontologyDao;

    @Override
    public void save(String name, String ontologyJson) {
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
    public long updateOntology(String ontologyName, String ontologyJson) {
        KnowledgeGraph ontology = JSON.parseObject(ontologyJson, KnowledgeGraph.class);
        return ontologyDao.updateOntology(ontologyName, ontology);
    }

    @Override
    public void deleteOntologyByName(String name) {
        ontologyDao.deleteOntologyByName(name);
    }
}
