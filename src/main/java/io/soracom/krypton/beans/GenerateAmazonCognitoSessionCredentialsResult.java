package io.soracom.krypton.beans;

public class GenerateAmazonCognitoSessionCredentialsResult {

	private String identityId;
	private Credentials credentials;

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public static class Credentials {
		private String accessKeyId;
		private String secretKey;
		private String sessionToken;
		private Long expiration;

		public String getAccessKeyId() {
			return accessKeyId;
		}

		public void setAccessKeyId(String accessKeyId) {
			this.accessKeyId = accessKeyId;
		}

		public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}

		public String getSessionToken() {
			return sessionToken;
		}

		public void setSessionToken(String sessionToken) {
			this.sessionToken = sessionToken;
		}

		public Long getExpiration() {
			return expiration;
		}

		public void setExpiration(Long expiration) {
			this.expiration = expiration;
		}

	}
}
