package com.nnulab.geoneo4jkgtr.Entity.Basic;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:44
 */
public interface BasicNodeInterface {

    Long getId();

    void setId(Long id);

    public int getFid();

    public void setFid(int fid);

    String getNodeName();

    void setNodeName(String nodeName);

    Long getAdded();

    void setAdded(Long added);
}
