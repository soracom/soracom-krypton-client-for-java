package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.SORACOMKryptonClientConfig;
import io.soracom.krypton.beans.GenerateAmazonCognitoSessionCredentialsResult;

public class GenerateAmazonCognitoSessionCredentialsOptionHandler
		implements KryptonOptionHandler<GenerateAmazonCognitoSessionCredentialsResult> {

	@Override
	public Option getOption() {
		return Option.builder().longOpt("generateAmazonCognitoSessionCredentials")//
				.desc("Generate AWS temporary session token by Amazon Cognito").build();
	}

	@Override
	public GenerateAmazonCognitoSessionCredentialsResult invoke(SORACOMKryptonClientConfig kryptonClientConfig,
			String requestParamJson) {
		SORACOMKryptonClient client = new SORACOMKryptonClient(kryptonClientConfig);
		GenerateAmazonCognitoSessionCredentialsResult result = client.generateAmazonCognitoSessionCredentials();
		return result;
	}

}
