package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.GenerateAmazonCognitoOpenIdTokenResult;

public class GenerateAmazonCognitoOpenIdTokenOptionHandler
		implements KryptonOptionHandler<GenerateAmazonCognitoOpenIdTokenResult> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("generateAmazonCognitoOpenIdToken")
				.desc("Generate open id token by Amazon Cognito").build();
	}

	@Override
	public GenerateAmazonCognitoOpenIdTokenResult invoke(SORACOMKryptonClientConfig kryptonClientConfig,
			String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		GenerateAmazonCognitoOpenIdTokenResult result = client.generateAmazonCognitoOpenIdToken();
		return result;
	}

}
