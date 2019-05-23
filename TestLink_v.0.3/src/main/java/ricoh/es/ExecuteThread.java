package ricoh.es;

import java.util.Map;
import java.util.TreeMap;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;


public class ExecuteThread extends Thread{
	
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

		String prefix = TestLinkClass.hmProjectPrefix.get(PanelClass.get()
				.jComboBoxTestProjects.getSelectedItem().toString());
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
		    	

		    	switch(PanelClass.get().jComboBoxLastExecution.getSelectedIndex()){
				case 0://EXECUTE AND REMOVE LAST EXECUTION
			    	beforeRemoveExecution = false;
				    executeTestCase = true;
				    afterRemoveExecution = true;
					break;
				case 1://ONLY EXECUTE
			    	beforeRemoveExecution = false;
				    executeTestCase = true;
				    afterRemoveExecution = false;
					break;
				case 2://REMOVE LAST EXECUTION AND EXECUTE
			    	beforeRemoveExecution = true;
				    executeTestCase = true;
				    afterRemoveExecution = false;
					break;
				case 3://REMOVE LAST EXECUTION
			    	beforeRemoveExecution = true;
				    executeTestCase = false;
				    afterRemoveExecution = false;
					break;
				default:
			    	beforeRemoveExecution = false;
				    executeTestCase = false;
				    afterRemoveExecution = false;
					break;	
		    	}
		    	
				if(!status.equals(ExecutionStatus.NOT_RUN)){
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
		ExecuteExcel.endExecute(executeResults);
	}

}
