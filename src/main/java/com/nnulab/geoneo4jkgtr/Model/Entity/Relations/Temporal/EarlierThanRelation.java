package com.nnulab.geoneo4jkgtr.Model.Entity.Relations.Temporal;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2023/4/18 11:35
 */
@RelationshipEntity(type = "EARLIERTHAN")
public class EarlierThanRelation extends ScenarioRelation implements BasicRelationInterface {
    @Property
    private String relationName = "早于";

    @Property
    private String relationName_en = "earlier than";

    @Property
    private String nodesType = "EARLIERTHAN_SS";

    public EarlierThanRelation() {

    }

    public <T extends GeoNode> EarlierThanRelation(T source, GeoEvent target) {
        super(source, target);
    }

    @Override
    public String getRelationName() {
        return relationName;
    }
}
