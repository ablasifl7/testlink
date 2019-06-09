/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ricoh.es;



/**
 *
 * @author Agustí
 */
public class Buttons {
	protected static void initializeProp() {
		UploadProp.setProp();
		UploadProp.initialize();
	}

	protected static void openProp() {
		ricoh.es.methods.Utils.openFile(UploadProp.PATH+"\\"+UploadProp.FILE);
	}

	protected static void projecsItemStateChanged() {
		Processing.checkProject(PanelClass.get().jComboBoxTestProjects.getSelectedItem().toString());

	}

	protected static void testPlanItemStateChanged() {
		Processing.checkTestPlant(PanelClass.get().jComboBoxTestProjects.getSelectedItem().toString(),
				PanelClass.get().jComboBoxTestPlan.getSelectedItem().toString());

	}
    protected static void uploadExcelToTestLink(){
    	UploadProp.uploadExcelToTestLink();
        
    }
     protected static void executeExcelToTestLink(){
    	 Execute.executeExcelToTestLink();
    }
     protected static void filter(){
    	 ExecuteExcel.filter();
     }
     protected static void uploadExcel(){
    	 Execute.uploadExcel();
     }
     protected static void checkExcel(){
    	 Execute.checkExcel();
     }
}
