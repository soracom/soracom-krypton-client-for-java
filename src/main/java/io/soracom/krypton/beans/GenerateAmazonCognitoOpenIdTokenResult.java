package io.soracom.krypton.beans;

public class GenerateAmazonCognitoOpenIdTokenResult {

	private String region;
	private String token;
	private String identityId;

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
}
