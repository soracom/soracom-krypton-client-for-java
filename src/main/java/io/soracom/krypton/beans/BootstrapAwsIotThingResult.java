package io.soracom.krypton.beans;

public class BootstrapAwsIotThingResult {

	private String region;
	private String certificate;
	private String rootCaCertificate;
	private String privateKey;
	private String host;
	private String clientId;

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getRootCaCertificate() {
		return rootCaCertificate;
	}

	public void setRootCaCertificate(String rootCaCertificate) {
		this.rootCaCertificate = rootCaCertificate;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

}
