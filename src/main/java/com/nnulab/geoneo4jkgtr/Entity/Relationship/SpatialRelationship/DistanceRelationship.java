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
@RelationshipEntity(type = "DISTANCE")
public class DistanceRelationship extends BasicRelation implements BasicRelationInterface {
    @Property
    private String relationName = "距离";

    public DistanceRelationship() {

    }

    public <T extends GeoNode> DistanceRelationship(T source, Boundary target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
