package com.nnulab.geoneo4jkgtr.Dao;

import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.BasicRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.CuttingOffRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.CuttingThroughRelation;
import com.nnulab.geoneo4jkgtr.Model.Entity.Relations.MutuallyCuttingRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
//import org.neo4j.driver.v1.types.Path;
//import org.neo4j.driver.types.Path;

import java.util.List;

/**
 * @author : LiuXianYu
 * @date : 2022/4/7 19:26
 */
@SuppressWarnings({"all"})
public interface BasicRelationDao extends Neo4jRepository<BasicRelation, Long> {

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层切割关系
     * 获取若干断层对，其存在若干下属界线满足关系：
     *  界线a、b、c属于断层f1
     *  界线d、e属于断层f2
     *      ab、ad、db邻接
     *      cb、be、be邻接
     *  或
     *      ab、ae、be邻接
     *      bc、cd、bd邻接
     * @return 切割关系集合
     */
    @Query("MATCH (A:Boundary)-[RB:BELONG]->(F1:Fault) \n" +
            "WITH A,F1 \n" +
            "MATCH (B:Boundary)-[RB:BELONG]->(F1:Fault)  \n" +
            "WITH F1,A,B \n" +
            "MATCH (A)-[RA:ADJACENT]->(B) \n" +
            "WITH F1,A,B \n" +
            "MATCH (C:Boundary)-[RB:BELONG]->(F1:Fault)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (B)-[RA:ADJACENT]->(C) \n" +
            "WITH F1,A,B,C\n" +
            "MATCH (B)-[RA:ADJACENT]->(D) \n" +
            "WITH F1,A,B,C,D\n" +
            "MATCH (A)-[RA:ADJACENT]->(D) \n" +
            "WITH F1,A,B,C,D\n" +
            "MATCH (B)-[RA:ADJACENT]->(E) \n" +
            "WITH F1,A,B,C,D,E\n" +
            "MATCH (C)-[RA:ADJACENT]->(E) \n" +
            "WITH F1,A,B,C,D,E\n" +
            "MATCH (D:Boundary)-[RB:BELONG]->(F2:Fault) \n" +
            "WITH F1,F2,A,B,C,D,E\n" +
            "MATCH (E:Boundary)-[RB:BELONG]->(F2:Fault) \n" +
            "WITH F1,F2,A,B,C,D,E\n" +
            "WHERE F1.name<>F2.name AND A.fid<>B.fid AND B.fid<>C.fid AND C.fid<>D.fid AND D.fid<>E.fid AND A.fid<>E.fid AND B.fid<>E.fid AND B.fid<>D.fid AND A.fid<>C.fid AND A.fid<>D.fid AND E.fid<>C.fid\n" +
            "with distinct F1,F2\n" +
            "CREATE (F1)-[RC:CUTTINGTHROUGH{relationName:'切割',relationName_en:'cutting through'}]->(F2)")
    List<CuttingThroughRelation> createCuttingThroughRelationOnFaults();

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层切割关系
     * 获取若干断层对，其存在若干下属界线满足关系：
     *  界线a、b属于断层f1
     *  界线c属于断层f2
     *      abc邻接
     *      与a、b邻接的其他线段不属于f2（有且只有一个c满足）
     * @return 截断关系集合
     */
    @Query("MATCH (A:Boundary)-[RB:BELONG]->(F1:Fault) \n" +
            "WITH A,F1 \n" +
            "MATCH (B:Boundary)-[RB:BELONG]->(F1)\n" +
            "WITH F1,A,B \n" +
            "MATCH (A)-[RA:ADJACENT]->(B) \n" +
            "WITH F1,A,B \n" +
            "MATCH (C:Boundary)-[RA:ADJACENT]->(A)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (C)-[RA:ADJACENT]->(B)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (C)-[RB:BELONG]->(F2:Fault)\n" +
            "WHERE F1.name <> F2.name\n" +
            "WITH F1,F2,A,B,count(C) as ctc\n" +
            "WHERE ctc = 1\n" +
            "WITH F1,F2,A,B\n" +
            "MATCH (C:Boundary)-[RA:ADJACENT]->(A)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (C)-[RA:ADJACENT]->(B)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (C)-[RB:BELONG]->(F2:Fault)\n" +
            "with F1,F2,A,B,C\n" +
            "where not exists ((F1)-[:CUTTINGTHROUGH]->(F2))\n" +
            "WITH distinct F1,F2\n" +
            "CREATE (F1)-[RC:CUTTINGOFF{relationName:'截断',relationName_en:'cutting off'}]->(F2)")
    List<CuttingOffRelation> createCuttingOffRelationOnFaults();

