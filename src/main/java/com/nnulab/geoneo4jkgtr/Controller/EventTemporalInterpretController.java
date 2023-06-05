package com.nnulab.geoneo4jkgtr.Controller;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Service.EventTemporalInterpretService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2023/4/25 15:45
 */
@RestController
@RequestMapping("/temporal")
public class EventTemporalInterpretController {

    @Resource
    private EventTemporalInterpretService eventTemporalInterpretService;

    /**
     * 地质关系创建
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/create-geological-relationship")
    public KnowledgeGraph createGeologicalRelationship() {
        return eventTemporalInterpretService.createGeologicalRelation();
    }

    /**
     * 时间关系创建
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/create-temporal-relationship")
    public KnowledgeGraph createTemporalRelationship() {
        return eventTemporalInterpretService.inferTemporalRelationshipOfFaultsAndStrata();
    }

    /**
     * 时间区间解析
     * todo 返回表格
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/parse-temporal-interval")
    public KnowledgeGraph parseTemporalIntervalOfEvent() {
        return eventTemporalInterpretService.parseTemporalIntervalOfEvent();
    }

    /**
     * 冲突检测与校正
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/detect-correct-conflict")
    public KnowledgeGraph detectAndCorrectTemporalConflict() {
        return eventTemporalInterpretService.detectAndCorrectTemporalConflict();
    }

    /**
     * 地质事件时间序列生成
     *
     * @return
     */
    @CrossOrigin
    @GetMapping("/generate-event-sequence")
    public KnowledgeGraph generateGeoEventSequence() {
        return eventTemporalInterpretService.generateGeoEventSequence();
    }
}
