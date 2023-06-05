package com.nnulab.geoneo4jkgtr.Util;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.nnulab.geoneo4jkgtr.Dao.BasicRelationDao;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author : LiuXianYu
 * @date : 2023/5/10 16:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuavaUtilTest {

    @Resource
    private Neo4jUtil neo4jUtil;

    @Resource
    private GuavaUtil guavaUtil;

    @Test
    public void testKg2GuavaGraph() {
        //获取子图
        KnowledgeGraph knowledgeGraph = neo4jUtil.result2KG(neo4jUtil.RunCypher("MATCH p=()-[r:EARLIERTHAN_FF]->() RETURN p"));
        MutableValueGraph<String, Integer> guavaGraph = guavaUtil.kg2GuavaGraph(knowledgeGraph);

    }

    @Test
    public void testSearchLoops() {
        MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed()//有向图
                .nodeOrder(ElementOrder.insertion())//节点按插入顺序输出
                .allowsSelfLoops(true) //允许自环
                .expectedNodeCount(5)//预期节点数
                .build();

        graph.putEdgeValue("A","B",1);
        graph.putEdgeValue("B","C",1);
        graph.putEdgeValue("C","A",1);
        graph.putEdgeValue("C","E",1);
        graph.putEdgeValue("E","D",1);
        graph.putEdgeValue("D","B",1);

        guavaUtil.searchLoops(graph);
    }
}
