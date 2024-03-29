package com.nnulab.geoneo4jkgtr.Model.Entity.Relations;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 18:53
 */
@RelationshipEntity(type = "MUTUALLYCUTTING")
public class MutuallyCuttingRelation extends ScenarioRelation implements BasicRelationInterface {

    @Property
    private String relationName = "相交";

    @Property
    private String relationName_en = "Mutually Cutting";

    public MutuallyCuttingRelation() {

    }

    public <T extends GeoNode> MutuallyCuttingRelation(T source, Fault target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
