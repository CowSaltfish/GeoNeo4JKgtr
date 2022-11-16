package com.nnulab.geoneo4jkgtr.Entity;

import com.nnulab.geoneo4jkgtr.Entity.Basic.GeoNode;
import com.nnulab.geoneo4jkgtr.Entity.Enum.EventType;
import com.nnulab.geoneo4jkgtr.Entity.Enum.StratumType;
import com.nnulab.geoneo4jkgtr.Entity.Relationship.SpatialRelationship.AdjacentRelation;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

/**
 * @author : LiuXianYu
 * @date : 2022/4/15 15:53
 * 地质事件
 */
@NodeEntity
public class GeoEvent extends GeoNode {

    private String subjectName;//如果是生成，主体就是地层；如果是侵入，主体就是岩浆岩；如果是断裂，主体就是断层；如果是褶皱，主体就是相关地层，名称写XX_XX_XX

    private EventType eventType;

    private final int fid;

    private static int count = 0;

    @Relationship(type = "SUBJECT", direction = Relationship.OUTGOING)
    private Set<AdjacentRelation> subject;

    @Relationship(type = "EARLIER_THAN", direction = Relationship.OUTGOING)
    private Set<AdjacentRelation> earlierThan;

    public GeoEvent() {
        fid = count++;
    }

    public GeoEvent(Face face) {
        subjectName = face.getNodeName();
        if (face.getType() == StratumType.Magmatic)
            eventType = EventType.INTRUSION;
        else
            eventType = EventType.GENERATION;
        setNodeName(subjectName);
        fid = count++;
        setLabelName("GeoEvent");
    }

    public GeoEvent(Fault fault) {
        subjectName = fault.getNodeName();
        eventType = EventType.FRACTURE;
        setNodeName(subjectName);
        fid = count++;
        setLabelName("GeoEvent");
    }


    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Set<AdjacentRelation> getSubject() {
        return subject;
    }

    public void setSubject(Set<AdjacentRelation> subject) {
        this.subject = subject;
    }

    public Set<AdjacentRelation> getEarlierThan() {
        return earlierThan;
    }

    public void setEarlierThan(Set<AdjacentRelation> earlierThan) {
        this.earlierThan = earlierThan;
    }
}
