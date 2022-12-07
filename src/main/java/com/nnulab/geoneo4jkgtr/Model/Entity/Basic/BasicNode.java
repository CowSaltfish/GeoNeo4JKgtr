package com.nnulab.geoneo4jkgtr.Model.Entity.Basic;

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

}
