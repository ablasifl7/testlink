package ricoh.es;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.xmlrpc.XmlRpcException;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ActionOnDuplicate;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.ResponseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseDetails;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.TestImportance;
import br.eti.kinoshita.testlinkjavaapi.constants.TestLinkMethods;
import br.eti.kinoshita.testlinkjavaapi.constants.TestLinkParams;
import br.eti.kinoshita.testlinkjavaapi.model.Build;
import br.eti.kinoshita.testlinkjavaapi.model.CustomField;
import br.eti.kinoshita.testlinkjavaapi.model.Execution;
import br.eti.kinoshita.testlinkjavaapi.model.Platform;
import br.eti.kinoshita.testlinkjavaapi.model.ReportTCResultResponse;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;
import br.eti.kinoshita.testlinkjavaapi.model.TestPlan;
import br.eti.kinoshita.testlinkjavaapi.model.TestProject;
import br.eti.kinoshita.testlinkjavaapi.model.TestSuite;
import br.eti.kinoshita.testlinkjavaapi.model.User;
import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;
import br.eti.kinoshita.testlinkjavaapi.util.Util;

public class TestLink {
	/*
	 * ES POT FER:
	 * Crear un TestSuite (Tidol + descripció)
	 * Crear un TestCase (Titol + Sumari + Precondicions + Passos)
	 * Modificar TestCase (només Sumari + Precondicions + Passos)
	 * Afegir un TestCase a un TestPlan per executar
	 * 
	 * 
	 * 
	 * NO ES POT FER (s'ha de fer manualment des del TestLink):
	 * Modificar titol Testcase
	 * El·liminar TestCase
	 * Treure un  TestCase d'un un TestPlan
	 * 
	 * 
	 * 
	 * */
	
	private boolean error;
	protected boolean isError() {
		return error;
	}
	protected void setError(boolean error) {
		this.error = error;
	}
	private TestLinkAPI testLinkAPI;
	public TestLink(String testLinkURL,  String devKey) {
		super();
		final String CAPSALERA = "WARNING";
		final String[] BOTONS = {"RE-CONNECT","CLOSE PANEL"};
		final String CANCEL = BOTONS[0];
		int op = -1;
		try {
			setError(false);
			URL url = new URL(testLinkURL);
			testLinkAPI = new TestLinkAPI(url, devKey);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			setError(true);
	        op = JOptionPane.showOptionDialog(null, e.getMessage(), CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                CANCEL);
	        if(op==0){
	        	UploadProp.initialize();
	        }
		} catch (TestLinkAPIException e){
			setError(true);
			op = JOptionPane.showOptionDialog(null, e.getMessage(), CAPSALERA,
	                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BOTONS,
	                CANCEL);
	        if(op==0){
	        	UploadProp.initialize();
	        }
		}

	}
	
