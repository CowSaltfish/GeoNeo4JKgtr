package com.nnulab.geoneo4jkgtr.Model;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Boundary;
import com.nnulab.geoneo4jkgtr.Model.Entity.Chronology;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Stratum;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import lombok.Data;
import org.gdal.ogr.Layer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 15:24
 */
@Data
public class GeoMap {

    private int id;
    private Layer stratumLayer;
    private Layer boundaryLayer;
    private ArrayList<Stratum> strata = new ArrayList<>();
    private HashMap<String, GeoEvent> events = new HashMap<>();
    private ArrayList<Boundary> boundaries = new ArrayList<>();
    private Chronology chronology = new Chronology();

    public GeoMap(String facePath, String boundaryPath) {
        //读取地层要素
        stratumLayer = GdalUtil.getLayerByPath(facePath);
        if (null != stratumLayer)
            setStratumLayer(stratumLayer);
        //读取地质界线
        boundaryLayer = GdalUtil.getLayerByPath(boundaryPath);
        if (null != boundaryLayer)
            setBoundaryLayer(boundaryLayer);

        //写入地层年代表
//        chronology.getList().add("P");
//        chronology.getList().add("E");
//        chronology.getList().add("Q");
//        chronology.getList().add("F");
//        chronology.getList().add("R");
//        chronology.getList().add("D");
//        chronology.getList().add("T");
//        chronology.getList().add("U");
//        chronology.getList().add("V");
//        chronology.getList().add("W");
//        chronology.getList().add("O");
//        chronology.getList().add("M");
//        chronology.getList().add("J");
//        chronology.getList().add("K");
//        chronology.getList().add("G");
//        chronology.getList().add("B");
    }

    public void addStratum(Stratum stratum) {
        this.strata.add(stratum);
    }

    public void addEvent(GeoEvent geoEvent) {
        this.events.put(geoEvent.getSubjectName(), geoEvent);
    }

    public void addBoundary(Boundary boundary) {
        this.boundaries.add(boundary);
    }
}
