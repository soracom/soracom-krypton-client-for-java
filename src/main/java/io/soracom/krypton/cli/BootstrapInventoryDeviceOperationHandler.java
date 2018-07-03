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
import io.soracom.krypton.beans.BootstrapInventoryDeviceParams;
import io.soracom.krypton.beans.BootstrapInventoryDeviceResult;

public class BootstrapInventoryDeviceOperationHandler
		implements KryptonOperationHandler<BootstrapInventoryDeviceResult> {

	@Override
	public OperationInfo getOperationInfo() {
		return new OperationInfo("bootstrapInventoryDevice", "Bootstrap SORACOM Inventory device");
	}

	@Override
	public BootstrapInventoryDeviceResult invoke(SORACOMKryptonClientConfig kryptonClientConfig,
			String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		BootstrapInventoryDeviceParams param = Utilities.fromJson(requestParamJson,
				BootstrapInventoryDeviceParams.class);
		BootstrapInventoryDeviceResult result = client.bootstrapInventoryDevice(param);
		return result;
	}

}
