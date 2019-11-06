
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Archivos {
	
	static List<File> listaFicherosXML = new ArrayList<File>();
	static List<File> listaFicherosScriptRec = new ArrayList<File>();

	public static ArrayList<File> listarArchivosXMLRecursivamente(File file) {	   	
		File[] ficheros = file.listFiles();

		for (int i = 0; i < ficheros.length; i++) {
			if (ficheros[i].isDirectory()) {
				listarArchivosXMLRecursivamente(ficheros[i]);
			} else if (getExtension(ficheros[i].getName()).equals("xml") && !ficheros[i].getName().equals("sicampc.xml") )
			{
				listaFicherosXML.add(ficheros[i]);				
			}
		}		
		return  (ArrayList<File>) listaFicherosXML;
	}	 
	
	public static ArrayList<File> listarArchivosScriptRec(File file) {	
		File[] ficheros = file.listFiles();	
		if (ficheros!=null) {
			for (int i = 0; i < ficheros.length; i++) {
				if (ficheros[i].isDirectory()) {
					listarArchivosScriptRec(ficheros[i]);
				} else if (getExtension(ficheros[i].getName()).equals("script"))
				{
					listaFicherosScriptRec.add(ficheros[i]);				
				}
			}		
			return  (ArrayList<File>) listaFicherosScriptRec;
		} 
		return null;
	}
	
	public static ArrayList<File> listarArchivosScript(File file) {	
		List<File> listaFicherosScript = new ArrayList<File>();
		listaFicherosScriptRec.clear();
		listaFicherosScript = listarArchivosScriptRec (file);
		return (ArrayList<File>) listaFicherosScript;
	}	
	
	public static boolean ficheroXMLValido(File file)  {		
		FileReader fr;
		try {
			fr = new FileReader (file);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			String criterio = "";
			linea = br.readLine();
            if (br != null)
                br.close();
            if (fr != null)
                fr.close();
			
			if (linea.length() > 15 ) 
				criterio = linea.substring(0, 14);
			else
				criterio = "";
			
			if (criterio.equals("<S3e xmlns:xsi") ){
				// no se descarta
				return true;
			} else 
				return false;
			
			
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return true;
	}	
	
	
    public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
              return "";
        } else {
              return filename.substring(index + 1);
        }
    }	
}