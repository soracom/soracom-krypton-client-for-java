package io.soracom.krypton.beans;

public class BootstrapInventoryDeviceResult {

	private String serverUri;
	private String pskId;
	private String applicationKey;

	public String getServerUri() {
		return serverUri;
	}

	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
	}

	public String getPskId() {
		return pskId;
	}

	public void setPskId(String pskId) {
		this.pskId = pskId;
	}

	public void setApplicationKey(String applicationKey) {
		this.applicationKey = applicationKey;
	}

	public String getApplicationKey() {
		return applicationKey;
	}
}
