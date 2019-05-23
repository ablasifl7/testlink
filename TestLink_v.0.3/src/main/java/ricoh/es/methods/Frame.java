/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ricoh.es.methods;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



import javax.swing.AbstractListModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;




public class Frame {

	@SuppressWarnings("serial")
	public static void generarTaula(JTable jTaula, String[] cap,Object[][] ob,
			int[] sizeCol,@SuppressWarnings("rawtypes") Class[] typeCol,boolean[] edit,Object[] comboBox){


    	if(typeCol==null){
    		if(ob != null){
            	if(ob.length>0){
            		typeCol = new Class[ob.length];
            		for(int i=0;i<ob[0].length;i++){
            			typeCol[i]=ob[0][i].getClass();
            		}
            	}
    		}
    	}else if(cap.length!=typeCol.length){
    		throw new IllegalArgumentException("cap.length <> typeCol.length");
    	}

    	@SuppressWarnings("rawtypes")
		final Class[] tipus = typeCol;

    	final boolean[] canEdit = edit;
    	jTaula.setModel(new DefaultTableModel(ob,cap)
    	{
    		public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				if(tipus==null){
					return null;
				}
			    return tipus [columnIndex];
			}


        });
    	jTaula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    	jTaula.getTableHeader().setReorderingAllowed(false);
    	if(sizeCol!=null){
            for(int i=0;i<sizeCol.length;i++) {
                jTaula.getColumnModel().getColumn(i).setPreferredWidth(sizeCol[i]);
            }
    	}
    	if(comboBox!=null){
    		 if(cap.length < comboBox.length){
    			 throw new IllegalArgumentException("cap.length < comboBox.length");
    		 }
            for(int i=0;i<comboBox.length;i++) {
            	if(comboBox[i]!=null){
                        String[] s = (String[])comboBox[i];
            		jTaula.getColumnModel().getColumn(i)
            		.setCellEditor((TableCellEditor) new DefaultCellEditor(crearComboBox(s)));
            	}
            }
    	}

    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static JComboBox crearComboBox(String[] s){
            JComboBox jComboBox = new JComboBox();
            jComboBox.setModel(new DefaultComboBoxModel(s));
        return jComboBox;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void generarComboBox(JComboBox jComboBox,String[] s,String sel){
    	jComboBox.setModel(new DefaultComboBoxModel(s));
    	if(sel!=null) jComboBox.setSelectedItem(sel);
    }
    @SuppressWarnings("serial")
    public static void generarList(JList<Object> jList,final String[] s){

    	jList.setModel(new AbstractListModel<Object>() {

			String[] strings = s;


            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });

    }

	public static void acotarTextField(final JTextField jTextTitol,final int l){
		jTextTitol.addKeyListener (new java.awt.event.KeyAdapter() {
			public void keyTyped (java.awt.event.KeyEvent ke) {
				if(jTextTitol.getText().length()>=l) ke.consume ();
			}}
        );
	}
	public static void nomesNumericTextField(final JTextField jnum){
		jnum.setText("0");
	    jnum.addKeyListener (new KeyAdapter() {
	            @Override
	            public void keyTyped (KeyEvent ke) {
	            char c = ke.getKeyChar ();
	            if (!((Character.isDigit (c) || (c == KeyEvent.VK_BACK_SPACE)))) {
	            ke.consume ();
	            }}}
	        );
	}


}
