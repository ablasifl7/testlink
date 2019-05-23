/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ricoh.es.methods;

/**
 *
 * @author Usuario
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Fitxer {

	public static void write(String path_file,String text){
	    try {
			FileWriter fw = new FileWriter(path_file,false);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(text);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void add(String path_file,String text){
	    try {
			FileWriter fw = new FileWriter(path_file,true);
			BufferedWriter out = new BufferedWriter(fw);
			out.write(text);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String[] read(String path_file){
		List<String> list = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(path_file);
			BufferedReader in = new BufferedReader(fr);
			String str = null;
			while ((str=in.readLine()) != null) {
				list.add(str);
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
	public static void writeUTF8(String path_file,String text){
	    try {
			OutputStreamWriter osw = new  OutputStreamWriter(new FileOutputStream(path_file),"UTF8");
			BufferedWriter out = new BufferedWriter(osw);
			out.write(text);
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void addUTF8(String path_file,String text){
	    try {
			OutputStreamWriter osw = new  OutputStreamWriter(new FileOutputStream(path_file,true),"UTF8");
			BufferedWriter out = new BufferedWriter(osw);
			out.write(text);
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String[] readUTF8(String path_file){
		List<String> list = new ArrayList<String>();
		try {
			File fileDirs = new File(path_file);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileDirs), "UTF-8"));
			String str = null;
			while ((str=in.readLine()) != null) {
				list.add(str);
			}
			in.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list.toArray(new String[list.size()]);
	}
	public static java.util.HashMap<String,String[]> readCSV(String path_csv){
		String[] lin = read(path_csv);
		int rows = lin.length;
		java.util.HashMap<String,String[]> hm = new java.util.HashMap<String,String[]>();
		if(rows > 0){
			String[] header = lin[0].split(";");
			hm.put("", header);
			for(int i=0;i<header.length;i++){
				hm.put(header[i], new String[rows-1]);
			}
			for(int j=1;j<rows;j++){
				for(int i=0;i<header.length;i++){
					String[] s = lin[j].split(";");
                                        if(s.length==header.length)
                                            hm.get(header[i])[j-1]=s[i];
				}
			}
		}
		return hm;
	}
	public static void writeCSV(java.util.HashMap<String,String[]> hm,String path_csv){
		String text = "";
		if(hm!=null){
			if(hm.get("")!=null){
				String[] header = hm.get("");
				for(int i=0;i<header.length;i++){
					if(i!=0) text = text.concat(";");
					text = text.concat(header[i]);
				}
				int rows = 0;
				if(hm.get(header[0])!=null) rows = hm.get(header[0]).length;
				for(int j=0;j<rows;j++){
					text = text.concat("\n");
					for(int i=0;i<header.length;i++){
						if(i!=0) text = text.concat(";");
						text = text.concat(hm.get(header[i])[j]);
					}
				}
			}
		}
		write(path_csv, text);
	}
	public static java.util.HashMap<String,String[]> readCSVutf8(String path_csv){
		String[] lin = readUTF8(path_csv);
		int rows = lin.length;
		java.util.HashMap<String,String[]> hm = new java.util.HashMap<String,String[]>();
		if(rows > 0){
			String[] header = lin[0].split(";");
			hm.put("", header);
			for(int i=0;i<header.length;i++){
				hm.put(header[i], new String[rows-1]);
			}
			for(int j=1;j<rows;j++){
				for(int i=0;i<header.length;i++){
					String[] s = lin[j].split(";");
					hm.get(header[i])[j-1]=s[i];
				}
			}
		}
		return hm;
	}
	public static void writeCSVutf8(java.util.HashMap<String,String[]> hm,String path_csv){
		String text = "";
		if(hm!=null){
			if(hm.get("")!=null){
				String[] header = hm.get("");
				for(int i=0;i<header.length;i++){
					if(i!=0) text = text.concat(";");
					text = text.concat(header[i]);
				}
				int rows = 0;
				if(hm.get(header[0])!=null) rows = hm.get(header[0]).length;
				for(int j=0;j<rows;j++){
					text = text.concat("\n");
					for(int i=0;i<header.length;i++){
						if(i!=0) text = text.concat(";");
						text = text.concat(hm.get(header[i])[j]);
					}
				}
			}
		}
		writeUTF8(path_csv, text);

	}
}

