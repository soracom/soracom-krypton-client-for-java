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
package io.soracom.krypton;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.soracom.endorse.EndorseAPI;
import io.soracom.endorse.SORACOMEndorseClient;
import io.soracom.endorse.SORACOMEndorseClientConfig;
import io.soracom.endorse.common.HttpRequestException;
import io.soracom.endorse.common.ITextLogListener;
import io.soracom.endorse.common.TextLog;
import io.soracom.endorse.common.TextLogItem;
import io.soracom.endorse.keycache.AuthResult;
import io.soracom.endorse.utils.Utilities;
import io.soracom.krypton.beans.BootstrapAwsIotThingResult;
import io.soracom.krypton.beans.BootstrapInventoryDeviceParams;
import io.soracom.krypton.beans.BootstrapInventoryDeviceResult;
import io.soracom.krypton.beans.GenerateAmazonCognitoOpenIdTokenResult;
import io.soracom.krypton.beans.GenerateAmazonCognitoSessionCredentialsResult;
import io.soracom.krypton.beans.GetSubscriberMetadataResult;
import io.soracom.krypton.beans.GetUserdataResult;
import io.soracom.krypton.beans.ProvisioningBean;
import io.soracom.krypton.common.KryptonClientRuntimeException;

public class SORACOMKryptonClient {

	private SORACOMKryptonClientConfig kryptonClientConfig;

	public SORACOMKryptonClient(SORACOMKryptonClientConfig kryptonClientConfig) {
		this(kryptonClientConfig, null);
	}

	public SORACOMKryptonClient(SORACOMKryptonClientConfig kryptonClientConfig, ITextLogListener logListener) {
		this.kryptonClientConfig = kryptonClientConfig;
		initLogger(logListener);
	}

	private void initLogger(ITextLogListener logListener) {
		if (logListener == null) {
			logListener = new KryptonClientLogListener(kryptonClientConfig.isDebug() == false);
		}
		TextLog.clerListener();
		TextLog.addListener(logListener);
	}

	public static class TrustedEntityRequst {
		private String apiEndpointUrl;
		private Object requestParameters;

		public void setApiEndpointUrl(String apiEndpointUrl) {
			this.apiEndpointUrl = apiEndpointUrl;
		}

		public void setRequestParameters(Object requestParameters) {
			this.requestParameters = requestParameters;
		}
	}

	public BootstrapInventoryDeviceResult bootstrapInventoryDevice(BootstrapInventoryDeviceParams params) {
		TrustedEntityRequst request = new TrustedEntityRequst();
		request.requestParameters = params;
		request.apiEndpointUrl = ProvisioningApiEndpoint
				.bootstrapInventoryDevice(kryptonClientConfig.getApiEndpointUrl());

		ProvisioningBean invokeProvisioingApiResult = invokeProvisioningApi(request, true);
		BootstrapInventoryDeviceResult result = Utilities.fromJson(
				invokeProvisioingApiResult.getServiceProviderResponse(), BootstrapInventoryDeviceResult.class);
		result.setApplicationKey(invokeProvisioingApiResult.getApplicationKey());
		return result;
	}

	public GetUserdataResult getUserdata() {
		TrustedEntityRequst request = new TrustedEntityRequst();
		request.apiEndpointUrl = ProvisioningApiEndpoint.getUserdata(kryptonClientConfig.getApiEndpointUrl());

		ProvisioningBean invokeProvisioingApiResult = invokeProvisioningApi(request, false);

		GetUserdataResult result = new GetUserdataResult();
		result.setUserData(invokeProvisioingApiResult.getServiceProviderResponse());
		return result;
	}

	public GetSubscriberMetadataResult getSubscriberMetadata() {
		TrustedEntityRequst request = new TrustedEntityRequst();
		request.apiEndpointUrl = ProvisioningApiEndpoint.getSubscriberMetadata(kryptonClientConfig.getApiEndpointUrl());

		ProvisioningBean invokeProvisioingApiResult = invokeProvisioningApi(request, false);

		GetSubscriberMetadataResult result = new GetSubscriberMetadataResult();

		result.setSubscriber(
				Utilities.fromJson(invokeProvisioingApiResult.getServiceProviderResponse(), JsonObject.class));
		return result;
	}

