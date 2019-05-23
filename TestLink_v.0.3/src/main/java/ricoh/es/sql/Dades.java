package ricoh.es.sql;


import java.util.ArrayList;

public class Dades {

	/**
	 * @return the registres
	 */
	public Object[][] getRegistres() {
		return registres;
	}
	/**
	 * @param registres the registres to set
	 */
	public void setRegistres(Object[][] registres) {
		this.registres = registres;
	}
	/**
	 * @return the sizeCol
	 */
	public ArrayList<Integer> getSizeCol() {
		return sizeCol;
	}
	/**
	 * @param sizeCol the sizeCol to set
	 */
	public void setSizeCol(ArrayList<Integer> sizeCol) {
		this.sizeCol = sizeCol;
	}
	/**
	 * @return the nomCol
	 */
	public String[] getNomCol() {
		return nomCol;
	}
	/**
	 * @param nomCol the nomCol to set
	 */
	public void setNomCol(String[] nomCol) {
		this.nomCol = nomCol;
	}
	/**
	 * @return the typeCol
	 */
	public ArrayList<Integer> getTypeCol() {
		return typeCol;
	}
	/**
	 * @param typeCol the typeCol to set
	 */
	public void setTypeCol(ArrayList<Integer> typeCol) {
		this.typeCol = typeCol;
	}

	public Dades(Object[][] registres, ArrayList<Integer> sizeCol,
			String[] nomCol, ArrayList<Integer> typeCol) {
		super();
		this.registres = registres;
		this.sizeCol = sizeCol;
		this.nomCol = nomCol;
		this.typeCol = typeCol;
	}


	Object[][] registres = null;
	ArrayList<Integer> sizeCol = null;
	String[] nomCol = null;
	ArrayList<Integer> typeCol = null;

	
}
