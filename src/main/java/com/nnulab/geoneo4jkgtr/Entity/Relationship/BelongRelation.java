package com.nnulab.geoneo4jkgtr.Entity.Relationship;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelationInterface;
import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Boundary;
import com.nnulab.geoneo4jkgtr.Entity.Face;
import com.nnulab.geoneo4jkgtr.Entity.Fault;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 18:50
 */
@RelationshipEntity(type = "BELONG")
public class BelongRelation extends BasicRelation implements BasicRelationInterface {
    @Property
    private String relationName = "属于";

    private String belongType = "others";//剖面图——top:边界为地层上边界，bottom:边界为地层下边界；平面图——outer:边界为广义环状地层的外边界，inner:边界为环状地层的内边界

    public BelongRelation() {

    }

    public <T extends GeoNode> BelongRelation(T source, Fault target) {
        super(source, target);
    }

    public <T extends GeoNode> BelongRelation(T source, Face target) {
        super(source, target);

    }

    @Override
    public String getRelationName() {
        return relationName;
    }

    public BelongRelation setBelongType(String type) {
        if ("top".equals(type) || "bottom".equals(type) || "outer".equals(type) || "inner".equals(type))
            belongType = type;
        return this;
    }
}
