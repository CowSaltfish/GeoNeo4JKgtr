package com.nnulab.geoneo4jkgtr.Service;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;

/**
 * @author : LiuXianYu
 * @date : 2023/5/10 11:09
 */
public interface EventTemporalInterpretService {


    KnowledgeGraph createGeologicalRelation();

    KnowledgeGraph inferTemporalRelationshipOfFaultsAndStrata();

    KnowledgeGraph parseTemporalIntervalOfEvent();

    KnowledgeGraph detectAndCorrectTemporalConflict();

    KnowledgeGraph generateGeoEventSequence();

}
