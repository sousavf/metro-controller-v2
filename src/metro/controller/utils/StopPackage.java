package metro.controller.utils;

public class StopPackage {
	private int StopID;
	private String StopDescription;
	private String CityDescription;
	private int CityID;
	private String ModuleDescription;
	private int ModuleID;
	private float Latitude;
	private float Longitude;
	private boolean LinhaAzul;
	private boolean LinhaAmarela;
	private boolean LinhaVermelha;
	private boolean LinhaLaranja;
	private boolean LinhaLilas;
	private boolean LinhaVerde;
	
	public boolean isLinhaAzul() {
		return LinhaAzul;
	}

	public void setLinhaAzul(boolean linhaAzul) {
		LinhaAzul = linhaAzul;
	}

	public boolean isLinhaAmarela() {
		return LinhaAmarela;
	}

	public void setLinhaAmarela(boolean linhaAmarela) {
		LinhaAmarela = linhaAmarela;
	}

	public boolean isLinhaVermelha() {
		return LinhaVermelha;
	}

	public void setLinhaVermelha(boolean linhaVermelha) {
		LinhaVermelha = linhaVermelha;
	}

	public boolean isLinhaLaranja() {
		return LinhaLaranja;
	}

	public void setLinhaLaranja(boolean linhaLaranja) {
		LinhaLaranja = linhaLaranja;
	}

	public boolean isLinhaLilas() {
		return LinhaLilas;
	}

	public void setLinhaLilas(boolean linhaLilas) {
		LinhaLilas = linhaLilas;
	}

	public boolean isLinhaVerde() {
		return LinhaVerde;
	}

	public void setLinhaVerde(boolean linhaVerde) {
		LinhaVerde = linhaVerde;
	}

	public int getCityID() {
		return CityID;
	}

	public void setCityID(int cityID) {
		CityID = cityID;
	}

	public int getModuleID() {
		return ModuleID;
	}

	public void setModuleID(int moduleID) {
		ModuleID = moduleID;
	}

	public int getStopID() {
		return StopID;
	}

	public void setStopID(int stopID) {
		StopID = stopID;
	}

	public String getStopDescription() {
		return StopDescription;
	}

	public void setStopDescription(String stopDescription) {
		StopDescription = stopDescription;
	}

	public String getCityDescription() {
		return CityDescription;
	}

	public void setCityDescription(String cityDescription) {
		CityDescription = cityDescription;
	}

	public String getModuleDescription() {
		return ModuleDescription;
	}

	public void setModuleDescription(String moduleDescription) {
		ModuleDescription = moduleDescription;
	}

	public float getLatitude() {
		return Latitude;
	}

	public void setLatitude(float latitude) {
		Latitude = latitude;
	}

	public float getLongitude() {
		return Longitude;
	}

	public void setLongitude(float longitude) {
		Longitude = longitude;
	}

	public String toString() {
		return StopDescription;
	}

}
