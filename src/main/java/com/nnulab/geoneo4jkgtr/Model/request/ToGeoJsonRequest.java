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

    private static final long serialVersionUID = -6489399826090075599L;

    private String sourcePath;

    private String targetPath;

}
