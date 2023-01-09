import org.CFLGraph.CFLGraph;
import org.util.Tuple;
import soot.*;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        soot.G.reset();
        Options.v().set_process_dir(Arrays.asList("./src/main/resources"));

////		Options.v().set_process_dir(Collections.singletonList("C:\\Files\\Java_project\\core\\target\\james-core-3.8.0-SNAPSHOT-tests.jar"));
        Options.v().set_prepend_classpath(true);
        Options.v().set_src_prec(Options.src_prec_java);
        Options.v().set_whole_program(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_output_format(Options.output_format_none);
        setSparkPointsToAnalysis();

        SootClass c = Scene.v().forceResolve("Example", SootClass.BODIES);
        c.setApplicationClass();
        Scene.v().loadNecessaryClasses();
        SootMethod method = c.getMethodByName("main");
        List entryPoints = new ArrayList();
        entryPoints.add(method);
        Scene.v().setEntryPoints(entryPoints);
        PackManager.v().runPacks();

        CallGraph callGraph = Scene.v().getCallGraph();
        System.out.println(callGraph.size());
//        Scene.v().getPointsToAnalysis();


//        CFLGraph cflGraph = initGraph("./res/CFLGraph");
//        cflGraph.printGraph();
//        CFLGrammar cflGrammar = initGrammar("./res/CFLGrammar");
//        cflGrammar.print_grammar();
//        CFLSolver solver = new CFLSolver();
//        solver.reachability(cflGrammar, cflGraph);
//        Boolean is_reach = solver.query("a", "A", "e");
//        if (is_reach){
//            System.out.println("可达！");
//        }else {
//            System.out.println("不可达！");
//        }
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


    static void setSparkPointsToAnalysis() {
        System.out.println("[spark] Starting analysis ...");

        HashMap opt = new HashMap();
        opt.put("enabled", "true");
        opt.put("verbose", "true");
        opt.put("ignore-types", "false");
        opt.put("force-gc", "false");
        opt.put("pre-jimplify", "false");
        opt.put("vta", "true");
        opt.put("rta", "false");
        opt.put("field-based", "false");
        opt.put("types-for-sites", "false");
        opt.put("merge-stringbuffer", "true");
        opt.put("string-constants", "false");
        opt.put("simulate-natives", "true");
        opt.put("simple-edges-bidirectional", "false");
        opt.put("on-fly-cg", "true");
        opt.put("simplify-offline", "false");
        opt.put("simplify-sccs", "false");
        opt.put("ignore-types-for-sccs", "false");
        opt.put("propagator", "worklist");
        opt.put("set-impl", "double");
        opt.put("double-set-old", "hybrid");
        opt.put("double-set-new", "hybrid");
        opt.put("dump-html", "false");
        opt.put("dump-pag", "true");
        opt.put("dump-solution", "false");
        opt.put("topo-sort", "false");
        opt.put("dump-types", "true");
        opt.put("class-method-var", "true");
        opt.put("dump-answer", "false");
        opt.put("add-tags", "false");
        opt.put("set-mass", "false");

        SparkTransformer.v().transform("", opt);

        System.out.println("[spark] Done!");
    }
}
