package com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 18:51
 */
@RelationshipEntity(type = "ADJACENT")
public class AdjacentRelation extends ScenarioRelation implements BasicRelationInterface {
    @Property
    private String relationName = "相邻";

    @Property
    private String relationName_en = "Adjacent";

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
