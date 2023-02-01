package com.nnulab.geoneo4jkgtr.Model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiuXianYu
 * @date : 2022/12/8 19:37
 */
@Data
public class OntologyRequest implements Serializable {
    private static final long serialVersionUID = -7851974448652861121L;
    private String name;
    private String ontologyJson;
}
