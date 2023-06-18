package com.nnulab.geoneo4jkgtr.Controller;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 导入地图数据
     * @param file
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/upload")
    public Object uploadFile(@RequestParam("file") MultipartFile file){
        //todo:上传到geoserver服务器

        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf('.'));
//        String newFileName = new Date().getTime() + suffix;
//        String path = "D:\\13222\\Desktop\\DesktopFiles\\ExperimentData\\MyProject\\KGTR\\GeoNeo4JKgtr\\src\\main\\resources\\static\\shp";
//        String path = "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Project\\GeoNeo4jKgtr\\src\\main\\resources\\static\\shp";
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\shp";
//        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static/shp";
        File newFile = new File(path +"/"+ fileName);
        try {
            file.transferTo(newFile);//复制shp文件

            JSONObject jsObject = new JSONObject();
            jsObject.put("shpPath", path +"\\"+ fileName);

            return jsObject;
        }
        catch (Exception e){
            e.printStackTrace();
            return new JSONObject();
        }
    }

    /**
     * 导入地质年代表
     * @param file 地质年代表文件
     * @return 是否成功
     */
    @CrossOrigin
    @PostMapping(value = "/import-stratigraphic-time-table")
    public void uploadStratigraphicTimeTable(@RequestParam("file") MultipartFile file){
        String fileName = "StratigraphicTimeTable.csv";
        //todo:存入mongodb中

        //存在本地文件夹中
        String path = "E:\\Users\\LiuXianyu\\Documents\\ExperimentData\\myProject\\GraduationThesis\\Project\\GeoNeo4jKgtr\\src\\main\\resources\\static";
        File newFile = new File(path +"/"+ fileName);//复制csv文件
        try {
            file.transferTo(newFile);
            JSONObject jsObject = new JSONObject();
            jsObject.put("StratigraphicTimeTablePath", path +"\\"+ fileName);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
