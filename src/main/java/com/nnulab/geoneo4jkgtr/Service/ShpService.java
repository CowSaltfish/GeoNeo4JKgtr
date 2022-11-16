package com.nnulab.geoneo4jkgtr.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author : LiuXianYu
 * @date : 2022/11/14 22:14
 */
public interface ShpService {

    JSONObject ToGeoJson(String sourcePath, String targetPath);
}
