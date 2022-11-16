package com.nnulab.geoneo4jkgtr.Service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.nnulab.geoneo4jkgtr.Service.ShpService;
import com.nnulab.geoneo4jkgtr.Util.GdalUtil;
import com.nnulab.geoneo4jkgtr.Util.ZipUtil;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ShpServiceImpl implements ShpService {

    /**
     *
     * @param sourcePath zip文件路径
     * @param targetPath
     * @return
     */
    @Override
    public JSONObject ToGeoJson(String sourcePath, String targetPath) {
        try {
            File zipFile = new File(sourcePath);
            ZipUtil.unZip(sourcePath, sourcePath);//解压zip
            GdalUtil.Shp2GeoJson(sourcePath, targetPath);

            FileInputStream fileInputStream = new FileInputStream(targetPath);
            JSONObject geoJson = JSONObject.parseObject(new String(fileInputStream.readAllBytes()));
            return geoJson;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
