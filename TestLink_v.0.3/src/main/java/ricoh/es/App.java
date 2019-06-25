package ricoh.es;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionType;
import br.eti.kinoshita.testlinkjavaapi.constants.TestCaseStatus;
import br.eti.kinoshita.testlinkjavaapi.model.ReportTCResultResponse;
import br.eti.kinoshita.testlinkjavaapi.model.TestCaseStep;

/**
 * Hello world!
 *
 */




public class App 
{
	private static String testLinkURL = "*";
	private static String devKey = "*";
    public static void main( String[] args )
    {
       //Test1.test1();
       //Test1.test3();
    	//Test3.test1();
    	//Test4.test4();
    	//Test4.test7();
    	//test2();
    	//TestExcel.test1();
    	//testExcel();
    	
    	//crypt();
    	 hashMapTest();
    	
       System.out.println("END");
      
    }
    private static void hashMapTest(){
    	java.util.Map<String,String> m = new java.util.HashMap<String,String>();
    	m.put("A", "Hola,");
    	m.put("B", "Com");
    	m.put("C", "Estàs?");

    	System.out.println("A: "+m.get("A"));
    	System.out.println("B: "+m.get("B"));
    	System.out.println("C: "+m.get("C"));
    	m.put("B", "Molt be gracies");
    	System.out.println("A: "+m.get("A"));
    	System.out.println("B: "+m.get("B"));
    	System.out.println("C: "+m.get("C"));
    	
    }
    
    
    private static void crypt(){
    	String plainText = "4e0d7eada29acec933831a07d8dfa74d";
    	String key = "R1C0H_1T_5ER31CE5";
    	String text = null;
    	text = ricoh.es.methods.Crypt.encrypt(plainText, key);
    	System.out.println(text);
    	text = ricoh.es.methods.Crypt.decrypt(text, key);
    	System.out.println(text);
    	
    }
    
    private static void testExcel(){
    	String path = "C:\\Users\\Agustí\\Desktop\\Ma_documents\\CVs\\RICOH\\HC3\\HC3_ETC_Retornar_ClinicalDocument\\PKK - Prova KKX - PP relacionats.xls";
    	ricoh.es.methods.Excel excel = new ricoh.es.methods.Excel(path, null);
    	
    	java.util.List<String> list = excel.sheetNames();
    	for(int i=0;i<list.size();i++){
    		System.out.println(list.get(i));
    	}
    }
    
    private static void test2(){
    	TestLink tl = new TestLink(testLinkURL, devKey);
    	
    	String projectPrefix = "pkk";
    	String testPlanName = "pp relacions";//Evolutiu
    	Integer testPlanId = tl.getTestPlanId(projectPrefix, testPlanName);
    	String fullExternalId = "pkk-12";///
    	String platformName = "Acceptació";///
    	Integer testCaseId = tl.getTestCaseId(fullExternalId, 1);
    	ExecutionStatus status = ExecutionStatus.BLOCKED;
    	String buildName = "b relacions";
    	
    	String notes = "Test executat";
    	Map<String,String> customFields = new TreeMap<String,String>();
    	customFields.put("Navegador ejecutado", "IE");
    	 tl.executeTestCaseMessage(testCaseId, testPlanId,
    			status, buildName, notes, platformName, customFields,true,true);

    }
    
    private static void test1(){
    	TestLink tl = new TestLink(testLinkURL, devKey);
    	String testSuitePathName = "prova kkx :: suite test :: Proves automatitzades 1";
    	Integer testSuiteId = tl.getTestSuiteId(testSuitePathName);
    	System.out.println(testSuiteId);
    	ArrayList<String> actions = new ArrayList<String>();
    	ArrayList<String> expectedResults = new ArrayList<String>();
    	ArrayList<ExecutionType> executionType = new ArrayList<ExecutionType>();
    	actions.add("PAS 1");
    	actions.add("PAS 2");
    	actions.add("PAS 3");
    	expectedResults.add("RESULTAT 1");
    	expectedResults.add("RESULTAT 2");
    	expectedResults.add("RESULTAT 3");
    	executionType.add(tl.ExecutionTypeAUTOMATED);
       	executionType.add(tl.ExecutionTypeAUTOMATED);
       	executionType.add(tl.ExecutionTypeAUTOMATED);
    	
    	List<TestCaseStep> steps = tl.getTestCaseList(actions, expectedResults, executionType);
    	
    	
    	Integer testProjectId = tl.getTestProjectId("pkk");
    	System.out.println(testProjectId);
    	String authorLogin = "Agusti.Blasi";
    	
    	int n = 20;
    	for(int i=4;i<n;i++){
    		createTestCase(i, tl, testSuiteId, testProjectId, authorLogin, steps);
    	}
    	
    	
    		
    }
    private static void createTestCase(int n,TestLink tl,Integer testSuiteId,
    		Integer testProjectId,String authorLogin,List<TestCaseStep> steps){
    	String testCaseName = "Cas de prova "+n;

    	String summary = "Resum "+n;
    	String preconditions = "Pre-condicions "+n;
    	Integer order = 1;
  
    
    
    	Integer testCaseId = tl.createTestCaseAndGetId(testCaseName, testSuiteId, testProjectId,
    			authorLogin, summary, preconditions, steps, 
    			tl.TestCaseStatusDRAFT, 
    			tl.TestImportanceMEDIUM, 
    			tl.ExecutionTypeAUTOMATED, order, false);
    	if(testCaseId > 0){
    		String fullExternalId = tl.getFullExternalId(testCaseId,1);
        	System.out.println(fullExternalId);
        	assigne(tl, testProjectId, testCaseId,fullExternalId);
    	}else{
    		String fullExternalId = tl.getFullExternalId(testSuiteId, testCaseName);
    		System.out.println(fullExternalId);
    		testCaseId = tl.getTestCaseId(fullExternalId, 1);
        	assigne(tl, testProjectId, testCaseId,fullExternalId);
    	}
    }
    
    
    
    
    private static void assigne(TestLink tl, Integer testProjectId,Integer testCaseId,String fullExternalId){
    	Integer testPlanId = tl.getTestPlanId("pkk", "pp relacions");
    	String platformName = "Acceptació";
    	Integer platformId = tl.getPlatform(testProjectId, platformName);
    	System.out.println("testPlanId: "+testPlanId);
    	System.out.println("platformId: "+platformId);
    	ExecutionStatus executionStatus = tl.getLastExecutionResult(testPlanId, testCaseId, tl.ExecutionTypeAUTOMATED);
 
    	if(executionStatus == null){
        	tl.addTestCaseToTestPlan(testProjectId, testPlanId, testCaseId, 1, platformId, 1);
        	System.out.println("NO RUN");
        	//execute(tl, fullExternalId, testPlanId, platformName);
    	}else{
           	System.out.println(executionStatus.name());
           	//execute(tl, fullExternalId, testPlanId, platformName);
    	}
  

    }
    private static void execute(TestLink tl,String fullExternalId,Integer testPlanId,String platformName){
    	Integer testCaseId = tl.getTestCaseId(fullExternalId, 1);
    	ExecutionStatus status = ExecutionStatus.PASSED;
    	String buildName = "b relacions";
    	
    	String notes = "Test executat";
    	Map<String,String> customFields = new TreeMap<String,String>();
    	customFields.put("Navegador ejecutado", "IE");
    	tl.executeTestCase(testCaseId, testPlanId, status, buildName, notes, platformName, customFields);
    	ExecutionStatus executionStatus = tl.getLastExecutionResult(testPlanId, testCaseId, tl.ExecutionTypeAUTOMATED);
    	System.out.println(executionStatus.name());
    }
}
