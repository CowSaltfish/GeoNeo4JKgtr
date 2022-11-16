package com.nnulab.geoneo4jkgtr.Entity.Relationship;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Face;
import com.nnulab.geoneo4jkgtr.Entity.Fault;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/15 16:28
 */
@RelationshipEntity(type = "SUBJECT")
public class SubjectRelation  extends BasicRelation implements BasicRelationInterface {
    @Property
    private String relationName = "发生于";

    public SubjectRelation() {

    }

    public <T extends GeoNode> SubjectRelation(T source, Face target) {
        super(source, target);
    }

    public <T extends GeoNode> SubjectRelation(T source, Fault target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