	public TestLinkClass[] getTestProjectClass(){
		TestProject[] projecs = testLinkAPI.getProjects();
		TestLinkClass[] tlc = new TestLinkClass[projecs.length];
		for(int i=0;i<projecs.length;i++){
			tlc[i] = new TestLinkClass();
			TestLinkClass.setError(false);
			tlc[i].setProjectId(projecs[i].getId());
			tlc[i].setProjectName(projecs[i].getName());
			tlc[i].setProjectPrefix(projecs[i].getPrefix());
			tlc[i].setProjectNotes(projecs[i].getNotes());
		}			
		return tlc;
	}
	public TestLinkClass[] getPlatformClass(String projectPrefix){
		Platform[] platform = testLinkAPI.getProjectPlatforms(getTestProjectId(projectPrefix));
		TestLinkClass[] tlc = new TestLinkClass[platform.length];
		for(int i=0;i<platform.length;i++){
			tlc[i] = new TestLinkClass();
			TestLinkClass.setError(false);
			tlc[i].setPlatformId(platform[i].getId());
			tlc[i].setPlatformName(platform[i].getName());
			tlc[i].setPlatformNotes(platform[i].getNotes());
		}
		return tlc;
	}
	public TestLinkClass[] getPlatformClass(String planName, String projectName){
		TestPlan tp = testLinkAPI.getTestPlanByName(planName, projectName);
		TestLinkClass[] tlc;
		try {
			Platform[] platform = testLinkAPI.getTestPlanPlatforms(tp.getId());
			tlc = new TestLinkClass[platform.length];
			for(int i=0;i<platform.length;i++){
				tlc[i] = new TestLinkClass();
				TestLinkClass.setError(false);
				tlc[i].setPlatformId(platform[i].getId());
				tlc[i].setPlatformName(platform[i].getName());
				tlc[i].setPlatformNotes(platform[i].getNotes());
			}
			return tlc;
		} catch (TestLinkAPIException e) {
			return null;
		}
	}
	
	
	public TestLinkClass[] getTestPlanClass(String projectPrefix){
		TestPlan[] testPlan = testLinkAPI.getProjectTestPlans(getTestProjectId(projectPrefix));
		TestLinkClass[] tlc = new TestLinkClass[testPlan.length];
		for(int i=0;i<testPlan.length;i++){
			tlc[i] = new TestLinkClass();
			TestLinkClass.setError(false);
			tlc[i].setTestPlanId(testPlan[i].getId());
			tlc[i].setTestPlanName(testPlan[i].getName());
			tlc[i].setTestPlanNotes(testPlan[i].getNotes());
			tlc[i].setTestPlanProjectName(testPlan[i].getProjectName());
			tlc[i].setCustomFields(tlc[i].getCustomFields());
		}
		return tlc;
	}
	public TestLinkClass getProjectClass(String projectName){
		TestProject testProject = testLinkAPI.getTestProjectByName(projectName);
		TestLinkClass tlc = new TestLinkClass();
		tlc.setProjectId(testProject.getId());
		tlc.setProjectName(testProject.getName());
		tlc.setProjectNotes(testProject.getNotes());
		tlc.setProjectPrefix(testProject.getPrefix());
		return tlc;
	}

	public TestLinkClass getTestCaseClass(String fullTestCaseExternalId,Integer version){
		
		
		TestCase testCase = testLinkAPI.getTestCaseByExternalId(fullTestCaseExternalId,
				version);
		

		
		TestLinkClass tlc = new TestLinkClass();
		tlc.setTestCaseExecutionStatus(testCase.getExecutionStatus());
		tlc.setTestCaseFullExternalId(testCase.getFullExternalId());
		tlc.setTestCaseId(testCase.getId());
		tlc.setTestCaseInternalId(testCase.getInternalId());
		tlc.setTestCaseName(testCase.getName());
		tlc.setTestCasePreconditions(testCase.getPreconditions());
		tlc.setTestCaseSummary(testCase.getSummary());
		return tlc;
	}
	
