package ricoh.es;

public class PanelClass  extends ricoh.es.Panel {
	
	private static Panel p;
	protected static Panel get(){
		return p;
	}
	
	protected static void set(Panel p){
		PanelClass.p = p;
	}
	
	
	
}
