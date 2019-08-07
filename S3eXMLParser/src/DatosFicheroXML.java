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
		return listaNumeroObjetosS3e;
	}	
	public List<List<String>> getListaNombreObjetosRepetidosEnXML() {
		return listaNombreObjetosRepetidosEnXML;
	}
	public List<List<String>> getListaIdentificadorObjetosEnXML() {
		return listaIdentificadorObjetosEnXML;
	}
	public List<List<String>> getListaNombreObjetosEnXML() {
		return listaNombreObjetosEnXML;
	}
	public List<String> getListaNombreObjetosS3e() {
		return listaNombreObjetosS3e;
	}
	
	public Integer getNumeroObjetosS3e() {
		return numeroObjetosS3e;
	}
	
	
	// setters
	public void setFichero(File fichero) {
		this.fichero = fichero;
	}
	public void setNombreFichero(String nombreFichero) {
		this.nombreFichero = nombreFichero;
	}
	
	public void addNombreObjetoEnXML(String nombreObjetoEnXML, int indice) {
		this.listaNombreObjetosEnXML.get(indice).add(nombreObjetoEnXML);
	}	
	
	public void addIdentificadorObjetoEnXML(String identificadorObjetoEnXML, int indice) {
		this.listaIdentificadorObjetosEnXML.get(indice).add(identificadorObjetoEnXML);
	}
	
	//devuelve en una unica lista todos los obejtos repetidos
	public List<String> getListaTotalNombreObjetosRepetidosEnXML() {
		List<String> listaTotalNombreObjetosRepetidosEnXML = new ArrayList<String>();
		for (int i=0;i<numeroObjetosS3e;i++) {
			for (int j=0;j<listaNombreObjetosRepetidosEnXML.get(i).size();j++) {
				listaTotalNombreObjetosRepetidosEnXML.add(listaNombreObjetosRepetidosEnXML.get(i).get(j));
			}
		}		
		return listaTotalNombreObjetosRepetidosEnXML;
	}	
	
	
	// constructor
	public DatosFicheroXML (File ficheroXML) {
		
		listaNombreObjetosS3e.add("MCS");
		listaNombreObjetosS3e.add("CSE");
		listaNombreObjetosS3e.add("ED");
		listaNombreObjetosS3e.add("SD");
		listaNombreObjetosS3e.add("MF");
		listaNombreObjetosS3e.add("MA");
		listaNombreObjetosS3e.add("IF");
		listaNombreObjetosS3e.add("IS");
		listaNombreObjetosS3e.add("PV");
		listaNombreObjetosS3e.add("SE");
		listaNombreObjetosS3e.add("CV");
		listaNombreObjetosS3e.add("AG");
		listaNombreObjetosS3e.add("CES");
		//listaNombreObjetosS3e.add("ALG");
		//listaNombreObjetosS3e.add("EDNV");
		//listaNombreObjetosS3e.add("DCO");
		listaNombreObjetosS3e.add("SC");
		//listaNombreObjetosS3e.add("DAS");
		//listaNombreObjetosS3e.add("DPT");
		//listaNombreObjetosS3e.add("DR");
		//listaNombreObjetosS3e.add("GSE");
		//listaNombreObjetosS3e.add("CPM");
		//listaNombreObjetosS3e.add("DZ");
		//listaNombreObjetosS3e.add("PG");
		//listaNombreObjetosS3e.add("RA");
		//listaNombreObjetosS3e.add("TY");
		//listaNombreObjetosS3e.add("DEM");
		//listaNombreObjetosS3e.add("BL");
		listaNombreObjetosS3e.add("MO");
		//listaNombreObjetosS3e.add("ML");
		//listaNombreObjetosS3e.add("DP");
		//listaNombreObjetosS3e.add("IAS");
		//listaNombreObjetosS3e.add("CSC");
		//listaNombreObjetosS3e.add("GIM");
	
		
		listaNombreObjetosS3e_inMCS.add("MF");
		listaNombreObjetosS3e_inMCS.add("ED");
		listaNombreObjetosS3e_inMCS.add("SD");
		listaNombreObjetosS3e_inMCS.add("MA");
		
		
		//listaNombreObjetosS3e.add("ED_local");
		//listaNombreObjetosS3e.add("SD_local");
		//listaNombreObjetosS3e.add("MF_local");
		
		this.numeroObjetosS3e = listaNombreObjetosS3e.size();
		this.fichero = ficheroXML;
		this.pathScriptsAutomaticos="";
		this.pathSicamPCXML="";
				
		nombreFichero= fichero.getName();
		
		//localizamos el sicampc.xml		
		pathSicamPCXML = fichero.getPath().substring(0, fichero.getPath().length() - nombreFichero.length())+"\\..\\..\\";
		ficheroSicamPC = new File(pathSicamPCXML+"sicampc.xml");
				
		// se crea una lista de numObjS3e elementos que contendra el numero de objetos de cada tipo (numeroMCS, numeroED, numeroSD, )
		// inicialmente todos con valor 0
		for (int i=0; i<numeroObjetosS3e; i++) 
			this.listaNumeroObjetosS3e.add(0);
		
		// se crea una lista de listas que contendran los elementos repetidos de cada tipo si los hubiese:
		// 
		// listaNombreObjetosRepetidosEnXML(0) 		= ("MCS_001", "MCS_001") en caso de que dos MCS tengan el mismo nombre "MCS_001"
		// listaNombreObjetosRepetidosEnXML(1) 		= ("EDVA1", "EDVA1")
		// listaNombreObjetosRepetidosEnXML(2) 		= vacio		
		// ...
		// listaNombreObjetosRepetidosEnXML(numObjS3e-1) = etc
		
		for (int i=0; i<numeroObjetosS3e; i++) {
			this.listaNombreObjetosRepetidosEnXML.add(new ArrayList<String>());
			this.listaNombreObjetosEnXML.add(new ArrayList<String>());
			this.listaIdentificadorObjetosEnXML.add(new ArrayList<String>());
			this.listaIdentificadorUsadosObjetosEnXML.add(new ArrayList<Boolean>());
			this.listaIdentificadorObjetos_noUsadosEnXML.add(new ArrayList<String>());			
		}		
		parseaXMLS3e();
		if (fichero.exists()) {
			parseaSicamPcXML();
			if (pathScriptsAutomaticos!=null) {
				fileScriptsAutomaticos = new File(pathScriptsAutomaticos);
			} else { 
				System.out.println("No hay pathScriptsAutomaticos en sicampc.xml");
			}
		} else {
			System.out.println("No existe sicampc.xml");
		}		
		
		
		
		// Se buscan los objetos que estan en las MCS para ver su grado de ocupacion
		for (int i=0; i<listaNombreObjetosS3e_inMCS.size(); i++) {
		
		}
		//parseaXMLS3e_inMCS();
		
		
		
		
		
	}
	
	public void parseaXMLS3e () {
		try {
			SAXParserFactory factory2 = SAXParserFactory.newInstance();
			SAXParser saxParser2 = factory2.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					String etiqueta = qName;
					// buscamos el startElement en nuestra lista de objetos a evaluar
					for (int startE=0; startE<numeroObjetosS3e; startE ++) {
						// si la cabecera leida del xml esta en mi lista de obj s3e
						if (etiqueta.equalsIgnoreCase(listaNombreObjetosS3e.get(startE))) {
							listaNombreObjetosEnXML.get(startE).add(attributes.getValue("Nombre"));
							listaIdentificadorObjetosEnXML.get(startE).add(attributes.getValue("Identificador"));							
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
	
	
	public void parseaXMLS3e_inMCS () {
		try {
			SAXParserFactory factory2 = SAXParserFactory.newInstance();
			SAXParser saxParser2 = factory2.newSAXParser();
			
			DefaultHandler handler = new DefaultHandler() {
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
					String etiqueta = qName;

					for (int startE=0; startE<listaNombreObjetosS3e_inMCS.size(); startE ++) {
						// si la cabecera leida del xml esta en mi lista de obj s3e
						
						if (etiqueta.equalsIgnoreCase(listaNombreObjetosS3e_inMCS.get(startE))) {
							listaNombreObjetosEnXML.get(startE).add(attributes.getValue("Nombre"));
							listaIdentificadorObjetosEnXML.get(startE).add(attributes.getValue("Identificador"));							
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
	
	public void parseaSicamPcXML() {
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
		for (int i=0; i<listaNumeroObjetosS3e.size(); i++) 
		{
			this.listaNumeroObjetosS3e.set(i,listaNombreObjetosEnXML.get(i).size());
		}
	}
	
	public String imprimeListaNombreObjetosRepetidosEnXML () {
		boolean existeRepetido = false;
		boolean primerRepetido = true;
		String texto2print ="";
		for (int aa=0; aa<numeroObjetosS3e;aa++) {					
			if (listaNombreObjetosRepetidosEnXML.get(aa).size() > 0) {
				if (primerRepetido) {
					texto2print+="ERROR: Existen nombres repetidos en este fichero: \n";
					primerRepetido = false;
				}
				texto2print += listaNombreObjetosS3e.get(aa) + ": " ;
				for (int j=0; j<listaNombreObjetosRepetidosEnXML.get(aa).size() ; j++) {
					texto2print += listaNombreObjetosRepetidosEnXML.get(aa).get(j) + "\t";				
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
		for (int aa=0; aa<numeroObjetosS3e;aa++) {						
			texto2print+=listaNombreObjetosS3e.get(aa) + ": " + listaNumeroObjetosS3e.get(aa) + "\t";
		}
		return texto2print;
	}	
	
	public String imprimelistaNumeroObjetosS3exx () {
		String texto2print ="";
		for (int aa=0; aa<numeroObjetosS3e;aa++) {						
			texto2print+=listaNombreObjetosS3e.get(aa) + ": " + listaNumeroObjetosS3e.get(aa) + "\t";
		}
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
		for (int i=0; i<numeroObjetosS3e; i++) 
		{
			// se llama a una función que devuelve una lista de objetos repetidos de la lista pasada como argumento			
			listaDevueltaConRepetidos = Listas.devuelveListaRepetidos((ArrayList<String>) listaNombreObjetosEnXML.get(i));
			
			//paso la lista devuelta a listaNombreObjetosRepetidosEnXML(i)
			for (int j=0;j<listaDevueltaConRepetidos.size();j++) {
				listaNombreObjetosRepetidosEnXML.get(i).add(listaDevueltaConRepetidos.get(j));
			}			
		}
	}
	
	public void calculaIdentificadorObjetosNoUsadosS3e () {
		this.calculaIdentificadorObjetoNoUsados("MCS");
		this.calculaIdentificadorObjetoNoUsados("CV");
		this.calculaIdentificadorObjetoNoUsados("ED");
		this.calculaIdentificadorObjetoNoUsados("SD");
		this.calculaIdentificadorObjetoNoUsados("AG");
		this.calculaIdentificadorObjetoNoUsados("MA");		
		this.calculaIdentificadorObjetoNoUsados("MF");		
	}
	
	public String imprimeIdentificadorObjetosNoUsadosS3e () {
		String texto2print ="";
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("MCS");
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("CV");
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("ED");
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("SD");
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("AG");
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("MA");		
		texto2print+=this.imprimeIdentificadorObjetoNoUsados("MF");		
		return texto2print;
	}
	
	
	public void calculaIdentificadorObjetoNoUsados (String tipoObjetoS3e) {
		int inTipoObjetoS3e = devuelveIndiceObjetoS3e(tipoObjetoS3e);		
		try {
			//iteramos para cada MCS o ED,  .... configurada (20500, 20501, 20502, ...)
			for (int inObjeto = 0; inObjeto<this.listaIdentificadorObjetosEnXML.get(inTipoObjetoS3e).size(); inObjeto++) {				
				int apariciones = 0;
				Scanner sc = new Scanner(this.fichero);
				String linea = "";
				
				this.listaIdentificadorUsadosObjetosEnXML.get(inTipoObjetoS3e).add(false);
				while (sc.hasNext()) {
					linea = sc.nextLine();
					if (linea.contains(this.listaIdentificadorObjetosEnXML.get(inTipoObjetoS3e).get(inObjeto))) {
						apariciones++;
						if (apariciones > 1) {
							this.listaIdentificadorUsadosObjetosEnXML.get(inTipoObjetoS3e).set(inObjeto, true);
							break;
						}						
					}
				}
				sc.close();		
			}
				
		} catch (IOException e) {			
			e.printStackTrace();
		}

		for (int inObjeto = 0; inObjeto<this.listaIdentificadorUsadosObjetosEnXML.get(inTipoObjetoS3e).size(); inObjeto++) {
			if (!this.listaIdentificadorUsadosObjetosEnXML.get(inTipoObjetoS3e).get(inObjeto)) {
				listaIdentificadorObjetos_noUsadosEnXML.get(inTipoObjetoS3e).add(this.listaIdentificadorObjetosEnXML.get(inTipoObjetoS3e).get(inObjeto));				
			}
		}
	}
	
	public String buscaStringScript (String stringBuscar, File fileScript) {
		String imprimeError ="";
		try {
			int apariciones = 0;
			Scanner sc = new Scanner(fileScript);
			String linea = "";
			
			
			//System.out.println("Analizamos: "+ stringBuscar + "\t"+  fileScript);
			//this.listaIdentificadorUsadosObjetosEnXML.get(inTipoObjetoS3e).add(false);
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
						//System.out.println("ERROR: Se usa en script: "+ stringBuscar + "en: " + fileScript );
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
	
	
	private int devuelveIndiceObjetoS3e(String nombreObjetoS3e) {
		for (int i = 0; i<numeroObjetosS3e; i++) {
			if (listaNombreObjetosS3e.get(i).equals(nombreObjetoS3e)) {
				return i;				
			}
		}		
		return -1;
	}
	
	// variables del objeto
	private String nombreFichero;
	private String pathScriptsAutomaticos;
	private String pathSicamPCXML;
	private List<MCS> MCSs = new ArrayList<MCS>();
	private List<String> listaNombreObjetosS3e = new ArrayList<String>();
	private List<Integer> listaNumeroObjetosS3e = new ArrayList<Integer>();
	private List<List<String>> listaNombreObjetosRepetidosEnXML = new ArrayList<List<String>>();
	private List<List<String>> listaNombreObjetosEnXML = new ArrayList<List<String>>();
	private List<List<String>> listaIdentificadorObjetosEnXML = new ArrayList<List<String>>();
	private List<List<String>> listaIdentificadorObjetos_noUsadosEnXML = new ArrayList<List<String>>();
	
	private List<String> listaNombreObjetosS3e_inMCS = new ArrayList<String>(); //lista de objetos que se van a ir identificando e incrustando en las MCS para contar lo llenas q estan
	
	private List<List<Boolean>> listaIdentificadorUsadosObjetosEnXML = new ArrayList<List<Boolean>>();
	private File fichero = new File("");
	private File ficheroSicamPC = new File("");
	private File fileScriptsAutomaticos;
	private int numeroObjetosS3e;		

}
