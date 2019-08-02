import java.util.ArrayList;
import java.util.List;

// f
public class Listas {
    static void mostrarRepetidos(ArrayList<String> Lista){
    	for (int i=0; i<Lista.size(); i++ ) {
        	for (int j=i+1; j <Lista.size(); j++ ) {
	    	   if(Lista.get(i).equals(Lista.get(j))) {
	    		   System.out.println("Elemento " + Lista.get(i) + " esta repetido");  
	    	   	}
        	}
        } 
    }
    
    static ArrayList<String> devuelveListaRepetidos(ArrayList<String> Lista){
    	List<String> listaDuplicados = new ArrayList<String>(); 
    	
    	for (int i=0; i<Lista.size(); i++ ) { 
        	for (int j=i+1; j <Lista.size(); j++ ) { 
	    	   if(Lista.get(i).equals(Lista.get(j))) {	    		   
	    		   listaDuplicados.add(Lista.get(i));
	    	   	}
        	}
        } 
    	return  (ArrayList<String>) listaDuplicados;
    }    
    
}
