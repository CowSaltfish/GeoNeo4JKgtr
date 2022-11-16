package com.nnulab.geoneo4jkgtr.Controller;

import com.alibaba.fastjson.JSONObject;
import com.nnulab.geoneo4jkgtr.Model.GeoMap;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Model.request.KGCreateRequest;
import com.nnulab.geoneo4jkgtr.Service.KGService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 15:19
 */
@RestController
@RequestMapping("/kg")
public class KGController {

    @Resource
    private KGService kgService;

    @PostMapping("/create")
    public void createKG(@RequestBody KGCreateRequest kgCreateRequest) {
        if (kgCreateRequest == null) {
            return;
        }
        String facePath = kgCreateRequest.getFacePath();
        String boundaryPath = kgCreateRequest.getBoundaryPath();
        if (StringUtils.isAnyBlank(facePath, boundaryPath)) {
            return;
        }
        //节点生成
        kgService.createKG(facePath, boundaryPath);
        //构建要素时空关系(邻接、方向、距离)、角度
        kgService.CreateTopoBetweenFacesFromBoundaries();
    }

    @GetMapping("/searchAllKG")
    public KnowledgeGraph searchAllKG() {
        //返回知识图谱全要素
        return kgService.searchAllKG();
    }

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层切割关系
     */
    public void inferCuttingThroughRelationOnFaults() {
        kgService.inferCuttingThroughRelationOnFaults();
    }

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层截断关系
     */
    public void inferCuttingOffRelationOnFaults() {
        kgService.inferCuttingOffRelationOnFaults();
    }

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层相交关系
     */
    public void inferMutuallyCuttingRelationOnFaults() {
        kgService.inferMutuallyCuttingRelationOnFaults();
    }

    /**
     * 基于断层地质关系，推断断层发育时间
     */
    public void inferTimeSeriesOfFaults() {
        kgService.inferTimeSeriesOfFaults();
    }

    public void inferGeoRelationshipBetweenStrata() {
        if (0 != kgService.getFaceCount() && 0 != kgService.getBoundaryCount()) {
            //添加地层接触关系
            kgService.inferContactRelationshipOnStrata();

        }
        //生成断层与面的拓扑关系
        if (0 != kgService.getFaultCount() && 0 != kgService.getFaceCount()) {
            //添加断层切割面关系
            kgService.inferCuttingBetweenFaultsAndFaces();
        }
    }

    public void inferTimeSeriesOfStrata() {
        if (0 != kgService.getFaceCount()) {
            kgService.inferTimeSeriesOfStrata();
        }
    }

    public void inferTimeOfIntrusionByStrata() {
        if (0 != kgService.getFaceCount()) {
            kgService.inferTimeOfIntrusionByStrata();
        }
    }

    public void inferTimeOfFaultsByStrata() {
        if (0 != kgService.getFaultCount() && 0 != kgService.getFaceCount()) {
            kgService.inferTimeOfFaultsByStrata();
        }
    }

    public void clearAll() {
        kgService.clearAll();
    }


}