	public GenerateAmazonCognitoSessionCredentialsResult generateAmazonCognitoSessionCredentials() {
		TrustedEntityRequst request = new TrustedEntityRequst();
		request.apiEndpointUrl = ProvisioningApiEndpoint
				.generateAmazonCognitoSessionCredentials(kryptonClientConfig.getApiEndpointUrl());
		return invokeKeyDistributionService(request, GenerateAmazonCognitoSessionCredentialsResult.class);
	}

	public GenerateAmazonCognitoOpenIdTokenResult generateAmazonCognitoOpenIdToken() {
		TrustedEntityRequst request = new TrustedEntityRequst();
		request.apiEndpointUrl = ProvisioningApiEndpoint
				.generateAmazonCognitoOpenIdToken(kryptonClientConfig.getApiEndpointUrl());
		return invokeKeyDistributionService(request, GenerateAmazonCognitoOpenIdTokenResult.class);
	}

	public BootstrapAwsIotThingResult bootstrapAwsIotThing() {
		TrustedEntityRequst request = new TrustedEntityRequst();
		request.apiEndpointUrl = ProvisioningApiEndpoint.bootstrapAwsIotThing(kryptonClientConfig.getApiEndpointUrl());
		return invokeKeyDistributionService(request, BootstrapAwsIotThingResult.class);
	}

	private <T> T invokeKeyDistributionService(TrustedEntityRequst request, Class<T> resultClass) {
		ProvisioningBean invokeProvisioingApiResult = invokeProvisioningApi(request, false);
		return Utilities.fromJson(invokeProvisioingApiResult.getServiceProviderResponse(), resultClass);
	}

	private ProvisioningBean invokeProvisioningApi(TrustedEntityRequst request, boolean requiredApplicationKey)
			throws KryptonClientRuntimeException {
		SORACOMEndorseClientConfig endorseConfig = kryptonClientConfig.getEndorseClientConfig();
		SORACOMEndorseClient endorseClient = new SORACOMEndorseClient(endorseConfig);
		AuthResult authResult = endorseClient.doAuthentication();

		long currentTimeStamp = System.currentTimeMillis();
		String requestParamJson = request.requestParameters == null ? null
				: new Gson().toJson(request.requestParameters);
		try {
			String serviceResponse = EndorseAPI.requestService(request.apiEndpointUrl, authResult.ckBytes(),
					currentTimeStamp, authResult.getKeyId(), endorseConfig.getKeyLength(),
					endorseConfig.getKeyAlgorithm(), requestParamJson);
			ProvisioningBean result = new ProvisioningBean();
			result.setServiceProviderResponse(serviceResponse);
			if (requiredApplicationKey) {
				// , result.getNonce() != null
				// TODO
				// validateApplicationKey(result, currentTimeStamp, authResult.ck);
			}
			return result;
		} catch (HttpRequestException e) {
			throw new KryptonClientRuntimeException("failed to invoke provisioning api.", e);
		}
	}

	public static class KryptonClientLogListener implements ITextLogListener {
		private boolean suppressLogOutput;

		public KryptonClientLogListener() {
		}

		public KryptonClientLogListener(boolean suppressLogOutput) {
			setSuppressLog(suppressLogOutput);
		}

		public void setSuppressLog(boolean suppressLogOutput) {
			this.suppressLogOutput = suppressLogOutput;
		}

		@Override
		public void itemAdded(TextLogItem item) {
			switch (item.getType()) {

			case DEBUG:
			case LOG:
			case WARN:
				if (suppressLogOutput == false) {
					System.out.println(item.toString());
				}
				break;
			case ERR:
				System.err.println(item.toString());
				break;
			}
		}
	}
}