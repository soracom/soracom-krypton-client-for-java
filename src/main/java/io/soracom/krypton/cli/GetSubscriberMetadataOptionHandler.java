package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.GetSubscriberMetadataResult;

public class GetSubscriberMetadataOptionHandler implements KryptonOptionHandler<GetSubscriberMetadataResult> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("getSubscriberMetadata").desc("Get subscriber metadata").build();
	}

	@Override
	public GetSubscriberMetadataResult invoke(SORACOMKryptonClientConfig kryptonClientConfig, String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		GetSubscriberMetadataResult subscriberMetadata = client.getSubscriberMetadata();
		return subscriberMetadata;
	}

}
