package com.nnulab.geoneo4jkgtr.Model.Entity.Basic;

import com.nnulab.geoneo4jkgtr.Model.Entity.Enum.Topology;
import lombok.Data;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2022/11/30 22:33
 */
@Data
@RelationshipEntity(type = "SCENARIO_RELATION")
public class ScenarioRelation extends BasicRelation {

    /**
     * 距离
     */
    @Property
    private double direction;
    /**
     * 方向
     */
    @Property
    private double distance;
    /**
     * 拓扑
     */
    @Property
    private Topology topology;
    /**
     * 关系类型
     */
//    @Property
//    private String relationName;

    //TODO:夹角

    /**
     * 时间先后
     */
    @Property
    private double directionOfTime;
    /**
     * 时间远近
     */
    @Property
    private double distanceOfTime;
    /**
     * 时间连续性
     */
    @Property
    private String topologyOfTime;

    /**
     * 属性
     */
    private Map<String, Object> properties = new HashMap<>();

    public ScenarioRelation() {
    }

    public <T extends GeoNode> ScenarioRelation(T source, T target) {
        super(source, target);
    }
}
