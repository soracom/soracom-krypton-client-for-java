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

		SORACOMKryptonCredentialsProvider credentialProvider = SORACOMKryptonCredentialsProvider.build();
		AmazonS3 s3Client = new AmazonS3Client(credentialProvider);

		String s3Bucket = credentialProvider.getSoracomKryptonClient().getUserdata();

		ObjectListing listObjects = s3Client.listObjects(s3Bucket);
		List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
		for (S3ObjectSummary s : objectSummaries) {
			System.out.println(s.getKey());
		}
	}
}
