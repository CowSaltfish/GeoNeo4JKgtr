package com.nnulab.geoneo4jkgtr.Model.Entity.Basic;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lombok.Data;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.id.UuidStrategy;
import org.neo4j.ogm.typeconversion.UuidStringConverter;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:44
 */
@NodeEntity
@Data
public class BasicNode implements BasicNodeInterface {

    @Property
    private String labelName;

    @Id
    @GeneratedValue
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
