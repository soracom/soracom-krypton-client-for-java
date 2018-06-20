package io.soracom.krypton.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MilenageParamsBean {
	private String keyId;
	private String rand;
	private String autn;
	
	
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getAutn() {
		return autn;
	}

	public void setAutn(String autn) {
		this.autn = autn;
	}

	public String toJson(){
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(this);
	}
	
	public static MilenageParamsBean fromJson(String json){
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, MilenageParamsBean.class);
	}
}
