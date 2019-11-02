import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;




import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



public class DatosFicheroXML {
	
	// getters
	public String getNombreFichero() {
		return nombreFichero;
	}
	public String getPathScriptsAutomaticos() {
		return pathScriptsAutomaticos;
	}
	public File getFichero() {
		return fichero;
	}
	public File getFileScriptsAutomaticos() {
		return fileScriptsAutomaticos;
	}
	public List<Integer> getListaNumeroObjetosS3e() {
		return lista_Obj_S3e_CANTIDAD;
	}	
	public List<List<String>> getListaNombreObjetosRepetidosEnXML() {
		return obj_XML_NOMBRE_Repetidos;
	}
	public List<List<String>> getListaIdentificadorObjetosEnXML() {
		return obj_XML_Id;
	}
	public List<List<String>> getListaNombreObjetosEnXML() {
		return obj_XML_NOMBRE;
	}
	public List<String> getListaNombreObjetosS3e() {
		return lista_Obj_S3e_NOMBRE;
	}
	
	public Integer getNumeroObjetosS3e() {
		return num_Obj_S3e;
	}
	public File getFicheroSicamPC() {
		return ficheroSicamPC;
	}
	public float getn_estimados_MCS_FR() {
		return n_estimados_MCS_FR;
	}
	public float getn_estimados_MCS_FRI() {
		return n_estimados_MCS_FRI;
	}
	public float getn_estimados_MCS_FNR() {
		return n_estimados_MCS_FNR;
	}
	public float getn_estimados_MCS_FNRI() {
		return n_estimados_MCS_FNRI;
	}
	public float getn_estimados_MCS_ES() {
		return n_estimados_MCS_ES;
	}
	public float getn_estimados_MCS_ESHD() {
		return n_estimados_MCS_ESHD;
	}
	
	// constructor
		public DatosFicheroXML (File ficheroXML) {
			
			// Activar para dimensionar 
		
			/*lista_Obj_S3e_NOMBRE.add("MCS/TipoDeMCS=MCS_focos");
			lista_Obj_S3e_NOMBRE.add("MCS/TipoDeMCS=MCS_ESgeneral");
			lista_Obj_S3e_NOMBRE.add("MCS/TipoDeMCS=MCS_ES_hiloDoble");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermApagado");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermEncendido");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_IntermApagado");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_InterEncFijoReposo");
			lista_Obj_S3e_NOMBRE.add("TRMF/TipoFoco=MF_noIntermApagado");
			lista_Obj_S3e_NOMBRE.add("TRMF/TipoFoco=MF_noIntermEncendido");
			lista_Obj_S3e_NOMBRE.add("TRMF/TipoFoco=MF_IntermApagado");
			lista_Obj_S3e_NOMBRE.add("TRMF/TipoFoco=MF_InterEncFijoReposo");
			lista_Obj_S3e_NOMBRE.add("ED/Ubicacion=ED_Local/Tipo=ED_dobleHilo");
			lista_Obj_S3e_NOMBRE.add("ED/Ubicacion=ED_Local/Tipo=ED_dobleConjugada");
			lista_Obj_S3e_NOMBRE.add("ED/Ubicacion=ED_Local/Tipo=ED_simpleHilo");
			lista_Obj_S3e_NOMBRE.add("SD/Ubicacion=SD_Local/Tipo=SD_simple");
			lista_Obj_S3e_NOMBRE.add("SD/Ubicacion=SD_Local/Tipo=SD_doble");	
			
			lista_Obj_S3e_NOMBRE.add("MA/IdentMCSEDMC!65535");
			lista_Obj_S3e_NOMBRE.add("MA/IdentMCSDR!65535");
			lista_Obj_S3e_NOMBRE.add("MA");
			lista_Obj_S3e_NOMBRE.add("MM");
		*/
			//--------------------------------

			
			// Activar estos solo para elementos no usados 
			lista_Obj_S3e_NOMBRE.add("ED");
			lista_Obj_S3e_NOMBRE.add("SD");
			lista_Obj_S3e_NOMBRE.add("MF");
			lista_Obj_S3e_NOMBRE.add("MA");
			lista_Obj_S3e_NOMBRE.add("MM");
			
			this.num_Obj_S3e = lista_Obj_S3e_NOMBRE.size();	
			this.fichero = ficheroXML;
	
			this.pathScriptsAutomaticos="";
			this.pathSicamPCXML="";
					
			nombreFichero= fichero.getName();
			
			//localizamos sicampc.xml		
			pathSicamPCXML = fichero.getPath().substring(0, fichero.getPath().length() - nombreFichero.length())+"\\..\\..\\";
			ficheroSicamPC = new File(pathSicamPCXML+"sicampc.xml");
					
			// se crea una lista de numObjS3e elementos que contendra el numero de objetos de cada 
			// tipo (numeroMCS, numeroED, numeroSD, ...).
			// inicialmente todos con valor 0
			for (int i=0; i<num_Obj_S3e; i++) 
				this.lista_Obj_S3e_CANTIDAD.add(0);
			
			// se crea una lista de listas que contendran los elementos repetidos de cada tipo si los hubiese:
			// 
			// listaNombreObjetosRepetidosEnXML(0) 		= ("MCS_001", "MCS_001") en caso de que dos MCS tengan el mismo nombre "MCS_001"
			// listaNombreObjetosRepetidosEnXML(1) 		= ("EDVA1", "EDVA1")
			// listaNombreObjetosRepetidosEnXML(2) 		= vacio		
			// ...
			// listaNombreObjetosRepetidosEnXML(numObjS3e-1) = etc
			
			for (int i=0; i<num_Obj_S3e; i++) {
				this.obj_XML_NOMBRE_Repetidos.add(new ArrayList<String>());
				this.obj_XML_NOMBRE.add(new ArrayList<String>());
				this.obj_XML_Id.add(new ArrayList<String>());
				this.obj_XML_Usados.add(new ArrayList<Integer>());
				this.listaIdentificadorObjetos_noUsadosEnXML.add(new ArrayList<String>());			
			}		
			parseaXMLS3e();
			if (ficheroSicamPC.exists()) {
				parsea_SicamPcXML();
				if (pathScriptsAutomaticos!=null) {
					fileScriptsAutomaticos = new File(pathScriptsAutomaticos);
				} else { 
					System.out.println("No hay pathScriptsAutomaticos en sicampc.xml");
				}
			} else {
				//System.out.println("No existe sicampc.xml");
				ficheroSicamPC=null;
			}				
		}
	
