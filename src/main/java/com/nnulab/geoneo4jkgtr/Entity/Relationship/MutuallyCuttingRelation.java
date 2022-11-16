package com.nnulab.geoneo4jkgtr.Entity.Relationship;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicNode;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Fault;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 18:53
 */
@RelationshipEntity(type = "MUTUALLYCUTTING")
public class MutuallyCuttingRelation extends BasicRelation implements BasicRelationInterface {

    @Property
    private String relationName = "相交";

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
