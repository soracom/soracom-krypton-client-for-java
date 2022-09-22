package io.soracom.krypton.beans;

public class RegisterAzureIotDeviceResult {

	private String operationId;
	private String status;

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

}
