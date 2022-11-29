package com.nnulab.geoneo4jkgtr.Entity.Basic;

import com.nnulab.geoneo4jkgtr.Model.ESFilter;
import com.nnulab.geoneo4jkgtr.Model.PropertiesFilter;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.Date;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:46
 */
@RelationshipEntity(type = "DEFAULT")
public class BasicRelation implements BasicRelationInterface{
    @GraphId
    private Long id;
    @Property
    private Long startNode;
    @Property
    private Long endNode;

    @Property
    private PropertiesFilter properties_filter;

    @Property
    private ESFilter es_filter;

    @StartNode
    private BasicNode source;

    @EndNode
    private BasicNode target;

    @Property
    private String relationName;

    @Property
    private Long added = new Date().getTime();

    public BasicRelation(){

    }
    public BasicRelation(BasicNode source, BasicNode target) {
        this.source = source;
        this.target = target;
    }

    public Long getStartNode() {
        return startNode;
    }

    public void setStartNode(Long startNode) {
        this.startNode = startNode;
    }

    public Long getEndNode() {
        return endNode;
    }

    public void setEndNode(Long endNode) {
        this.endNode = endNode;
    }

    public PropertiesFilter getProperties_filter() {
        return properties_filter;
    }

    public void setProperties_filter(PropertiesFilter properties_filter) {
        this.properties_filter = properties_filter;
    }

    public ESFilter getEs_filter() {
        return es_filter;
    }

    public void setEs_filter(ESFilter es_filter) {
        this.es_filter = es_filter;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public BasicNode getSource() {
        return source;
    }

    @Override
    public void setSource(BasicNode source) {
        this.source = source;
    }

    @Override
    public BasicNode getTarget() {
        return target;
    }

    @Override
    public void setTarget(BasicNode target) {
        this.target = target;
    }

    @Override
    public Long getAdded() {
        return added;
    }

    @Override
    public void setAdded(Long added) {
        this.added = added;
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
