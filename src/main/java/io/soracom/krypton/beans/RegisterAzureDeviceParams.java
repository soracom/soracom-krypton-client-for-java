package io.soracom.krypton.beans;

public class RegisterAzureDeviceParams {

	private String deviceId;
	private String globalEndpoint;
	private String idScope;
	private String azureIotCredentialId;
	private String x509CredentialId;
	private String host;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGlobalEndpoint() {
		return globalEndpoint;
	}

	public void setGlobalEndpoint(String globalEndpoint) {
		this.globalEndpoint = globalEndpoint;
	}

	public String getIdScope() {
		return idScope;
	}

	public void setIdScope(String idScope) {
		this.idScope = idScope;
	}

	public String getAzureIotCredentialId() { return azureIotCredentialId; }

	public void setAzureIotCredentialId(String azureIotCredentialId) { this.azureIotCredentialId = azureIotCredentialId; }

	public String getX509CredentialId() {
        return x509CredentialId;
    }

  public void setX509CredentialId(String x509CredentialId) { this.x509CredentialId = x509CredentialId; }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
