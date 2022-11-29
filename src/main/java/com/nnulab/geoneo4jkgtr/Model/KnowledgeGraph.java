package com.nnulab.geoneo4jkgtr.Model;

import com.nnulab.geoneo4jkgtr.Entity.Basic.BasicRelation;
import lombok.Data;

import java.util.List;

@Data
public class KnowledgeGraph {

    List<Object> nodes;
    List<BasicRelation> relationships;

}
