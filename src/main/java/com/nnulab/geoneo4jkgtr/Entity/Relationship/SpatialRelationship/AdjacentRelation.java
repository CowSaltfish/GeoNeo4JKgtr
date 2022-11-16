package com.nnulab.geoneo4jkgtr.Entity.Relationship.SpatialRelationship;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Boundary;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 18:51
 */
@RelationshipEntity(type = "ADJACENT")
public class AdjacentRelation extends BasicRelation implements BasicRelationInterface {
    @Property
    private String relationName = "相邻";

    public AdjacentRelation() {

    }

    public <T extends GeoNode> AdjacentRelation(T source, Boundary target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
