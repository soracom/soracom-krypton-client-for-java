package io.soracom.krypton.beans;

public class BootstrapAzureRequestParams {
    private BootstrapAzureDeviceParams requestParameters;

    public BootstrapAzureRequestParams(){
        requestParameters = new BootstrapAzureDeviceParams();
    }

    public BootstrapAzureRequestParams(BootstrapAzureDeviceParams requestParameters){
        this.requestParameters = requestParameters;
    }

    public BootstrapAzureDeviceParams getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(BootstrapAzureDeviceParams requestParameters) {
        this.requestParameters = requestParameters;
    }
}
