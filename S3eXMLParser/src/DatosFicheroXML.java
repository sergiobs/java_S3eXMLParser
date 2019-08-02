import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DatosFicheroXML {
	
	// getters
	public String getNombreFichero() {
		return nombreFichero;
	}
	public File getFichero() {
		return fichero;
	}
	public List<Integer> getListaNumeroObjetosS3e() {
		return listaNumeroObjetosS3e;
	}	
	public List<List<String>> getListaNombreObjetosRepetidosEnXML() {
		return listaNombreObjetosRepetidosEnXML;
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
	
	// constructor
	public DatosFicheroXML () {
		
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
		listaNombreObjetosS3e.add("ALG");
		listaNombreObjetosS3e.add("EDNV");
		listaNombreObjetosS3e.add("DCO");
		listaNombreObjetosS3e.add("SC");
		listaNombreObjetosS3e.add("DAS");
		listaNombreObjetosS3e.add("DPT");
		listaNombreObjetosS3e.add("DR");
		listaNombreObjetosS3e.add("GSE");
		listaNombreObjetosS3e.add("CPM");
		listaNombreObjetosS3e.add("DZ");
		listaNombreObjetosS3e.add("PG");
		listaNombreObjetosS3e.add("RA");
		listaNombreObjetosS3e.add("TY");
		listaNombreObjetosS3e.add("DEM");
		listaNombreObjetosS3e.add("BL");
		listaNombreObjetosS3e.add("MO");
		listaNombreObjetosS3e.add("ML");
		listaNombreObjetosS3e.add("DP");
		listaNombreObjetosS3e.add("IAS");
		listaNombreObjetosS3e.add("CSC");
		listaNombreObjetosS3e.add("GIM");
		
		this.numeroObjetosS3e = listaNombreObjetosS3e.size();
		
		// se crea una lista de numObjS3e elementos que contendra el numero de obejtos de cada tipo (numeroMCS, numeroED, numeroSD, )
		// inicialmente todos con valor 0
		for (int i=0; i<listaNombreObjetosS3e.size(); i++) 
			this.listaNumeroObjetosS3e.add(0);
		
		// se crea una lista de listas que contendran los elementos repetidos de cada tipo si los hubiese:
		// 
		// listaNombreObjetosRepetidosEnXML(0) 		= ("MCS_001", "MCS_001") en caso de que dos MCS tengan el mismo nombre "MCS_001"
		// listaNombreObjetosRepetidosEnXML(1) 		= ("EDVA1", "EDVA1")
		// listaNombreObjetosRepetidosEnXML(2) 		= vacio		
		// ...
		// listaNombreObjetosRepetidosEnXML(numObjS3e-1) = etc
		
		
		for (int i=0; i<listaNombreObjetosS3e.size(); i++) 
		{
			this.listaNombreObjetosRepetidosEnXML.add(new ArrayList<String>());
		}
		for (int i=0; i<listaNombreObjetosS3e.size(); i++) 
		{
			this.listaNombreObjetosEnXML.add(new ArrayList<String>());
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
					texto2print+="ERROR: Existen nombres repetidos en este fichero\n";
					primerRepetido = false;
				}
				texto2print += listaNombreObjetosS3e.get(aa) + ": " + listaNombreObjetosRepetidosEnXML.get(aa) + "\t";				
				existeRepetido = true;
			}					
		}
		if (!existeRepetido) {
			texto2print += "INFO: No existen nombres repetidos en este fichero\n";
		}
		return texto2print;
	}
	
	public void calculaObjetosRepetidosS3e () {
		List<String> listaDevueltaConDuplicados = new ArrayList<String>();
		for (int i=0; i<listaNumeroObjetosS3e.size(); i++) 
		{
			// se llama a una función que devuelve una lista de objetos repetidos de la lista pasada como argumento			
			listaDevueltaConDuplicados = Listas.devuelveListaRepetidos((ArrayList<String>) listaNombreObjetosEnXML.get(i));
			
			//paso la lista devuelta a listaNombreObjetosRepetidosEnXML(i)
			for (int j=0;j<listaDevueltaConDuplicados.size();j++) {
				listaNombreObjetosRepetidosEnXML.get(i).add(listaDevueltaConDuplicados.get(j));
			}			
		}
	}
	
	// variables del objeto
	private String nombreFichero;
	private List<String> listaNombreObjetosS3e = new ArrayList<String>();
	private List<Integer> listaNumeroObjetosS3e = new ArrayList<Integer>();
	private List<List<String>> listaNombreObjetosRepetidosEnXML = new ArrayList<List<String>>();
	private List<List<String>> listaNombreObjetosEnXML = new ArrayList<List<String>>();
	private File fichero = new File("");
	private int numeroObjetosS3e;	
}
