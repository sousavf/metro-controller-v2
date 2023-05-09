package metro.controller.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ResponsePackage {
	private int UserID;
	private long ReportTime;
	private String StopDescription;
	private int StopID;
	private String CityDescription;
	private int CityID;
	private String ModuleDescription;
	private int ModuleID;
	private float Latitude;
	private float Longitude;
	private int UserRating;
	
	

	public int getStopID() {
		return StopID;
	}



	public void setStopID(int stopID) {
		StopID = stopID;
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



	public int getUserID() {
		return UserID;
	}



	public void setUserID(int userID) {
		UserID = userID;
	}



	public long getReportTime() {
		return ReportTime;
	}



	public void setReportTime(long reportTime) {
		ReportTime = reportTime;
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



	public int getUserRating() {
		return UserRating;
	}



	public void setUserRating(int userRating) {
		UserRating = userRating;
	}



	public String toString() {
		Timestamp aux = null;
		//aux = this.getReportTime();
		
	    String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	    SimpleDateFormat sdf =
	          new SimpleDateFormat(DATE_FORMAT);
		Calendar cal=GregorianCalendar.getInstance();	    
		cal.setTime(aux);

		return this.getStopDescription() + "\n" + sdf.format(cal.getTime());
		
	//	if(this.getUserName().equalsIgnoreCase("Anonymous"))
	//		return this.getStopName() + "\n" + aux.toString().substring(0, 19);
				//	+ "\n" + this.getCity() + ", " + this.getCountry() + "\n"
				//	+ this.getModule();
	/*	else
			return this.getStopName() + "\n" + aux.toString().substring(0, 19)
			+ "\n" + this.getCity() + ", " + this.getCountry() + "\n"
			+ this.getModule() + " - " + this.getUserName() + " > "
			+ this.getRating();*/
	}
}
