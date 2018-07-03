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
package io.soracom.krypton.aws_sdk;

import java.util.Date;

import com.amazonaws.auth.STSSessionCredentialsProvider;
import com.amazonaws.services.securitytoken.AbstractAWSSecurityTokenService;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;

import io.soracom.krypton.SORACOMKryptonClient;
import io.soracom.krypton.beans.GenerateAmazonCognitoSessionCredentialsResult;

/**
 * Credentials provider for AWS SDK for Java.
 * 
 * @author c9katayama
 *
 */
public class SORACOMKryptonCredentialsProvider extends STSSessionCredentialsProvider {
	private SORACOMKryptonClient soracomKryptonClient;

	public SORACOMKryptonCredentialsProvider() {
		this(new SORACOMKryptonClient());
	}

	public SORACOMKryptonCredentialsProvider(SORACOMKryptonClient client) {
		super(new KryptonAWSSecurityTokenService(client));
		this.soracomKryptonClient = client;
	}

	public static SORACOMKryptonCredentialsProvider build() {
		return new SORACOMKryptonCredentialsProvider();
	}

	public static SORACOMKryptonCredentialsProvider build(SORACOMKryptonClient client) {
		return new SORACOMKryptonCredentialsProvider(client);
	}

	public SORACOMKryptonClient getSoracomKryptonClient() {
		return soracomKryptonClient;
	}

	private static class KryptonAWSSecurityTokenService extends AbstractAWSSecurityTokenService {
		private SORACOMKryptonClient client;

		private KryptonAWSSecurityTokenService(SORACOMKryptonClient client) {
			this.client = client;
		}

		@Override
		public GetSessionTokenResult getSessionToken(GetSessionTokenRequest request) {
			GenerateAmazonCognitoSessionCredentialsResult generateResult = client
					.generateAmazonCognitoSessionCredentials();
			GetSessionTokenResult result = new GetSessionTokenResult();
			Credentials credentials = new Credentials();
			credentials.setAccessKeyId(generateResult.getCredentials().getAccessKeyId());
			credentials.setSecretAccessKey(generateResult.getCredentials().getSecretKey());
			credentials.setSessionToken(generateResult.getCredentials().getSessionToken());
			credentials.setExpiration(new Date(generateResult.getCredentials().getExpiration()));
			result.setCredentials(credentials);
			return result;
		}
	}
}
