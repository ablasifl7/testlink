package ricoh.es;

import java.util.Map;
import java.util.TreeMap;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;


public class ExecuteThread extends Thread{
	final private int EXECUTE_AND_REMOVE_LAST_EXECUTION = 0;
	final private int ONLY_EXECUTE = 1;
	final private int REMOVE_LAST_EXECUTION_AND_EXECUTE = 2;
	final private int REMOVE_LAST_EXECUTION = 3;
	final private int CHECK_EXECUTION = 4;
	
	public void run(){
		//TestLinkClass.getTestLink().
		int nRows = PanelClass.get().jTableToExecute.getRowCount();
		// 0: executat
		// 1: codi
		// 2: entorn
		// 3: navegador
		// 4: resultats
		PanelClass.get().jProgressBarExelToTestLink.setMaximum(nRows);
		String fullTestCaseExternalId = null; 
		String environment = null;
		String browser = null;
		String result = null;
		String testPlan = null;
		String buildName = null;
		
		Integer testPlanId = -1;
		String planName = null;
		
		DataBase db = null;
		
		
		PanelClass.get().jComboBoxLastExecution.setEnabled(false);
		java.util.Map<String,String> map = null;
		if( CHECK_EXECUTION == PanelClass.get().jComboBoxLastExecution.getSelectedIndex()){
			PanelClass.get().jProgressBarExelToTestLink.setIndeterminate(true);
			String url = "jdbc:mysql://192.168.200.49:3306";
			String user = "rsa_ro";
			String password = "sogtulapdt";
			db = new DataBase(url, user, password);
			
		}


		String prefix = TestLinkClass.hmProjectPrefix.get(PanelClass.get()
				
				.jComboBoxTestProjects.getSelectedItem().toString());
		Integer testProjectId = TestLinkClass.getTestLink().getTestProjectId(prefix);
		String projectName = TestLinkClass.hmPrefixProjName.get(prefix);
		TestLinkClass tlc = TestLinkClass.getTestLink().getProjectClass(projectName);
		
		TestLinkClass[] tlcList = TestLinkClass.getTestLink().getTestPlanClass(prefix);
		for(int i=0;i<tlcList.length;i++){
			if(ricoh.es.methods.Utils.equals( tlcList[i].getTestPlanName(),
					PanelClass.get().jComboBoxTestPlan.getSelectedItem().toString())){
				testPlanId = tlcList[i].getTestPlanId();
				planName = tlcList[i].getTestPlanName();
			}
		}
		if(CHECK_EXECUTION == PanelClass.get().jComboBoxLastExecution.getSelectedIndex()){
			db.executedResultCustomFields(testPlanId);
			PanelClass.get().jProgressBarExelToTestLink.setIndeterminate(false);
		}
		
		String[] executeResults = new String[nRows];
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
		PanelClass.get().jProgressBarExelToTestLink.setForeground(new java.awt.Color(0, 255, 0));
		TestLinkClass.getTestLink().setError(false);
		for(int i=0;i<nRows;i++){
			PanelClass.get().jProgressBarExelToTestLink.setValue(i);
			if(TestLinkClass.getTestLink().isError()){
				PanelClass.get().jProgressBarExelToTestLink.setForeground(new java.awt.Color(255, 0, 0));
			}
			if((Boolean)PanelClass.get().jTableToExecute.getValueAt(i,ExecuteExcel.Col_SELECT)){
				fullTestCaseExternalId = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_CODE);
				environment = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_ENVIRONMENT);
				testPlan = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_TESTPLAN);
				buildName = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_BUILD);
				browser = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_BROWSER);
				result = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_RESULT);

				Integer testCaseId = TestLinkClass.getTestLink().getTestCaseId(fullTestCaseExternalId, 1);
				ExecutionStatus status = null;
				switch ((String) ExecuteExcel.hmResults.get(result)) {
				case "PASSED":
					status = ExecutionStatus.PASSED;
					break;
				case "BLOCKED":
					status = ExecutionStatus.BLOCKED;
					break;
				case "FAILED":
					status = ExecutionStatus.FAILED;
					break;
				case "NO_EXECUTED":
					status = ExecutionStatus.NOT_RUN;
					break;	
				default:
					status = null;
					break;	
				}
				String notes = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_NOTES);
				String platformName = (String)PanelClass.get().jTableToExecute.getValueAt(i, ExecuteExcel.Col_ENVIRONMENT);
		    	Map<String,String> customFields = new TreeMap<String,String>();
		    	customFields.put(Execute.executedBrowser, browser);
		    	boolean beforeRemoveExecution = false;
		    	boolean executeTestCase = true;
		    	boolean afterRemoveExecution = true;
		    	boolean checkexecution = false;

		    	switch(PanelClass.get().jComboBoxLastExecution.getSelectedIndex()){
				case EXECUTE_AND_REMOVE_LAST_EXECUTION:
			    	beforeRemoveExecution = false;
				    executeTestCase = true;
				    afterRemoveExecution = true;
					break;
				case ONLY_EXECUTE:
			    	beforeRemoveExecution = false;
				    executeTestCase = true;
				    afterRemoveExecution = false;
					break;
				case REMOVE_LAST_EXECUTION_AND_EXECUTE:
			    	beforeRemoveExecution = true;
				    executeTestCase = true;
				    afterRemoveExecution = false;
					break;
				case REMOVE_LAST_EXECUTION:
			    	beforeRemoveExecution = true;
				    executeTestCase = false;
				    afterRemoveExecution = false;
					break;
				case CHECK_EXECUTION:
					checkexecution = true;
					break;
				default:
			    	beforeRemoveExecution = false;
				    executeTestCase = false;
				    afterRemoveExecution = false;
					break;	
		    	}
		    	if(checkexecution){
		    		
		    		Integer idExecutionVersion = TestLinkClass.getTestLink()
		    				.getIdExecutionVersion(testPlanId, testCaseId);
		    		executeResults[i] = db.getExecutedResultCustomFields(idExecutionVersion, browser);
	
		    	}else if(!status.equals(ExecutionStatus.NOT_RUN)){
					if(beforeRemoveExecution){
						TestLinkClass.getTestLink().removeExecuteTestCase(testCaseId, testPlanId);
					}
					if(executeTestCase){
						executeResults[i] = TestLinkClass.getTestLink().executeTestCaseMessage(testCaseId, 
								testPlanId, status, buildName, notes, platformName, customFields,afterRemoveExecution,true);
						
						
					}
				}
			}
		}
		PanelClass.get().jProgressBarExelToTestLink.setValue(nRows);
		PanelClass.get().jComboBoxLastExecution.setEnabled(true);
		ExecuteExcel.endExecute(executeResults);
	}

}
