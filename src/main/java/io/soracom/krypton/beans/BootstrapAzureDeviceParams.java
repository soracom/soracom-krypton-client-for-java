package io.soracom.krypton.beans;

public class BootstrapAzureDeviceParams {

	private String deviceId;
	private String globalEndpoint;
	private String connectionString;
	private String idScope;
	private String credentialsId;

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

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getIdScope() {
		return idScope;
	}

	public void setIdScope(String idScope) {
		this.idScope = idScope;
	}

	public String getCredentialsId() {
		return credentialsId;
	}

	public void setCredentialsId(String credentialsId) {
		this.credentialsId = credentialsId;
	}
}
