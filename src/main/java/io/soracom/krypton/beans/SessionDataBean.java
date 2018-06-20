package io.soracom.krypton.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SessionDataBean {
	private String imsi;
	private String rand;
	private String auts;
	
	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	
	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getAuts() {
		return auts;
	}

	public void setAuts(String auts) {
		this.auts = auts;
	}

	public String toJson(){
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
	
	public static SessionDataBean fromJson(String json){
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, SessionDataBean.class);
	}
	
	public boolean isEmpty(){
		return (imsi==null);
	}
}
