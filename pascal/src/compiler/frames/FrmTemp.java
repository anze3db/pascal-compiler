package compiler.frames;

/** Opis zacasne spremenljivke v programu.  */
public class FrmTemp {
/**
 * 
 * ker je potrebno pri dolocenih procesorjih zacasne reultate shraniti, ker ni dovolj registrov.
 * 
 * trenutno damo na 0
 */
	private static int count = 0;

	private int num;

	public FrmTemp() {
		num = count++;
	}

	@Override
	public boolean equals(Object t) {
		return num == ((FrmTemp)t).num;
	}

	public String name() {
		return "T" + num;
	}

}
