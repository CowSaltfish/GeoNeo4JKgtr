package com.nnulab.geoneo4jkgtr.Entity.Relationship.SpatialRelationship;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Boundary;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/11/12 22:21
 */
@RelationshipEntity(type = "ORIENTATION")
public class OrientateRelationship extends BasicRelation implements BasicRelationInterface {
    @Property
    private String relationName = "方向";

    public OrientateRelationship() {

    }

    public <T extends GeoNode> OrientateRelationship(T source, Boundary target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
