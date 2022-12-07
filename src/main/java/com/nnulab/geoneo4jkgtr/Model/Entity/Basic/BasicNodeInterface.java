package com.nnulab.geoneo4jkgtr.Model.Entity.Basic;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:44
 */
public interface BasicNodeInterface {

    Long getId();

    void setId(Long id);

    int getFid();

    void setFid(int fid);

    String getNodeName();

    void setNodeName(String nodeName);

    Long getAdded();

    void setAdded(Long added);
}
