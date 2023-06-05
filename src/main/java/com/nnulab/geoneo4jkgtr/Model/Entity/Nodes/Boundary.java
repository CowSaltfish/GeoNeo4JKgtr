package com.nnulab.geoneo4jkgtr.Model.Entity.Nodes;

import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.BelongRelation;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
//import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : LiuXianYu
 * @date : 2022/3/29 15:49
 */
@NodeEntity
public class Boundary extends Line {

    public HashSet<Stratum> adjStratum;

    public HashSet<Boundary> adjBoundary;

    @Relationship(type = "ADJACENT")
    private Set<AdjacentRelation> adjacent;

    @Relationship(type = "BELONG", direction = Relationship.OUTGOING)
    private Set<BelongRelation> belong;

    public Boundary() {
        super();
        adjStratum = new HashSet<>();
        adjBoundary = new HashSet<>();
        setLabelName("Boundary");
    }

    public HashSet<Stratum> getAdjStratum() {
        return adjStratum;
    }

    public void setAdjFace(HashSet<Stratum> adjFace) {
        this.adjStratum = adjFace;
    }

    public HashSet<Boundary> getAdjBoundary() {
        return adjBoundary;
    }

    public void setAdjBoundary(HashSet<Boundary> adjBoundary) {
        this.adjBoundary = adjBoundary;
    }

    public void addAdjBoundary(Boundary boundary) {
        adjBoundary.add(boundary);
    }

    public void addAdjFace(Stratum stratum) {
        adjStratum.add(stratum);
    }

    public Set<AdjacentRelation> getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(Set<AdjacentRelation> adjacent) {
        this.adjacent = adjacent;
    }

    public Set<BelongRelation> getBelong() {
        return belong;
    }

    public void setBelong(Set<BelongRelation> belong) {
        this.belong = belong;
    }

    @Override
    public void setFid(int fid) {
        super.setFid(fid);
        this.setNodeName(Integer.toString(fid));
    }
}
