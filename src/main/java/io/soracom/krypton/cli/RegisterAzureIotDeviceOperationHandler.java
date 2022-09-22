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
import io.soracom.krypton.beans.RegisterAzureDeviceParams;
import io.soracom.krypton.beans.RegisterAzureIotDeviceResult;

/**
 * Handler to invoke operation of registerAzureIotDevice
 * 
 * @author olivier-soracom
 *
 */
public class RegisterAzureIotDeviceOperationHandler implements KryptonOperationHandler<RegisterAzureIotDeviceResult> {

	@Override
	public OperationInfo getOperationInfo() {
		return new OperationInfo("registerAzureIotDevice", "Register Azure IoT Devices");
	}

	@Override
	public RegisterAzureIotDeviceResult invoke(SORACOMKryptonClientConfig kryptonClientConfig, String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		RegisterAzureDeviceParams param = Utilities.fromJson(requestParamJson,
				RegisterAzureDeviceParams.class);
		RegisterAzureIotDeviceResult result = client.registerAzureIotDevice(param);
		return result;
	}

}
