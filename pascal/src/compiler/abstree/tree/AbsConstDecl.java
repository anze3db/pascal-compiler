package compiler.abstree.tree;

import compiler.abstree.AbsVisitor;

/**
 * Deklaracija konstante.
 */
public class AbsConstDecl extends AbsDecl {

	/** Vrednost konstante. */
	public AbsValExpr value;
	
	public AbsConstDecl(AbsDeclName name, AbsValExpr value) {
		this.name = name;
		this.value = value;
	}

	public void accept(AbsVisitor visitor) {
		visitor.visit(this);
	}

}
