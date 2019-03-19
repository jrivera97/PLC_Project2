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
	private ParserRuleContext ctx;

	Functions(List<TerminalNode> params, ParserRuleContext ctx) {
		this.params = params;
		this.ctx = ctx;
	}

	ParserRuleContext getCtx() {
		return this.ctx;
	}
	// public Double invoke(List<ExpressionContext> params, Map<String, Function> functions, Scope scope) {
  //       if (params.size() != this.params.size()) {
  //           throw new RuntimeException("Illegal Function call");
  //       }
  //       Scope scopeNext = new Scope(null); // create function scope
	//
  //       EvalVisitor evalVisitor = new EvalVisitor(scope, functions);
  //       for (int i = 0; i < this.params.size(); i++) {
  //           TLValue value = evalVisitor.visit(params.get(i));
  //           scopeNext.assignParam(this.params.get(i).getText(), value);
  //       }
  //       EvalVisitor evalVistorNext = new EvalVisitor(scopeNext,functions);
	//
  //       Double ret = 0.0;
  //       try {
  //       	evalVistorNext.visit(this.block);
  //       } catch (Double returnValue) {
  //       	ret = returnValue;
  //       }
  //       return ret;
  //   }
}
