package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.nnulab.geoneo4jkgtr.Dao.FaceDao;
import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.Face;
import com.nnulab.geoneo4jkgtr.Service.FaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2023/3/15 15:53
 */
@Service
public class FaceServiceImpl implements FaceService {

    @Resource
    private FaceDao faceDao;

    @Override
    public List<Face> findSedimentaryRockArea() {
        return faceDao.findSedimentaryRockArea();
    }
}
