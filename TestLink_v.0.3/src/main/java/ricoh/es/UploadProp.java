package ricoh.es;

import java.io.File;



public class UploadProp {
    protected static void initialize(Panel p){
    	PanelClass.set(p);
    	setProp();
    }
	final protected static String PATH = (new java.io.File("")).getAbsolutePath().concat("\\prop");
	final protected static String FILE = "config.properties";

	final private static String KEY = "R1C0H_1T_5ER31CE5";

	private static String devKey;
	
    private static ricoh.es.methods.Propietats prop = null;
    protected static void setProp(){

    	String titol = null;
    	prop = new ricoh.es.methods.Propietats(PATH, FILE, null, null,titol);

    	if(prop.propBoolean("CRYPT")){
    		devKey = ricoh.es.methods.Crypt
    				.decrypt(prop.propString("DEVKEY"), KEY);
    	}else{
    		devKey = prop.propString("DEVKEY");
    	}
     	Processing.initialize(prop.propString("TL_URL"),devKey,
    			prop.propString("DEF_PROJPREF")+":"+prop.propString("DEF_PROJNAME"),
    			prop.propString("DEF_TESTPLAN"));
    }
    protected static void initialize(){
    	if(prop.propBoolean("CRYPT")){
    		devKey = ricoh.es.methods.Crypt
    				.decrypt(prop.propString("DEVKEY"), KEY);
    	}else{
    		devKey = prop.propString("DEVKEY");
    	}
    	
     	Processing.initialize(prop.propString("TL_URL"),devKey,
 
    			prop.propString("DEF_PROJPREF"),
    			prop.propString("DEF_PROJNAME"),
    			prop.propString("DEF_TESTPLAN"));
     	Processing.initialitzeExcelToTestLink();
     	PanelClass.get().jTextFieldExcelToTestlink.setText("");
     	PanelClass.get().jProgressBarExelToTestLink.setForeground(new java.awt.Color(0, 255, 0));
     	// RED .setForeground(new java.awt.Color(255, 0, 0));
     	
     	PanelClass.get().jProgressBarExelToTestLink.setIndeterminate(false);
     	
     	Execute.initalize(prop.propString("CUSTOMFIELDS.KEYS"),
     			prop.propStringSplit("CUSTOMFIELDS.HEADER",";"),
     			prop.propStringSplit("CUSTOMFIELDS.VALUES",";"),
     			prop.propString("MAIN.SHEET"),
     			prop.propStringSplit("ENVIRONMENT.SHEET",";"),
     			prop.propString("ENVIRONMENT.HEADER"),
     			prop.propString("TESTPLAN.HEADER"),
     			prop.propString("BUILD.HEADER"),
     			prop.propInteger("MAIN.HEADER.ROW"),
     			prop.propInteger("RESULTS.HEADER.ROW"),
     			prop.propStringSplit("RESULTS.VALID",";"),
     			prop.propStringSplit("RESULTS.RELAT",";"),
     			prop.propString("RESULTS.HEADER.CODE"),
    			prop.propString("RESULTS.HEADER.TEST"),
     			prop.propString("RESULTS.HEADER.COMMENT"),
     			prop.propString("EXECUTED_BROWSER"));
     		
     	

     	
    }
    protected static void uploadExcelToTestLink(){
    	String path = prop.propString("EXCEL.PATH");
    	String fileName = prop.propString("EXCEL.FILE","");
    	String formatsName = "*.xls,*.xlsx";
    	String[] formats = {"*.xls","*.xlsx"};
    	String headerButton = "CHOOSE EXCEL";
    	
    	File f = ricoh.es.methods.Panell.fileChooser(path, fileName, formatsName, formats, headerButton);
    	if(f != null){
    		PanelClass.get().jTextFieldExcelToTestlink.setText(f.getAbsolutePath());

    	}

    }
    
    		
}
