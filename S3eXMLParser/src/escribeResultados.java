import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class escribeResultados {
    static void escribe(String text, File file, Integer nivelTraza){
   	
    	// nivelTraza = 0:	Solo pantalla
    	//              1:	Solo fichero
    	//              2:	pantalla y fichero
    	
    	
    	if ((nivelTraza == 1)||(nivelTraza == 2)) {
        	BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
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
    	if ((nivelTraza == 0)||(nivelTraza == 2)) {
    		System.out.print(text);    		
    	}
       
    }
}
