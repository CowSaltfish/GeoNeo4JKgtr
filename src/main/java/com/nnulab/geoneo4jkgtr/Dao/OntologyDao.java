package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

/**
 * @author : LiuXianYu
 * @date : 2022/12/1 22:01
 */

public interface OntologyDao {
    public void save(KnowledgeGraph ontology);

    public KnowledgeGraph findByName(String name);

    public long updateOntology(String name, KnowledgeGraph ontology);

    public void deleteOntologyByName(String name);
}
