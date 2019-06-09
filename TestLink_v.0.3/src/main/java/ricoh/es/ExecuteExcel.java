package ricoh.es;

import javax.swing.JOptionPane;

public class ExecuteExcel {
/*
	private static String path;
	private static String customField;
	private static String[] customFieldHeders;
	private static String[] customFieldValues;
	private static String mainSheet;
	private static String[] environmentSheet;
	private static String environmentHeader;
	private static String testPlanHeader;
	private static String buildHeader;
	private static int mainHeaderRow;
	private static int environmentHeaderRow;
	private static String[] resultValues;
	private static String[] resultRelations;
	private static String codeHeader;
	private static String testHeader;
	private static String commentHeader;
*/
	protected static java.util.HashMap<String,String> hmResults;
	private static java.util.HashMap<String,String> hmCustomFields;
	
	//private static Object[][] ob;
	private static java.util.ArrayList<Object[]> al = null;
	
	final static int Col_SELECT = 0; 
	final static int Col_CODE = 1; 
	final static int Col_ENVIRONMENT = 2; 
	final static int Col_TESTPLAN = 3; 
	final static int Col_BUILD = 4; 
	final static int Col_BROWSER = 5; 
	final static int Col_RESULT = 6; 
	final static int Col_EXECUTE_RESULTS = 7; 
	final static int Col_TESTNAME = 8; 
	final static int Col_NOTES = 9; 
	
