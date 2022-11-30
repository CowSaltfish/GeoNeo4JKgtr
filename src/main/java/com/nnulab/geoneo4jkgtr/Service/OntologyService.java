package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

/**
 * @author : LiuXianYu
 * @date : 2022/11/29 20:38
 */
public interface OntologyService {

    KnowledgeGraph create(String ontologyJson);

    KnowledgeGraph searchByName(String ontologyName);

    KnowledgeGraph editByName(String ontologyName, String ontologyJson);

}
