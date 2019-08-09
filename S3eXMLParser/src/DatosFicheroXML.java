import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
			lista_Obj_S3e_NOMBRE.add("MCS/TipoDeMCS=MCS_focos");
			lista_Obj_S3e_NOMBRE.add("MCS/TipoDeMCS=MCS_ESgeneral");
			lista_Obj_S3e_NOMBRE.add("MCS/TipoDeMCS=MCS_ES_hiloDoble");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermApagado");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermEncendido");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_IntermApagado");
			lista_Obj_S3e_NOMBRE.add("MF/Ubicacion=MF_Local/TipoFoco=MF_InterEncFijoReposo");	
			lista_Obj_S3e_NOMBRE.add("ED/Ubicacion=ED_Local/Tipo=ED_dobleHilo");
			lista_Obj_S3e_NOMBRE.add("ED/Ubicacion=ED_Local/Tipo=ED_dobleConjugada");
			lista_Obj_S3e_NOMBRE.add("ED/Ubicacion=ED_Local/Tipo=ED_simpleHilo");
			lista_Obj_S3e_NOMBRE.add("SD/Ubicacion=SD_Local/Tipo=SD_simple");
			lista_Obj_S3e_NOMBRE.add("SD/Ubicacion=SD_Local/Tipo=SD_doble");	
			lista_Obj_S3e_NOMBRE.add("MA");
			lista_Obj_S3e_NOMBRE.add("MM");
			//listaNombreObjetosS3e.add("MCS");		
			//lista_Obj_S3e_NOMBRE.add("ED");	
			//listaNombreObjetosS3e.add("CSE");		
			//lista_Obj_S3e_NOMBRE.add("SD");
			//lista_Obj_S3e_NOMBRE.add("MF");
			lista_Obj_S3e_NOMBRE.add("IF");
			lista_Obj_S3e_NOMBRE.add("IS");
			//listaNombreObjetosS3e.add("PV");
			lista_Obj_S3e_NOMBRE.add("SE");
			lista_Obj_S3e_NOMBRE.add("CV");
			lista_Obj_S3e_NOMBRE.add("AG");
			lista_Obj_S3e_NOMBRE.add("MO");
			lista_Obj_S3e_NOMBRE.add("ML");		
			this.num_Obj_S3e = lista_Obj_S3e_NOMBRE.size();
			this.fichero = ficheroXML;
			this.pathScriptsAutomaticos="";
			this.pathSicamPCXML="";
					
			nombreFichero= fichero.getName();
			
			//localizamos el sicampc.xml		
			pathSicamPCXML = fichero.getPath().substring(0, fichero.getPath().length() - nombreFichero.length())+"\\..\\..\\";
			ficheroSicamPC = new File(pathSicamPCXML+"sicampc.xml");
							
			// se crea una lista de numObjS3e elementos que contendra el numero de objetos de cada tipo (numeroMCS, numeroED, numeroSD, )
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
							int nro_argumentos = (xxxx.size()-1)/2;
							
							xxxx.get(0);
							boolean cumpleCriterio = true;
							
							if (etiqueta.equalsIgnoreCase(xxxx.get(0))&&xxxx.size()>1) {
								// recorremos bucle para ver que se cumplen todos los pares de arg/val
								// si todos los pares se cumplen, cumpleCriterio = true
								for (int j=0;j<nro_argumentos;j++) {								
									
									if (!xxxx.get(2*j+2).equalsIgnoreCase(attributes.getValue(xxxx.get(2*j+1)))) {
										cumpleCriterio = false;
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
		
		texto2print+="MCS_FR: " + n_estimados_MCS_FR +	"\n" +					
						"MCS_FNR: " + n_estimados_MCS_FNR +"\n" +
						"MCS_FRI: " + n_estimados_MCS_FRI +"\n" +
						"MCS_FNRI: " + n_estimados_MCS_FNRI +"\n" +
						"MCS_ES: " + n_estimados_MCS_ES +"\n" +
						"MCS_ESHD: " + n_estimados_MCS_ESHD +"\n" +
						"MCS_(SD): " + n_estimados_MCS_SD;
		
		
		texto2print+="\nMCS Focos: " + (n_estimados_MCS_FR +n_estimados_MCS_FNR+ n_estimados_MCS_FRI+ n_estimados_MCS_FNRI) +	"\n" +					
				"MCS ES(por ED): " + (n_estimados_MCS_ES +n_estimados_MCS_ESHD) +  "\n" +
				"MCS ES(por SD): " + (n_estimados_MCS_SD );
		texto2print+="\nMCS Total: " + 
				(n_estimados_MCS_FR +n_estimados_MCS_FNR+ n_estimados_MCS_FRI+ n_estimados_MCS_FNRI + n_estimados_MCS_ES +n_estimados_MCS_ESHD) +  " / " + 
				(n_estimados_MCS_FR +n_estimados_MCS_FNR+ n_estimados_MCS_FRI+ n_estimados_MCS_FNRI + n_estimados_MCS_SD);
		texto2print+="\nMCSQ4 Total: " + 
				(n_estimados_MCSQ4_AF +n_estimados_MCSQ4_AES);
		return texto2print;
	}
	
	public String imprimeIdentificadorObjetoNoUsados (String tipoObjetoS3e) {
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
			this.calcula_Id_Objetos_Usados_porTipo(lista_Obj_S3e_NOMBRE.get(i));
		}		
	}
	
	public String imprime_Id_No_UsadosS3e () {
		String texto2print ="";		
		for (int i=0;i<num_Obj_S3e;i++) {
			texto2print+=this.imprimeIdentificadorObjetoNoUsados(lista_Obj_S3e_NOMBRE.get(i));
		}	
		return texto2print;
	}
	
	public void calcula_Id_Objetos_Usados_porTipo (String tipoObjetoS3e) {
		int inTipoObjetoS3e = devuelveIndiceObjetoS3e(tipoObjetoS3e);		
		try {
			for (int inObjeto = 0; inObjeto<this.obj_XML_Id.get(inTipoObjetoS3e).size(); inObjeto++) {	
				//primero los que se excluyen en este calculo por reducir tiempo de calculo
				if (tipoObjetoS3e.contentEquals("MO") ||
						tipoObjetoS3e.contentEquals("ED") ||
						tipoObjetoS3e.contentEquals("SD") ||
						tipoObjetoS3e.contentEquals("MF") ||
						tipoObjetoS3e.contentEquals("MCS") ) {
					this.obj_XML_Usados.get(inTipoObjetoS3e).add(0);
					this.obj_XML_Usados.get(inTipoObjetoS3e).set(inObjeto, -1); // no aplica
				} else {
					int apariciones = 0;
					Scanner sc = new Scanner(this.fichero);
					String linea = "";
					
					this.obj_XML_Usados.get(inTipoObjetoS3e).add(0);
					while (sc.hasNext()) {
						linea = sc.nextLine();
						if (linea.contains(this.obj_XML_Id.get(inTipoObjetoS3e).get(inObjeto))) {
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
	}
	
	public String buscaStringScript (String stringBuscar, File fileScript) {
		String imprimeError ="";
		try {
			int apariciones = 0;
			Scanner sc = new Scanner(fileScript);
			String linea = "";
			while (sc.hasNext()) {
				linea = sc.nextLine();
				if (    
						linea.contains(" "+stringBuscar+" ")||
						linea.contains(","+stringBuscar+",")||
						linea.contains(","+stringBuscar+")")||
						linea.contains("("+stringBuscar+",")||
						
						linea.contains("\""+stringBuscar+"\"")||
						
						linea.contains(" "+stringBuscar+",")||
						linea.contains(","+stringBuscar+" ")||
						linea.contains("("+stringBuscar+" ")||
						linea.contains(" "+stringBuscar+")")

						) {
					apariciones++;
					if (apariciones > 0) {						
						imprimeError = "ERROR: Se usa elemento repetido (" + stringBuscar + ") en script: " + fileScript;
					}						
				}
			}
			sc.close();		
							
		} catch (IOException e) {
			e.printStackTrace();
			return  imprimeError+" " + e;
		}
		return  imprimeError;
	}
	
	public void estimaMCS() {
		String objeto; 		
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermApagado";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_FNR += lista_Obj_S3e_CANTIDAD.get(posicion)/4; 
			n_estimados_MCSQ4_AF += lista_Obj_S3e_CANTIDAD.get(posicion)/8;			
		} else {
			System.out.println("ERROR: para estimar MCS, debe tenerse en cuenta " +  objeto );
		}
				
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_noIntermEncendido";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_FR += lista_Obj_S3e_CANTIDAD.get(posicion)/4;
			n_estimados_MCSQ4_AF += lista_Obj_S3e_CANTIDAD.get(posicion)/8;			
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_IntermApagado";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);		
			n_estimados_MCS_FNRI += lista_Obj_S3e_CANTIDAD.get(posicion)/2;
			n_estimados_MCSQ4_AF += lista_Obj_S3e_CANTIDAD.get(posicion)/8;			
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		objeto = "MF/Ubicacion=MF_Local/TipoFoco=MF_InterEncFijoReposo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_FRI += lista_Obj_S3e_CANTIDAD.get(posicion)/2;
			n_estimados_MCSQ4_AF += lista_Obj_S3e_CANTIDAD.get(posicion)/8;	
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_dobleHilo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ESHD += lista_Obj_S3e_CANTIDAD.get(posicion)/8;
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/40;			
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_dobleConjugada";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += lista_Obj_S3e_CANTIDAD.get(posicion)/8; 
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/20;   // no estoy seguro
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "ED/Ubicacion=ED_Local/Tipo=ED_simpleHilo";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += lista_Obj_S3e_CANTIDAD.get(posicion)/16; 
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/40;   // no estoy seguro
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "MA";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ES += lista_Obj_S3e_CANTIDAD.get(posicion)/2 + 1/8; //1/8 por el final de carrera
			n_estimados_MCS_SD += lista_Obj_S3e_CANTIDAD.get(posicion)/2 + 1/8;  
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/4;   // no estoy seguro
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "MM";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_ESHD += lista_Obj_S3e_CANTIDAD.get(posicion)/2 ; 
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/4;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		
		objeto = "SD/Ubicacion=SD_Local/Tipo=SD_simple";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_SD += lista_Obj_S3e_CANTIDAD.get(posicion)/4 ; 
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}
		objeto = "SD/Ubicacion=SD_Local/Tipo=SD_doble";
		if(lista_Obj_S3e_NOMBRE.contains(objeto))
		{
			int posicion;
			posicion = Listas.buscaEnLista(lista_Obj_S3e_NOMBRE, objeto);			
			n_estimados_MCS_SD += lista_Obj_S3e_CANTIDAD.get(posicion)/2 ; 
			n_estimados_MCSQ4_AES += lista_Obj_S3e_CANTIDAD.get(posicion)/8;
		} else {
			System.out.println("ERROR: debe tenerse en cuenta " +  objeto );
		}		
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
	private float n_estimados_MCSQ4_AES = 0;
	private float n_estimados_MCSQ4_AF = 0;
}
