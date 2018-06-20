package io.soracom.krypton.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TokenBean {
	private String token;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String toJson(){
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
	
	public static TokenBean fromJson(String json){
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, TokenBean.class);
	}
	
	public boolean isEmpty(){
		return (token==null);
	}
}
