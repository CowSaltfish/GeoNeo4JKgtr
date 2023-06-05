package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Stratum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2023/3/15 15:52
 */
public interface FaceService {

    public List<Stratum> findSedimentaryRockArea();
}