    /**
     * 基于界线属于断层关系、界线间邻接关系，推断断层切割关系
     * 获取若干断层对，其存在若干下属界线满足关系：
     *  界线a、b属于断层f1
     *  界线c、d属于断层f2
     *      abcd邻接
     * @return 相交关系集合
     */
    @Query("MATCH (A:Boundary)-[RB:BELONG]->(F1:Fault) \n" +
            "WITH A,F1 \n" +
            "MATCH (B:Boundary)-[RB:BELONG]->(F1)\n" +
            "WITH F1,A,B \n" +
            "MATCH (A)-[RA:ADJACENT]->(B) \n" +
            "WITH F1,A,B \n" +
            "MATCH (C:Boundary)-[RA:ADJACENT]->(A)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (C)-[RA:ADJACENT]->(B)\n" +
            "WITH F1,A,B,C\n" +
            "MATCH (D:Boundary)-[RA:ADJACENT]->(A)\n" +
            "WHERE C.fid <> D.fid\n" +
            "WITH F1,A,B,C,D\n" +
            "MATCH (D)-[RA:ADJACENT]->(B)\n" +
            "WITH F1,A,B,C,D\n" +
            "MATCH (C)-[RB:BELONG]->(F2:Fault)\n" +
            "WHERE F1.name <> F2.name\n" +
            "WITH F1,F2,A,B,C,D\n" +
            "MATCH (D)-[RB:BELONG]->(F2)\n" +
            "WITH distinct F1,F2\n" +
            "CREATE (F1)-[RC:MUTUALLYCUTTING{relationName:'相交',relationName_en:'mutually cutting'}]->(F2)")
    List<MutuallyCuttingRelation> createMutuallyCuttingRelationOnFaults();

    /**
     * 基于断层间地质关系，推断断层时间序列
     *  获取若干断层对f1-f2，其地质关系为：
     *      f1被f2切割
     *      f1截断f2
     *  则f1早于f2
     *      另f1f2相交
     *  则发育时间相同
     */
    @Query("match (f1:Fault)-[:CUTTINGTHROUGH|CUTTINGOFF]-(f2:Fault)\n" +
            "where exists ((f2)-[:CUTTINGTHROUGH]->(f1)) or exists ((f1)-[:CUTTINGOFF]->(f2))\n" +
            "match (g1:GeoEvent)-[:SUBJECT]->(f1)\n" +
            "match (g2:GeoEvent)-[:SUBJECT]->(f2)\n" +
            "with distinct g1,g2\n" +
            "create (g1)-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than',nodesType:'EARLIERTHAN_FF'}]->(g2)")
    void inferTimeSeriesOfFaults();

    /**
     * 基于弧段，构建地层间包含关系
     */
    @Query("match (b:Boundary)-[:BELONG{belongType:'outer'}]->(s1:Face) " +
            "where not exists((b)-[:BELONG]->(:Fault))" +
            "with b,s1 " +
            "match (b)-[:BELONG{belongType:'inner'}]->(s2:Face) " +
            "where s1.fid <> s2.fid \n" +
            "with distinct s1,s2 \n" +
            "create (s1)-[:CONTAINS]->(s2)")
    void CreateContainsRelationBetweenFaces();

    /**
     * 基于弧段，构建地层间邻接关系
     */
    @Query("match (b:Boundary)-[r1:BELONG]->(s1:Face)\n" +
//            "where not exists((b)-[:BELONG]->(:Fault)) and r1.belongType <> 'outer' and  r1.belongType <> 'inner' \n" +
            "where r1.belongType <> 'outer' and  r1.belongType <> 'inner' \n" +
            "with b,s1,r1 \n" +
            "match (b)-[r2:BELONG]->(s2:Face) \n" +
            "where s1.fid <> s2.fid and r2.belongType <> 'outer' and  r2.belongType <> 'inner' \n" +
            "with distinct s1,s2 " +
            "create (s1)-[:ADJACENT{relationName:'邻接',relationName_en:'adjacent'}]->(s2)")
    void CreateAdjacentRelationBetweenFaces();