	// setters
	public void setFichero(File fichero) {
		this.fichero = fichero;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	
	public void addNombreObjetoEnXML(String nombreObjetoEnXML, int indice) {
		this.obj_XML_NOMBRE.get(indice).add(nombreObjetoEnXML);
	}	
	
	public void addIdentificadorObjetoEnXML(String identificadorObjetoEnXML, int indice) {
		this.obj_XML_Id.get(indice).add(identificadorObjetoEnXML);
	}
	
	//devuelve en una unica lista todos los objetos repetidos
	public List<String> getListaTotalNombreObjetosRepetidosEnXML() {
		List<String> listaTotalNombreObjetosRepetidosEnXML = new ArrayList<String>();
		for (int i=0;i<num_Obj_S3e;i++) {
			for (int j=0;j<obj_XML_NOMBRE_Repetidos.get(i).size();j++) {
				listaTotalNombreObjetosRepetidosEnXML.add(obj_XML_NOMBRE_Repetidos.get(i).get(j));
			}
		}		
		return listaTotalNombreObjetosRepetidosEnXML;
	}	
		
	public void parseaXMLS3e () {
		try {
			SAXParserFactory factory2 = SAXParserFactory.newInstance();
			SAXParser saxParser2 = factory2.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					String etiqueta = qName;
					// buscamos el startElement en nuestra lista de objetos a evaluar
					for (int startE=0; startE<num_Obj_S3e; startE ++) {
						
						boolean elementoSimple_analizado = false;
						//para recoger los de tipo "ED", "SD", "MO", ....
						if (etiqueta.equalsIgnoreCase(lista_Obj_S3e_NOMBRE.get(startE))) {						
							obj_XML_NOMBRE.get(startE).add(attributes.getValue("Nombre"));
							obj_XML_Id.get(startE).add(attributes.getValue("Identificador"));		
							elementoSimple_analizado = true;
						}
						
						if (true) {
							List<String> xxxx = new ArrayList<String>();
							xxxx		= utilidades.analizaObjArgs(lista_Obj_S3e_NOMBRE.get(startE));
							int nro_argumentos = (xxxx.size()-1)/3;
							
							xxxx.get(0);
							boolean cumpleCriterio = true;
							
							if (etiqueta.equalsIgnoreCase(xxxx.get(0))&&xxxx.size()>1) {
								// recorremos bucle para ver que se cumplen todos los pares de arg/val (teniendo en cuent al operador )
								// si todos los pares se cumplen, cumpleCriterio = true
								for (int j=0;j<nro_argumentos;j++) {
									String argumento_i = xxxx.get(3*j+1);
									String operador_i = xxxx.get(3*j+2);
									String valor_i = xxxx.get(3*j+3);
									
									String argumento_xml=attributes.getValue(argumento_i);
									
									switch (operador_i) {
									case "=":
										if (!valor_i.equalsIgnoreCase(argumento_xml)) {
											cumpleCriterio = false;
										}
										break;
									case "!":
										if (valor_i.equalsIgnoreCase(argumento_xml)) {
											cumpleCriterio = false;
										}
										break;
									}
								}
								
								if (cumpleCriterio) {
									obj_XML_NOMBRE.get(startE).add(attributes.getValue("Nombre"));
									obj_XML_Id.get(startE).add(attributes.getValue("Identificador"));
									//System.out.println(xxxx+ ": "+attributes.getValue("Nombre")+", " );
								}
							}
						}											
					}
				}				
			};
			saxParser2.parse(fichero, handler);
			
		} catch (Exception e) {
			System.out.println("main: " + e);
			e.printStackTrace();
		}
	}
	
	public void parsea_SicamPcXML() {
		try {
			SAXParserFactory factory2 = SAXParserFactory.newInstance();
			SAXParser saxParser2 = factory2.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					String etiqueta = qName;
					String atributo = attributes.getValue("Tipo");
					String valor = attributes.getValue("PathScriptsAutomaticos");

					// buscamos el startElement en nuestra lista de objetos a evaluar
					if (etiqueta.equalsIgnoreCase("Proceso")&&(atributo.equals("IHM"))) {
						//puede no existir pathScriptsAutomaticos						
						pathScriptsAutomaticos = valor;
						if (valor != null) {
							pathScriptsAutomaticos = pathScriptsAutomaticos.replace("/", "\\");
							pathScriptsAutomaticos = pathSicamPCXML + pathScriptsAutomaticos;	
						}
					}
				}				
			};			

			saxParser2.parse(ficheroSicamPC, handler);
			
		} catch (Exception e) {
			System.out.println("main: " + e);
			e.printStackTrace();
		}
	}
	
	public void cuentaObjetosS3e () {
		for (int i=0; i<lista_Obj_S3e_CANTIDAD.size(); i++) 
		{
			this.lista_Obj_S3e_CANTIDAD.set(i,obj_XML_NOMBRE.get(i).size());
		}
	}
	
	public String imprimeListaNombreObjetosRepetidosEnXML () {
		boolean existeRepetido = false;
		boolean primerRepetido = true;
		String texto2print ="";
		for (int aa=0; aa<num_Obj_S3e;aa++) {					
			if (obj_XML_NOMBRE_Repetidos.get(aa).size() > 0) {
				if (primerRepetido) {
					texto2print+="ERROR: Existen nombres repetidos en este fichero: \n";
					primerRepetido = false;
				}
				texto2print += lista_Obj_S3e_NOMBRE.get(aa) + ": " ;
				for (int j=0; j<obj_XML_NOMBRE_Repetidos.get(aa).size() ; j++) {
					texto2print += obj_XML_NOMBRE_Repetidos.get(aa).get(j) + "\t";				
					existeRepetido = true;
				}
			}					
		}
		if (!existeRepetido) {
			texto2print += "INFO: NO existen nombres repetidos en este fichero\n";
		}
		return texto2print;
	}
	
	public String imprimelistaNumeroObjetosS3e () {
		String texto2print ="";
		for (int aa=0; aa<num_Obj_S3e;aa++) {						
			texto2print+=lista_Obj_S3e_NOMBRE.get(aa) + ": " + lista_Obj_S3e_CANTIDAD.get(aa) + "\n";
		}
		return texto2print;
	}	
	
	public String imprimeMCS_Estimados () {
		String texto2print ="";

		texto2print+="\nMCS Total: " + 
				(n_estimados_MCS_FR +n_estimados_MCS_FNR+ n_estimados_MCS_FRI+ n_estimados_MCS_FNRI + n_estimados_MCS_ES +n_estimados_MCS_ESHD) +  " / " + 
				(n_estimados_MCS_FR +n_estimados_MCS_FNR+ n_estimados_MCS_FRI+ n_estimados_MCS_FNRI + n_estimados_MCS_SD)+"\n";
		
		texto2print+=   "Q4_FNR: " + n_estimados_Q4_FNR	+", ";
		texto2print+=   "Q4_FR: " +n_estimados_Q4_FR	+", ";
		texto2print+=   "Q4_SD: " +n_estimados_Q4_SD	+", ";
		texto2print+=   "Q4_ED: " +n_estimados_Q4_ED		+", ";
		texto2print+=   "Q4_AGelec: " +n_estimados_Q4_AGelec	+", ";
		texto2print+=   "Q4_AGmec: " +n_estimados_Q4_AGmec	+", ";

		
		texto2print+="\nMCSQ4 Total (AF, AES (por SD), AES_ED (por ED)): " + 
				(n_estimados_MCSQ4_AF + ", " + n_estimados_MCSQ4_AES_Salidas + ", " + n_estimados_MCSQ4_AES_Entradas );

		DecimalFormat df = new DecimalFormat("#.00");				
		float nTotal = 0;
		if (n_estimados_MCSQ4_AES_Salidas>n_estimados_MCSQ4_AES_Entradas) {
			nTotal = n_estimados_MCSQ4_AF + n_estimados_MCSQ4_AES_Salidas;
			texto2print+="\nMCSQ4 Total (SD>ED): " + df.format(nTotal);
		}else {
			nTotal = n_estimados_MCSQ4_AF + n_estimados_MCSQ4_AES_Entradas;
			texto2print+="\nMCSQ4 Total (SD<ED): " + df.format(nTotal);
		}
		return texto2print;
	}

	public String imprimeMCS_Estimados_corto () {
		String texto2print ="";
		DecimalFormat df = new DecimalFormat("#.00");				
		float nTotal = 0;
		if (n_estimados_MCSQ4_AES_Salidas>n_estimados_MCSQ4_AES_Entradas) {
			nTotal = n_estimados_MCSQ4_AF + n_estimados_MCSQ4_AES_Salidas;
			texto2print+="\t" + df.format(nTotal);
		}else {
			nTotal = n_estimados_MCSQ4_AF + n_estimados_MCSQ4_AES_Entradas;
			texto2print+="\t" + df.format(nTotal);
		}
		return texto2print;
	}	


	public String imprimeMCS_EstimadosF () {
		String texto2print ="";
		DecimalFormat df = new DecimalFormat("#.00");				
		float nTotal = 0;
		if (n_estimados_MCSQ4_AES_Salidas_ahorro>n_estimados_MCSQ4_AES_Entradas_ahorro) {
			nTotal = n_estimados_MCSQ4_AF_ahorro + n_estimados_MCSQ4_AES_Salidas_ahorro;
			texto2print+="" + df.format(nTotal);
		}else {
			nTotal = n_estimados_MCSQ4_AF_ahorro + n_estimados_MCSQ4_AES_Entradas_ahorro;
			texto2print+="" + df.format(nTotal);
		}
		return texto2print;
	}	

	public String imprimeMCS_EstimadosF_corto () {
		String texto2print ="";
		DecimalFormat df = new DecimalFormat("#.00");				
		float nTotal = 0;
		if (n_estimados_MCSQ4_AES_Salidas_ahorro>n_estimados_MCSQ4_AES_Entradas_ahorro) {
			nTotal = n_estimados_MCSQ4_AF_ahorro + n_estimados_MCSQ4_AES_Salidas_ahorro;
			texto2print+="\t" + df.format(nTotal);
		}else {
			nTotal = n_estimados_MCSQ4_AF_ahorro + n_estimados_MCSQ4_AES_Entradas_ahorro;
			texto2print+="\t" + df.format(nTotal);
		}
		return texto2print;
	}	
	
	public String imprime_IdObjetoNoUsados_porTipo (String tipoObjetoS3e) {
		int inTipoObjetoS3e = devuelveIndiceObjetoS3e(tipoObjetoS3e);
		String texto2print ="";
		
		if (this.listaIdentificadorObjetos_noUsadosEnXML.get(inTipoObjetoS3e).size()>0) {
			texto2print+=tipoObjetoS3e+": ";			
			for (int aa=0; aa<this.listaIdentificadorObjetos_noUsadosEnXML.get(inTipoObjetoS3e).size();aa++) {						
				texto2print+=this.listaIdentificadorObjetos_noUsadosEnXML.get(inTipoObjetoS3e).get(aa) + ", ";
			}
			texto2print+="\n"; 
		}
		return texto2print;
	}	
	
	public void borra_Objetos_NoUsados_porTipo (String tipoObjetoS3e) {
	
	}	
	
	
	public void calculaNombreObjetosRepetidosS3e () {
		List<String> listaDevueltaConRepetidos = new ArrayList<String>();
		for (int i=0; i<num_Obj_S3e; i++) 
		{
			// se llama a una función que devuelve una lista de objetos repetidos de la lista pasada como argumento			
			listaDevueltaConRepetidos = Listas.devuelveListaRepetidos((ArrayList<String>) obj_XML_NOMBRE.get(i));
			
			//paso la lista devuelta a listaNombreObjetosRepetidosEnXML(i)
			for (int j=0;j<listaDevueltaConRepetidos.size();j++) {
				obj_XML_NOMBRE_Repetidos.get(i).add(listaDevueltaConRepetidos.get(j));
			}			
		}
	}
	
	public void calcula_Id_Objetos_UsadosS3e () {
		for (int i=0;i<num_Obj_S3e;i++) {
			/* "obj_XML_Usados" contendrá para cada tipo de objeto del s3e			 * 
			 * 			 0: elemento no usado 
			 * 			 1:	elemento usado por lo menos una vez
			 * 			-1: no aplica (en los objetos excluidos del calculo)		  
			 */ 
			this.calcula_nivel_utilizacion_TipoObjetosS3e(lista_Obj_S3e_NOMBRE.get(i));
		}
		
		System.out.println("listaIdentificadorObjetos_noUsadosEnXML: " +  listaIdentificadorObjetos_noUsadosEnXML );
	}
	
	public String imprime_Objetos_NoUsadosS3e () {
		String texto2print ="";		
		for (int i=0;i<num_Obj_S3e;i++) {
			texto2print+=this.imprime_IdObjetoNoUsados_porTipo(lista_Obj_S3e_NOMBRE.get(i));
		}	
		return texto2print;
	}
	
	public String borra_Objetos_NoUsadosS3e () {		
		String texto2print ="";				
		        
        //primero se determina si sobran elementos
        boolean sobranElementos = false;
        for (int i=0;i<num_Obj_S3e;i++) {
        	if (listaIdentificadorObjetos_noUsadosEnXML.get(i).size()>0) {
        		sobranElementos = true;
        	}
        }
        
 		if (sobranElementos) {
 			
 			String fichero_nombre_modificado = fichero.getAbsolutePath()+".mod";
 			File fichero_modificado = new File(fichero_nombre_modificado);
 	        if (!fichero_modificado.exists())
 				try {
 					fichero_modificado.createNewFile();
 				} catch (IOException e1) {
 					e1.printStackTrace();
 				}
 			else
 	        	fichero_modificado.delete();
			
			try {			
				Scanner sc = new Scanner(fichero);
	            FileWriter fw = new FileWriter(fichero_modificado.getAbsoluteFile(), true);
	        	BufferedWriter bw = new BufferedWriter(fw);            
	 
	    		while (sc.hasNext()) {
					String linea = "";
					String linea_nueva = "";					
					linea = sc.nextLine();	
					boolean lineaProcesada = false;
										
					for (int i=0;i<num_Obj_S3e;i++) {
						if ((lista_Obj_S3e_NOMBRE.get(i)=="ED") ||
								(lista_Obj_S3e_NOMBRE.get(i)=="SD") ||
								(lista_Obj_S3e_NOMBRE.get(i)=="MF") ||
								(lista_Obj_S3e_NOMBRE.get(i)=="MA") ||
								(lista_Obj_S3e_NOMBRE.get(i)=="MM")) {
				
							if (this.listaIdentificadorObjetos_noUsadosEnXML.get(i).size()>0) {
								for (int aa=0; aa<this.listaIdentificadorObjetos_noUsadosEnXML.get(i).size();aa++) {								
									//if (lista_Obj_S3e_NOMBRE.get(i)=="ED") {
										String string_a_buscar = "<"+lista_Obj_S3e_NOMBRE.get(i)+" Identificador=\"" + 
												this.listaIdentificadorObjetos_noUsadosEnXML.get(i).get(aa) + "\"";
										
										if (linea.contains(string_a_buscar)) { 	
											lineaProcesada = true;
											//linea_nueva = "<!--" + linea + "-->";
											linea_nueva = "";
											break;
										}
										else {
											//comentaLinea = false;
											linea_nueva = linea;
										}
									//}
									if (lineaProcesada) break;
								}						
							}	
							if (lineaProcesada) break;
						}
		    		}				
					bw.write(linea_nueva+"\r\n");
					
	    		}
	
	            if (bw != null)
	                bw.close();
	            if (fw != null)
	                fw.close();
				sc.close();	
				
	 						
			} catch (IOException e) {
				e.printStackTrace();
				//return  imprimeError+" " + e;
			}	  
			
			// se renombran ficheros
			File fichero_original_bck = new File(fichero.getAbsolutePath()+".bck");			
			System.out.println(fichero.renameTo(fichero_original_bck));						
			System.out.println(fichero_modificado.renameTo(fichero));
			
 		}
        
        
	return texto2print;
	}
	
	public void calcula_nivel_utilizacion_TipoObjetosS3e (String tipoObjetoS3e) {
		/* Esta función rellena la tabla "obj_XML_Usados" para cada tipo de objeto del s3e 
		 * con el siguiente valor:
		 * 			 0: elemento no usado 
		 * 			 1:	elemento usado por lo menos una vez
		 * 			-1: no aplica (en los objetos excluidos del calculo)		  
		 */
		
		
		int inTipoObjetoS3e = devuelveIndiceObjetoS3e(tipoObjetoS3e);		
		try {
			for (int inObjeto = 0; inObjeto<this.obj_XML_Id.get(inTipoObjetoS3e).size(); inObjeto++) {	

				//primero los que se excluyen en este calculo por reducir tiempo de calculo				
				this.obj_XML_Usados.get(inTipoObjetoS3e).add(0); //por defecto añadimos 0 = no usado
				if (tipoObjetoS3e.contentEquals("MO") 						
						|| tipoObjetoS3e.contentEquals("TRMF/TipoFoco=MF_noIntermApagado")
						|| tipoObjetoS3e.contentEquals("TRMF/TipoFoco=MF_noIntermEncendido")
						|| tipoObjetoS3e.contentEquals("TRMF/TipoFoco=MF_IntermApagado")
						|| tipoObjetoS3e.contentEquals("TRMF/TipoFoco=MF_InterEncFijoReposo")
						
						// se excluyen los TRMF porque de hecho no se usan (no son llamados por otro objeto)
						
						//|| tipoObjetoS3e.contentEquals("xxx")
						//|| tipoObjetoS3e.contentEquals("xxx") ...						
						) {					
					this.obj_XML_Usados.get(inTipoObjetoS3e).set(inObjeto, -1); // no aplica
				} else {
					int apariciones = 0;
					Scanner sc = new Scanner(this.fichero);
					String linea = "";
					while (sc.hasNext()) {
						linea = sc.nextLine();
						if (linea.contains("\""+this.obj_XML_Id.get(inTipoObjetoS3e).get(inObjeto)+"\"")) {
							apariciones++;
							if (apariciones > 1) {
								this.obj_XML_Usados.get(inTipoObjetoS3e).set(inObjeto, 1);
								break;
							}						
						}
					}
					sc.close();						
				}	
			}
				
		} catch (IOException e) {			
			e.printStackTrace();
		}

		for (int inObjeto = 0; inObjeto<this.obj_XML_Usados.get(inTipoObjetoS3e).size(); inObjeto++) {
			if (this.obj_XML_Usados.get(inTipoObjetoS3e).get(inObjeto)==0) {
				listaIdentificadorObjetos_noUsadosEnXML.get(inTipoObjetoS3e)
					.add(this.obj_XML_Id.get(inTipoObjetoS3e).get(inObjeto));				
			}
		}
		//System.out.println("_: " +  listaIdentificadorObjetos_noUsadosEnXML );
		
		
	}
	

	public int estimaMCS() {
		
		// Consideraciones Q4e:
		// MCS-AES
		//		* 8  SD Dobles
		//		* 40 ED
		//		* 4  agujas
		// MCS-AF
		// 		* 8 Salidas de focos (intermitentes o no)
		// 			+ Las 4 primeras Salidas FNR
		//			+ Las 2 siguientes salidas para FR
		//			+ Las 2 siguientes con configurables
		
		
		
		// ----------------- FOCOS ------------------------------------------------------
		String objeto; 		
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermApagado";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
			n_estimados_MCS_FNR += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4; 
			n_estimados_Q4_FNR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: para estimar MCS, debe tenerse en cuenta " +  objeto );
			return -1;
		}
				
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermEncendido";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
			n_estimados_MCS_FR += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4;
			n_estimados_Q4_FR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;			
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_IntermApagado";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
			n_estimados_MCS_FNRI += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/2;
			n_estimados_Q4_FNR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_InterEncFijoReposo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_FRI += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/2;
			n_estimados_Q4_FR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;	
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		// TRMF ---------------------------------------------------------------
		
		objeto = "TRMF/TipoFoco=MF_noIntermApagado";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
			n_estimados_MCS_FNR += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4; 
			n_estimados_Q4_FNR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: para estimar MCS, debe tenerse en cuenta " +  objeto );
			return -1;
		}
				
		objeto = "TRMF/TipoFoco=MF_noIntermEncendido";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
			n_estimados_MCS_FR += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4;
			n_estimados_Q4_FR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;			
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		objeto = "TRMF/TipoFoco=MF_IntermApagado";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
			n_estimados_MCS_FNRI += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/2;
			n_estimados_Q4_FNR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		objeto = "TRMF/TipoFoco=MF_InterEncFijoReposo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_FRI += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/2;
			n_estimados_Q4_FR += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AF += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;	
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		
		// ------------------------------------------------------------------------------
		
		// ----------------- ED ------------------------------------------------------
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_dobleHilo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ESHD += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
			n_estimados_Q4_ED += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/40;			
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_dobleConjugada";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8; 
			n_estimados_Q4_ED += lista_Obj_S3e_CANTIDAD.get(posicion)*2;
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/20;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_simpleHilo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/16;
			n_estimados_Q4_ED += lista_Obj_S3e_CANTIDAD.get(posicion);			
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/40;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		// ------------------------------------------------------------------------------
				
		
		// ----------------- AGUJAS ------------------------------------------------------
		
		objeto = "MA";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += (float) lista_Obj_S3e_CANTIDAD.get(posicion) * (1/2 + 1/8); //1/8 por el final de carrera
			n_estimados_MCS_SD += (float) lista_Obj_S3e_CANTIDAD.get(posicion) * (1/2);  
			n_estimados_Q4_AGelec += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4;
			n_estimados_MCSQ4_AES_Salidas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}

		objeto = "MA/IdentMCSEDMC!65535";  // Agujas que tienen Mando Local
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += (float) lista_Obj_S3e_CANTIDAD.get(posicion) * (1/8); //1/8 por las ED de ML
			n_estimados_Q4_ED += lista_Obj_S3e_CANTIDAD.get(posicion)*2;
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/20;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		objeto = "MA/IdentMCSDR!65535";  // Agujas con detector de rueda
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += (float) lista_Obj_S3e_CANTIDAD.get(posicion) * (1/4); //1/4 por las ED de Detector de rueda			  
			n_estimados_Q4_ED += lista_Obj_S3e_CANTIDAD.get(posicion)*2;
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/20;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}

		
		objeto = "MM";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ESHD += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/2 ; 
			n_estimados_Q4_AGmec += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AES_Entradas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4;
			n_estimados_MCSQ4_AES_Salidas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		
		// ------------------------------------------------------------------------------
				
		// ----------------- SD ------------------------------------------------------
		
		objeto = "SD/Ubicacion=SD_Local/Tipo=SD_simple";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_SD += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/4 ; 
			n_estimados_Q4_SD += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AES_Salidas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}
		objeto = "SD/Ubicacion=SD_Local/Tipo=SD_doble";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_SD +=(float)  lista_Obj_S3e_CANTIDAD.get(posicion)/2 ; 
			n_estimados_Q4_SD += lista_Obj_S3e_CANTIDAD.get(posicion);
			n_estimados_MCSQ4_AES_Salidas += (float) lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
			return -1;
		}	
		return 0;
		// ------------------------------------------------------------------------------
	}
	
	
