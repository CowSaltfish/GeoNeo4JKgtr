package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import org.springframework.stereotype.Service;

/**
 * @author : LiuXianYu
 * @date : 2022/11/29 20:39
 */
@Service
public class OntologyServiceImpl implements OntologyService {

    @Override
    public KnowledgeGraph create(String ontologyJson) {
        //基于本体json创建本体

        return null;
    }

    @Override
    public KnowledgeGraph searchByName(String ontologyName) {
        return null;
    }

    @Override
    public KnowledgeGraph editByName(String ontologyName, String ontologyJson) {
        return null;
    }

}
