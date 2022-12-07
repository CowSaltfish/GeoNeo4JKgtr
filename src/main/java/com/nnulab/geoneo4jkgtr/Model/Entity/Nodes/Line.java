package com.nnulab.geoneo4jkgtr.Model.Entity.Nodes;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.GeoNode;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.ArrayList;

/**
 * @author : LiuXianYu
 * @date : 2022/4/3 16:45
 */
@NodeEntity
@Data
public class Line extends GeoNode {

    private ArrayList<Vertex> vertices;

    public Line() {
        vertices = new ArrayList<>();
        setLabelName("Line");
    }

}
