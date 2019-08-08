import java.util.ArrayList;
import java.util.List;

public class MCS {
	public String getNombreMCS() {
		return nombreMCS;
	}
	public void setNombreMCS(String nombreMCS) {
		this.nombreMCS = nombreMCS;
	}
	public String getIdMCS() {
		return idMCS;
	}
	public void setIdMCS(String idMCS) {
		this.idMCS = idMCS;
	}
	public String getTipoMCS() {
		return tipoMCS;
	}
	public void setTipoMCS(String tipoMCS) {
		this.tipoMCS = tipoMCS;
	}
	public List<Integer> getListaEDs() {
		return listaEDs;
	}
	public void setListaEDs(List<Integer> listaEDs) {
		this.listaEDs = listaEDs;
	}
	public List<Integer> getListaSDs() {
		return listaSDs;
	}
	public void setListaSDs(List<Integer> listaSDs) {
		this.listaSDs = listaSDs;
	}
	private String nombreMCS = "";
	private String idMCS = "";
	private String tipoMCS = "";
	private List<Integer> listaEDs = new ArrayList<Integer>();
	private List<Integer> listaSDs = new ArrayList<Integer>();
}
