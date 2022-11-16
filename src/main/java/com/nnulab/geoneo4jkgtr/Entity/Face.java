package com.nnulab.geoneo4jkgtr.Entity;

import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Enum.StratumType;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.BelongRelation;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author : LiuXianYu
 * @date : 2022/3/29 15:50
 * 面要素
 */
@NodeEntity
@Data
public class Face extends GeoNode {

    private List<Vertex> vertices = new ArrayList<>();
    private StratumType type;
    private double center_y = -1.0;
    private double area;
    //minX,maxX,minY,maxY
    private double[] envelope = new double[4];
    //长短轴比例
    private double ratioLongShortAxis;

    @Relationship(type = "ADJACENT", direction = Relationship.UNDIRECTED)
    private Set<AdjacentRelation> adjacent;

    @Relationship(type = "BELONG", direction = Relationship.INCOMING)
    private Set<BelongRelation> belong;

    public Face() {
        setLabelName("Face");
    }

    public void addPoint(Vertex vertex) {
        vertices.add(vertex);
    }

    public double getMinX() {
        return envelope[0];
    }

    public double getMaxX() {
        return envelope[1];
    }

    public double getMinY() {
        return envelope[2];
    }

    public double getMaxY() {
        return envelope[3];
    }

    public double calculateRatioLongShortAxis() {
        double xAxis = Math.abs(envelope[1] - envelope[0]);
        double yAxis = Math.abs(envelope[3] - envelope[2]);
        if (xAxis != 0 && yAxis != 0) {
            double ratio = xAxis / yAxis;
            return ratio > 1 ? ratio : 1 / ratio;
        }
        return 0.0;
    }
}
