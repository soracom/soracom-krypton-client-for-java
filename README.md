# soracom-krypton-client-for-java
Java based client library for SORACOM Krypton 

## How to build SORACOM Krypton client for Java
User can use Gradle to build the project. If you want to build the project from source code, execute following command after checkout.
 
```sh
./gradlew build
```

You can generate configuration file for IDEs with following command.

```sh
// for Eclipse
./gradlew eclipse

// for IntelliJ
./gradrew idea
```

After running this command, you can import this project to your IDE.

Also you can generate application zip archive with following command.

```sh
./gradlew dist
```

after that, you can see following archives in build directory.

- distributions/soracom-krypton-cli-xxx.zip( and tar)
These archives are application archive that contains "soracom-krypton" command.

- libs/soracom-krypton.jar
Fat jar type of application archive. You can run krypton cli with "java -jar soracom-krypton.jar" command.

- libs/soracom-krypton-client-for-java-xxxx.jar
Krypton client library to provide krypton api to other application. Also you can use this library with following gradle setting.

```java
apply plugin: 'java'

repositories {
  jcenter()
  maven { url 'https://soracom.github.io/maven-repository/' }
}

dependencies {
    compile "io.soracom:soracom-krypton-client-for-java-core:0.1.0"
}
```

## For AWS SDK for Java
This library includes CredentialsProvider class for AWS SDK. This class enables you to retrieve credential by provisioning API for Amazon Cognito.
Here is a sample code how to use the class.

```java
public class SORACOMKryptonCredentialsProviderTest {

	public static void main(String[] args)throws Exception {
		//create Krypton credentials provider with default parameter 
		SORACOMKryptonCredentialsProvider credentialProvider = SORACOMKryptonCredentialsProvider.build();
		//create S3 client with the credentials provider
		//Temporary AWS credentials is retrieved from Amazon Cognito through Krypton API with SIM(ASA) authentication
		AmazonS3 s3Client = new AmazonS3Client(credentialProvider);
		//Also you can retrieve configuration from userdata that is set to SIM group.
		String s3Bucket = credentialProvider.getSoracomKryptonClient().getUserdata();

		ObjectListing listObjects = s3Client.listObjects(s3Bucket);
		List<S3ObjectSummary> objectSummaries = listObjects.getObjectSummaries();
		for (S3ObjectSummary s : objectSummaries) {
			System.out.println(s.getKey());
		}
	}
}
```

The soracom-krypton-client-for-java is released under version 2.0 of Apache License