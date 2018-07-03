package io.soracom.krypton.aws_sdk;

import java.util.List;

import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class SORACOMKryptonCredentialsProviderTest {

	@Test
	public void testS3Access() throws Exception {

		SORACOMKryptonCredentialsProvider credentialProvider = new SORACOMKryptonCredentialsProvider();
		AmazonS3 s3 = new AmazonS3Client(credentialProvider);

		ObjectListing listObjects = s3.listObjects("krypton-cognito-demo");
		List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
		for (S3ObjectSummary s : objectSummaries) {
			System.out.println(s.getKey());
		}
	}
}
