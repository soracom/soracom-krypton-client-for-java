package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.GetSubscriberMetadataResult;
import io.soracom.krypton.beans.GetUserdataResult;

public class GetUserdataOptionHandler implements KryptonOptionHandler<GetUserdataResult> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("getUserdata")//
				.desc("Get user data from group settings").build();
	}

	@Override
	public GetUserdataResult invoke(SORACOMKryptonClientConfig kryptonClientConfig, String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		GetUserdataResult result = client.getUserdata();
		return result;
	}

}
