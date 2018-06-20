package io.soracom.krypton;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.soracom.krypton.beans.AppkeyBean;

public class KeyDistributionBean extends AppkeyBean {
	private JSONObject serviceProviderResponse;

	public JSONObject getServiceProviderResponse() {
		return serviceProviderResponse;
	}

	public void setServiceProviderResponse(JSONObject serviceProviderResponse) {
		this.serviceProviderResponse = serviceProviderResponse;
	}

	public static KeyDistributionBean fromJson(String json) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, KeyDistributionBean.class);
	}
}
