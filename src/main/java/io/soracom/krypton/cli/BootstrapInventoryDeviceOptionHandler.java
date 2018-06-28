package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.endorse.utils.Utilities;
import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.BootstrapInventoryDeviceParams;
import io.soracom.krypton.beans.BootstrapInventoryDeviceResult;

public class BootstrapInventoryDeviceOptionHandler implements KryptonOptionHandler<BootstrapInventoryDeviceResult> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("bootstrapInventoryDevice")//
				.desc("Bootstrap SORACOM Inventory device").build();
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
