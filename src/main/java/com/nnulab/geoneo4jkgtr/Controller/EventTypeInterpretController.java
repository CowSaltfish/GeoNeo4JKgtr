package com.nnulab.geoneo4jkgtr.Controller;

import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : LiuXianYu
 * @date : 2023/4/25 15:44
 */
@RestController
@RequestMapping("/type")
public class EventTypeInterpretController {

    /**
     * 地质事件实体建立
     *
     * @return
     */
    @CrossOrigin
    @PostMapping("/infer-normal-geological-events")
    public void inferNormalGeologicalEvents() {

    }

    /**
     * 断裂构造个体识别
     *
     * @return
     */
    @CrossOrigin
    @PostMapping("/infer-fault")
    public void inferFault() {

    }

    /**
     * 断裂构造组合识别
     *
     * @return
     */
    @CrossOrigin
    @PostMapping("/infer-fault-group")
    public void inferFaultGroup() {

    }

    /**
     * 褶皱构造识别
     *
     * @return
     */
    @CrossOrigin
    @PostMapping("/infer-fold")
    public void inferFold() {

    }

    /**
     * 穹窿/盆地/方山构造识别
     *
     * @return
     */
    @CrossOrigin
    @PostMapping("/infer-dome-basin-mesa")
    public void inferDomeBasinMesa() {

    }

}
