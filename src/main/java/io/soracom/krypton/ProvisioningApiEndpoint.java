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

/**
 * Endpoint definition for SORACOM Krypton
 * 
 * @author c9katayama
 *
 */
public enum ProvisioningApiEndpoint {

	GLOBAL_COVERAGE("https://g.api.soracom.io"), //
	JAPAN_COVERAGE("https://api.soracom.io");
	private String apiEndpoint;

	private ProvisioningApiEndpoint(String url) {
		apiEndpoint = url;
	}

	public String getApiEndpoint() {
		return apiEndpoint;
	}

	private static String trimSlash(String baseUrl) {
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}
		return baseUrl;
	}

	public static String bootstrapAwsIotThing(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/aws/iot/bootstrap";
	}

	public static String bootstrapAzureIotDevice(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/azure/iot/bootstrap";
	}

	public static String bootstrapInventoryDevice(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/soracom/inventory/bootstrap";
	}

	public static String bootstrapArcDevice(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/soracom/arc/bootstrap";
	}

	public static String generateAmazonCognitoOpenIdToken(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/aws/cognito/open_id_tokens";
	}

	public static String generateAmazonCognitoSessionCredentials(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/aws/cognito/credentials";
	}

	public static String getSubscriberMetadata(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/soracom/air/subscriber_metadata";
	}

	public static String getUserdata(String baseUrl) {
		return trimSlash(baseUrl) + "/v1/provisioning/soracom/air/userdata";
	}

	public static ProvisioningApiEndpoint getDefault() {
		return GLOBAL_COVERAGE;
	}
}
