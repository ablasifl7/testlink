package ricoh.es;

import javax.swing.JOptionPane;

public class Execute {
	protected static String[] cap = {"S","CODE","ENVIRONMENT","TESTPLAN","BUILD",
			"BROWSER","RESULT","EXECUTE RESULTS","TEST NAME","COMMENT"};
	private static Object[][] ob = null;
	//protected static int[] sizeCol = {15,75,100,75,100,150,400,1000};
	protected static int[] sizeCol = {15,75,100,75,50,75,75,125,300,1000};
	@SuppressWarnings("rawtypes")
	protected static Class[] typeCol = {Boolean.class,String.class,String.class,
			String.class,String.class,String.class,String.class,String.class,
			String.class,String.class};
	protected static boolean[] edit = {true,false,false,false,false,false,false,
			false,false,false};
	
	//private static ExecuteExcelThread eet = new ExecuteExcelThread();
	private static ExecuteThread et = null;
	
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
	protected static String executedBrowser;
	
	@SuppressWarnings("unchecked")
	protected static void initalize(String customField, String[] customFieldHeders,
			String[] customFieldValues,String mainSheet, String[] environmentSheet,
			String  environmentHeader,String testPlanHeader,String buildHeader,
			int mainHeaderRow,int environmentHeaderRow,
	
			String[] resultValues,String[] resultRelations,String codeHeader,
			String testHeader,String commentHeader,String executedBrowser){
		
		PanelClass.get().jLabelLogs.setText("");
		//ricoh.es.methods.Frame.generarList(PanelClass.get().jListEnvironment, environmentNames);
		String[] results = new String[resultValues.length - 1];
		int n = 0;
		for(int i=0;i<resultValues.length;i++){
			 if(!ricoh.es.methods.Utils.equals(resultRelations[i],"NA")){
				 results[n++] = resultValues[i];
			 }
		}
		ricoh.es.methods.Frame.generarList(PanelClass.get().jListExecuteResults, results);
		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTableToExecute
				, cap, ob, sizeCol, typeCol, edit, null);
		
		Execute.customField = customField;
		Execute.customFieldHeders = customFieldHeders;
		Execute.customFieldValues = customFieldValues;
		Execute.mainSheet = mainSheet;
		Execute.environmentSheet = environmentSheet;
		Execute.environmentHeader = environmentHeader;
		Execute.testPlanHeader = testPlanHeader;
		Execute.buildHeader = buildHeader;
		Execute.mainHeaderRow = mainHeaderRow;
		Execute.environmentHeaderRow = environmentHeaderRow;
		Execute.resultValues = resultValues;
		Execute.resultRelations = resultRelations;
		Execute.codeHeader = codeHeader;
		Execute.testHeader = testHeader;
		Execute.commentHeader = commentHeader;
		
		Execute.executedBrowser = executedBrowser;
		
		/*
		eet.setCustomField(Execute.customField = customField);
		eet.setCustomFieldHeders(Execute.customFieldHeders = customFieldHeders);
		eet.setCustomFieldValues(Execute.customFieldValues = customFieldValues);
		eet.setMainSheet(Execute.mainSheet = mainSheet);
		eet.setEnvironmentSheet(Execute.environmentSheet = environmentSheet);
		eet.setEnvironmentNames(Execute.environmentNames = environmentNames);
		eet.setMainHeaderRow(Execute.mainHeaderRow = mainHeaderRow);
		eet.setEnvironmentHeaderRow(Execute.environmentHeaderRow = environmentHeaderRow);
		eet.setResultValues(Execute.resultValues = resultValues);
		eet.setResultRelations(Execute.resultRelations = resultRelations);
		eet.setCodeHeader(Execute.codeHeader = codeHeader);
		eet.setTestHeader(Execute.testHeader = testHeader);
		eet.setCommentHeader(Execute.commentHeader = commentHeader);
		*/

	}
	final static String CAPSALERA = "WARNING";
	final static String[] BOTONS = {"ACCEPT"};
	final static String ACCEPT = BOTONS[0];

	protected static void uploadExcel(){
		
		java.io.File fExel = new java.io.File(PanelClass.get().jTextFieldExcelToTestlink.getText());
		String fileName = fExel.getName().toLowerCase();
		if(!fExel.exists()){
			JOptionPane.showOptionDialog(null, "FILE '"
					+ ""+PanelClass.get().jTextFieldExcelToTestlink.getText()+"' DON'T EXIST", CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                ACCEPT);
		}else if( fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
			ExecuteExcel.upload(PanelClass.get().jTextFieldExcelToTestlink.getText(),
					customField,customFieldHeders,customFieldValues, mainSheet,
					environmentSheet,environmentHeader ,testPlanHeader,buildHeader ,
					mainHeaderRow, environmentHeaderRow,
					resultValues, resultRelations, codeHeader,testHeader,commentHeader);
			/*
			eet = new ExecuteExcelThread();
			eet.setCustomField(customField);
			eet.setCustomFieldHeders(customFieldHeders);
			eet.setCustomFieldValues(customFieldValues);
			eet.setMainSheet(mainSheet);
			eet.setEnvironmentSheet(environmentSheet);
			eet.setEnvironmentNames(environmentNames);
			eet.setMainHeaderRow(mainHeaderRow);
			eet.setEnvironmentHeaderRow(environmentHeaderRow);
			eet.setResultValues(resultValues);
			eet.setResultRelations(resultRelations);
			eet.setCodeHeader(codeHeader);
			eet.setTestHeader(testHeader);
			eet.setCommentHeader(commentHeader);
			eet.setPath(PanelClass.get().jTextFieldExcelToTestlink.getText());
			
			eet.start();
			*/
		}else{
			JOptionPane.showOptionDialog(null, "FILE '"
					+ ""+PanelClass.get().jTextFieldExcelToTestlink.getText()+"' ISN'T AN EXCEL FILE", CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                ACCEPT);
		}
		
		
	}
	
	
	
	protected static void executeExcelToTestLink(){
		et = new ExecuteThread();
		et.start();
	}
	protected static void checkExcel(){
		
		
		java.io.File fExel = new java.io.File(PanelClass.get().jTextFieldExcelToTestlink.getText());
		String fileName = fExel.getName().toLowerCase();
		if(!fExel.exists()){
			JOptionPane.showOptionDialog(null, "FILE '"
					+ ""+PanelClass.get().jTextFieldExcelToTestlink.getText()+"' DON'T EXIST", CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                ACCEPT);
		}else if( fileName.endsWith(".xls") || fileName.endsWith(".xlsx")){
			ExecuteExcel.checkExcel(PanelClass.get().jTextFieldExcelToTestlink.getText(),
					customFieldHeders, environmentSheet, environmentHeader, testPlanHeader, buildHeader, environmentHeaderRow, codeHeader, testHeader, commentHeader);


		}else{
			JOptionPane.showOptionDialog(null, "FILE '"
					+ ""+PanelClass.get().jTextFieldExcelToTestlink.getText()+"' ISN'T AN EXCEL FILE", CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                ACCEPT);
		}
		
	}

}
