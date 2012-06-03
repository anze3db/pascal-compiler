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
	private int record;

	@Override
	public void visit(AbsAlloc acceptor) {
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);
	}

	@Override
	public void visit(AbsAtomConst acceptor) {

		try {
			switch (acceptor.type) {
			case AbsAtomConst.INT:
				SemDesc.setActualConst(acceptor,
						Integer.parseInt(acceptor.value));
				break;
			case AbsAtomConst.CHAR:
				SemDesc.setActualConst(acceptor,
						(int) (acceptor.value.charAt(0)));
				break;
			case AbsAtomConst.BOOL:
				SemDesc.setActualConst(acceptor,
						acceptor.value.equals("true") ? 1 : 0);
				break;
			}
		} catch (Exception e) {
			error = true;
			Report.warning(String.format("Set actual const error at (%d, %d)",
					acceptor.begLine, acceptor.endLine));
		}
	}

	@Override
	public void visit(AbsAtomType acceptor) {
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		acceptor.fstExpr.accept(this);

		if (acceptor.oper == AbsBinExpr.RECACCESS)
			record++;

		acceptor.sndExpr.accept(this);

		if (acceptor.oper == AbsBinExpr.RECACCESS)
			record--;

		Integer fst = SemDesc.getActualConst(acceptor.fstExpr);

		Integer snd = SemDesc.getActualConst(acceptor.sndExpr);

		if (fst == null || snd == null) {
			// error=true; Report.warning("Value not declared",
			// acceptor.begLine,
			// acceptor.begColumn);
			return;
		}

		switch (acceptor.oper) {
		case AbsBinExpr.ADD:
			SemDesc.setActualConst(acceptor, fst + snd);
			break;
		case AbsBinExpr.SUB:
			SemDesc.setActualConst(acceptor, fst - snd);
			break;
		case AbsBinExpr.MUL:
			SemDesc.setActualConst(acceptor, fst * snd);
			break;
		case AbsBinExpr.DIV:
			SemDesc.setActualConst(acceptor, snd != 0 ? fst / snd : 0);
			break;
		case AbsBinExpr.NEQ:
			SemDesc.setActualConst(acceptor, fst != snd ? 1 : 0);
			break;
		case AbsBinExpr.LTH:
			SemDesc.setActualConst(acceptor, fst < snd ? 1 : 0);
			break;
		case AbsBinExpr.GTH:
			SemDesc.setActualConst(acceptor, fst > snd ? 1 : 0);
			break;
		case AbsBinExpr.LEQ:
			SemDesc.setActualConst(acceptor, fst <= snd ? 1 : 0);
			break;
		case AbsBinExpr.GEQ:
			SemDesc.setActualConst(acceptor, fst >= snd ? 1 : 0);
			break;
		case AbsBinExpr.AND:
			SemDesc.setActualConst(acceptor, fst & snd);
			break;
		case AbsBinExpr.OR:
			SemDesc.setActualConst(acceptor, fst | snd);
			break;
		// TODO: arraccess, recaccess
		case AbsBinExpr.ARRACCESS:
			break;
		case AbsBinExpr.RECACCESS:
			break;
		}

	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		acceptor.stmts.accept(this);
	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		acceptor.name.accept(this);
		acceptor.args.accept(this);
		AbsDecl a = SemTable.fnd(acceptor.name.name);
		if (a == null) {
			error = true;
			Report.warning(String.format("Subprogram %s not defined",
					acceptor.name.name), acceptor.begLine, acceptor.begColumn);
		}
	}

	@Override
	public void visit(AbsConstDecl acceptor) {

		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (Exception e) {
			// TODO: could be doing something wrong here...
		}
		acceptor.value.accept(this);
		Integer ac = SemDesc.getActualConst(acceptor.value);

		if (ac != null) {
			SemDesc.setActualConst(acceptor, ac);
		} else {
			error = true;
			Report.warning(String.format("'%s' is null", acceptor.name.name),
					acceptor.begLine, acceptor.begColumn);
		}
	}

	@Override
	public void visit(AbsDeclName acceptor) {
	}

	@Override
	public void visit(AbsDecls acceptor) {
		for (AbsDecl decl : acceptor.decls) {
			decl.accept(this);
		}
	}

	@Override
	public void visit(AbsExprStmt acceptor) {
		acceptor.expr.accept(this);
	}

	@Override
	public void visit(AbsForStmt acceptor) {
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
		acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			error = true;
			Report.warning(String.format("Function '%s' already declared",
					acceptor.name.name), acceptor.begLine, acceptor.begColumn);
		}
		SemTable.newScope();
		acceptor.pars.accept(this);
		acceptor.type.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
		SemTable.oldScope();
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);

	}

	@Override
	public void visit(AbsNilConst acceptor) {
		SemDesc.setActualConst(acceptor, 0);
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		acceptor.type.accept(this);

	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			// TODO Auto-generated catch block
			error = true;
			Report.warning(String.format("Function '%s' already declared",
					acceptor.name.name), acceptor.begLine, acceptor.begColumn);
		}

		SemTable.newScope();
		acceptor.pars.accept(this);
		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
		SemTable.oldScope();

	}

	@Override
	public void visit(AbsProgram acceptor) {

		try {
			SemTable.ins(acceptor.name.name, acceptor.name);
		} catch (Exception ex) {
		}

		acceptor.decls.accept(this);
		acceptor.stmt.accept(this);
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
		if(record != 0) return;
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (Exception e) {
			// TODO: handle exception
		}
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsTypeName acceptor) {
		AbsDecl d = SemTable.fnd(acceptor.name);
		if (d == null) {
			error = true;
			Report.warning(acceptor.name + " type not defined",
					acceptor.begLine, acceptor.begColumn);
		} else {
			SemDesc.setNameDecl(acceptor, d);
		}
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		acceptor.expr.accept(this);

		Integer una = SemDesc.getActualConst(acceptor.expr);
		if (una == null)
			return;
		switch (acceptor.oper) {
		case AbsUnExpr.ADD:
			SemDesc.setActualConst(acceptor, una);
			break;
		case AbsUnExpr.SUB:
			SemDesc.setActualConst(acceptor, -una);
			break;
		case AbsUnExpr.NOT:
			SemDesc.setActualConst(acceptor, ~una);
			break;
		case AbsUnExpr.MEM:
			// TODO: MEM
			break;
		case AbsUnExpr.VAL:
			// TODO: CAL
			break;
		}
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		for (AbsValExpr e : acceptor.exprs) {
			e.accept(this);
		}
	}

	@Override
	public void visit(AbsValName acceptor) {

		if (record != 0)
			return;

		AbsDecl d = SemTable.fnd(acceptor.name);

		if (d == null) {
			error = true;
			Report.warning(acceptor.name + " not declared");
		} else {
			SemDesc.setNameDecl(acceptor, d);
			Integer v = SemDesc.getActualConst(d);
			if (v != null) {
				SemDesc.setActualConst(acceptor, v);
			}
		}
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (SemIllegalInsertException e) {
			error = true;
			Report.warning(String.format("Var '%s' already declared",
					acceptor.name.name), acceptor.begLine, acceptor.begColumn);
		}
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
	}

}
