
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Archivos {
	
	static List<File> listaFicherosXML = new ArrayList<File>();

	public static ArrayList<File> listarArchivosRecursivamente(File file) {		   
		//List<String> listaFicherosXMLString = new ArrayList<String>();
		//List<File> listaFicherosXML = new ArrayList<File>();		   	

		File[] ficheros = file.listFiles();

		for (int i = 0; i < ficheros.length; i++) {
			if (ficheros[i].isDirectory()) {
				//listarArchivosRecursivamente(ficheros[i], ficheros[i].getAbsolutePath());
				listarArchivosRecursivamente(ficheros[i]);
			} else if (getExtension(ficheros[i].getName()).equals("xml") && !ficheros[i].getName().equals("sicampc.xml") )
			{
				listaFicherosXML.add(ficheros[i]);				
			}
		}		
		return  (ArrayList<File>) listaFicherosXML;
	}	 
	
	public static boolean ficheroXMLValido(File file) throws FileNotFoundException {
		
		FileReader fr;
		try {
			fr = new FileReader (file);
			BufferedReader br = new BufferedReader(fr);
			String linea = br.readLine();
			String criterio = "";
			linea = br.readLine();
			
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