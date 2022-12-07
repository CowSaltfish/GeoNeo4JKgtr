package com.nnulab.geoneo4jkgtr.Model.Entity;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Vertex;
import lombok.Data;

/**
 * @author : LiuXianYu
 * @date : 2022/11/30 21:35
 */
@Data
public class Attitude extends Vertex {
    private double trend;

    private double dipDirection;

    private double dip;
}
