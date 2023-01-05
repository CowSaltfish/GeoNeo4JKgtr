package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

/**
 * @author : LiuXianYu
 * @date : 2022/11/29 20:38
 */
public interface OntologyService {

    void save(String name, String ontologyJson);

    KnowledgeGraph findByName(String name);

    /**
     * 查找对称重复的地质构造
     * @param name
     * @return
     */
    KnowledgeGraph findSymmetricRepetition(String name);

    KnowledgeGraph findAsymmetricDuplication(String name);


    long updateOntology(String ontologyName, String ontologyJson);

    void deleteOntologyByName(String name);

}
