package ricoh.es.sql;

public class Parametres {
	/**
	 * @return the database
	 */
	public String getDatabase() {
		return Database;
	}
	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		Database = database;
	}
	/**
	 * @return the field
	 */
	public String getField() {
		return Field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		Field = field;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return Type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		Type = type;
	}
	/**
	 * @return the null
	 */
	public String getNull() {
		return Null;
	}
	/**
	 * @param null1 the null to set
	 */
	public void setNull(String null1) {
		Null = null1;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return Key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		Key = key;
	}
	/**
	 * @return the default
	 */
	public String getDefault() {
		return Default;
	}
	/**
	 * @param default1 the default to set
	 */
	public void setDefault(String default1) {
		Default = default1;
	}
	/**
	 * @return the extra
	 */
	public String getExtra() {
		return Extra;
	}
	/**
	 * @param extra the extra to set
	 */
	public void setExtra(String extra) {
		Extra = extra;
	}

	public Parametres(String database, String tablesIn) {
		super();
		Database = database;
		Tables_in = tablesIn;
	}
	public Parametres(String field, String type, String null1, String key,
			String default1, String extra) {
		super();
		Field = field;
		Type = type;
		Null = null1;
		Key = key;
		Default = default1;
		Extra = extra;
	}
	/**
	 * @return the tables_in
	 */
	public String getTables_in() {
		return Tables_in;
	}
	/**
	 * @param tablesIn the tables_in to set
	 */
	public void setTables_in(String tablesIn) {
		Tables_in = tablesIn;
	}
	
	/**
	 * @return the table_type
	 */
	public String getTable_type() {
		return Table_type;
	}

	public Parametres(String database, String tablesIn, String tableType) {
		super();
		Database = database;
		Tables_in = tablesIn;
		Table_type = tableType;
	}
	public Parametres(String database) {
		super();
		Database = database;
	}
	private String Database;
	private String Tables_in;
	private String Field;
	private String Type;
	private String Null;
	private String Key;
	private String Default;
	private String Extra;
	private String Table_type;

}

