package io.soracom.krypton.beans;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class KeyDistributionBeanTest {

	@Test
	public void testDeserialize() {
		KeyDistributionBean bean = new KeyDistributionBean();
		String json = "{\"privateKey\":\".....\", \"clientId\":\"kengo-device-295050910001332\"}";
		bean.setServiceProviderResponse(new Gson().fromJson(json, JsonObject.class));
		System.out.println(bean.toJson());
		assertEquals("{\"serviceProviderResponse\":{\"privateKey\":\".....\",\"clientId\":\"kengo-device-295050910001332\"}}", bean.toJson());

	}
}
