package compiler.semanal;

import compiler.abstree.AbsVisitor;
import compiler.abstree.tree.AbsAlloc;
import compiler.abstree.tree.AbsArrayType;
import compiler.abstree.tree.AbsAssignStmt;
import compiler.abstree.tree.AbsAtomConst;
import compiler.abstree.tree.AbsAtomType;
import compiler.abstree.tree.AbsBinExpr;
import compiler.abstree.tree.AbsBlockStmt;
import compiler.abstree.tree.AbsCallExpr;
import compiler.abstree.tree.AbsConstDecl;
import compiler.abstree.tree.AbsDecl;
import compiler.abstree.tree.AbsDeclName;
import compiler.abstree.tree.AbsDecls;
import compiler.abstree.tree.AbsExprStmt;
import compiler.abstree.tree.AbsForStmt;
import compiler.abstree.tree.AbsFunDecl;
import compiler.abstree.tree.AbsIfStmt;
import compiler.abstree.tree.AbsNilConst;
import compiler.abstree.tree.AbsPointerType;
import compiler.abstree.tree.AbsProcDecl;
import compiler.abstree.tree.AbsProgram;
import compiler.abstree.tree.AbsRecordType;
import compiler.abstree.tree.AbsStmt;
import compiler.abstree.tree.AbsStmts;
import compiler.abstree.tree.AbsTypeDecl;
import compiler.abstree.tree.AbsTypeName;
import compiler.abstree.tree.AbsUnExpr;
import compiler.abstree.tree.AbsValExpr;
import compiler.abstree.tree.AbsValExprs;
import compiler.abstree.tree.AbsValName;
import compiler.abstree.tree.AbsVarDecl;
import compiler.abstree.tree.AbsWhileStmt;
import compiler.report.Report;

public class SemNameResolver implements AbsVisitor {
	
	public boolean error;
	
	@Override
	public void visit(AbsAlloc acceptor) {
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		if (acceptor.error) {
			return;
		}

		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);

	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		if (acceptor.error) {
			return;
		}

		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);

	}

	@Override
	public void visit(AbsAtomConst acceptor) {
	}

	@Override
	public void visit(AbsAtomType acceptor) {
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		acceptor.fstExpr.accept(this);

		if (acceptor.oper != AbsBinExpr.RECACCESS)
			acceptor.sndExpr.accept(this);
	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		acceptor.stmts.accept(this);
	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		acceptor.name.accept(this);
		acceptor.args.accept(this);
	}

	@Override
	public void visit(AbsConstDecl acceptor) {
		acceptor.name.accept(this);
		acceptor.value.accept(this);
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		AbsDecl decl = SemTable.fnd(acceptor.name);

		if (decl != null) {
			SemDesc.setNameDecl(acceptor, decl);
		}
	}

	@Override
	public void visit(AbsDecls acceptor) {
		// 1. prelet - napolni SemTable
		for (AbsDecl d : acceptor.decls) {
			String name = getDeclName(d);

			try {
				SemTable.ins(name, d);
			} catch (Exception e) {
			}
		}

		// 2. prelet - povezovanje deklaracij z uporabo
		for (AbsDecl d : acceptor.decls) {
			d.accept(this);
		}

	}

	@Override
	public void visit(AbsExprStmt acceptor) {
		acceptor.expr.accept(this);
	}

	@Override
	public void visit(AbsForStmt acceptor) {
		acceptor.name.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);

	}

	@Override
	public void visit(AbsNilConst acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsPointerType acceptor) {
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		acceptor.name.accept(this);

		SemTable.newScope();
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
		SemTable.oldScope();
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		acceptor.name.accept(this);

		SemTable.newScope();
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
		SemTable.oldScope();

		acceptor.type.accept(this);

	}

	@Override
	public void visit(AbsProgram acceptor) {
		String progName = acceptor.name.name;

		try {
			SemTable.ins(progName, acceptor.name);
		} catch (Exception ex) {
			Report.warning(
					"Ime programa ne sme biti enako imenom vgrajenih funkcij: "
							+ progName, 1, 1);
		}

		SemTable.newScope();
		// 1. prelet
		acceptor.decls.accept(this);
		// 2. prelet
		acceptor.stmt.accept(this);
		SemTable.oldScope();
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		SemTable.newScope();
		acceptor.fields.accept(this);
		SemTable.oldScope();

		for (AbsDecl d : acceptor.fields.decls)
			SemDesc.setScope(d, 0);
	}

	@Override
	public void visit(AbsStmts acceptor) {
		for (AbsStmt s : acceptor.stmts) {
			s.accept(this);
		}

	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		acceptor.name.accept(this);
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		AbsDecl decl = SemTable.fnd(acceptor.name);

		if (decl != null) {
			SemDesc.setNameDecl(acceptor, decl);

		}
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		acceptor.expr.accept(this);
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		for (AbsValExpr e : acceptor.exprs) {
			e.accept(this);
		}
	}

	@Override
	public void visit(AbsValName acceptor) {
		AbsDecl decl = SemTable.fnd(acceptor.name);

		if (decl != null) {
			SemDesc.setNameDecl(acceptor, decl);
		}
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		acceptor.name.accept(this);
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
	}
	private String getDeclName(AbsDecl d) {

		if (d instanceof AbsConstDecl)
			return ((AbsConstDecl) d).name.name;
		else if (d instanceof AbsFunDecl)
			return ((AbsFunDecl) d).name.name;
		else if (d instanceof AbsProcDecl)
			return ((AbsProcDecl) d).name.name;
		else if (d instanceof AbsTypeDecl)
			return ((AbsTypeDecl) d).name.name;
		else if (d instanceof AbsVarDecl)
			return ((AbsVarDecl) d).name.name;
		else
			return "?";
	}

}
