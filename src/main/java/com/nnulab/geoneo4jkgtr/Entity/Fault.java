package com.nnulab.geoneo4jkgtr.Entity;

import com.nnulab.geoneo4jkgtr.Entity.Relationship.BelongRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.CuttingOffRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.CuttingThroughRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.MutuallyCuttingRelation;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : LiuXianYu
 * @date : 2022/3/29 15:43
 * 断层
 */
@NodeEntity
public class Fault extends Line {

    private String name;
    private String code;
    private HashSet<Boundary> boundaries;

    @Relationship(type = "BELONG", direction = Relationship.INCOMING)
    private Set<BelongRelation> belong;

    @Relationship(type = "CUTTINGTHROUGH", direction = Relationship.OUTGOING)
    private Set<CuttingThroughRelation> cuttingThrough;

    @Relationship(type = "CUTTINGOFF", direction = Relationship.OUTGOING)
    private Set<CuttingOffRelation> cuttingOff;

    @Relationship(type = "MUTUALLYCUTTING", direction = Relationship.OUTGOING)
    private Set<MutuallyCuttingRelation> mutuallyCutting;

    public Fault() {
        boundaries = new HashSet<>();
        setLabelName("Fault");
    }

    public Fault(Boundary boundary) {
        setId(boundary.getId());
        setVertices(boundary.getVertices());
        boundaries = new HashSet<>();
        boundaries.add(boundary);
        setLabelName("Fault");
    }

    public HashSet<Boundary> getBoundaries() {
        return boundaries;
    }

    public void addBoundary(Boundary boundary) {
        this.boundaries.add(boundary);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.setNodeName(name);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<BelongRelation> getBelong() {
        return belong;
    }

    public void setBelong(Set<BelongRelation> belong) {
        this.belong = belong;
    }

    public Set<CuttingThroughRelation> getCuttingThrough() {
        return cuttingThrough;
    }

    public void setCuttingThrough(Set<CuttingThroughRelation> cuttingThrough) {
        this.cuttingThrough = cuttingThrough;
    }

    public Set<CuttingOffRelation> getCuttingOff() {
        return cuttingOff;
    }

    public void setCuttingOff(Set<CuttingOffRelation> cuttingOff) {
        this.cuttingOff = cuttingOff;
    }

    public Set<MutuallyCuttingRelation> getMutuallyCutting() {
        return mutuallyCutting;
    }

    public void setMutuallyCutting(Set<MutuallyCuttingRelation> mutuallyCutting) {
        this.mutuallyCutting = mutuallyCutting;
    }
}
