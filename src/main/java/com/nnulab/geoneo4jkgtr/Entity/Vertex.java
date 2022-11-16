package com.nnulab.geoneo4jkgtr.Entity;

import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author : LiuXianYu
 * @date : 2022/3/29 15:49
 */
@NodeEntity
@Data
public class Vertex extends GeoNode {
    private double x;
    private double y;
    private double z;

    public Vertex() {
        setLabelName("Vertex");
    }

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
        setLabelName("Vertex");
    }

    public Vertex(double x, double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        setLabelName("Vertex");
    }
}
