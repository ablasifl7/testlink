package ricoh.es;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import ricoh.es.sql.BaseDades;

public class DataBase {
	

	private BaseDades bbdd;
	public DataBase(String url, String user, String password) {
		super();
		bbdd = new BaseDades(url, user, password);
		bbdd.comprovarConnexio();
		
		
	}
	protected BaseDades getBbdd() {
		return bbdd;
	}
	private java.util.Map<Integer,java.util.Map<String,String>> map;
	private java.util.Map<String,String> mapValueStatus;
	protected void executedResultCustomFields(Integer testPlanId){
		map = new java.util.HashMap<Integer,java.util.Map<String,String>>();
		String database = "bitnami_testlink";
		String query = "select exec.tcversion_id, cfev.value, exec.`status` "
				+ "from executions exec,cfield_execution_values cfev where "
				+ "exec.testplan_id = ? and exec.id = cfev.execution_id order "
				+ "by exec.execution_ts;";
		Object[] values = {testPlanId}; 
		java.util.Map<String,java.util.ArrayList<Object>> mRes = bbdd.query(database, query, values);
		
		java.util.ArrayList<Object> tcversion_id = mRes.get("tcversion_id");
		java.util.ArrayList<Object> value = mRes.get("value");
		java.util.ArrayList<Object> status = mRes.get("status");
			
		for(int i=0;i<tcversion_id.size();i++){	

			Integer id = Integer.valueOf(tcversion_id.get(i).toString());
			if(!map.containsKey(id)){
				map.put(id, new java.util.HashMap<String,String>());
			}
			mapValueStatus = map.get(id);
			/*if(mapValueStatus == null){
				mapValueStatus = new java.util.TreeMap<String,String>();
			}*/
			mapValueStatus.put(value.get(i).toString(), status.get(i).toString());
			map.put(id, mapValueStatus);
		}


		
	}
	protected String getExecutedResultCustomFields(Integer testCaseVersionId,String browser){

		if (map.get( testCaseVersionId) == null){
			return ExecutionStatus.NOT_RUN.name();
		}else{
			mapValueStatus = map.get( testCaseVersionId);
		
			switch (map.get( testCaseVersionId).get(browser)){
			case "p":
				return ExecutionStatus.PASSED.name();
			case "b":
				return ExecutionStatus.BLOCKED.name();
			case "f":
				return ExecutionStatus.FAILED.name();
			default:
				return null;
			}
		}
	}
	

	
	
}
