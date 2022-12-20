import org.CFLGraph.EdgeNode;
import org.util.Tuple;

import java.util.*;

public class CFLGrammar {
    private Set<String>  empty_rule = new HashSet<>();
    private Set<Tuple<String, String>> unaryRules = new HashSet<>();
    private Set<Tuple<String, String>> binaryRules = new HashSet<>();

    public Set<String> getEmpty_rule() {
        return empty_rule;
    }

    public Set<Tuple<String, String>> getUnaryRules() {
        return unaryRules;
    }

    public Set<Tuple<String, String>> getBinaryRules() {
        return binaryRules;
    }

    public void print_grammar(){
        for (String empty : empty_rule){
            System.out.println(empty);
        }

        for (Tuple unary : unaryRules) {
            System.out.println(unary.x + " " + unary.y);
        }

        for (Tuple binary : binaryRules) {
            System.out.println(binary.x + " " + binary.y);
        }
    }
}