    /**
     * 若两个地质界线相邻，且分别属于两个断层，则两个断层相邻
     */
    @Query("match (b1:Boundary)-[:ADJACENT]-(b2:Boundary)\n" +
            "match (b1)-[:BELONG]->(f1:Fault)\n" +
            "match (b2)-[:BELONG]->(f2:Fault)\n" +
            "where f1.code<>f2.code\n" +
            "with distinct f1,f2 \n" +
            "create (f1)-[:ADJACENT{relationName:'邻接',relationName_en:'adjacent'}]->(f2)")
    void CreateAdjacentRelationBetweenFaults();

    /**
     * 若一个地质界线同时属于一个断层一个地层，则该断层与地层相邻
     */
    @Query("match (b:Boundary)-[:BELONG]->(f:Fault)\n" +
            "match (b)-[:BELONG]->(s:Stratum)\n" +
            "with distinct f,s \n" +
            "create (f)-[:ADJACENT{relationName:'邻接',relationName_en:'adjacent'}]->(s),\n" +
            "(f)<-[:ADJACENT{relationName:'邻接',relationName_en:'adjacent'}]-(s)")
    void CreateAdjacentRelationBetweenFaultsAndFaces();


    /**
     * 推断地层面间拓扑关系
     *  若两个地层间与同一条地质界线邻接，
     *  则两地层邻接
     */
    @Query("match (b:Boundary)-[:BELONG]->(s1:Face) " +
            "where not exists((b)-[:BELONG]->(:Fault))" +
            "with b,s1 " +
            "match (b)-[:BELONG]->(s2:Face) " +
            "where s1.fid <> s2.fid and s1.stratumType = 'Sedimentary' and s2.stratumType = 'Sedimentary'" +
            "with distinct s1,s2 " +
            "create (s1)-[:CONTACTS{type:'Strata'}]->(s2)")
    void inferContactRelationshipOfSSOnStrata();

    @Query("match (b:Boundary)-[:BELONG]->(s1:Face) " +
            "with b,s1 " +
            "match (b)-[:BELONG]->(s2:Face) " +
            "where s1.fid <> s2.fid and (s1.stratumType = 'Magmatic' or s2.stratumType = 'Magmatic')" +
            "with distinct s1,s2 " +
            "create (s1)-[:CONTACTS{type:'Intrusion'}]->(s2)")
    void inferContactRelationshipOfIntrusionOnStrata();

    @Query("match (b:Boundary)-[:BELONG]->(s1:Face) " +
            "where exists((b)-[:BELONG]->(:Fault))" +
            "with b,s1 " +
            "match (b)-[:BELONG]->(s2:Face) " +
            "where s1.fid <> s2.fid " +
            "and s1.stratumType = 'Sedimentary' and s2.stratumType = 'Sedimentary'" +
            "with distinct s1,s2 " +
            "create (s1)-[:CONTACTS{type:'Fault'}]->(s2)")
    void inferContactRelationshipOfFaultOnStrata();

    /**
     * 推断地层面间同质关系
     *  若两个地层面同名，
     *  则两地层同质/时代
     */
    @Query("MATCH (n1:Face)\n" +
            "match (n2:Face) \n" +
            "where n1.fid <> n2.fid and n1.nodeName = n2.nodeName \n" +
            "with distinct n1, n2 \n" +
            "create (n1)-[:SAMETYPE]->(n2)")
    void inferSameTypeOnStrata();

    /**
     * 推断断层切割地层关系
     */
    @Query("match (b:Boundary)-[:BELONG]->(f:Fault) " +
            "with b,f " +
            "match (b)-[:BELONG]->(s:Face) " +
            "with distinct f,s " +
            "create (f)-[:CUTTING]->(s)")
    void inferCuttingBetweenFaultsAndFaces();

