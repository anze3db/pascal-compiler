package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Deklaracija spremenljivke.
 */
public class AbsVarDecl extends AbsDecl {

	public boolean positive = false;
	
	/** Tip spremenljivke. */
	public AbsTypeExpr type;
	
	public AbsVarDecl(AbsDeclName name, AbsTypeExpr type) {
		this.name = name;
		this.type = type;
	}

	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
