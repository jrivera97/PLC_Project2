import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

//import CalculatorParser.antlr4.CalculatorParser.ExpressionContext;
//import ParserRuleContext;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

class Functions {
	private List<TerminalNode> params;
	private CalculatorParser.Function_defContext ctx;

	Functions(List<TerminalNode> params, CalculatorParser.Function_defContext ctx) {
		this.params = params;
		this.ctx = ctx;
	}

	CalculatorParser.Function_defContext getCtx() {
		return this.ctx;
	}
	public Double call(List<TerminalNode> params) {
        int compare = Integer.compare(params.size(), this.params.size());
        if (compare < 0) {
            throw new RuntimeException("too few arguments");
        }
        else if (compare > 0) {
          throw new RuntimeException("too many arguments");
        }
	
        EvalVisitor evalVisitor = new EvalVisitor();
        for (int i = 0; i < this.params.size(); i++) {
            Double value = evalVisitor.visit(params.get(i));
        }
        EvalVisitor evalVistorNext = new EvalVisitor();
	
        Double ret = evalVistorNext.visit(this.ctx.stat_block());
        
        return ret;
    }
}
