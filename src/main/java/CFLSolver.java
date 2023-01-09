import org.CFLGraph.CFLGraph;
import org.util.Tuple;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class CFLSolver {
    private Queue<Tuple<String, Tuple<String, String>>> workList = new LinkedList<>();
    private CFLGraph cflGraph;
    private CFLGrammar cflGrammar;

    public void reachability(CFLGrammar cflGrammar, CFLGraph cflGraph){
        this.cflGraph = cflGraph;
        this.cflGrammar = cflGrammar;
        init();
        while (!workList.isEmpty()){
            Tuple<String, Tuple<String, String>> edge = workList.poll();
            // Y
            String weight = edge.y.x;

            //处理 X --> Y 的产生式
            for (Tuple unaryRule : cflGrammar.getUnaryRules()) {
                if (weight.equals(unaryRule.y)){
                    //查看有没有  vi --x--> vj 的边，没有就加上。
                    AtomicBoolean is_add = new AtomicBoolean(true);
                    cflGraph.getAdj_list().get(edge.x).forEach(old_edge -> {
                        if (old_edge.y.equals(edge.y.y) && old_edge.x.equals(unaryRule.x)){
                            is_add.set(false);
                        }
                    });

                    if (is_add.get()){
                        Tuple new_edge = new Tuple(edge.x, new Tuple<>(unaryRule.x, edge.y.y));
                        workList.add(new_edge);
                        cflGraph.addEdge(new_edge);
                    }
                }
            }

            //处理 X --> YZ 和  X --> ZY 产生式
            for (Tuple<String, String> binaryRule : cflGrammar.getBinaryRules()) {
                //处理 X --> YZ
                Set<Tuple> new_edge_set = new HashSet<>();
                if (binaryRule.y.substring(0,1).equals(weight)){
                    cflGraph.getAdj_list().get(edge.y.y).forEach(old_edge -> {
                        if (old_edge.x.equals(binaryRule.y.substring(1,2))){
                            Tuple edge_tar = new Tuple<>(binaryRule.x, old_edge.y);
                            Tuple new_edge = new Tuple(edge.x, edge_tar);
                            Set<Tuple<String, String>> edge_set = cflGraph.getAdj_list().get(edge.x);
                            if (!edge_set.contains(edge_tar)){
                                workList.add(new_edge);
                                new_edge_set.add(new_edge);
                            }
                        }
                    });
                }
                //处理 X --> ZY
                if (binaryRule.y.substring(1,2).equals(weight)){
                    CFLGraph re_graph = cflGraph.reverse();
                    re_graph.getAdj_list().get(edge.x).forEach(old_edge -> {
                        if (old_edge.x.equals(binaryRule.y.substring(0,1))){
                            Tuple edge_tar = new Tuple<>(binaryRule.x, edge.y.y);
                            Tuple new_edge = new Tuple<>(old_edge.y, edge_tar);
                            Set<Tuple<String, String>> edge_set = cflGraph.getAdj_list().get(old_edge.y);
                            if (!edge_set.contains(edge_tar)){
                                workList.add(new_edge);
                                new_edge_set.add(new_edge);
                            }
                        }
                    });
                }

                new_edge_set.forEach(new_edge -> {
                    cflGraph.addEdge(new_edge);
                });
            }
        }
    }

    private void init(){
        cflGraph.getAdj_list().forEach((source, edge_set) -> {
            edge_set.forEach(edge->{
                Tuple add_edge = new Tuple<>(source, edge);
                workList.add(add_edge);
            });
        });

        //对每个空串产生式 A --> ε，添加  vi --A--> vi
        for (String x : cflGrammar.getEmpty_rule()) {
            cflGraph.getAdj_list().forEach( (source,edge_set)  -> {
                //遍历每个节点的所有出边，查看有没有 vi --x--> vi  的边。
                AtomicBoolean is_add = new AtomicBoolean(true);
                edge_set.forEach( edge -> {
                    if (edge.y.equals(source) && edge.x.equals(x) ){
                        is_add.set(false);
                    }
                });
                //如果没有的话，添加该边。
                if (is_add.get()){
                    Tuple new_edge = new Tuple<>(source, new Tuple<>(x, source));
                    workList.add(new_edge);
                    cflGraph.addEdge(new_edge);
                }
            });
        }
    }

    public boolean query(String source, String weight, String target){
        AtomicReference<Boolean> is_reach = new AtomicReference<>(false);
        cflGraph.getAdj_list().get(source).forEach(edge -> {
            if (edge.x.equals(weight) && edge.y.equals(target)){
                is_reach.set(true);
            }
        });
        return is_reach.get();
    }

    public CFLGraph getCflGraph() {
        return cflGraph;
    }
}
