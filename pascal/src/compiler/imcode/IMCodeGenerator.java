package compiler.imcode;

import java.util.LinkedList;

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

public class IMCodeGenerator implements AbsVisitor {
/**
 * Dostop do arraya: FP - offset + (type of integer * (odmik - lowbound)) 
 * Dostop do recorda: podobno kot array
 * 
 * Pozor: Bin operator od arraya vrne dejansko vrednost namesto naslova. (en MEM prevec, ki ga je potrebno odstraniti).
 * 
 * Izracuni dreves: 1. hash tabela, 2. spremenljivka v kateri vracamo.
 */

	/**
	 * V pascalu imamo lahko funkcije znotraj funkcij. Kako prevesti funkcijo ki ima znotraj sebe se 12 drugih fincij
	 * 	edini pameten nacin: kodo za prvo funkcijo (brez 12 notranjih) gre v en blok assembly kode, drugo funkcijo (eno od 12ih) damo v svoj blok itd.
	 * 
	 * v assembly vse te funkcije se napise zaporedno... Za vsako funkcijo zato naredis en chunk (to so code chunki), pozor
	 *  funkciji se lahko enako imenujejo (razlicno nestano), ne smejo imeti iste labele. internim funkcijam se daje imena, ki so sigurno unikatna
	 *  
	 * data chunk - namenjeni staticnim spremenljivkam - vse kar je pod program pod vars. Vsaka spremenljivka dobi svoj chunk.
	 * 
	 * 
	 */
	public LinkedList<ImcChunk> chunks;
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsAtomType acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsBinExpr acceptor) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsDeclName acceptor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(AbsDecls acceptor) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
