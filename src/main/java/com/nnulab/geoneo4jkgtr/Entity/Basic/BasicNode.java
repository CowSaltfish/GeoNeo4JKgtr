package com.nnulab.geoneo4jkgtr.Entity.Basic;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:44
 */
@NodeEntity
@Data
public class BasicNode implements BasicNodeInterface{

    @Property
    private String labelName;

    @GraphId
    private Long id;

    @Property
    private int fid;

    @Property
    private String nodeName;

    @Property
    private Long added = new Date().getTime();

    @Relationship(direction = Relationship.OUTGOING)
    private Set<BasicRelation> outGoing;

    @Relationship(direction = Relationship.INCOMING)
    private Set<BasicRelation> inComing;

    public BasicNode() {
    }

//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public int getFid() {
//        return fid;
//    }
//
//    public void setFid(int fid) {
//        this.fid = fid;
//    }
//
//    @Override
//    public String getNodeName() {
//        return nodeName;
//    }
//
//    public void setNodeName(String nodeName) {
//        this.nodeName = nodeName;
//    }
//
//    @Override
//    public Long getAdded() {
//        return added;
//    }
//
//    public void setAdded(Long added) {
//        this.added = added;
//    }
//
//    public Set<BasicRelation> getOutGoing() {
//        return outGoing;
//    }
//
//    public void setOutGoing(Set<BasicRelation> outGoing) {
//        this.outGoing = outGoing;
//    }
//
//    public Set<BasicRelation> getInComing() {
//        return inComing;
//    }
//
//    public void setInComing(Set<BasicRelation> inComing) {
//        this.inComing = inComing;
//    }
}