    /**
     * 推断地层生成的序列
     * 根据两个地层的中心上下关系添加成岩的先后关系
     * （没法很好地处理倒转的地层）
     */
    @Query("match (s1:Face)-[:CONTACTS{type:'Strata'}]->(s2:Face)\n" +
            "where s1.nodeName <> s2.nodeName\n" +
            "with s1,s2\n" +
            "match (b:Boundary)-[:BELONG{belongType:'bottom'}]->(s1)\n" +
            "with s1,s2,b\n" +
            "match (b)-[:BELONG{belongType:'top'}]->(s2)\n" +
            "with s1,s2,b\n" +
            "match (e1:GeoEvent)-->(s1)\n" +
            "with s1,s2,e1\n" +
            "match (e2:GeoEvent)-->(s2)\n" +
            "with distinct e1 ,e2\n" +
            "create (e1)<-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than'}]-(e2)")
    void inferTimeSeriesOfStrata();

    /**
     * 对每个入度大于1的节点，只保留时间最近的上级地层
     * 事件e2指向e1
     * 事件e3指向e1
     * e2、e3不是同一个事件
     * e2到e3存在路径
     * 删除e2指向e1的关系
     */
    @Query("match (e2:GeoEvent)-[r:EARLIERTHAN]->(e1:GeoEvent)\n" +
            "match (e3:GeoEvent)-[:EARLIERTHAN]->(e1)\n" +
            "where e2.fid<>e3.fid \n" +
            "with e1,e2,e3,r \n" +
            "match p=(e2)-[:EARLIERTHAN*]->(e3)\n" +
            "delete r")
    void deleteExceptTimeAdjacentRelationship();

    /**
     * 基于侵入岩与地层接触关系，推断侵入岩发育时间
     * 判断侵入岩与沉积岩的接触边界是否是上边界
     * 如果接触边界是上边界，则侵入事件在当前地层形成之后，后生地层形成之前发生
     */
    @Query("match (ei:GeoEvent{eventType:\"INTRUSION\"})-[:SUBJECT]->(si:Face)\n" +
            "with ei,si\n" +
            "match (si)-[:CONTACTS{type:'Intrusion'}]->(ss1:Face)\n" +
            "with ei,si,ss1\n" +
            "match (si)-[:CONTACTS{type:'Intrusion'}]->(ss2:Face)\n" +
            "with ei,si,ss1,ss2\n" +
            "match (ss1)-[:CONTACTS{type:'Strata'}]->(ss2)\n" +
            "with ei,si,ss1,ss2\n" +
            "match (si)-[:CONTACTS{type:'Intrusion'}]->(ss3:Face)\n" +
            "with ei,si,ss1,ss2,ss3\n" +
            "match (ss1)-[:CONTACTS{type:'Strata'}]->(ss3)\n" +
            "where ss1.fid<>ss2.fid and ss2.fid<>ss3.fid and ss3.fid<>ss1.fid\n" +
            "with ei,si,ss1,ss2,ss3\n" +
            "match (es1:GeoEvent)-[:SUBJECT]->(ss1)\n" +
            "with ei,es1,si,ss1,ss2,ss3\n" +
            "match (es23:GeoEvent)-[:SUBJECT]->(ss2)\n" +
            "where es1.fid<>es23.fid\n" +
            "with ei,es1,es23,si,ss1,ss2,ss3\n" +
            "match (es23)-[:SUBJECT]->(ss3)\n" +
            "with ei,es1,es23,si,count(ss1) as count_ss1,ss2,ss3\n" +
            "where count_ss1 = 1\n" +
            "with distinct ei,es1,es23\n" +
            "create (es1)<-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than'}]-(ei), (ei)<-[:EARLIERTHAN{relationName:'早于'}]-(es23)")
    void inferTimeOfIntrusionByStrata_0();

    /**
     * 前一步中如果有侵入岩未找到上边界，则推断它晚于所有地层生成事件
     */
    @Query("match (ei:GeoEvent{eventType:\"INTRUSION\"})\n" +
            "where not exists  ((:GeoEvent)-[:EARLIERTHAN]-(ei))\n" +
            "with distinct ei\n" +
            "match (es:GeoEvent{eventType: \"GENERATION\"})\n" +
            "where not exists  ((:GeoEvent)<-[:EARLIERTHAN]-(es))\n" +
            "create (ei)<-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than'}]-(es)")
    void inferTimeOfIntrusionByStrata_1();

