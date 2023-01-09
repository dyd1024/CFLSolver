package org.CFLGraph;

import org.util.Tuple;

import java.util.*;

public class CFLGraph {
    private int V;     //顶点数目
    private int E;     //边的个数

    private HashMap<String, Set<Tuple<String, String>>> adj_list;    //邻接表

    public int getV() {
        return V;
    }

    public int getE() {
        return E;
    }

    public HashMap<String, Set<Tuple<String, String>>> getAdj_list() {
        return adj_list;
    }

    public CFLGraph( ){
        this.V = 0;
        this.E = 0;
        adj_list = new HashMap<>();
    }

    public void addEdge(Tuple<String, Tuple<String, String>> edge){
        String source = edge.x;
        String weight = edge.y.x;
        String target = edge.y.y;

        if (!adj_list.containsKey(source)){
            adj_list.put(source, new HashSet<>());
            this.V += 1;
        }

        if (!adj_list.containsKey(target)){
            adj_list.put(target, new HashSet<>());
            this.V += 1;
        }

        Set edge_set = adj_list.get(source);         //看来 get 返回的是值传递

        Tuple new_edge = new Tuple(weight, target);
        if ( !edge_set.contains(new_edge) ){
            edge_set.add(new_edge);
            this.E += 1;
        }
        adj_list.put(source, edge_set);
    }

    public CFLGraph reverse(){
        CFLGraph re_graph = new CFLGraph();
        re_graph.V = this.V;
        re_graph.E = this.E;
        adj_list.forEach( (source, edge_set) -> {
            edge_set.forEach(edge -> {
                re_graph.addEdge(new Tuple<>(edge.y, new Tuple(edge.x, source)));
            });
        });
        return re_graph;
    }

    public void printGraph(){
        adj_list.forEach((source, edge_set) -> {
            System.out.println("node:"+ source);
            edge_set.forEach(edge->{
                System.out.println("     weight:"+ edge.x + "  target:" + edge.y);
            });
        });
    }
}
