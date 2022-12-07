package com.nnulab.geoneo4jkgtr.Model.Entity.Relations;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 19:15
 */
@RelationshipEntity(type = "CUTTINGOFF")
public class CuttingOffRelation  extends ScenarioRelation implements BasicRelationInterface {
    @Property
    private String relationName = "截断";

    public CuttingOffRelation() {

    }

    public <T extends GeoNode> CuttingOffRelation(T source, Fault target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
