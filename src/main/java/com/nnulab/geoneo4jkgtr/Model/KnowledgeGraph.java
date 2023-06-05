package com.nnulab.geoneo4jkgtr.Model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import lombok.Data;
//import org.hibernate.validator.internal.xml.FieldType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
public class KnowledgeGraph {

    @Id
    @Field("_id")
    @JsonIgnore
    private String id;

    @JSONField(name="name")
    private String name;

    @JsonIgnore
    private boolean isOntology;

    @JSONField(name="nodes")
    private List<Object> nodes;

    @JSONField(name="relationships")
    private List<ScenarioRelation> relationships;

}
