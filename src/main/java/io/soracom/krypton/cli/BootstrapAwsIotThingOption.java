package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.BootstrapAwsIotThingResult;

public class BootstrapAwsIotThingOption implements KryptonOptionHandler<BootstrapAwsIotThingResult> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("bootstrapAwsIotThing")//
				.desc("Bootstrap AWS IoT Things").build();
	}

	@Override
	public BootstrapAwsIotThingResult invoke(SORACOMKryptonClientConfig kryptonClientConfig, String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		BootstrapAwsIotThingResult result = client.bootstrapAwsIotThing();
		return result;
	}

}
