package com.nnulab.geoneo4jkgtr.Entity.Relationship;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Fault;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 18:51
 */
@RelationshipEntity(type = "CUTTINGTHROUGH")
public class CuttingThroughRelation extends BasicRelation implements BasicRelationInterface {
    @Property
    private String relationName = "相交";

    public CuttingThroughRelation() {

    }

    public <T extends GeoNode> CuttingThroughRelation(T source, Fault target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
