package com.nnulab.geoneo4jkgtr.Controller;

import com.alibaba.fastjson.JSONObject;
import com.nnulab.geoneo4jkgtr.Model.request.ToGeoJsonRequest;
import com.nnulab.geoneo4jkgtr.Service.ShpService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/shp")
public class ShpController {

    @Resource
    private ShpService shpService;

    @GetMapping("/toGeoJson")
    public JSONObject ToGeoJson(@RequestBody ToGeoJsonRequest toGeoJsonRequest) {
        if (toGeoJsonRequest == null) {
            return null;
        }
//        String sourcePath = toGeoJsonRequest.getSourcePath();
//        String targetPath = toGeoJsonRequest.getTargetPath();
        String sourcePath = "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\stratas.shp";
        String targetPath =  "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Data\\StudyData\\StudyData\\geojson\\strata.json";
        if (StringUtils.isAnyBlank(sourcePath, targetPath)) {
            return null;
        }
        JSONObject geoJson = shpService.ToGeoJson(sourcePath, targetPath);
        return geoJson;
    }
}
