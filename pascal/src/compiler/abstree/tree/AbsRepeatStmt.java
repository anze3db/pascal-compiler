package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Stavek 'while'.
 */
public class AbsRepeatStmt extends AbsStmt {

	/** Pogoj. */
	public AbsValExpr cond;
	
	/** Stavek. */
	public AbsStmt stmt;
	
	public AbsRepeatStmt(AbsValExpr cond, AbsStmt stmt) {
		this.cond = cond;
		this.stmt = stmt;
	}

	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
