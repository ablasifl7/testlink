package ricoh.es.sql;


public class Taules {
	private String taula;
	private String tipus;
	private boolean view;
	public Taules(String taula, String tipus, boolean view) {
		super();
		this.taula = taula;
		this.tipus = tipus;
		this.view = view;
	}
	/**
	 * @return the taula
	 */
	public String getTaula() {
		return taula;
	}
	/**
	 * @return the tipus
	 */
	public String getTipus() {
		return tipus;
	}
	/**
	 * @return the view
	 */
	public boolean isView() {
		return view;
	}

}

