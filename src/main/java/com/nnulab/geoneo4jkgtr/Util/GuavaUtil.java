package com.nnulab.geoneo4jkgtr.Util;

import com.google.common.graph.ElementOrder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.nnulab.geoneo4jkgtr.Model.Entity.Basic.ScenarioRelation;
import com.nnulab.geoneo4jkgtr.Model.KnowledgeGraph;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : LiuXianYu
 * @date : 2023/5/10 15:33
 */
@Component
public class GuavaUtil {

    static int WHITE = 0, GRAY = 1, BLACK = 2;
    private Stack<String> loop = new Stack<>();
    List<List<String>> loops = new ArrayList<>();
    List<Set<String>> loopSetList = new ArrayList<>();//用于判定是否已存在某一循环


    /**
     * 将知识图谱子图转化为Guava图结构
     *
     * @param knowledgeGraph
     * @return
     */
    public MutableValueGraph<String, Integer> kg2GuavaGraph(KnowledgeGraph knowledgeGraph) {
        MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed()//有向图
                .nodeOrder(ElementOrder.insertion())//节点按插入顺序输出
                .allowsSelfLoops(true) //允许自环
                .expectedNodeCount(knowledgeGraph.getNodes().size())//预期节点数
                .build();

        for (ScenarioRelation sr : knowledgeGraph.getRelationships()) {
            graph.putEdgeValue(sr.getStartNode().toString(), sr.getEndNode().toString(), sr.getWeight());
        }
        System.out.println(graph);
        return graph;
    }

    /**
     * 获取环中权值最小的边
     *
     * @param graph
     * @param loop
     * @return
     */
    public String[] getMinValueEdgeOnLoop(MutableValueGraph<String, Integer> graph, List<String> loop) {
        String[] ids = new String[2];
        String[] nodeList = graph.nodes().toArray(new String[0]);
        ids[0] = nodeList[0];
        ids[1] = nodeList[1];
        Integer minValue = Integer.MAX_VALUE;
        for (EndpointPair<String> nodes : graph.edges()) {
            Integer edgeValue = graph.edgeValue(nodes.nodeU(), nodes.nodeV());
            if (minValue > edgeValue) {
                minValue = edgeValue;
                ids[0] = nodes.nodeU();
                ids[1] = nodes.nodeV();
            }
        }
        return ids;
    }


    /**
     * 检测环
     *
     * @return 有向环序号
     */
    public List<List<String>> searchLoops(MutableValueGraph<String, Integer> graph) {
        //初始所有顶点都是白色
        Map<String, Integer> color = new HashMap<>();
        for (String nodeCode1 : graph.nodes()) {
            color.put(nodeCode1, WHITE);
        }

        //对所有顶点做DFS
        for (String nodeCode : graph.nodes()) {
            if (color.get(nodeCode) == WHITE) {
//                if (DFSUtil(graph, nodeCode, color, loops)) {
//                    return loops;
//                }
                DFSUtil(graph, nodeCode, color);
                color.put(loop.pop(), WHITE);

            }
        }
        return loops;
    }

    /**
     * Recursive function to find if there is back edge in DFS subtree tree rooted with 'u'
     *
     * @param graph
     * @param nodeCode
     * @param color
     * @return
     */
    private void DFSUtil(MutableValueGraph<String, Integer> graph, String nodeCode, Map<String, Integer> color) {
        // GRAY : This vertex is being processed (DFS for this vertex has started, but not ended)
        // (or this vertex is in function call stack)
        //灰色表示该节点还在回溯中
        color.put(nodeCode, GRAY);
        loop.push(nodeCode);
        //遍历其邻接顶点
        for (String in : graph.successors(nodeCode)) {
            //若邻接顶点也在回溯中，则存在环
            if (color.get(in) == GRAY) {
                Stack<String> loop1 = new Stack<>();
                loop1.addAll(loop);
                HashSet<String> loopset = new HashSet<>(loop1);
//                if (!loopSetList.contains(loopset) || !sameLoop(loopset)) {
                if (!sameLoop(loopset)) {
                    loops.add(new ArrayList<>(loop1));
                    loopSetList.add(loopset);
                }
                return;
            }
            // If v is not processed and there is a back edge in subtree rooted with v
            //若邻接顶点还未被遍历，则对其进行DFS
            if (color.get(in) == WHITE) {
                DFSUtil(graph, in, color);
                color.put(loop.pop(), WHITE);
            }
        }
        //顶点结束访问出栈，标记为黑色
//        color.put(nodeCode, BLACK);
    }

    private boolean sameLoop(HashSet<String> loopset) {
        for (Set<String> ls : loopSetList) {
            if (loopset.containsAll(ls)) {
                return true;
            }
        }
        return false;
    }

}
