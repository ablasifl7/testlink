package ricoh.es;

public class ExecuteExcelThread extends Thread{
	protected void setPath(String path) {
		this.path = path;
	}
	protected void setCustomField(String customField) {
		this.customField = customField;
	}
	protected void setCustomFieldHeders(String[] customFieldHeders) {
		this.customFieldHeders = customFieldHeders;
	}
	protected void setCustomFieldValues(String[] customFieldValues) {
		this.customFieldValues = customFieldValues;
	}
	protected void setMainSheet(String mainSheet) {
		this.mainSheet = mainSheet;
	}
	protected void setEnvironmentSheet(String[] environmentSheet) {
		this.environmentSheet = environmentSheet;
	}
	protected void setEnvironmentNames(String[] environmentNames) {
		this.environmentNames = environmentNames;
	}
	protected void setMainHeaderRow(int mainHeaderRow) {
		this.mainHeaderRow = mainHeaderRow;
	}
	protected void setEnvironmentHeaderRow(int environmentHeaderRow) {
		this.environmentHeaderRow = environmentHeaderRow;
	}
	protected void setResultValues(String[] resultValues) {
		this.resultValues = resultValues;
	}
	protected void setResultRelations(String[] resultRelations) {
		this.resultRelations = resultRelations;
	}
	protected void setCodeHeader(String codeHeader) {
		this.codeHeader = codeHeader;
	}
	protected void setTestHeader(String testHeader) {
		this.testHeader = testHeader;
	}
	protected void setCommentHeader(String commentHeader) {
		this.commentHeader = commentHeader;
	}
	private String path;
	private String customField;
	private String[] customFieldHeders;
	private String[] customFieldValues;
	private String mainSheet;
	private String[] environmentSheet;
	private String[] environmentNames;
	private int mainHeaderRow;
	private int environmentHeaderRow;
	private String[] resultValues;
	private String[] resultRelations;
	private String codeHeader;
	private String testHeader;
	private String commentHeader;
	
	private java.util.HashMap<String,String> hmEnvironment;
	private java.util.HashMap<String,String> hmResults;
	private java.util.HashMap<String,String> hmCustomFields;
	
	
	private String[] cap = {"S","CODE","ENVIRONMENT","BROWSER","RESULT","TEST NAME","COMMENT"};
	private Object[][] ob = null;
	private int[] sizeCol = {15,75,100,75,100,400,1150};
	@SuppressWarnings("rawtypes")
	private  Class[] typeCol = {Boolean.class,String.class,String.class,String.class,
			String.class,String.class,String.class};
	private  boolean[] edit = {true,false,false,false,false,false,false};
	
	public void run(){
		ricoh.es.methods.Excel excel = new ricoh.es.methods.Excel(path, null);

		
		int idCode = -1;
		java.util.HashMap<String,Integer> idExecution = new java.util.HashMap<String,Integer>();
		for(int i=0;i<customFieldValues.length;i++){
			idExecution.put(customFieldValues[i], -1);
		}
		int idTestName = -1;
		int idComment = -1;
		hmEnvironment = putToHashMap(environmentNames,environmentSheet);
		hmResults = putToHashMap(resultValues,resultRelations);
		hmCustomFields = putToHashMap(customFieldHeders,customFieldValues);
		
		int n = 0;
		String text = excel.readCell(hmEnvironment.get(environmentNames[0]), environmentHeaderRow, n++);
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
			text = excel.readCell(hmEnvironment.get(environmentNames[0]), environmentHeaderRow, n++);
		}while (text != null);

		int lastRow = excel.getLastRow(hmEnvironment.get(environmentNames[0]));
		
		java.util.ArrayList<Object[]> al = new java.util.ArrayList<Object[]>();
		Object[] obAux = null;
		
		final int N = environmentNames.length * (lastRow-environmentHeaderRow-1) * customFieldValues.length;
		int nProg = 0;
		PanelClass.get().jProgressBarExelToTestLink.setMaximum(N);
		PanelClass.get().jProgressBarExelToTestLink.setValue(nProg);
		for(int i=0;i<environmentNames.length;i++){
			for(int j=environmentHeaderRow+1;j<lastRow;j++){
				for(int k=0;k<customFieldValues.length;k++){
					obAux = new Object[cap.length];
					obAux[0] = true;
					obAux[1] = excel.readCell(hmEnvironment.get(environmentNames[i]),j,idCode);
					obAux[2] = environmentNames[i];
					if (ricoh.es.methods.Utils.equals("WS", customFieldValues[k])){
						obAux[3] = "WS";
						obAux[4] = hmResults.get(excel.readCell(hmEnvironment.get(environmentNames[i]),j,idExecution.get("WS")));
					}else if (ricoh.es.methods.Utils.equals("IE", customFieldValues[k])){
						obAux[3] = "IE";
						obAux[4] = hmResults.get(excel.readCell(hmEnvironment.get(environmentNames[i]),j,idExecution.get("IE")));
					}else if (ricoh.es.methods.Utils.equals("Chrome", customFieldValues[k])){
						obAux[3] = "Chrome";
						obAux[4] = hmResults.get(excel.readCell(hmEnvironment.get(environmentNames[i]),j,idExecution.get("Chrome")));
					}else{
						obAux[3] = "?";
						obAux[4] = "?";
					}
					obAux[5] = excel.readCell(hmEnvironment.get(environmentNames[i]),j,idTestName);
					PanelClass.get().jProgressBarExelToTestLink.setValue(++nProg);
					obAux[6] = excel.readCell(hmEnvironment.get(environmentNames[i]),j,idComment);
					if (!ricoh.es.methods.Utils.equals("NA",obAux[4].toString())){
						al.add(obAux);
					}
				}
			}
		}
		
		
	
		ob = new Object[al.size()][cap.length];
		for(int i=0;i<al.size();i++){
			ob[i] = al.get(i);
		}
		
		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTableToExecute
				, cap, ob, sizeCol, typeCol, edit, null);
		
		
		//String[] envi = getSelectetValues(PanelClass.get().jListEnvironment,environmentNames);
		// envi = getSelectetValues(PanelClass.get().jListExecuteResults,resultValues);
	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	private java.util.HashMap<String,String> putToHashMap(String[] key,String[] value){
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
}
