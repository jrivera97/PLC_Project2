import java.lang.Exception;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            args = new String[]{"./temptests.txt"};
        }

        System.out.println("parsing: " + args[0]);

				 HashMap<String, Double> memory = new HashMap<String, Double>();
				 HashMap<String, Functions> linker = new HashMap<String, Functions>();
				 HashMap<String, Integer> scopes = new HashMap<String, Integer>();
				int scope = 0;


        CharStream charStream = CharStreams.fromFileName(args[0]);
        CalculatorLexer lexer = new CalculatorLexer(charStream);
        CalculatorParser parser = new CalculatorParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.prog();
        EvalVisitor visitor = new EvalVisitor(memory, linker, scopes, scope);

        visitor.visit(tree);
    }
}
