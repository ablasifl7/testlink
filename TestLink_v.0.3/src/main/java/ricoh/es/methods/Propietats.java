/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ricoh.es.methods;

/**
 *
 * @author Usuario
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Propietats {
	private Properties prop;
	private String path;
	public Properties getProp() {
		return prop;
	}
	public void setProp(Properties prop) {
		this.prop = prop;
	}
	public String getPath() {
		return path;
	}
	private void setPath(String path) {
		this.path = path;
	}

	//'path' = null: path = (new File(path)).getAbsolutePath();
	//'path' has to exist
	//'file' could be call 'file.properties' or 'config//file.properties'. It is not need to exist
	public Propietats(String path, String file, String titol, String[] camp,
			String[] valor) throws FileNotFoundException, IOException {
		if (camp == null) {
			camp = new String[0];
			valor = new String[0];
		}
		if (camp.length != valor.length) {
			throw new IllegalArgumentException("camp.length != valor.length");
		}
		if (path == null)
			path = "";
		path = (new File(path)).getAbsolutePath();
		if (!(new File(path)).exists())
			throw new IllegalArgumentException("Path '" + path + "' Not Found");
		String path_file = path.concat("\\").concat(file);
		File f = new File(path_file);
			boolean createDir = (new File((f).getParent())).mkdirs();

			Properties pr = new Properties();

			if (!f.exists() || createDir) {
			for (int i = 0; i < camp.length; i++) {
				pr.setProperty(camp[i], valor[i]);
				pr.store(new FileOutputStream(path_file), titol);
			}
		}	
		pr.load(new FileInputStream(path_file));
		setProp(pr);
		setPath(path_file);
	}

	public Propietats(String path, String file, String[] camp, String[] valor,
			String titol) {
		try {
			if (camp == null) {
				camp = new String[0];
				valor = new String[0];
			}
			if (camp.length != valor.length) {
				throw new IllegalArgumentException("camp.length != valor.length");
			}
			if (path == null)
				path = "";
			path = (new File(path)).getAbsolutePath();
			if (!(new File(path)).exists())
				throw new IllegalArgumentException("Path '" + path + "' Not Found");
			String path_file = path.concat("\\").concat(file);
			File f = new File(path_file);
			boolean createDir = (new File((f).getParent())).mkdirs();

			Properties pr = new Properties();

			if (!f.exists() || createDir) {

				for (int i = 0; i < camp.length; i++) {
					pr.setProperty(camp[i], valor[i]);
					pr.store(new FileOutputStream(path_file), titol);
				}
			}
			pr.load(new FileInputStream(path_file));
			setProp(pr);
			setPath(path_file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void afegirOmodificar(String camp,String valor,String titol){
		try {
			addOrReplace(camp,valor,titol);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void addOrReplace(String field,String value,String title) throws IOException{
		getProp().setProperty(field,value);
		getProp().store(new FileOutputStream(getPath()), title);
	}
	public void borrar(String camp,String titol){
		try {
			remove(camp,titol);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void remove(String field,String title) throws IOException{
		getProp().remove(field);
		getProp().store(new FileOutputStream(getPath()), title);
	}

	public String propString(String camp,String valor) {
		try {
			return string(camp,valor);
		} catch (Exception e) {
			return valor;
		}
	}
	private String string(String field,String value) throws Exception {
		return getProp().getProperty(field,value);
	}

	public String propString(String camp) {
		try {
			return string(camp);
		} catch (Exception e) {
			return null;
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String[] propStringArray(String camp) {
		try {
			java.util.List list = new java.util.ArrayList();
			String txt = propString(camp.concat("0"),null);
			int n = 0;
			while(txt != null) {
				txt = propString(camp.concat(""+(++n)),null);
				list.add(txt);
			}
			return (String[]) list.toArray(new String[list.size()]);
		} catch (Exception e) {
			return null;
		}
	}
	public String[] propStringSplit(String camp,String separat) {
		try {
			return string(camp).split(separat);
		} catch (Exception e) {
			return null;
		}
	}
	
	public boolean propBoolean(String camp) {
		try {
			return Boolean.parseBoolean(string(camp));
		} catch (Exception e) {
			return false;
		}
	}
	public int propInteger(String camp) {
		try {
			return Integer.parseInt(string(camp));
		} catch (Exception e) {
			return 0;
		}
	}
	public long propLong(String camp) {
		try {
			return Long.parseLong(camp);
		} catch (Exception e) {
			return 0;
		}
	}
	private String string(String field) throws Exception {
		return getProp().getProperty(field,null);
	}
}

