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

    @CrossOrigin
    @GetMapping("/toGeoJson")
    public JSONObject ToGeoJson(@RequestParam("fullShpPath") String fullShpPath, @RequestParam("fullGeoPath") String fullGeoPath) {
        System.out.println(fullShpPath);
        System.out.println(fullGeoPath);

        String[] shpArr = fullShpPath.split("/");
        String sourcePath = String.join("\\", shpArr);
        String[] geoArr = fullGeoPath.split("/");
        String targetPath = String.join("\\", geoArr);

        if (StringUtils.isAnyBlank(sourcePath, targetPath)) {
            return null;
        }
        return shpService.ToGeoJson(sourcePath, targetPath);

    }

}
