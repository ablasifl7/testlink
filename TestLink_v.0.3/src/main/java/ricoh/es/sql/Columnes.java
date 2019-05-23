package ricoh.es.sql;




public class Columnes {

	private String camp;
	private String tipus;
	private String nul;
	private String clau;
	private String defecte;
	private String extre;
	/**
	 * @return the camp
	 */
	public String getCamp() {
		return camp;
	}
	/**
	 * @return the tipus
	 */
	public String getTipus() {
		return tipus;
	}
	/**
	 * @return the nul
	 */
	public String getNul() {
		return nul;
	}
	/**
	 * @return the clau
	 */
	public String getClau() {
		return clau;
	}
	/**
	 * @return the defecte
	 */
	public String getDefecte() {
		return defecte;
	}
	/**
	 * @return the extre
	 */
	public String getExtre() {
		return extre;
	}
	public Columnes(String camp, String tipus, String nul, String clau,
			String defecte, String extre) {
		super();
		this.camp = camp;
		this.tipus = tipus;
		this.nul = nul;
		this.clau = clau;
		this.defecte = defecte;
		this.extre = extre;
	}
}
