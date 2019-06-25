package ricoh.es;

public class Processing {
	protected static TestLink tl;
	
	
	protected static void initialize(String testLinkURL,
			String devKey,String defProject,String defTestPlan){
	
		String[] projects = {defProject};
		String[] testPlan = {defTestPlan};
		TestLinkClass.setProject(defProject);
		TestLinkClass.setTestPlant(defTestPlan);
		ricoh.es.methods.Frame.generarComboBox(PanelClass.get().jComboBoxTestProjects,
				projects, defProject);
		ricoh.es.methods.Frame.generarComboBox(PanelClass.get().jComboBoxTestPlan,
				testPlan,defTestPlan);

		PanelClass.get().jComboBoxLastExecution.setSelectedIndex(4);
	}


	private static ProcessingThread tp = new ProcessingThread();
	protected static void initialize(String testLinkURL,
			String devKey,String defProjectPrefix,String 
			defProjectName,String defTestPlan){
		tp = new ProcessingThread();

		tp.setTestLinkURL(testLinkURL);
		tp.setDevKey(devKey);
		tp.setProjectPrefix(defProjectPrefix);
		tp.setProjectName(defProjectName);
		tp.setTestPlan(defTestPlan);
		tp.setDef(true);
		tp.start();

	}
	protected static void checkProject(String project){
		if(!ricoh.es.methods.Utils.equals(project, TestLinkClass.getProject())){
			TestLinkClass.setProject(project);
			tp = new ProcessingThread();
			tp.setProjectPrefix(TestLinkClass.hmProjectPrefix.get(project));
			tp.setTestPlan(null);
			tp.setDef(false);
			tp.start();
		}
	}
	protected static void checkTestPlant(String project, String testPlant){
		String prefix = null;
		if(!ricoh.es.methods.Utils.equals(testPlant, TestLinkClass.getTestPlant())){
			TestLinkClass.setTestPlant(testPlant);
			tp = new ProcessingThread();
			prefix = TestLinkClass.hmProjectPrefix.get(TestLinkClass.getProject());
			tp.setProjectPrefix(prefix);
			tp.setTestPlan(testPlant);
			tp.setProjectName(TestLinkClass.hmPrefixProjName.get(prefix));
			tp.setDef(false);
			tp.start();
		}
		


	}
	protected static void initialitzeExcelToTestLink(){
		
	}
	
	
	
	
}
