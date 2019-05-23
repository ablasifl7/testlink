package ricoh.es;

import javax.swing.JOptionPane;

import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

public class ProcessingThread extends Thread  {

	protected void setTestLinkURL(String testLinkURL) {
		this.testLinkURL = testLinkURL;
	}
	protected void setDevKey(String devKey) {
		this.devKey = devKey;
	}

	protected void setTestPlan(String testPlan) {
		this.testPlan = testPlan;
	}
	protected void setProjectPrefix(String projectPrefix) {
		this.projectPrefix = projectPrefix;
	}
	protected void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	protected boolean isDef() {
		return def;
	}
	protected void setDef(boolean def) {
		this.def = def;
	}


	private String testLinkURL;
	private String devKey;
	private static TestLink tl;

	private String projectPrefix;
	private String projectName;
	private String testPlan;
	private boolean def;

	private String[] cap = {"S","PLATFORMS"};
	private Object[][] ob = null;
	private int[] sizeCol = {20,220 };
	@SuppressWarnings("rawtypes")
	private Class[] typeCol = {Boolean.class,String.class};
	private boolean[] edit = {true,false};
	
	public void run(){
		PanelClass.get().jProgressBarUpload.setStringPainted(true);
		
		PanelClass.get().jProgressBarUpload.setStringPainted(true);
		PanelClass.get().jProgressBarUpload.setString("PROCESSING...");
		PanelClass.get().jProgressBarUpload.setIndeterminate(true);
		PanelClass.get().jComboBoxTestProjects .setEnabled(false);
		PanelClass.get().jComboBoxTestPlan.setEnabled(false);

		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTablePlatforms
				, cap, ob, sizeCol, typeCol, edit, null);
		if(isDef()){
			tl = new TestLink(testLinkURL, devKey);
		}else{
			tl.setError(false);
		}
		if(tl.isError()){
			PanelClass.get().jProgressBarUpload.setString(" ");
			PanelClass.get().jProgressBarUpload.setIndeterminate(false);
			PanelClass.get().jProgressBarUpload.setMaximum(1);
			PanelClass.get().jProgressBarUpload.setValue(1);
		}else{
			String errorMiss = null;
			if(isDef()){
				errorMiss = processingTestPlan();
			}else{
				String[] tp = tl.getTestPlans(projectPrefix);
				if(testPlan == null && tp.length > 0){
					testPlan = tp[0];
				}
				ricoh.es.methods.Frame.generarComboBox(PanelClass.get().jComboBoxTestPlan,
						tp,testPlan);
			}
			if(errorMiss == null){
				processingPlatform();
			}else{
				warningPanel(errorMiss);
			}
			endProcessing();
		}
     }

	private String processingTestPlan(){
		TestLinkClass[] tlc = tl.getTestProjectClass();
		String[] projects = new String[tlc.length];
		for(int i=0;i<tlc.length;i++){
			projects[i] = tlc[i].getProjectPrefix()+":"+tlc[i].getProjectName();
			TestLinkClass.hmProjectPrefix.put(projects[i], tlc[i].getProjectPrefix());
			TestLinkClass.hmPrefixProjName.put(tlc[i].getProjectPrefix(), tlc[i].getProjectName());
		}

		ricoh.es.methods.Frame.generarComboBox(PanelClass.
				get().jComboBoxTestProjects, projects,
				projectPrefix+":"+projectName);
		try {
			String[] tp = tl.getTestPlans(projectPrefix);
			ricoh.es.methods.Frame.generarComboBox(PanelClass.get().jComboBoxTestPlan,
					tp,testPlan);
		} catch (TestLinkAPIException e) {
			return "ERROR PROJECT PREFIX: '"+projectPrefix+"'";
		}
		return null;
	}
	private void processingPlatform(){
		TestLinkClass[] tlc = tl.getPlatformClass(projectPrefix);
		
		//System.out.println("projectPrefix: "+projectPrefix);
		//System.out.println("projectName:   "+projectName);
		//System.out.println("testPlan:      "+testPlan);
		
		tlc = tl.getPlatformClass(testPlan, TestLinkClass.hmPrefixProjName.get(projectPrefix));
		if(tlc==null){
			ob = new Object[0][2];
		}else{
			ob = new Object[tlc.length][2];
			for(int i=0;i<tlc.length;i++){
				ob[i][0] = true;
				ob[i][1] = tlc[i].getPlatformName();	
			}
		}


		ricoh.es.methods.Frame.generarTaula(PanelClass.get().jTablePlatforms
				, cap, ob, sizeCol, typeCol, edit, null);
		PanelClass.get().jComboBoxTestPlan.setEnabled(true);
		PanelClass.get().jComboBoxTestProjects.setEnabled(true);

	}
	private void processingPlatformAux(){
		
	}
	
	
	
	private void endProcessing(){
		PanelClass.get().jProgressBarUpload.setString(" ");
		
		PanelClass.get().jProgressBarUpload.setIndeterminate(false);
		PanelClass.get().jProgressBarUpload.setMaximum(1);
		PanelClass.get().jProgressBarUpload.setValue(1);
		
		TestLinkClass.setTestLink(tl);
		
		setDef(false);
	}
	
	private void warningPanel(String text){
		final String[] BUTTONS = {"ACCEPT"};
		JOptionPane.showOptionDialog(null, text, "WARNING",
                JOptionPane.CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, BUTTONS,
                BUTTONS[0]);
	}

	
}
