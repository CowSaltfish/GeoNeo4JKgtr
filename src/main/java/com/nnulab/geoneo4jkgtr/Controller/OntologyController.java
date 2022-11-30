package com.nnulab.geoneo4jkgtr.Controller;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import com.nnulab.geoneo4jkgtr.Model.request.KGCreateRequest;
import com.nnulab.geoneo4jkgtr.Service.OntologyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2022/11/29 20:37
 */
@RestController
@RequestMapping("/ontology")
public class OntologyController {

    @Resource
    private OntologyService ontologyService;

    @CrossOrigin
    @PostMapping("/create")
    public KnowledgeGraph create(@RequestBody String ontologyJson) {
        if (StringUtils.isAnyBlank(ontologyJson)) {
            return null;
        }
        return ontologyService.create(ontologyJson);
    }

    @CrossOrigin
    @PostMapping("/searchByName")
    public KnowledgeGraph searchByName(@RequestBody String ontologyName) {
        if (StringUtils.isAnyBlank(ontologyName)) {
            return null;
        }
        return ontologyService.searchByName(ontologyName);
    }

    @CrossOrigin
    @PostMapping("/editByName")
    public KnowledgeGraph editByName(@RequestBody String ontologyName, @RequestBody String ontologyJson) {
        if (StringUtils.isAnyBlank(ontologyName, ontologyJson)) {
            return null;
        }
        return ontologyService.editByName(ontologyName, ontologyJson);
    }



}
