/**
 * Copyright (c) 2018 SORACOM, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soracom.krypton.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class KeyDistributionBean extends AppkeyBean {
	private JsonObject serviceProviderResponse;

	public JsonObject getServiceProviderResponse() {
		return serviceProviderResponse;
	}

	public void setServiceProviderResponse(JsonObject serviceProviderResponse) {
		this.serviceProviderResponse = serviceProviderResponse;
	}

	public static KeyDistributionBean fromJson(String json) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, KeyDistributionBean.class);
	}
}
