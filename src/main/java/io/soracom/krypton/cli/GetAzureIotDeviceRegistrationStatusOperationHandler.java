/**
 * Copyright (c) 2018 SORACOM, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soracom.krypton.cli;

import io.soracom.endorse.utils.Utilities;
import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.GetAzureIotDeviceRegistrationStatusParams;
import io.soracom.krypton.beans.GetAzureIotDeviceRegistrationStatusResult;


/**
 * Handler to invoke operation of registerAzureIotDevice
 * 
 * @author olivier-soracom
 *
 */
public class GetAzureIotDeviceRegistrationStatusOperationHandler implements KryptonOperationHandler<GetAzureIotDeviceRegistrationStatusResult> {

	@Override
	public OperationInfo getOperationInfo() {
		return new OperationInfo("getAzureIotDeviceRegistrationStatus", "Get Registeration Status of Azure IoT Devices");
	}

	@Override
	public GetAzureIotDeviceRegistrationStatusResult invoke(SORACOMKryptonClientConfig kryptonClientConfig, String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		GetAzureIotDeviceRegistrationStatusParams param = Utilities.fromJson(requestParamJson,
				GetAzureIotDeviceRegistrationStatusParams.class);
		GetAzureIotDeviceRegistrationStatusResult result = client.getAzureIotDeviceRegistrationStatus(param);
		return result;
	}

}
