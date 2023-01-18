package io.soracom.krypton.beans;

public class RegisterAzureRequestParams {
    private RegisterAzureDeviceParams requestParameters;

    public RegisterAzureRequestParams(){
        requestParameters = new RegisterAzureDeviceParams();
    }

    public RegisterAzureRequestParams(RegisterAzureDeviceParams requestParameters){
        this.requestParameters = requestParameters;
    }

    public RegisterAzureDeviceParams getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RegisterAzureDeviceParams requestParameters) {
        this.requestParameters = requestParameters;
    }
}
