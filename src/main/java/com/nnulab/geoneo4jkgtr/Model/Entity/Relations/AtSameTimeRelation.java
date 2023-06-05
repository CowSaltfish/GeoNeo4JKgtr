package com.nnulab.geoneo4jkgtr.Model.Entity.Relations;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Fault;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Stratum;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2023/1/5 17:40
 */
@RelationshipEntity(type = "ATSAMETIME")
public class AtSameTimeRelation extends ScenarioRelation implements BasicRelationInterface {
    @Property
    private String relationName = "同时发生";

    public AtSameTimeRelation() {

    }

    public <T extends GeoNode> AtSameTimeRelation(T source, Stratum target) {
        super(source, target);
    }

    public <T extends GeoNode> AtSameTimeRelation(T source, Fault target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }

}
