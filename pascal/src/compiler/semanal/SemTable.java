package compiler.semanal;

import java.util.HashMap;
import java.util.LinkedList;

import compiler.abstree.tree.AbsAtomType;
import compiler.abstree.tree.AbsBlockStmt;
import compiler.abstree.tree.AbsDecl;
import compiler.abstree.tree.AbsDeclName;
import compiler.abstree.tree.AbsDecls;
import compiler.abstree.tree.AbsFunDecl;
import compiler.abstree.tree.AbsPointerType;
import compiler.abstree.tree.AbsProcDecl;
import compiler.abstree.tree.AbsStmts;
import compiler.abstree.tree.AbsTypeExpr;
import compiler.abstree.tree.AbsVarDecl;
import compiler.frames.FrmDesc;
import compiler.frames.FrmFrame;
import compiler.report.Report;

public class SemTable {

	private static HashMap<String, LinkedList<AbsDecl>> mapping = new HashMap<String, LinkedList<AbsDecl>>();

	private static int scope = 0;
	
	static{
		//vgrajene funkcije
		try{			
			ins("putch", createProc("putch", new AbsAtomType(AbsAtomType.CHAR)));	
			ins("putint", createProc("putint", new AbsAtomType(AbsAtomType.INT)));	
			ins("getch", createFun("getch", null, new AbsAtomType(AbsAtomType.CHAR)));
			ins("getint", createFun("getint", null, new AbsAtomType(AbsAtomType.INT)));
			ins("ord", createFun("ord", new AbsAtomType(AbsAtomType.CHAR), new AbsAtomType(AbsAtomType.INT)));
			ins("chr", createFun("chr", new AbsAtomType(AbsAtomType.INT), new AbsAtomType(AbsAtomType.CHAR)));
			ins("free", createProc("free", new AbsPointerType(null)));
			
		}catch(Exception e){}
	}
	
	private static AbsProcDecl createProc(String name, AbsTypeExpr param){
		AbsDecls params = new AbsDecls();		
		if(param != null){
			params.decls.add(new AbsVarDecl(new AbsDeclName(""), param));
		}
		AbsProcDecl d = new AbsProcDecl(new AbsDeclName(name), params,  new AbsDecls(), new AbsBlockStmt(new AbsStmts()));
		FrmFrame frame = new FrmFrame(d, 1);		
		FrmDesc.setFrame(d, frame);
		
		return d;
	}
	
	private static AbsFunDecl createFun(String name, AbsTypeExpr param, AbsTypeExpr retParam){
		AbsDecls params = new AbsDecls();		
		if(param != null){
			params.decls.add(new AbsVarDecl(new AbsDeclName(""), param));
		}
		AbsFunDecl d = new AbsFunDecl(new AbsDeclName(name), params, retParam,  new AbsDecls(), new AbsBlockStmt(new AbsStmts()));
		FrmFrame frame = new FrmFrame(d, 1);		
		FrmDesc.setFrame(d, frame);
		
		return d;
	}

	public static void newScope() {
		scope++;
	}

	public static void oldScope() {
		LinkedList<String> allNames = new LinkedList<String>();
		allNames.addAll(mapping.keySet());
		for (String name : allNames) {
			try {
				SemTable.del(name);
			} catch (SemIllegalDeleteException _) {
			}
		}
		scope--;
	}

	public static void ins(String name, AbsDecl newDecl)
			throws SemIllegalInsertException {
		LinkedList<AbsDecl> allNameDecls = mapping.get(name);
		if (allNameDecls == null) {
			allNameDecls = new LinkedList<AbsDecl>();
			allNameDecls.addFirst(newDecl);
			SemDesc.setScope(newDecl, scope);
			mapping.put(name, allNameDecls);
			return;
		}
		if ((allNameDecls.size() == 0) || (SemDesc.getScope(allNameDecls.getFirst()) == null)) {
			Thread.dumpStack();
			Report.error("Internal error.", 1);
			return;
		}
		if (SemDesc.getScope(allNameDecls.getFirst()) == scope)
			throw new SemIllegalInsertException();
		allNameDecls.addFirst(newDecl);
		SemDesc.setScope(newDecl, scope);
	}

	public static void del(String name)
			throws SemIllegalDeleteException {
		LinkedList<AbsDecl> allNameDecls = mapping.get(name);
		if (allNameDecls == null)
			throw new SemIllegalDeleteException();
		if ((allNameDecls.size() == 0) || (SemDesc.getScope(allNameDecls.getFirst()) == null)) {
			Thread.dumpStack();
			Report.error("Internal error.", 1);
			return;			
		}
		if (SemDesc.getScope(allNameDecls.getFirst()) < scope)
			throw new SemIllegalDeleteException();
		allNameDecls.removeFirst();
		if (allNameDecls.size() == 0)
			mapping.remove(name);
	}

	public static AbsDecl fnd(String name) {
		LinkedList<AbsDecl> allNameDecls = mapping.get(name);
		if (allNameDecls == null)
			return null;
		if (allNameDecls.size() == 0)
			return null;
		return allNameDecls.getFirst();
	}

}
