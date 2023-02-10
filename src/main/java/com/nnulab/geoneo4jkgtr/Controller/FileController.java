package com.nnulab.geoneo4jkgtr.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {
    @CrossOrigin
    @PostMapping(value = "/upload")
    public void uploadFile(@RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf('.'));
//        String newFileName = new Date().getTime() + suffix;
        String path = "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\GeoNeo4JKgtr\\src\\main\\resources\\static\\shp";
//        String path = "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\data\\StudyData\\StudyData";
//        String path = "static/shp";
        File newFile = new File(path +"/"+ fileName);
        try {
            file.transferTo(newFile);
//            return "成功";
        }
        catch (Exception e){
            e.printStackTrace();
//            return "失败";
        }
    }

}
