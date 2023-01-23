package com.nnulab.geoneo4jkgtr.Controller;

import com.nnulab.geoneo4jkgtr.Model.Entity.Nodes.GeoEvent;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SubjectRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.KGService;
import com.nnulab.geoneo4jkgtr.Service.KnowledgeReasoningService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2023/1/8 22:01
 */
@RestController
@RequestMapping("/knowledgeReasoning")
public class KnowledgeReasoningController {

    @Resource
    private KnowledgeReasoningService knowledgeReasoningService;
    @Resource
    private KGService kgService;

    /**
     * 地质事件类型解析
     *
     * @param ontologyName 地质事件本体名
     * @return 查询结果
     */
    @CrossOrigin
    @GetMapping("/geologicalEventTypesAnalysis")
    public KnowledgeGraph geologicalEventTypesAnalysis (@RequestBody String ontologyName) {
        if (StringUtils.isAnyBlank(ontologyName)) {
            return null;
        }
        //查询地质事件相关地质要素
        KnowledgeGraph knowledgeGraph = kgService.searchByOntology(ontologyName);
        //创建地质事件实体，存入库中，并构建地质要素-事件实体关系
        GeoEvent geoEvent;
        List<GeoEvent> geoEvents;
//        if (knowledgeGraph.getEvents().containsKey(face.getNodeName()))
//            saveRelation(new SubjectRelation(geoMap.getEvents().get(face.getNodeName()), face));
//        else if (!(geoEvents = findGeoEventBySubjectName(face.getNodeName())).isEmpty()) {
//            saveRelation(new SubjectRelation(geoEvents.get(0), face));
//        } else {
//            geoEvent = new GeoEvent(face);
//            geoMap.addEvent(geoEvent);
//            saveNode(geoEvent);
//            saveRelation(new SubjectRelation(geoEvent, face));
//            }
        //返回图谱对象中添加地质事件实体和地质要素-事件实体关系

        return knowledgeGraph;
    }
}
