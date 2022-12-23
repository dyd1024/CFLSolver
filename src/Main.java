import org.CFLGraph.CFLGraph;
import org.util.Tuple;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        CFLGraph cflGraph = initGraph("./res/CFLGraph");
        cflGraph.printGraph();
        CFLGrammar cflGrammar = initGrammar("./res/CFLGrammar");
        cflGrammar.print_grammar();
        CFLSolver solver = new CFLSolver();
        solver.reachability(cflGrammar, cflGraph);
        Boolean is_reach = solver.query("a", "A", "e");
        if (is_reach){
            System.out.println("可达！");
        }else {
            System.out.println("不可达！");
        }
        System.out.println("Hello world!");
    }

    public static CFLGraph initGraph(String graph_add) throws IOException {
        CFLGraph graph = new CFLGraph();
        Scanner sc = new Scanner(new FileReader(graph_add));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] edge = line.split(" ");
            assert edge.length == 3;
            graph.addEdge(new Tuple<>(edge[0], new Tuple<>(edge[1], edge[2])));
        }
        return graph;
    }

    public static CFLGrammar initGrammar(String grammar_add) throws FileNotFoundException {
        CFLGrammar cflGrammar = new CFLGrammar();
        Scanner sc = new Scanner(new FileReader(grammar_add));
        while (sc.hasNext()){
            String line = sc.nextLine().replace(" ", "");
            if (line.length() == 1){
                cflGrammar.getEmpty_rule().add(line.substring(0,1));
            } else if (line.length() == 2) {
                cflGrammar.getUnaryRules().add(new Tuple<>(line.substring(0,1), line.substring(1,2)));
            }else if (line.length() == 3){
                cflGrammar.getBinaryRules().add(new Tuple<>(line.substring(0,1), line.substring(1,3)));
            }
        }
        return cflGrammar;
    }
}
