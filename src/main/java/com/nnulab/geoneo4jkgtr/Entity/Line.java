package com.nnulab.geoneo4jkgtr.Entity;

import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
