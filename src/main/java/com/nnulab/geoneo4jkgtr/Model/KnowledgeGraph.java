package com.nnulab.geoneo4jkgtr.Model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import lombok.Data;
//import org.hibernate.validator.internal.xml.FieldType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
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

    @JSONField(name="loops")
    private List<List<String>> loops = new ArrayList<>();

    public List<Integer> getNodesFidList() {
        List<Integer> Ids = new ArrayList<>();
        for (Object boundary : this.getNodes()) {
            Long fid = ((HashMap<String, Long>) boundary).get("fid");
            Ids.add(fid.intValue());
        }
        return Ids;
    }

    public void addLoops(List<List<String>> newLoops){
        loops.addAll(newLoops);
    }

}
