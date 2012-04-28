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
import compiler.abstree.tree.AbsStmts;
import compiler.abstree.tree.AbsTypeDecl;
import compiler.abstree.tree.AbsTypeName;
import compiler.abstree.tree.AbsUnExpr;
import compiler.abstree.tree.AbsValExprs;
import compiler.abstree.tree.AbsValName;
import compiler.abstree.tree.AbsVarDecl;
import compiler.abstree.tree.AbsWhileStmt;
import compiler.report.Report;

public class SemNameResolver implements AbsVisitor {

	public boolean error;

	@Override
	public void visit(AbsAlloc acceptor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		// TODO Auto-generated method stub

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
			Report.warning(String.format("Set actual const error at (%d, %d)",
					acceptor.begLine, acceptor.endLine));
		}
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		acceptor.fstExpr.accept(this);
		acceptor.sndExpr.accept(this);

		Integer fst = SemDesc.getActualConst(acceptor.fstExpr);
		Integer snd = SemDesc.getActualConst(acceptor.sndExpr);
		System.out.println(snd);
		switch (acceptor.oper) {
		case AbsBinExpr.ADD:
			SemDesc.setActualConst(acceptor, fst + snd);
			break;
		case AbsBinExpr.SUB:
			SemDesc.setActualConst(acceptor, fst - snd);
			break;
		case AbsBinExpr.DIV:
			SemDesc.setActualConst(acceptor, fst / snd);
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
		case AbsBinExpr.ARRACCESS:
			SemDesc.setActualConst(acceptor, fst - snd);
			break;
		case AbsBinExpr.RECACCESS:
			SemDesc.setActualConst(acceptor, fst - snd);
			break;
		}

	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsConstDecl acceptor) {

		try {
			SemTable.ins(acceptor.name.name, acceptor);
		} catch (Exception e) {
			// TODO: handle exception
		}
		acceptor.value.accept(this);
		Integer ac = SemDesc.getActualConst(acceptor.value);

		if (ac != null) {
			SemDesc.setActualConst(acceptor, ac);
		} else {
			Report.warning(String.format("'%s' is null at (%d, %d)",
					acceptor.name.name, acceptor.begLine, acceptor.begColumn));
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
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AbsForStmt acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsNilConst acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsPointerType acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsProgram acceptor) {
		acceptor.name.accept(this);
		acceptor.decls.accept(this);
		// acceptor.stmt.accept(this);
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsStmts acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsTypeName acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsValExprs acceptor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AbsValName acceptor) {
		AbsDecl d = SemTable.fnd(acceptor.name);
		if (d == null) {
			Report.warning(acceptor.name + " not declared");
		} else {
			SemDesc.setNameDecl(acceptor, d);
		}
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		// TODO Auto-generated method stub

	}

}
