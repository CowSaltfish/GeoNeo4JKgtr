package com.nnulab.geoneo4jkgtr.Model.Entity.Basic;

import com.nnulab.geoneo4jkgtr.Model.Entity.Attitude;
import com.nnulab.geoneo4jkgtr.Model.Entity.Enum.GeometryForm;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Vertex;
import lombok.Data;
import org.neo4j.ogm.annotation.Properties;
import org.neo4j.ogm.annotation.Property;

import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2022/11/30 21:17
 */
@Data
public class ScenarioNode extends BasicNode {

    /**
     * 位置或中心位置(x,y,z)
     * 如果使用引用类型，无法作为属性添加到节点上
     */
//    @Property
    private Vertex positionPoint;
    /**
     * 位置或中心位置(x,y,z)
     */
    @Property
    private double[] position = new double[2];
    /**
     * 四至
     */
    @Property
    private double[] envelope = new double[4];
    /**
     * 类型
     */
    @Property
    private String type;
    /**
     * 几何形态
     */
    @Property
    private GeometryForm geometryForm = GeometryForm.BEND;
    /**
     * 长度
     */
    @Property
    private double length;
    /**
     * 面积
     */
    @Property
    private double area;
    /**
     * 产状
     */
//    @Property
    private Attitude attitude;
    /**
     * 年代
     */
    @Property
    private String times;

    /**
     * 理化属性（主要是属性表中的属性）
     */
    @Properties
    private Map<String, Object> Attribute;

    public ScenarioNode() {
    }

//    @Relationship(type = "SCENARIO_RELATION", direction = Relationship.UNDIRECTED)
//    private Set<ScenarioRelation> scenarioRelations;

}