public int estimaMCS_ahorro() {
		String objeto; 		
		
		// MF -----------------------------------------------------------------------------
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermApagado";
		int posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;

		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermEncendido";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;

		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_IntermApagado";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;

		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_InterEncFijoReposo";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;
		
		//TRMF ---------------------------------------------------------------------------
		objeto = "TRMF/TipoFoco=MF_noIntermApagado";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;

		objeto = "TRMF/TipoFoco=MF_noIntermEncendido";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;

		objeto = "TRMF/TipoFoco=MF_IntermApagado";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;

		objeto = "TRMF/TipoFoco=MF_InterEncFijoReposo";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AF_ahorro = n_estimados_MCSQ4_AF_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;		

		// ED -----------------------------------------------------------
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_dobleHilo";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AES_Entradas_ahorro = n_estimados_MCSQ4_AES_Entradas - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/40;

		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_simpleHilo";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AES_Entradas_ahorro = n_estimados_MCSQ4_AES_Entradas_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/40;

		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_dobleConjugada";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AES_Entradas_ahorro = n_estimados_MCSQ4_AES_Entradas_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/20;		

		objeto = "SD/Ubicacion=SD_Local/Tipo=SD_doble";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AES_Salidas_ahorro = n_estimados_MCSQ4_AES_Salidas - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;		

		objeto = "SD/Ubicacion=SD_Local/Tipo=SD_simple";
		posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);
		n_estimados_MCSQ4_AES_Salidas_ahorro = n_estimados_MCSQ4_AES_Salidas_ahorro - (float) listaIdentificadorObjetos_noUsadosEnXML.get(posicion).size()/8;
		
		
		
		
		return 0;


	}
	
	private int devuelveIndiceObjetoS3e(String nombreObjetoS3e) {
		for (int i = 0; i<num_Obj_S3e; i++) {
			if (lista_Obj_S3e_NOMBRE.get(i).equals(nombreObjetoS3e)) return i;			
		}		
		return -1;
	}
	
	// variables del objeto
	private String nombreFichero;
	private String pathScriptsAutomaticos;
	private String pathSicamPCXML;
	private int num_Obj_S3e;		
	private List<String> lista_Obj_S3e_NOMBRE = new ArrayList<String>();
	private List<Integer> lista_Obj_S3e_CANTIDAD = new ArrayList<Integer>();
	
	private List<List<String>> obj_XML_NOMBRE = new ArrayList<List<String>>();
	private List<List<String>> obj_XML_Id = new ArrayList<List<String>>();	
	private List<List<String>> obj_XML_NOMBRE_Repetidos = new ArrayList<List<String>>();
	private List<List<Integer>> obj_XML_Usados = new ArrayList<List<Integer>>();
	
	private List<List<String>> listaIdentificadorObjetos_noUsadosEnXML = new ArrayList<List<String>>();
	private List<Integer> numero_Objetos_noUsadosEnXML = new ArrayList<Integer>();

	private File fichero = new File("");
	private File ficheroSicamPC = new File("");
	private File fileScriptsAutomaticos;
	
	private float n_estimados_MCS_FR = 0;
	private float n_estimados_MCS_FNR = 0;
	private float n_estimados_MCS_FRI = 0;
	private float n_estimados_MCS_FNRI = 0;
	private float n_estimados_MCS_ES = 0;
	private float n_estimados_MCS_ESHD = 0;	
	private float n_estimados_MCS_SD = 0;	
	private float n_estimados_MCSQ4_AES_Entradas = 0;
	private float n_estimados_MCSQ4_AES_Salidas = 0;
	private float n_estimados_MCSQ4_AF = 0;

	private float n_estimados_MCSQ4_AES_Entradas_ahorro = 0;
	private float n_estimados_MCSQ4_AES_Salidas_ahorro = 0;
	private float n_estimados_MCSQ4_AF_ahorro = 0;	
	
	private int n_estimados_Q4_FNR = 0;
	private int n_estimados_Q4_FR = 0;
	private int n_estimados_Q4_SD = 0;
	private int n_estimados_Q4_ED = 0;	
	private int n_estimados_Q4_AGelec = 0;
	private int n_estimados_Q4_AGmec = 0;
	
}