    /**
     * 基于断层与地层接触关系，推断断层发育时间
     *
     */
    @Query("match (ef:GeoEvent)-[:SUBJECT]->(f:Fault)\n" +
            "with ef,f\n" +
            "match (f)-[:CUTTING]->(s:Face)\n" +
            "with ef,s\n" +
            "match (es:GeoEvent)-[:SUBJECT]-(s)\n" +
            "with distinct ef,es\n" +
            "create (ef)<-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than'}]-(es)")
    void inferTimeOfFaultsByStrata_0();

    @Query("match (ef:GeoEvent)-[:SUBJECT]->(f:Fault)\n" +
            "with ef\n" +
            "match (ef)<-[:EARLIERTHAN]-(es1:GeoEvent)\n" +
            "with ef,es1\n" +
            "match (ef)<-[r:EARLIERTHAN]-(es2:GeoEvent)\n" +
            "where es1.fid<>es2.fid\n" +
            "with es1,es2,r\n" +
            "match (es1)-[EARLIERTHAN*]->(es2)\n" +
            "delete r")
    void inferTimeOfFaultsByStrata_1();

    @Query("MATCH ()-[r]->() RETURN r")
    List<ScenarioRelation> searchAllRelationships();

    @Query("MATCH (n) RETURN n")
    List<Object> searchAllNodes();

    @Query("match (b:Boundary)-[:BELONG]->(:Face)\n" +
            "match p=(b)-[:BELONG]->(:Face)\n" +
            "with count(p) as cp,b\n" +
            "where cp=1\n" +
            "with b\n" +
            "match (b)-[:BELONG]->(f1:Face)\n" +
            "with f1\n" +
            "match (b:Boundary)-[:BELONG]->(:Face)\n" +
            "match p=(b)-[:BELONG]->(:Face)\n" +
            "with count(p) as cp,p,b,f1\n" +
            "where cp=1\n" +
            "with b,f1\n" +
            "match (b)-[:BELONG]->(f2:Face)\n" +
            "with f1,f2\n" +
            "where f1.nodeName=f2.nodeName\n" +
            "match p=(f1:Face)-[:ADJACENT*4]-(f2:Face) \n" +
            "with nodes(p) as nps,f1,f2,p\n" +
            "where SIZE(apoc.coll.toSet(nps)) = LENGTH(p) + 1\n" +
            "unwind nps as np1\n" +
            "with size(collect(distinct np1.nodeName)) as distinctNpName,f1,f2,nps,p\n" +
            "where (length(p)+1)/2+1=distinctNpName\n" +
            "return p skip 0 limit 100")
    List<Object> GetStrataList();

    @Query("match (b1:Boundary)-[:ADJACENT]-(b2:Boundary)\n" +
            "match (b2)-[:BELONG]->(s:Face)\n" +
            "match (b1)-[:BELONG]->(f:Fault)\n" +
            "where not exists ((f)-[:CUTTING]->(s)) and b1.end_line='Y'\n" +
            "with distinct s,f\n" +
            "create (s)-[:GLAND{relationName:'压盖',relationName_en:'gland'}]->(f)")
    void createGlandRelationBetweenFaultsAndStrata();

    @Query("match (b:Boundary)-[:BELONG]->(s:Face)\n" +
            "match (b:Boundary)-[:BELONG]->(f:Fault)\n" +
            "with distinct f,s\n" +
            "create (f)-[:CUTTING{relationName:'切割',relationName_en:'cutting'}]->(s)")
    void createCuttingRelationBetweenFaultsAndStrata();

    @Query("match (b1:Boundary)-[:BELONG]->(f:Fault)\n" +
            "match (b2:Boundary)-[:ADJACENT]->(b1)\n" +
            "match (b2)-[:BELONG]->(f)\n" +
            "with b1, collect(b2) as cb2\n" +
            "where size(cb2)=1\n" +
            "set b1.end_line='Y'")
    void setBoundaryOnFaultEnd();

