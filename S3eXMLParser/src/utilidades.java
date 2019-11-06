import java.util.ArrayList;
import java.util.List;

public class utilidades {
    static List<String> analizaObjArgs(String cadena){   
    	
    	// descompone la cadena "ED/Ubicacion=ED_Local/Tipo!=ED_dobleHilo" en un arraylist: [ED, Ubicacion, =, ED_local, !, Tipo ED_dobleHilo]
    	//
    	// es decir, [TipoObjetoS3e, argumento1, operador1, valor1, argumento2, operador2, valor2, ....]
    	
    	int numArgumentos = 0;
    	int numArgumentos2 = 0;
    	String tipoObjetoS3e ="";
    	String argumento_i ="";
    	String valor_i ="";
    	String operador_i ="";
    	
    	List<String> listaObjeto_Argumentos = new ArrayList<String>();
    	for (int i=0; i<cadena.length();i++) {
    		if (cadena.charAt(i)=='/') {
    			numArgumentos++;
    		}
        	if (cadena.charAt(i)=='=') {
    			numArgumentos2++;
    		}
        	if (cadena.charAt(i)=='!') {
    			numArgumentos2++;
    		}
    	}
    	
    	if (numArgumentos!=numArgumentos2) {
    		return null;
    	}
    	
    	if (numArgumentos==0) { 
    		listaObjeto_Argumentos.add(cadena);
    		return listaObjeto_Argumentos;
    	}
    	
    	boolean leoTipoObjeto = true;
    	boolean leoArgumento = false;
    	boolean leoValor = false;

    	for (int i=0; i<cadena.length();i++) {
    		
    		if (cadena.charAt(i)=='/') {
    			if (leoTipoObjeto) {
    				listaObjeto_Argumentos.add(tipoObjetoS3e);
    			}
    			if (leoValor) {
    				listaObjeto_Argumentos.add(valor_i);
    				valor_i="";
    			}
    			leoTipoObjeto = false;
    			leoArgumento = true;
    			leoValor = false;    			
    			i++;
    		}   		
    		if ((cadena.charAt(i)=='=')||(cadena.charAt(i)=='!')) {    			
    			if (leoArgumento) {
    				listaObjeto_Argumentos.add(argumento_i);
    				listaObjeto_Argumentos.add(""+cadena.charAt(i));
    				argumento_i="";
    			}
    			leoTipoObjeto = false;
    			leoArgumento = false;
    			leoValor = true;     			
    			i++;
    		}     		
    		
    		if (i==cadena.length()-1) {    			
    			if (leoValor) {
    				valor_i+=cadena.charAt(i);
    				listaObjeto_Argumentos.add(valor_i);
    				valor_i="";
    			}    			
    		}   
    		
    		if (leoTipoObjeto) {
    			tipoObjetoS3e+=cadena.charAt(i);
    		}
    		if (leoArgumento) {
    			argumento_i+=cadena.charAt(i);    			
    		}
    		if (leoValor) {
    			valor_i+=cadena.charAt(i);    			
    		}

    	}  	
    	return listaObjeto_Argumentos;
    }

 static Integer buscaCaracterDesdeFin(String cadena, char caracter){   
    	for (int i=0; i<(cadena.length());i++) {    		
    		if (cadena.charAt(cadena.length()-1-i)==caracter) {
    			return cadena.length()-i-1;    			
    		}
    	}  	    	
    	return -1;    	
    }    
}