	protected static void upload(String path, String customField,
			String[] customFieldHeders,
			String[] customFieldValues,String mainSheet,String[] environmentSheet,
			String environmentHeader,String testPlanHeader,String buildHeader,
			int mainHeaderRow,int environmentHeaderRow,
			String[] resultValues,String[] resultRelations,String codeHeader,
			String testHeader,String commentHeader){
	/*
		ExecuteExcel.path = path;
		ExecuteExcel.customField = customField;
		ExecuteExcel.customFieldHeders = customFieldHeders;
		ExecuteExcel.customFieldValues = customFieldValues;
		ExecuteExcel.mainSheet = mainSheet;
		ExecuteExcel.environmentSheet = environmentSheet;
		ExecuteExcel.environmentHeader = environmentHeader;
		ExecuteExcel.testPlanHeader = testPlanHeader;
		ExecuteExcel.buildHeader = buildHeader;
		ExecuteExcel.mainHeaderRow = mainHeaderRow;
		ExecuteExcel.environmentHeaderRow = environmentHeaderRow;
		ExecuteExcel.resultValues = resultValues;
		ExecuteExcel.resultRelations = resultRelations;
		ExecuteExcel.codeHeader = codeHeader;
		ExecuteExcel.testHeader = testHeader;
		ExecuteExcel.commentHeader = commentHeader;
	*/
		ricoh.es.methods.Excel excel = new ricoh.es.methods.Excel(path, null);
		

		int idCode = -1;
		java.util.HashMap<String,Integer> idExecution = new java.util.HashMap<String,Integer>();
		for(int i=0;i<customFieldValues.length;i++){
			idExecution.put(customFieldValues[i], -1);
		}
		int idTestName = -1;
		int idComment = -1;
		int idEnvironment = -1;
		int idTestPlan = -1;
		int idBuild = -1;
		
		
		hmResults = putToHashMap(resultValues,resultRelations);
		hmCustomFields = putToHashMap(customFieldHeders,customFieldValues);
		
		String environmentSheetRef = null;
		java.util.List<String> sheetList = excel.sheetNames();
		for(int i=0;i<environmentSheet.length && environmentSheetRef == null;i++){
			if(sheetList.contains(environmentSheet[i])){
				environmentSheetRef = environmentSheet[i];
			}
		}
		
		int n = 0;
		String text = excel.readCell(environmentSheetRef, environmentHeaderRow, n++);
		do{

			if (ricoh.es.methods.Utils.equals(text, codeHeader)){
				idCode = n-1;
			}
			for(int i=0;i<customFieldHeders.length;i++){
				if (ricoh.es.methods.Utils.equals(text, customFieldHeders[i])){
					idExecution.put(customFieldValues[i], n-1);
				}
			}
			if (ricoh.es.methods.Utils.equals(text, testHeader)){
				idTestName = n-1;
			}
			if (ricoh.es.methods.Utils.equals(text, commentHeader)){
				idComment = n-1;
			}
			if (ricoh.es.methods.Utils.equals(text, environmentHeader)){
				idEnvironment = n-1;
			}
			if (ricoh.es.methods.Utils.equals(text, testPlanHeader)){
				idTestPlan  = n-1;
			}
			if (ricoh.es.methods.Utils.equals(text, buildHeader)){
				idBuild  = n-1;
			}
			text = excel.readCell(environmentSheetRef, environmentHeaderRow, n++);
		}while (text != null);
	
		int lastRow = excel.getLastRow(environmentSheetRef);

		al = new java.util.ArrayList<Object[]>();
		Object[][] ob = null;
		Object[] obAux = null;
		
		java.util.List<String> alEnvironment = new java.util.ArrayList<String>();
		for(int i=0;i<PanelClass.get().jTablePlatforms.getRowCount();i++){
			if((Boolean)PanelClass.get().jTablePlatforms.getValueAt(i, 0)){
				alEnvironment.add((String)PanelClass.get().jTablePlatforms.getValueAt(i, 1));
			}
		}
		/*
	final static int Col_SELECT = 0; 
	final static int Col_CODE = 1; 
	final static int Col_ENVIRONMENT = 2; 
	final static int Col_TESTPLAN = 3; 
	final static int Col_BUILD = 4; 
	final static int Col_BROWSER = 5; 
	final static int Col_RESULT = 6; 
	final static int Col_EXECUTE_RESULTS = 7; 
	final static int Col_TESTNAME = 8; 
	final static int Col_NOTES = 9; 
		 * */
		final String WS = "WS";
		final String IE = "IE";
		final String Chrome = "Chrome";
		

		for(int i=0;i<environmentSheet.length;i++){
			if(sheetList.contains(environmentSheet[i])){
				for(int j=environmentHeaderRow+1;j<lastRow;j++){
					for(int k=0;k<customFieldValues.length;k++){
						String environment = excel.readCell(environmentSheet[i],j,idEnvironment);
						String testPlan    = excel.readCell(environmentSheet[i],j,idTestPlan);
						String build       = excel.readCell(environmentSheet[i],j,idBuild);			
						
						if(alEnvironment.contains(environment)){
							obAux = new Object[Execute.cap.length];
							obAux[Col_SELECT] = true;
							obAux[Col_CODE] = excel.readCell(environmentSheet[i],j,idCode);
							obAux[Col_ENVIRONMENT] = environment;
							obAux[Col_TESTPLAN] = testPlan;
							obAux[Col_BUILD] = build;
							if (ricoh.es.methods.Utils.equals(WS, customFieldValues[k])){
								obAux[Col_BROWSER] = WS;
								obAux[Col_RESULT] = excel.readCell(environmentSheet[i],j,idExecution.get(WS));
							}else if (ricoh.es.methods.Utils.equals(IE, customFieldValues[k])){
								obAux[Col_BROWSER] = IE;
								obAux[Col_RESULT] = excel.readCell(environmentSheet[i],j,idExecution.get(IE));
							}else if (ricoh.es.methods.Utils.equals(Chrome, customFieldValues[k])){
								obAux[Col_BROWSER] = Chrome;
								obAux[Col_RESULT] = excel.readCell(environmentSheet[i],j,idExecution.get(Chrome));
							}else{
								obAux[Col_BROWSER] = "?";
								obAux[Col_RESULT] = "?";
							}
							obAux[Col_EXECUTE_RESULTS] = null;
							obAux[Col_TESTNAME] = excel.readCell(environmentSheet[i],j,idTestName);
							obAux[Col_NOTES] = excel.readCell(environmentSheet[i],j,idComment);			
							if(obAux[Col_RESULT] != null) {
								if (!ricoh.es.methods.Utils.equals("NA",hmResults.get(obAux[Col_RESULT].toString()))){
									al.add(obAux);
								}
							}
						}
					}
				}
			}
		}
		

	
		ob = new Object[al.size()][Execute.cap.length];
		for(int i=0;i<al.size();i++){
			ob[i] = al.get(i);
		}
		
		
		PanelClass.get().jLabelLogs.setText("ROWS: "+al.size()+"/"+al.size());
		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTableToExecute
				, Execute.cap, ob, Execute.sizeCol, Execute.typeCol, Execute.edit, null);

	}

	private static java.util.HashMap<String,String> putToHashMap(String[] key,String[] value){
		java.util.HashMap<String,String> hm = new java.util.HashMap<String,String>();
		if(key == null || value == null){
			return null;
		}else if (key.length != value.length){
			return null;
		}
		for(int i=0;i<key.length;i++){
			hm.put(key[i], value[i]);
		}
		return hm;
	}
	@SuppressWarnings("unchecked")
	protected static void filter(){
		Object[][] obFilter = null;
		java.util.ArrayList<Object[]> alAux = new java.util.ArrayList<Object[]>();
		Object[] obAux = null;
		
		java.util.List<Object> listExecuteResu = PanelClass.get().jListExecuteResults.getSelectedValuesList();
	
		boolean add = false;
		
		for(int i=0;i<al.size();i++){
			obAux = al.get(i);
			Object res = obAux[Col_RESULT].toString();

			if(listExecuteResu.size() == 0 || listExecuteResu.contains(res)){
				add = true;
			}else{
				add = false;
			}
			if(add){
				alAux.add(al.get(i));
			}
		}
		obFilter = new Object[alAux.size()][Execute.cap.length];
		for(int i=0;i<alAux.size();i++){
			obFilter[i] = alAux.get(i);
		}
		PanelClass.get().jLabelLogs.setText("ROWS: "+alAux.size()+"/"+al.size());
		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTableToExecute
				, Execute.cap, obFilter, Execute.sizeCol, Execute.typeCol, Execute.edit, null);
		
	}
	
	/*
final static int Col_SELECT = 0; 
final static int Col_CODE = 1; 
final static int Col_ENVIRONMENT = 2; 
final static int Col_TESTPLAN = 3; 
final static int Col_BUILD = 4; 
final static int Col_BROWSER = 5; 
final static int Col_RESULT = 6; 
final static int Col_EXECUTE_RESULTS = 7; 
final static int Col_TESTNAME = 8; 
final static int Col_NOTES = 9; 
	 * */
	
	protected static void endExecute(String[] executeResults) {
		Object[][] ob = null;
		ob = new Object[executeResults.length][Execute.cap.length];
		for(int i=0;i<executeResults.length;i++){
			ob[i][Col_SELECT ]         = PanelClass.get().jTableToExecute.getValueAt(i, Col_SELECT );
			ob[i][Col_CODE]            = PanelClass.get().jTableToExecute.getValueAt(i, Col_CODE);
			ob[i][Col_ENVIRONMENT]     = PanelClass.get().jTableToExecute.getValueAt(i, Col_ENVIRONMENT);
			ob[i][Col_TESTPLAN]        = PanelClass.get().jTableToExecute.getValueAt(i, Col_TESTPLAN);
			ob[i][Col_BUILD ]          = PanelClass.get().jTableToExecute.getValueAt(i, Col_BUILD );
			ob[i][Col_BROWSER]         = PanelClass.get().jTableToExecute.getValueAt(i, Col_BROWSER);
			ob[i][Col_RESULT]          = PanelClass.get().jTableToExecute.getValueAt(i, Col_RESULT);
			ob[i][Col_EXECUTE_RESULTS] = executeResults[i];
			ob[i][Col_TESTNAME]        = PanelClass.get().jTableToExecute.getValueAt(i, Col_TESTNAME );
			ob[i][Col_NOTES]           = PanelClass.get().jTableToExecute.getValueAt(i, Col_NOTES );
		}
		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTableToExecute
				, Execute.cap, ob, Execute.sizeCol, Execute.typeCol, Execute.edit, null);
	}
	

	final static String CAPSALERA = "WARNING";
	final static String[] BOTONS = {"ACCEPT","OPEN EXCEL"};
	final static String ACCEPT = BOTONS[0];
	protected static void checkExcel(String path,String[] customFieldHeders,
			String[] environmentSheet,String environmentHeader,
			String testPlanHeader,String buildHeader,int environmentHeaderRow,
			String codeHeader,String testHeader,String commentHeader){
		String message = "";
		ricoh.es.methods.Excel excel = new ricoh.es.methods.Excel(path, null);
		java.util.List<String> sheets = new java.util.ArrayList<String>();
		java.util.List<String> sheetList = excel.sheetNames();
		for(int i=0;i<environmentSheet.length;i++){
			if(sheetList.contains(environmentSheet[i])){
				sheets.add(environmentSheet[i]);
			}else{
				message += "Sheet '"+environmentSheet[i]+"' doesn't exist\n";
			}
		}
		

		
		java.util.List<String> headers = new java.util.ArrayList<String>(); 
		headers.add(codeHeader);
		for(int i=0;i<customFieldHeders.length;i++){
			headers.add(customFieldHeders[i]);
		}
		headers.add(testHeader);
		headers.add(commentHeader);
		headers.add(environmentHeader);
		headers.add(testPlanHeader);
		headers.add(buildHeader);

		java.util.List<String> sheetHeaders = null;
		
		for(String sheet:sheets){
			int n = -1;
			sheetHeaders = new java.util.ArrayList<String>(); 
			String text = excel.readCell(sheet, environmentHeaderRow, ++n);
			do{
				sheetHeaders.add(text);
				text = excel.readCell(sheet, environmentHeaderRow, ++n);
			}while (text != null);
			for(String header:headers){
				if(!sheetHeaders.contains(header)){
					message += "In sheet '"+sheet+"', header '" +header+"' isn't displayed\n";
				}
			}
		}	
		int op = -2;
		if(message.length() == 0){
			message = "All fields OK";
			op = JOptionPane.showOptionDialog(null,message, CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, null, BOTONS,
	                ACCEPT);
		}else{
			op = JOptionPane.showOptionDialog(null,message, CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                ACCEPT);
		}
		if(op == 1){
			ricoh.es.methods.Utils.openFile(path);
		}
	
		
	}
	
	private String[] getSelectetValues(javax.swing.JList list,String[] s){
		java.util.List<Object> l = list.getSelectedValuesList();
		if(l.size() != 0){
			s = new String[l.size()];
			for(int i=0;i<l.size();i++){
				s[i] = l.get(i).toString();
			}
		}
		return s;
	}
	

}
