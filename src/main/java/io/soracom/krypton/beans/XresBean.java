package io.soracom.krypton.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class XresBean {
	private String xres;
	
	
	public String getXres() {
		return xres;
	}

	public void setXres(String xres) {
		this.xres = xres;
	}

	public String toJson(){
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
	
	public static XresBean fromJson(String json){
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, XresBean.class);
	}
	
	public boolean isEmpty(){
		return (xres==null);
	}
}
