package compiler.frames;

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
import compiler.semanal.SemDesc;
import compiler.semanal.type.SemType;

public class FrmEvaluator implements AbsVisitor {

	private int sizeArgs;
	private boolean isAnyCall;

	@Override
	public void visit(AbsAtomType acceptor) {
	}

	@Override
	public void visit(AbsConstDecl acceptor) {
	}

	@Override
	public void visit(AbsFunDecl acceptor) {
		FrmFrame frame = new FrmFrame(acceptor, SemDesc.getScope(acceptor));
		for (AbsDecl decl : acceptor.pars.decls) {
			if (decl instanceof AbsVarDecl) {
				AbsVarDecl varDecl = (AbsVarDecl) decl;
				FrmArgAccess access = new FrmArgAccess(varDecl, frame);
				FrmDesc.setAccess(varDecl, access);

			}
		}
		for (AbsDecl decl : acceptor.decls.decls) {
			if (decl instanceof AbsVarDecl) {
				AbsVarDecl varDecl = (AbsVarDecl) decl;
				FrmLocAccess access = new FrmLocAccess(varDecl, frame);
				frame.locVars.add(access);
				FrmDesc.setAccess(varDecl, access);
			}
			decl.accept(this);
		}
		sizeArgs = 0;
		isAnyCall = false;
		acceptor.stmt.accept(this);
		frame.sizeArgs = sizeArgs;
		if (isAnyCall)
			frame.sizeArgs += 4;
		FrmDesc.setFrame(acceptor, frame);
	}

	@Override
	public void visit(AbsProgram acceptor) {

		for (AbsDecl decl : acceptor.decls.decls) {
			if (decl instanceof AbsVarDecl) {
				// Setting global var access:
				AbsVarDecl varDecl = (AbsVarDecl) decl;
				FrmVarAccess access = new FrmVarAccess(varDecl);
				FrmDesc.setAccess(varDecl, access);
			}
			decl.accept(this);
		}
	}

	@Override
	public void visit(AbsProcDecl acceptor) {
		FrmFrame frame = new FrmFrame(acceptor, SemDesc.getScope(acceptor));
		for (AbsDecl decl : acceptor.pars.decls) {
			if (decl instanceof AbsVarDecl) {
				// Setting argument var access:
				AbsVarDecl varDecl = (AbsVarDecl) decl;
				FrmArgAccess access = new FrmArgAccess(varDecl, frame);
				FrmDesc.setAccess(varDecl, access);
			}
		}
		for (AbsDecl decl : acceptor.decls.decls) {
			if (decl instanceof AbsVarDecl) {
				// Setting local var access:
				AbsVarDecl varDecl = (AbsVarDecl) decl;
				FrmLocAccess access = new FrmLocAccess(varDecl, frame);
				frame.locVars.add(access);
				FrmDesc.setAccess(varDecl, access);
			}
			decl.accept(this);
		}
		sizeArgs = 0;
		isAnyCall = false;
		acceptor.stmt.accept(this);
		frame.sizeArgs = sizeArgs;
		if (isAnyCall)
			frame.sizeArgs += 4;
		FrmDesc.setFrame(acceptor, frame);
	}

	@Override
	public void visit(AbsRecordType acceptor) {
		int offset = 0;
		for (AbsDecl decl : acceptor.fields.decls)
			if (decl instanceof AbsVarDecl) {
				AbsVarDecl varDecl = (AbsVarDecl) decl;
				FrmCmpAccess access = new FrmCmpAccess(varDecl, offset);
				FrmDesc.setAccess(varDecl, access);
				offset += SemDesc.getActualType(varDecl.type).size();
			}
	}

	@Override
	public void visit(AbsTypeDecl acceptor) {
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsVarDecl acceptor) {
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsAlloc acceptor) {
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsArrayType acceptor) {
		acceptor.type.accept(this);

	}

	@Override
	public void visit(AbsAssignStmt acceptor) {
		acceptor.dstExpr.accept(this);
		acceptor.srcExpr.accept(this);

	}

	@Override
	public void visit(AbsAtomConst acceptor) {
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		acceptor.fstExpr.accept(this);
		acceptor.sndExpr.accept(this);
	}

	@Override
	public void visit(AbsBlockStmt acceptor) {
		acceptor.stmts.accept(this);

	}

	@Override
	public void visit(AbsCallExpr acceptor) {
		isAnyCall = true;
		int arSize = 0;
		for (AbsValExpr e : acceptor.args.exprs) {
			SemType st = SemDesc.getActualType(e);
			if (st == null)
				Report.warning("SemType is null");
			else {
				arSize += st.size();
			}
		}
		if (arSize > sizeArgs)
			sizeArgs = arSize;
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
		acceptor.name.accept(this);
		acceptor.loBound.accept(this);
		acceptor.hiBound.accept(this);
	}

	@Override
	public void visit(AbsIfStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.thenStmt.accept(this);
		acceptor.elseStmt.accept(this);
	}

	@Override
	public void visit(AbsNilConst acceptor) {
	}

	@Override
	public void visit(AbsPointerType acceptor) {
		acceptor.type.accept(this);
	}

	@Override
	public void visit(AbsStmts acceptor) {
		for (AbsStmt s : acceptor.stmts) {
			s.accept(this);
		}
	}

	@Override
	public void visit(AbsTypeName acceptor) {
	}

	@Override
	public void visit(AbsUnExpr acceptor) {
		acceptor.expr.accept(this);
	}

	@Override
	public void visit(AbsValExprs acceptor) {
		for (AbsValExpr expr : acceptor.exprs) {
			expr.accept(this);
		}
	}

	@Override
	public void visit(AbsValName acceptor) {

	}

	@Override
	public void visit(AbsWhileStmt acceptor) {
		acceptor.cond.accept(this);
		acceptor.stmt.accept(this);
	}

}
