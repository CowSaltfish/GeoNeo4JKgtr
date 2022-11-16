package com.nnulab.geoneo4jkgtr.Model;

import com.nnulab.geoneo4jkgtr.Entity.Boundary;
import com.nnulab.geoneo4jkgtr.Entity.Face;
import com.nnulab.geoneo4jkgtr.Entity.GeoEvent;
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
    private Layer faceLayer;
    private Layer boundaryLayer;
    private ArrayList<Face> faces = new ArrayList<>();
    private HashMap<String, GeoEvent> events = new HashMap<>();
    private ArrayList<Boundary> boundaries = new ArrayList<>();

    public GeoMap(String facePath, String boundaryPath) {
        //读取面要素
        faceLayer = GdalUtil.getLayerByPath(facePath);
        if (null != faceLayer)
            setFaceLayer(faceLayer);
        //读取地质界线
        boundaryLayer = GdalUtil.getLayerByPath(boundaryPath);
        if (null != boundaryLayer)
            setBoundaryLayer(boundaryLayer);
    }

    public void addFace(Face face) {
        this.faces.add(face);
    }

    public void addEvent(GeoEvent geoEvent) {
        this.events.put(geoEvent.getSubjectName(), geoEvent);
    }

    public void addBoundary(Boundary boundary) {
        this.boundaries.add(boundary);
    }
}