    @Query("MATCH (f:Fault)-[r:CUTTING]->(s:Face)\n" +
            "match (g1:GeoEvent)-[:SUBJECT]->(f)\n" +
            "match (g2:GeoEvent)-[:SUBJECT]->(s)\n" +
            "with distinct g2,g1\n" +
            "create (g2)-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than',nodesType:'EARLIERTHAN_FS'}]->(g1)")
    void inferTimeSeriesFromFaultsCuttingStrata();

    @Query("MATCH (f:Fault)<-[r:GLAND]-(s:Face)\n" +
            "match (g1:GeoEvent)-[:SUBJECT]->(f)\n" +
            "match (g2:GeoEvent)-[:SUBJECT]->(s)\n" +
            "with distinct g2,g1\n" +
            "create (g1)-[:EARLIERTHAN{relationName:'早于',relationName_en:'earlier than',nodesType:'EARLIERTHAN_FS'}]->(g2)")
    void inferTimeSeriesFromStrataGlandFaults();

    @Query("match (e:GeoEvent)\n" +
            "with distinct e.eventType as e_type, collect(distinct e) as event\n" +
            "call apoc.create.addLabels(event,[apoc.text.upperCamelCase(e_type)]) yield node\n" +
            "return *")
    void classifyEventsByType();

    @Query("MATCH(g1:GeoEvent)-[r:EARLIERTHAN]->(g2:GeoEvent)\n" +
            "CALL apoc.create.relationship(g1,r.nodesType,{relationName:'早于', relationName_en:'earlier than'},g2)\n" +
            "YIELD rel\n" +
            "RETURN rel;")
    void classifyTemperaRelationByType();

    @Query("match (e2:GeoEvent{eventType:'GENERATION'})-[r:EARLIERTHAN_FS]->(e1:GeoEvent{eventType:'FRACTURE'})\n" +
            "match (e3:GeoEvent{eventType:'GENERATION'})-[:EARLIERTHAN_FS]->(e1)\n" +
            "where e2.fid<>e3.fid \n" +
            "with e1,e2,e3,r \n" +
            "match p=(e2)-[:EARLIERTHAN_SS*]->(e3)\n" +
            "delete r")
    void DefineUpperBound();

    @Query("match (e2:GeoEvent{eventType:'GENERATION'})<-[r:EARLIERTHAN_FS]-(e1:GeoEvent{eventType:'FRACTURE'})\n" +
            "match (e3:GeoEvent{eventType:'GENERATION'})<-[:EARLIERTHAN_FS]-(e1)\n" +
            "where e2.fid<>e3.fid \n" +
            "with e1,e2,e3,r \n" +
            "match p=(e2)<-[:EARLIERTHAN_SS*]-(e3)\n" +
            "delete r")
    void DefineLowerBound();

    /**
     * 若压盖断层的地层被该断层切割，则为假性压盖，应当删除
     */
    @Query("match p=(s1:Stratum)-[r:GLAND]->(f:Fault)\n" +
            "match (f)-[:CUTTING]->(s2:Stratum)\n" +
            "where s1.nodeName=s2.nodeName\n" +
            "delete r")
    void deleteFakeGlandRelationship();

    @Query("match (n1)-[r]-(n2)\n" +
            "where id(n1)= $id0 and id(n2) = $id1\n" +
            "return r")
    void deleteByNodeIds(@Param("id0") Long id0, @Param("id1") Long id1);

    @Query("match (n) set n.id=id(n)")
    void setAllNodesId();

    /**
     * 将一对多地层接触的两个不同年代地层的接触关系由普通地层接触改为角度不整合接触
     * match (s1:Face)-[:CONTACTS{type:'Strata'}]->(s2:Face)
     * where s1.center_y>s2.center_y
     * with distinct s1,s2.nodeName as sn
     * with s1, count(*) as nss
     * where nss>1 with s1
     * match (s1)-[r:CONTACTS{type:'Strata'}]-(s2:Face)
     * where s1.center_y>s2.center_y
     * delete.txt r
     * with distinct s1,s2
     * create (s1)-[:ADJACENT_SS_AU]->(s2),(s2)-[:ADJACENT_SS_AU]->(s1)
     */

}
