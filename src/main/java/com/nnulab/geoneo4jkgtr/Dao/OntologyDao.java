package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

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
