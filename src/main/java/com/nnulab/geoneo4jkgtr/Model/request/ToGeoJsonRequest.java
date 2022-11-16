package com.nnulab.geoneo4jkgtr.Model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 实例知识图谱创建请求体
 *
 * @author laoniu
 */
@Data
public class ToGeoJsonRequest implements Serializable {

    private static final long serialVersionUID = 1167006973621551056L;

    private String sourcePath;

    private String targetPath;

}
