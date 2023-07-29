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
        String[] ids1 = new String[2];
        ids[0] = loop.get(0);
        ids[1] = loop.get(1);
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < loop.size() - 1; i++) {
            ids[0] = loop.get(i);
            ids[1] = loop.get(i + 1);
            int edgeValue = graph.edgeValue(ids[0], ids[1]);
            if (minValue > edgeValue) {
                minValue = edgeValue;
                ids1[0] = ids[0];
                ids1[1] = ids[1];
            }
        }
        return ids1;
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
                ArrayList<String> loop1 = new ArrayList<>(loop);
                HashSet<String> loopSet = new HashSet<>(loop1);
//                if (!loopSetList.contains(loopset) || !sameLoop(loopset)) {
                if (!sameLoop(loopSet, loop1)) {//若一个环包含一个环，则只保留小环
                    loops.add(loop1);
                    loopSetList.add(loopSet);
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

    private boolean sameLoop(HashSet<String> loopSet, ArrayList<String> loop) {
        for (Set<String> ls : loopSetList) {
            if (loopSet.containsAll(ls)) {
                return true;
            } else if (ls.containsAll(loopSet)) {
                loopSetList.remove(ls);
                loopSetList.add(loopSet);
                for (List<String> l : loops) {
                    if (l.containsAll(loop)) {
                        loops.remove(l);
                        loops.add(loop);
                    }
                }
                return true;
            }
        }
        return false;
    }

}