	public Integer getTestProjectIdByName(String name) {
		TestProject testProject[] = testLinkAPI.getProjects();
		for(int i=0;i<testProject.length;i++){
			if(0 == testProject[i].getName().compareTo(name)){
				return testProject[i].getId();
			}
		}
		return null;
	}
	public Integer getTestProjectId(String prefix){
		TestProject testProject[] = testLinkAPI.getProjects();
		for(int i=0;i<testProject.length;i++){
			if(0 == testProject[i].getPrefix().compareTo(prefix)){
				return testProject[i].getId();
			}
		}
		return null;
	}
	public Integer getTestPlanId(String projectPrefix,String testPlanName){
		TestPlan[] testPlan = testLinkAPI.getProjectTestPlans(getTestProjectId(projectPrefix));
		for(int i=0;i<testPlan.length;i++){		
			if(0 == testPlan[i].getName().compareTo(testPlanName)){
				return testPlan[i].getId();
			}
		}
		return null;
	}
	public Integer getPlatformId(String projectPrefix,String platformName){
		Platform[] platform = testLinkAPI.getProjectPlatforms(getTestProjectId(projectPrefix));
		for(int i=0;i<platform.length;i++){
			if(0 == platform[i].getName().compareTo(platformName)){
				return platform[i].getId();
			}
		}
		return null;
	}
	public Integer getTestCaseId(String fullTestCaseExternalId,Integer version){
		try {
			TestCase testCase =  testLinkAPI.getTestCaseByExternalId(fullTestCaseExternalId,version);

			return testCase.getId();
		} catch (TestLinkAPIException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public Integer getLastVersion(String fullTestCaseExternalId){
		Integer version = 0;
		try {
			while (true) {
				testLinkAPI.getTestCaseByExternalId(fullTestCaseExternalId, ++version);
			}
		} catch (TestLinkAPIException e) {
			// TODO Auto-generated catch block
			return testLinkAPI.getTestCaseByExternalId(fullTestCaseExternalId, --version).getVersion();
		}
	}
	public String getFullExternalId(String testCasePathName ){
		String[] pathNames = testCasePathName.split(" :: ");
		Integer testSuiteId =  getFirstLevelTestSuiteId(pathNames[0],pathNames[1]);
		for(int i=2;i<pathNames.length-1;i++){
			testSuiteId = getTestSuiteId(testSuiteId,pathNames[i]);
		}
		TestCaseDetails detail = TestCaseDetails.FULL;
		boolean deep = true;
		TestCase[] testCases = testLinkAPI.getTestCasesForTestSuite(testSuiteId, deep, detail);
		for(int i=0;i<testCases.length;i++){
			if(0 == testCases[i].getName().compareTo(pathNames[pathNames.length-1])){
				return testCases[i].getFullExternalId();
			}
		}
		return null;
	}
	public String getFullExternalId(Integer testCaseId,Integer version){
		TestCase testCase = testLinkAPI.getTestCase(testCaseId, null, version);
		return testCase.getFullExternalId();
	}
	
	private Integer getFirstLevelTestSuiteId(String testProjectName,String testSuiteName){
		TestSuite[] testSuite = null;
		testSuite = testLinkAPI.getFirstLevelTestSuitesForTestProject( getTestProjectIdByName(testProjectName));
		for(int i=0;i<testSuite.length;i++){
			if(0 == testSuite[i].getName().compareTo(testSuiteName)){
				return testSuite[i].getId();
			}
		}
		return null;

	}
	
	private Integer getTestSuiteId(Integer testSuiteId,String testSuiteName){
		TestSuite[] testSuite = null;
	
		testSuite = testLinkAPI.getTestSuitesForTestSuite(testSuiteId);
		for(int i=0;i<testSuite.length;i++){
			if(0 == testSuite[i].getName().compareTo(testSuiteName)){
				return testSuite[i].getId();
			}
		}
		return null;
	}
	public Integer getTestSuiteId(String testSuitePathName){
		String[] pathNames = testSuitePathName.split(" :: ");
		Integer testSuiteId =  getFirstLevelTestSuiteId(pathNames[0],pathNames[1]);
		for(int i=2;i<pathNames.length;i++){
			testSuiteId = getTestSuiteId(testSuiteId,pathNames[i]);
		}
		return testSuiteId;
		
	}
	
	public Integer getPlatform(Integer testProjectId,String platformName){
		Platform[] platform = testLinkAPI.getProjectPlatforms(testProjectId);
		for(int i=0;i<platform.length;i++){
			
			if(0 == platform[i].getName().compareTo(platformName)){
				return platform[i].getId();
			}
		}
		return null;
	}
	public Integer createTestSuiteAndGetId(Integer testProjectId,String name,String details,
			Integer lastTestSuiteIdOrProjectId,Integer order ){
		boolean checkDuplicatedName = true;
		ActionOnDuplicate actionOnDuplicatedName = ActionOnDuplicate.BLOCK;
		return testLinkAPI.createTestSuite(testProjectId, name, details, 
				lastTestSuiteIdOrProjectId, order, checkDuplicatedName, 
				actionOnDuplicatedName).getId();
	}
	public final ExecutionType ExecutionTypeAUTOMATED = ExecutionType.AUTOMATED;
	public final ExecutionType ExecutionTypeMANUAL = ExecutionType.MANUAL;
	private List<TestCaseStep> steps = null;
	public List<TestCaseStep> getTestCaseList(boolean firstStep,int number,
			String expectedResults, String actions, ExecutionType executionType ){
		  TestCaseStep step = new TestCaseStep();
	      step.setNumber(number);
	      step.setExpectedResults(expectedResults);
	      step.setExecutionType(executionType);
	      step.setActions(actions);
	      if(firstStep){
	    	  steps = new ArrayList<TestCaseStep>();
	      }
	      steps.add(step);
		return null;
	}
	public List<TestCaseStep> getTestCaseList(ArrayList<String> actions, 
			ArrayList<String> expectedResults, ArrayList<ExecutionType> executionType){
		if (actions.size() != expectedResults.size()) {
			throw new IllegalArgumentException("actions.size() != expectedResults.size()");
		}else if (actions.size() != executionType.size()) {
			throw new IllegalArgumentException("actions.size() != executionType.size()");
		}
		List<TestCaseStep> steps = new ArrayList<TestCaseStep>();
		TestCaseStep step = null;
		int number = 1;
		for(int i=0;i<actions.size();i++){
			step = new TestCaseStep();
		    step.setNumber(number + i);
		    step.setExpectedResults(expectedResults.get(i));
		    step.setExecutionType(executionType.get(i));
		    step.setActions(actions.get(i));
		    steps.add(step);
		}
		return null;
	}
	
	
	public final TestCaseStatus TestCaseStatusDRAFT = TestCaseStatus.DRAFT;
	public final TestCaseStatus TestCaseStatusFINAL = TestCaseStatus.FINAL;
	public final TestCaseStatus TestCaseStatusFUTURE = TestCaseStatus.FUTURE;
	public final TestCaseStatus TestCaseStatusOBSOLETE = TestCaseStatus.OBSOLETE;
	public final TestCaseStatus TestCaseStatusREADY_FOR_REVIEW = TestCaseStatus
			.READY_FOR_REVIEW;
	public final TestCaseStatus TestCaseStatusREVIEW_IN_PROGRESS = TestCaseStatus
			.REVIEW_IN_PROGRESS;
	public final TestCaseStatus TestCaseStatusREWORK = TestCaseStatus.REWORK;
	
	public final TestImportance TestImportanceLOW = TestImportance.LOW;
	public final TestImportance TestImportanceHIGH = TestImportance.HIGH;
	public final TestImportance TestImportanceMEDIUM = TestImportance.MEDIUM;


	
	public Integer createTestCaseAndGetId(String testCaseName,
			Integer testSuiteId, Integer testProjectId,
			String authorLogin, String summary,String preconditions,
			List<TestCaseStep> steps,TestCaseStatus status,TestImportance importance,
			ExecutionType execution,int order,boolean newVersion){
		Integer internalId = 0;
		boolean checkDuplicatedName = true;
		
		ActionOnDuplicate actionOnDuplicatedName = ActionOnDuplicate.BLOCK;
		if(newVersion){
			actionOnDuplicatedName = ActionOnDuplicate.CREATE_NEW_VERSION;
		}
		return  testLinkAPI.createTestCase(testCaseName, testSuiteId, testProjectId, 
				authorLogin, summary, steps, preconditions,status, importance, execution,
				 order, internalId, checkDuplicatedName, actionOnDuplicatedName).getId();
	}
	public void addTestCaseToTestPlan(Integer testProjectId,Integer testPlanId,
			Integer testCaseId,Integer version,Integer platformId,int  order){
		Integer urgency = 1;
		testLinkAPI.addTestCaseToTestPlan(testProjectId, testPlanId,
				testCaseId, version, platformId, order, urgency);
	}
	
	
	public final ExecutionStatus ExecutionStatusBLOCKED = ExecutionStatus.BLOCKED;
	public final ExecutionStatus ExecutionStatusFAILED = ExecutionStatus.FAILED;
	public final ExecutionStatus ExecutionStatusNOT_RUN = ExecutionStatus.NOT_RUN;
	public final ExecutionStatus ExecutionStatusPASSED = ExecutionStatus.PASSED;
	
	private boolean isSetToExecuted(Integer testPlanId,Integer testCaseId,
			ExecutionType executionType){
		TestCase[] testCases = testLinkAPI.getTestCasesForTestPlan(testPlanId,
				null, null, null, null, null, null, null,
                ExecutionType.AUTOMATED, null, TestCaseDetails.FULL);
		for(int i=0;i<testCases.length;i++){
			if((int)testCases[i].getId() == (int)testCaseId){
				return true;
			}
		}
		testCases = testLinkAPI.getTestCasesForTestPlan(testPlanId,
				null, null, null, null, null, null, null,
                ExecutionType.MANUAL, null, TestCaseDetails.FULL);
		for(int i=0;i<testCases.length;i++){
			if((int)testCases[i].getId() == (int)testCaseId){
				return true;
			}
		}
		return false;
	}

	
	
	public ExecutionStatus getLastExecutionResult(String planName,String projectName,
			String fullTestCaseExternalId,
			ExecutionType executionType,Integer version){
		TestPlan testPlan = testLinkAPI.getTestPlanByName(planName, projectName);
		Integer testPlanId = testPlan.getId();
		TestCase testCase = testLinkAPI.getTestCaseByExternalId(fullTestCaseExternalId, version);
		Integer testCaseId = testCase.getId();
		Integer testCaseExternalId = 0;
		Execution execution = testLinkAPI.getLastExecutionResult(testPlanId, testCaseId, testCaseExternalId);
		
		//testLinkAPI.resul

		
		if(execution == null){
			return ExecutionStatus.NOT_RUN;
		} else {
			if(ExecutionStatus.PASSED != execution.getStatus()){
				return ExecutionStatus.PASSED;	
			}else if(ExecutionStatus.FAILED != execution.getStatus()){
				return ExecutionStatus.FAILED;
			}else if(ExecutionStatus.BLOCKED!= execution.getStatus()){
				return ExecutionStatus.BLOCKED;
			}else{
				return ExecutionStatus.NOT_RUN;
			}
		}
		
		
	}

	
	public ExecutionStatus getLastExecutionResult(Integer testPlanId,Integer testCaseId,
			ExecutionType executionType){
		if(isSetToExecuted(testPlanId, testCaseId, executionType)){
			Execution execution = testLinkAPI.getLastExecutionResult(testPlanId, testCaseId, 0);
			if(execution == null){
				return ExecutionStatus.NOT_RUN;
			}
			return execution.getStatus();
		}
		return null;
	}
	public Build[] getBuilds(Integer testPlanId){
		return testLinkAPI.getBuildsForTestPlan(testPlanId);
	}

	
	public void executeTestCase(Integer testCaseId,Integer testPlanId,
			ExecutionStatus status,String buildName,String notes,
			String platformName,Map<String,String> customFields){
		Integer testCaseExternalId = 0;
		Boolean guess = false;
		String bugId = null;
		Integer platformId = 0;
		Boolean overwrite = true;
		testLinkAPI.reportTCResult(testCaseId, testCaseExternalId, testPlanId, status, null, buildName, notes, guess, bugId, platformId, platformName, customFields, overwrite);
	}
	public String getExecutionResults(Integer testPlanId,Integer testCaseId,Integer testProjectId,String customFieldName){
		ResponseDetails details = ResponseDetails.FULL;
		
		TestCase tc = testLinkAPI.getTestCase(testCaseId, null, 1);
		Integer testCaseExternalId = tc.getId();
		Integer  versionNumber = tc.getVersion();
	
		
		System.out.println("testCaseId: "+testCaseId);
		//CustomField cf = testLinkAPI.getTestCaseCustomFieldDesignValue(testCaseId, testCaseExternalId, versionNumber,
			//	testProjectId, customFieldName, details);
		

        Map<String, Object> executionData = new HashMap<String, Object>();
        executionData.put(TestLinkParams.TEST_CASE_ID.toString(), testCaseId);
        executionData.put(TestLinkParams.TEST_CASE_EXTERNAL_ID.toString(), testCaseExternalId);
        executionData.put(TestLinkParams.VERSION.toString(), versionNumber);
        executionData.put(TestLinkParams.TEST_PROJECT_ID.toString(), testProjectId);
        executionData.put(TestLinkParams.CUSTOM_FIELD_NAME.toString(), customFieldName);
        executionData.put(TestLinkParams.DETAILS.toString(), Util.getStringValueOrNull(details));
		
        
        
        try {
			Object response = testLinkAPI.executeXmlRpcCall(TestLinkMethods.GET_TEST_CASE_CUSTOM_FIELD_DESIGN_VALUE.toString(),
					executionData);
			System.out.println(response.toString());
		} catch (TestLinkAPIException | XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
		return null;
	}
	
	public String getExecutionResults(Integer testPlanId,Integer testCaseId,Integer testCaseExternalId,DataBase db,String browser){
		Execution exection = testLinkAPI.getLastExecutionResult(testPlanId, testCaseId, testCaseExternalId);

	
		if(exection == null){
			return TestLinkClass.getTestLink().ExecutionStatusNOT_RUN.name();
		}else{
			System.out.println("testPlanId:         "+testPlanId);
			Integer idExecutionVersion = exection.getTestCaseVersionId();
			Integer id = exection.getId();
			System.out.println("idExecutionVersion: "+idExecutionVersion);
			System.out.println("id:                 "+id);
			return exection.getStatus().name();
		}
	
	}
	public Integer getIdExecutionVersion(Integer testPlanId,Integer testCaseId){
		Execution exection = testLinkAPI.getLastExecutionResult(testPlanId, testCaseId, null);
		if(exection == null){
			return null;
		}else{
			return exection.getTestCaseVersionId();
		}
	}
	
	public String executeTestCaseMessage(Integer testCaseId,Integer testPlanId,
			ExecutionStatus status,String buildName,String notes,
			String platformName,Map<String,String> customFields,boolean afterRemoveExecution,boolean set){
		Integer testCaseExternalId = 0;
		Boolean guess = false;
		String bugId = null;
		Integer platformId = 0;
		//Boolean overwrite = true;
		Boolean overwrite = false;
		ReportTCResultResponse rtcrr;
		Integer buildId = null;
		try {
			if(set) {
				rtcrr = testLinkAPI.setTestCaseExecutionResult(testCaseId,
						testCaseExternalId, testPlanId, status, buildId, buildName, notes, guess,
						bugId, platformId, platformName, customFields,overwrite);
			}else {
				rtcrr = testLinkAPI.reportTCResult(testCaseId,
						testCaseExternalId, testPlanId, status, buildId, buildName, notes, guess,
						bugId, platformId, platformName, customFields, overwrite);
			}
			if(afterRemoveExecution){
				removeExecuteTestCase(testCaseId,testPlanId);
			}
			return rtcrr.getMessage();
		} catch (TestLinkAPIException e) {
			setError(true);
			return e.getMessage();
		}
		
	}
	
	public int removeExecuteTestCase(Integer testCaseId,Integer testPlanId){
		Integer testCaseExternalId = 0;
		Execution execution = testLinkAPI.getLastExecutionResult(testPlanId, testCaseId, testCaseExternalId);
		if(execution == null){
			return  -1;
		}else{
			Integer executionId = execution.getId();
			testLinkAPI.deleteExecution(executionId);
			return (int)executionId;
		}

	}
	
	
	public String getFullExternalId(Integer testSuiteId,String testCaseName){
		TestCaseDetails detail = TestCaseDetails.FULL;
		boolean deep = true;
		TestCase[] testCases = testLinkAPI.getTestCasesForTestSuite(testSuiteId, deep, detail);
		for(int i=0;i<testCases.length;i++){
			if(0 == testCases[i].getName().compareTo(testCaseName)){
				return testCases[i].getFullExternalId();
			}
		}
		return null;
	}
	public String[] getTestProjects(){
		TestProject[] projecs = testLinkAPI.getProjects();
		String[] projectPrfixsNames = new String[projecs.length];
		for(int i=0;i<projecs.length;i++){
			projectPrfixsNames[i] = projecs[i].getPrefix()+":"+projecs[i].getName();
		}
		return projectPrfixsNames;
	}
	
	public String[] getTestPlans(String prefix){
		Integer projectId = getTestProjectId(prefix);
		TestPlan[] testPlan = testLinkAPI.getProjectTestPlans(projectId);
		String[] testPlanNames = new String[testPlan.length];
		for(int i=0;i<testPlan.length;i++){
			testPlanNames[i] = testPlan[i].getName();
		}
		return testPlanNames;
	}
	
	public String[] getProjectPlatforms(String prefix){
		Integer projectId = getTestProjectId(prefix);
		Platform[] platforms = testLinkAPI.getProjectPlatforms(projectId);
		String[] platformNames = new String[platforms.length];
		
		for(int i=0;i<platforms.length;i++){
			platformNames[i] = platforms[i].getName();
		}
		return platformNames;
	}
}
