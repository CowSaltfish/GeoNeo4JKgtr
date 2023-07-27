package com.nnulab.geoneo4jkgtr.Controller;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Model.request.KGCreateRequest;
import com.nnulab.geoneo4jkgtr.Service.EventTemporalInterpretService;
import com.nnulab.geoneo4jkgtr.Service.KGService;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2022/4/1 15:19
 */
@RestController
@RequestMapping("/kg")
public class KGController {

    @Resource
    private KGService kgService;
    @Resource
    private EventTemporalInterpretService eventTemporalInterpretService;
    @Resource
    private OntologyService ontologyService;

    /**
     * 构建初始地质知识图谱
     * @param kgCreateRequest
     * @return
     */
    @CrossOrigin
    @PostMapping("/create")
    public KnowledgeGraph create(@RequestBody KGCreateRequest kgCreateRequest) {
        if (kgCreateRequest == null) {
            return null;
        }
        String facePath = kgCreateRequest.getFacePath();
        String boundaryPath = kgCreateRequest.getBoundaryPath();
        if (StringUtils.isAnyBlank(facePath, boundaryPath)) {
            return null;
        }
        kgService.create(facePath, boundaryPath);
        return kgService.searchAllKG();
//        return null;
    }

    @CrossOrigin
    @GetMapping("/searchAllKG")
    public KnowledgeGraph searchAllKG() {
        //返回知识图谱全要素
        return kgService.searchAllKG();
    }

    /**
     * cypher查询
     *
     * @param cypher
     * @return
     */
    @CrossOrigin
    @GetMapping("/search")
    public KnowledgeGraph search(@RequestParam String cypher) {
        //根据本体查询相应知识图谱
        return kgService.search(cypher);
    }

    /**
     * 本体查询
     * todo 改成根据地理场景查询
     *
     * @param ontologyName 本体名
     * @return 查询结果
     */
    @CrossOrigin
    @GetMapping("/searchByOntology")
    public KnowledgeGraph searchByOntology(@RequestBody String ontologyName) {
        if (StringUtils.isAnyBlank(ontologyName)) {
            return null;
        }
        return kgService.searchByOntology(ontologyName);
    }

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层切割关系
     */
    public void inferCuttingThroughRelationOnFaults() {
        kgService.createCuttingThroughRelationOnFaults();
    }

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层截断关系
     */
    public void inferCuttingOffRelationOnFaults() {
        kgService.createCuttingOffRelationOnFaults();
    }

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层相交关系
     */
    public void inferMutuallyCuttingRelationOnFaults() {
        kgService.createMutuallyCuttingRelationOnFaults();
    }

    /**
     * 基于断层地质关系，推断断层发育时间
     */
    public void inferTimeSeriesOfFaults() {
        eventTemporalInterpretService.inferTemporalRelationshipOfFaultsAndStrata();
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

    @CrossOrigin
    @GetMapping("/clearAll")
    public void clearAll() {
        kgService.clearAll();
    }


}
