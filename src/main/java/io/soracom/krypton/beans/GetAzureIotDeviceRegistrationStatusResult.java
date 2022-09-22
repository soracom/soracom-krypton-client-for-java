package io.soracom.krypton.beans;

public class GetAzureIotDeviceRegistrationStatusResult {


	private String certificate;
	private String privateKey;
	private String rootCaCertificate;
	private String deviceId;
	private String operationId;
  private String status;
	private String host;

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getRootCaCertificate() { return rootCaCertificate; }

	public void setRootCaCertificate(String rootCaCertificate) { this.rootCaCertificate = rootCaCertificate; }

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getOperationId() {
        return operationId;
    }

  public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

  public String getStatus() {
        return status;
    }

  public void setStatus(String status) {
        this.status = status;
    }
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
