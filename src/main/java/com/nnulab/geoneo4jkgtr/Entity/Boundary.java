package com.nnulab.geoneo4jkgtr.Entity;

import com.nnulab.geoneo4jkgtr.Entity.Relationship.SpatialRelationship.AdjacentRelation;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.BelongRelation;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : LiuXianYu
 * @date : 2022/3/29 15:49
 */
@NodeEntity
public class Boundary extends Line {

    public HashSet<Face> adjFace;

    public HashSet<Boundary> adjBoundary;

    @Relationship(type = "ADJACENT", direction = Relationship.UNDIRECTED)
    private Set<AdjacentRelation> adjacent;

    @Relationship(type = "BELONG", direction = Relationship.OUTGOING)
    private Set<BelongRelation> belong;

    public Boundary() {
        super();
        adjFace = new HashSet<>();
        adjBoundary = new HashSet<>();
        setLabelName("Boundary");
    }

    public HashSet<Face> getAdjFace() {
        return adjFace;
    }

    public void setAdjFace(HashSet<Face> adjFace) {
        this.adjFace = adjFace;
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

    public void addAdjFace(Face face) {
        adjFace.add(face);
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
