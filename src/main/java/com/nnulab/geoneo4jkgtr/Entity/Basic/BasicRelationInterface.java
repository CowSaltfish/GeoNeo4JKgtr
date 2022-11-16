package com.nnulab.geoneo4jkgtr.Entity.Basic;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 16:46
 */
public interface BasicRelationInterface {
    BasicNode getSource();

    void setSource(BasicNode source);

    BasicNode getTarget();

    void setTarget(BasicNode target);

    Long getId();

    void setId(Long id);

    String getRelationName();

    Long getAdded();

    void setAdded(Long added);
}
