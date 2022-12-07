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
    @PostMapping("/save")
    public void save(@RequestBody String name, @RequestBody String ontologyJson) {
        if (StringUtils.isAnyBlank(ontologyJson)) {
            return;
        }
        ontologyService.save(name, ontologyJson);
    }

    @CrossOrigin
    @PostMapping("/findByName")
    public KnowledgeGraph findByName(@RequestBody String ontologyName) {
        if (StringUtils.isAnyBlank(ontologyName)) {
            return null;
        }
        return ontologyService.findByName(ontologyName);
    }

    @CrossOrigin
    @PostMapping("/updateOntology")
    public long updateOntology(@RequestBody String ontologyName, @RequestBody String ontologyJson) {
        if (StringUtils.isAnyBlank(ontologyName, ontologyJson)) {
            return 0;
        }
        return ontologyService.updateOntology(ontologyName, ontologyJson);
    }

    @CrossOrigin
    @PostMapping("/deleteOntologyByName")
    public void deleteOntologyByName(@RequestBody String ontologyName) {
        if (StringUtils.isAnyBlank(ontologyName)) {
            return;
        }
        ontologyService.deleteOntologyByName(ontologyName);
    }

}
