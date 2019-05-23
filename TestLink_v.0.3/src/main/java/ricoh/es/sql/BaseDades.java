package ricoh.es.sql;


import java.io.File;
import java.sql.*;
import java.util.*;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class BaseDades {

    final private static String DriverName = "com.mysql.cj.jdbc.Driver";

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getTunnelUser() {
        return tunnelUser;
    }

    public void setTunnelUser(String tunnelUser) {
        this.tunnelUser = tunnelUser;
    }

    public String getTunnelPassword() {
        return tunnelPassword;
    }

    public void setTunnelPassword(String tunnelPassword) {
        this.tunnelPassword = tunnelPassword;
    }

    private boolean isTunnel() {
        return tunnel;
    }

    private void setTunnel(boolean tunnel) {
        this.tunnel = tunnel;
    }

    public BaseDades(String url, String user, String password) {
        super();
        this.url = url;
        this.user = user;
        this.password = password;
        setTunnel(false);
    }

    public BaseDades(String user, String password, int localPort,
            String remoteHost, String host, int remotePort,
            String tunnelUser, String tunnelPassword) {
        super();
        this.user = user;
        this.password = password;
        this.localPort = localPort;
        this.remoteHost = remoteHost;
        this.host = host;
        this.remotePort = remotePort;
        this.tunnelUser = tunnelUser;
        this.tunnelPassword = tunnelPassword;
        setTunnel(true);
    }

    public BaseDades() {
        super();
    }
    private String url;
    private String user;
    private String password;
    private boolean tunnel;
    private int localPort;
    private String remoteHost;
    private String host;
    private int remotePort;
    private String tunnelUser;
    private String tunnelPassword;
    static Connection cx = null;
    static PreparedStatement ps = null;
    static ResultSet rs = null;
    static Session session = null;
    static Statement st = null;
    ResultSet rsx = null;
    static ResultSetMetaData rsmd = null;

    static {
        try {
            Class.forName(DriverName);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    private void connection(String database) throws SQLException {
        String url = getUrl();
        if (database == null) {
            database = "";
        }
        if (database.length() != 0) {
            url += "/" + database;
        }
        cx = DriverManager.getConnection(url, getUser(), getPassword());
 
        if (cx != null) {
            if (database.equals("")) {
                System.out.println("Estem conectats ....");
            } else {
                System.out.println("Estem conectats la base de dades '"+database+"'");
            }
        }
    }

    private void tunnelConnection(String database) throws SQLException, JSchException {
        String url = "jdbc:mysql://localhost:" + getLocalPort();
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        session = jsch.getSession(getTunnelUser(), getHost(), 22);
        session.setPassword(getTunnelPassword());
        session.setConfig(config);
        session.connect();
        session.setPortForwardingL(getLocalPort(), getRemoteHost(), getRemotePort());
        //Class.forName(DriverName).newInstance();

        if (database == null) {
            database = "";
        }
        if (database.length() != 0) {
            url += "/" + database;
        }
        cx = DriverManager.getConnection(url, getUser(), getPassword());
    }

    public void connexio(String database) {
        try {
            if (isTunnel()) {
                tunnelConnection(database);
            } else { 	
                connection(database);
            }
        } catch (SQLException e) {
            System.out.println(e);
        } catch (JSchException e) {
        }
    }

    private void closeConnection() throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (cx != null) {
            cx.close();
        }
    }

    public void tancarConnexio() {
        try {
            closeConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /********************************************/
    public String comprovarConnexio() {
        try {
            connection(null);
            closeConnection();
            return null;
        } catch (SQLException e) {
            return e.getMessage();
        }
    }
	/*************/
    public String query(String database, String query , boolean update){
        String results = null;
        try {
            connexio(database);
            if (update) {
                st = cx.createStatement();
                st.executeUpdate(query);
            } else {
                ps = cx.prepareStatement(query);
                st = cx.createStatement();
                ps.execute();
            }
            results = "OK";
        } catch (SQLException e) {
                        results = "ERROR\n";
            // TODO Auto-generated catch block
            results += e.getMessage();
        } finally {
            tancarConnexio();
        }
        return results;
    }


	private void query(String query) throws SQLException {
		st = cx.createStatement();
		st.executeUpdate(query);
	}
	public void queryCrearTaula(String database,String query){
		try {
			connexio(database);
			query(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tancarConnexio();
		}
	}
	public void queryModificarTaula(String database,String query){
		try {
			connexio(database);
			query(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tancarConnexio();
		}
	}
	public void queryIncertarDadesTaula(String database,String query){
		try {
			connexio(database);
			query(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			tancarConnexio();


		}
	}
	/*************/
    private Dades queryData(String query, Object[] values) throws
            SQLException, NullPointerException, ClassNotFoundException {
        Object[][] registres = null;
        ArrayList<Integer> sizeCol = null;
        String[] nomCol = null;
        ArrayList<Integer> typeCol = null;
        ps = cx.prepareStatement(query);
        st = cx.createStatement();
        if (values == null) {
            values = new Object[0];
        }
        for (int i = 0; i < values.length; i++) {
            definedClass(ps, values, i);
        }
        //rsmd = (ResultSetMetaData) st.executeQuery(query).getMetaData();
        rsmd = (ResultSetMetaData) ps.executeQuery().getMetaData();
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Vector<Object[]> vr = new Vector<Object[]>();
        sizeCol = new ArrayList<Integer>();
        ArrayList<String> alnc = new ArrayList<String>();
        typeCol = new ArrayList<Integer>();
        int numCol = rsmd.getColumnCount();
        for (int i = 0; i < numCol; i++) {
            sizeCol.add(rsmd.getColumnDisplaySize(i + 1));
            alnc.add(rsmd.getColumnLabel(i + 1));
            typeCol.add(rsmd.getColumnType(i + 1));
        }
        rs = ps.executeQuery();
        while (rs.next()) {
            Object[] ob = new Object[numCol];
            for (int i = 0; i < numCol; i++) {
                ob[i] = rs.getObject(i + 1);
            }
            vr.add(ob);
        }
        registres = new Object[vr.size()][numCol];
        for (int i = 0; i < vr.size(); i++) {
            registres[i] = vr.get(i);
        }
        nomCol = new String[numCol];
        for (int i = 0; i < numCol; i++) {
            nomCol[i] = alnc.get(i);
        }
        ps.execute();
        return new Dades(registres, sizeCol, nomCol, typeCol);
    }

    public Dades queryDades(String database, String query, Object[] values) {
        try {
            connexio(database);
            return queryData(query, values);
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }

    private HashMap<String, ArrayList<Object>> query(String query,
            Object[] values) throws NullPointerException,
            SQLException, ClassNotFoundException {
        HashMap<String, ArrayList<Object>> hm = new HashMap<String, ArrayList<Object>>();
        ArrayList<Object> colName = new ArrayList<Object>();
        ps = cx.prepareStatement(query);
        if (values == null) {
            values = new Object[0];
        }

        for (int i = 0; i < values.length; i++) {
            definedClass(ps, values, i);
        }

        hm.put(COLUMN_NAME, new ArrayList<Object>());
        hm.put(COLUMN_SIZE, new ArrayList<Object>());
        hm.put(COLUMN_TYPE, new ArrayList<Object>());

        st = cx.createStatement();
        rs = ps.executeQuery();
        int numCol = rs.getMetaData().getColumnCount();
        for (int i = 0; i < numCol; i++) {
            hm.get(COLUMN_NAME).add(rs.getMetaData().getColumnName(i + 1));
            hm.get(COLUMN_SIZE).add(rs.getMetaData().getColumnDisplaySize(i + 1));
            hm.get(COLUMN_TYPE).add(rs.getMetaData().getColumnType(i + 1));

            hm.put(rs.getMetaData().getColumnName(i + 1), new ArrayList<Object>());
        }
        while (rs.next()) {
            colName = hm.get(COLUMN_NAME);
            for (int i = 0; i < colName.size(); i++) {
                hm.get(colName.get(i)).add(rs.getObject((String) colName.get(i)));
            }
        }
        ps.execute();
        return hm;
    }

    public HashMap<String, ArrayList<Object>> query(String database, String query, Object[] values) {
        try {
            connexio(database);
            return query(query, values);
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }

    private ArrayList<String> showDatabase() throws SQLException {
        ArrayList<String> al = new ArrayList<String>();
        ps = cx.prepareStatement("Show databases");
        rs = ps.executeQuery();
        while (rs.next()) {
            al.add(rs.getString("Database"));
        }
        //ps.execute();
        return al;
    }

    public ArrayList<String> mostrarBaseDeDades() {
        try {
            connexio(null);
            return showDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }

    private void createDataBase(String database) throws SQLException, SQLSyntaxErrorException {
        st = cx.createStatement();
        st.executeUpdate("create DATABASE if not exists " + database);
    }

    public void crearBaseDeDades(String database) {
        try {
            connexio(null);
            createDataBase(database);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    public String crearBaseDeDadesEx(String database) {
        String ex = null;
        try {
            connexio(null);
            createDataBase(database);
        } catch (Exception e) {
            ex = e.getMessage();
        } finally {
            tancarConnexio();
        }
        return ex;
    }

    private void deleteDatabase(String database) throws SQLException, SQLSyntaxErrorException {
        st = cx.createStatement();
        st.executeUpdate("drop DATABASE if exists " + database);
    }

    public void borrarBaseDeDades(String database) {
        try {
            connexio(null);
            deleteDatabase(database);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    public String borrarBaseDeDadesEx(String database) {
        String ex = null;
        try {
            connexio(null);
            deleteDatabase(database);
        } catch (Exception e) {
            ex = e.getMessage();
        } finally {
            tancarConnexio();
        }
        return ex;
    }

    private ArrayList<String> showTables(String database) throws SQLException {
        ArrayList<String> al = new ArrayList<String>();
        ps = cx.prepareStatement("Show tables");
        rs = ps.executeQuery();
        while (rs.next()) {
            al.add(rs.getString("Tables_in_" + database));
        }
        ps.execute();
        return al;
    }

    public ArrayList<String> mostrarTaules(String database) {
        try {
            connexio(database);
            return showTables(database);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }

    public boolean existeixTaula(String database, String table) {
        try {
            connexio(database);
            for (String tables : showTables(database)) {
                if (0 == table.compareToIgnoreCase(tables)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return false;
    }

    private ArrayList<Parametres> showFullTables(String database) throws SQLException {
        ArrayList<Parametres> al = new ArrayList<Parametres>();
        ps = cx.prepareStatement("show full tables");
        rs = ps.executeQuery();
        Parametres param = null;
        while (rs.next()) {
            param = new Parametres(database, rs.getString("Tables_in_" + database), rs.getString("Table_type"));
            al.add(param);
        }
        return al;
    }

    public ArrayList<Parametres> mostrarTipusTaules(String database) {
        try {
            connexio(database);
            return showFullTables(database);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }

    private void deleteAllRows(String table) throws SQLException {
        st = cx.createStatement();
        st.executeUpdate("TRUNCATE TABLE " + table);
    }

    public void borrarTotsElsRegistres(String database, String table) {
        try {
            connexio(database);
            deleteAllRows(table);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    private void deleteTable(String table, boolean update) throws SQLException {
        if (update) {
            st = cx.createStatement();
            st.executeUpdate("DROP TABLE IF EXISTS " + table);
        } else {
            ps = cx.prepareStatement("DROP TABLE IF EXISTS " + table);
            ps.executeQuery();
        }
    }
    //�s possible table= "TABLE1,TABLE2,TABLE3..."

    public void borrarTaula(String database, String table, boolean update) {
        try {
            connexio(database);
            deleteTable(table, update);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    private ArrayList<Parametres> showColumns(String table) throws SQLException{
        ArrayList<Parametres> al = new ArrayList<Parametres>();
        ps = cx.prepareStatement("Show columns from " + table);
        rs = ps.executeQuery();

        Parametres param = null;
        while (rs.next()) {
            param = new Parametres(rs.getString("Field"),
                    rs.getString("Type"),
                    rs.getString("Null"),
                    rs.getString("Key"),
                    rs.getString("Default"),
                    rs.getString("Extra"));
            al.add(param);
        }
        return al;
    }

    public ArrayList<Parametres> mostrarColumnes(String database, String table) {
        try {
            connexio(database);
            return showColumns(table);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }

    public Dades mostrarColumnasDades(String database, String table) {
        String query = "Show columns from " + table;
        try {
            connexio(database);
            return queryData(query, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }

        return null;
        //Dades queryData(String query, Object[] values)
    }

    private String showCreateTable(String table_or_view, boolean view) throws SQLException {
        String s = null;
        if (view) {
            ps = cx.prepareStatement("Show create view " + table_or_view);
        } else {
            ps = cx.prepareStatement("Show create table " + table_or_view);
        }
        rs = ps.executeQuery();
        while (rs.next()) {
            s = rs.getString(2);
        }
        return s;
    }

    public String mostrarCrearTaula(String database, String table_or_view, boolean view) {
        try {
            connexio(database);
            return showCreateTable(table_or_view, view);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return null;
    }
/////////////////////////////////////////////////////////////////////////////////

    public String incertarDades(String database, String table, String[] columns,
            Object[] values) {
        String results = null;
        try {
            connexio(database);
            if (columns == null || values == null) {
                throw new IllegalArgumentException("columns or values is null");
            } else if (columns.length == 0 || values.length == 0) {
                throw new IllegalArgumentException("columns.size() or values.size() is zero");
            } else if (values.length % columns.length != 0) {
                throw new IllegalArgumentException("values.size() % columns.size() != 0");
            }
            String query = "INSERT INTO " + table + " (" + columns[0];
            for (int i = 1; i < columns.length; i++) {
                query += "," + columns[i];
            }
            query += ") VALUES (?";
            for (int j = 0; j < values.length / columns.length; j++) {
                for (int i = 1; i < columns.length; i++) {
                    query += ",?";
                }
                if (j != values.length / columns.length - 1) {
                    query += "),(?";
                }
            }
            query += ");";
            ps = cx.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                definedClass(ps, values, i);
            }
            ps.execute();
            results = "OK";
        } catch (SQLException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } catch (IllegalArgumentException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } catch (ClassNotFoundException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } finally {
            tancarConnexio();
        }
        return results;
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    private void insertData(String table, ArrayList<String> columns,
            ArrayList<Object> values) throws  SQLException,
            IllegalArgumentException {
        if (columns == null || values == null) {
            throw new IllegalArgumentException("columns or values is null");
        } else if (columns.size() == 0 || values.size() == 0) {
            throw new IllegalArgumentException("columns.size() or values.size() is zero");
        } else if (values.size() % columns.size() != 0) {
            throw new IllegalArgumentException("values.size() % columns.size() != 0");
        }
        String query = "INSERT INTO " + table + " (" + columns.get(0);
        for (int i = 1; i < columns.size(); i++) {
            query += "," + columns.get(i);
        }
        query += ") VALUES (?";
        for (int j = 0; j < values.size() / columns.size(); j++) {
            for (int i = 1; i < columns.size(); i++) {
                query += ",?";
            }
            if (j != values.size() / columns.size() - 1) {
                query += "),(?";
            }
        }
        query += ");";
        ps = cx.prepareStatement(query);
        for (int i = 0; i < values.size(); i++) {
            try {
				definedClass(ps, values.toArray(), i);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        ps.execute();

    }

    public void incertarDades(String database, String table, ArrayList<String> columns,
            ArrayList<Object> values) {
        try {
            connexio(database);
            insertData(table, columns, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
  
        } finally {
            tancarConnexio();
        }
    }

    private void updateData(String table, String set, String where, ArrayList<Object> values) throws SQLException,  ClassNotFoundException, SQLException {
        String query = "UPDATE " + table + " SET " + set + " " + where;
        ps = cx.prepareStatement(query);
        for (int i = 0; i < values.size(); i++) {
            definedClass(ps, values.toArray(), i);
        }
        ps.execute();
    }
////////////////////////////////////
    public String modificarDades(String database, String table, String set, String where, Object[] values) {
        String results = null;
        //set   = "COL1 = ?,COL2 = ?, COL3 = ?,..."
        //where = "WHERE "+COL_ID1+" = ? OR/AND COL_ID2+" = ?,..."
        //values: valueCOL1, valueCOL2, valueCOL3,..., valueCOL_ID1, valueCOL_ID2,...
        try {
            connexio(database);
            String query = "UPDATE " + table + " SET " + set + " " + where;
            ps = cx.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                definedClass(ps, values, i);
            }
            ps.execute();
            results = "OK";
        } catch (SQLException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } catch (IllegalArgumentException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } catch (ClassNotFoundException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } finally {
            tancarConnexio();
        }
        return results;

    }

/////////////////////////////////////
    public void modificarDades(String database, String table, String set, String where, ArrayList<Object> values) {
        //set   = "COL1 = ?,COL2 = ?, COL3 = ?,..."
        //where = "WHERE "+COL_ID1+" = ? OR/AND COL_ID2+" = ?,..."
        //values: valueCOL1, valueCOL2, valueCOL3,..., valueCOL_ID1, valueCOL_ID2,...
        try {
            connexio(database);
            updateData(table, set, where, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    public void modificarDades(String database, String table, String columnId, Object id, ArrayList<String> columns,
            ArrayList<Object> values) {
        try {
            connexio(database);
            String set = "";
            values.add(id);
            for (int i = 0; i < columns.size(); i++) {
                if (i == 0) {
                    set = columns.get(i) + " = ?";
                } else {
                    set += ", " + columns.get(i) + " = ?";
                }
            }
            String where = "WHERE " + columnId + " = ?;";
            updateData(table, set, where, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    private void deleteData(String table, String where,
            ArrayList<Object> values) throws SQLException,
            ClassNotFoundException, SQLException {
        String query = "DELETE FROM " + table + " " + where;

        ps = cx.prepareStatement(query);
        for (int i = 0; i < values.size(); i++) {
            definedClass(ps, values.toArray(), i);
        }
        ps.execute();
    }

    public void borrarDada(String database, String table, String where, ArrayList<Object> values) {
        //where = "COL1 = ? AND/OR = COL2 = ? ...more conditions"
        //value: valueCOL1, valueCOL2,...
        try {
            connexio(database);
            deleteData(table, where, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }

    }

    public void borrarDada(String database, String table, String columnId, Object id) {
        try {
            connexio(database);
            String where = "WHERE " + columnId + " = ?;";
            ArrayList<Object> al = new ArrayList<Object>();
            al.add(id);
            deleteData(table, where, al);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }
/////////////////////////////////////////////
    public String borrarDada(String database, String table, String where, Object[] values) {
        //where = "COL1 = ? AND/OR = COL2 = ? ...more conditions"
        //value: valueCOL1, valueCOL2,...
        String results = null;
                    String query = "DELETE FROM " + table + " " + where;
        try {
            connexio(database);

            ps = cx.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                definedClass(ps, values, i);
            }
            ps.execute();
            results = "";

        } catch (SQLException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } catch (IllegalArgumentException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } catch (ClassNotFoundException e) {
            results = "ERROR\n";
            results += e.getMessage();
        } finally {
            tancarConnexio();
        }
        return results;
    }

/////////////////////////////////////////////

    private void createTableView(String[] queries) throws SQLException {
        st = cx.createStatement();
        for (int i = 0; i < queries.length; i++) {
            st.executeUpdate(queries[i]);
        }
    }

    public void crearTaulaVista(String database, String[] queries) {
        try {
            connexio(database);
            createTableView(queries);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    public void crearTaulaVista(String database, String query) {
        try {
            connexio(database);
            createTableView(new String[]{query});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }
/////////////////////////////////////
        public long numRegistres(String database, String table, String where, Object[] values) {
         //where = "WHERE "+COL_ID1+" = ? OR/AND COL_ID2+" = ?,..."
        //values: valueCOL1, valueCOL2, valueCOL3,..., valueCOL_ID1, valueCOL_ID2,...
        String query = "select count(*) from "+table+" "+where;
        try {
            connexio(database);
            return (Long)queryData(query, values).getRegistres()[0][0];
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
        return 0;
    }
/////////////////////////////////////
    private void export(String database, String table, String file, int from, int max) throws SQLException {
        String query = "select * from " + table;
        if (from < 0 || max < 0) {
            query += " ";
        } else {
            query += " limit " + from + "," + max + " ";
        }
        (new File(file)).delete();
        query += "INTO OUTFILE \n'" + file.replace("\\", "/") + "' "
                + "FIELDS TERMINATED BY ';' "
                + "OPTIONALLY ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\n\r';";
        ps = cx.prepareStatement(query);
        ps.execute();
    }

    public void exportar(String database, String table, String file, int from, int max) {
        try {
            connexio(database);
            export(database, table, file, from, max);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    public void exportar(String database, String table, String file) {
        try {
            connexio(database);
            export(database, table, file, -1, -1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    private void importData( String table, String file, String[] columns, boolean remplace, int ignoreLines)
            throws SQLException {
        String query = "LOAD DATA INFILE \n'" + file.replace("\\", "/") + "'";
        if (remplace) {
            query += " REPLACE ";
        } else {
            query += " ";
        }
        query += "INTO TABLE " + table + " "
                + "FIELDS TERMINATED BY ';' "
                + "OPTIONALLY ENCLOSED BY '\"' "
                + "LINES TERMINATED BY '\n\r' "
                + "IGNORE " + ignoreLines + " LINES "
                + "(" + columns[0];
        for (int i = 1; i < columns.length; i++) {
            query += "," + columns[i];
        }
        query += ");";
        ps = cx.prepareStatement(query);
        ps.execute();
    }

    public void importar(String database, String table, String file, String[] columns, boolean remplace, int ignoreLines) {
        try {
            connexio(database);
            importData( table, file, columns, remplace, ignoreLines);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }

    public void importar(String database, String table, String file, boolean remplace) {
        try {
            connexio(database);
            ArrayList<Parametres> alp = showColumns(table);
            String[] columns = new String[alp.size()];
            for (int i = 0; i < alp.size(); i++) {
                columns[i] = alp.get(i).getField();
            }
            importData(table, file, columns, remplace, 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            tancarConnexio();
        }
    }
    /**************************************************************************************************************************************/
    final public static String COLUMN_NAME = "COLUMN NAME";
    final public static String COLUMN_SIZE = "COLUMN SIZE";
    final public static String COLUMN_TYPE = "COLUMN TYPE";

    /***********************************************************/
    private static void definedClass(PreparedStatement ps, Object[] values,
            int i) throws ClassNotFoundException, SQLException {
        if (values[i].getClass().equals(Class.forName("java.lang.Byte"))) {
            ps.setByte((i + 1), (Byte) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Short"))) {
            ps.setShort((i + 1), (Short) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Integer"))) {
            ps.setInt((i + 1), (Integer) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Long"))) {
            ps.setLong((i + 1), (Long) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Float"))) {
            ps.setFloat((i + 1), (Float) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Double"))) {
            ps.setDouble((i + 1), (Double) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.String"))) {
            ps.setString((i + 1), (String) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Boolean"))) {
            ps.setBoolean((i + 1), (Boolean) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.lang.Character"))) {
            ps.setString((i + 1), (String) values[i]);
        } else if (values[i].getClass().equals(Class.forName("java.sql.Date"))) {
            ps.setDate((i + 1), (java.sql.Date) values[i]);
        } else {
            ps.setString((i + 1), values[i].toString());
        }


    }

    public java.lang.Class mostrarClasse(int type) {
        try {
            return getClass(type);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private java.lang.Class getClass(int type) throws ClassNotFoundException {
        String className;
        switch (type) {
            case 1://char,enum,set
                className = "java.lang.String";
                break;
            case 12://varchar
                className = "java.lang.String";
                break;
            case -1://tinytext,mediumtext,longtext
                className = "java.lang.String";
                break;
            case -7://boolean,tinyint
                className = "java.lang.Boolean";
                break;
            case 5://smalltext
                className = "java.lang.String";
                break;
            case 4://mediumint,int
                className = "java.lang.Integer";
                break;
            case -5://bigint
                className = "java.lang.Long";
                break;
            case 7://float
                className = "java.lang.Float";
                break;
            case 8://double
                className = "java.lang.Double";
                break;
            case 3://decimal
                className = "java.math.BigDecimal";
                break;
            case 91://data
                className = "java.lang.String";
                break;
            case 93://datatime,timestamp
                className = "java.util.Date";
                break;
            case 92://time
                className = "java.sql.Time";
                break;
            default:
                className = "java.lang.String";
                break;
        }

        return Class.forName(className);
    }
        /*
         * 1  char
         * 12 varchar
         * -1 tinytext
         * -1 text
         * -1 mediumtext
         * -1 longtext
         * -7 boolean
         * -7 tinyint
         * 5  smalltext
         * 4  mediumint
         * 4  int
         * -5 bigint
         * 7  float
         * 8  double
         * 3  decimal
         * 91 data YYYY-MM-DD 0001-01-01 < 9999-12-31
         * 93 datatime YYYY-MM-DD 1753-01-01 < 9999-12-31
         * 93 timestamp YYYY-MM-DD HH:MI:SS
         * 92 time hh:mm:ss 00:00:00.0000000 < 23:59:59.9999999
         * 1  enum An ENUM is a string object with a value chosen from a list
         * 1  set A SET value must be the empty string or a value consisting only of the values listed in the column definition separated by commas.
         */

}

