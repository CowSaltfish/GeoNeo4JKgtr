package com.nnulab.geoneo4jkgtr.Entity.Basic;

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
    private Long sourceId;
    @Property
    private Long targetId;

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

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public BasicRelation(BasicNode source, BasicNode target) {
        this.source = source;
        this.target = target;
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
