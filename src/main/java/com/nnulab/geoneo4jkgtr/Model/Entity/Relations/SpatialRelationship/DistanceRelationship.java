package com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/11/12 22:21
 */
@RelationshipEntity(type = "DISTANCE")
public class DistanceRelationship extends ScenarioRelation implements BasicRelationInterface {
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
