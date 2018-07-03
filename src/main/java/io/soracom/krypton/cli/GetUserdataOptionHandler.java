package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;

public class GetUserdataOptionHandler implements KryptonOptionHandler<String> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("getUserdata")//
				.desc("Get user data from group settings").build();
	}

	@Override
	public String invoke(SORACOMKryptonClientConfig kryptonClientConfig, String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		return client.getUserdata();
	}

}
