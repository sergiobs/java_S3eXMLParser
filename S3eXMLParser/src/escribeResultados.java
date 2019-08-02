import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class escribeResultados {
    static void escribe(String text, File file){
//        FileWriter fw;
//		try {
//			fw = new FileWriter(file);
//			BufferedWriter bw = new BufferedWriter(fw);
//	        bw.append(text);
//	        bw.close();
//		} catch (IOException e) {
//			System.out.println("Algo falla en escribeResultados.java:\n" + e);
//			e.printStackTrace();
//		}
    	
    	BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            
            //////////////File file = new File("archivo.txt");
            // Si el archivo no existe, se crea!
            if (!file.exists()) {
                file.createNewFile();
            }
            // flag true, indica adjuntar información al archivo.
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(text);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                            //Cierra instancias de FileWriter y BufferedWriter
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
       
    }
}
