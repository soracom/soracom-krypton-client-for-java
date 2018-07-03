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
package io.soracom.krypton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.soracom.endorse.SORACOMEndorseCLI;
import io.soracom.endorse.SORACOMEndorseCLI.EndorseCLIOptions;
import io.soracom.endorse.SORACOMEndorseClientConfig;
import io.soracom.endorse.common.TextLog;
import io.soracom.endorse.utils.Utilities;
import io.soracom.krypton.cli.BootstrapAwsIotThingOption;
import io.soracom.krypton.cli.BootstrapInventoryDeviceOptionHandler;
import io.soracom.krypton.cli.CLIOutput;
import io.soracom.krypton.cli.GenerateAmazonCognitoOpenIdTokenOptionHandler;
import io.soracom.krypton.cli.GenerateAmazonCognitoSessionCredentialsOptionHandler;
import io.soracom.krypton.cli.GetSubscriberMetadataOptionHandler;
import io.soracom.krypton.cli.GetUserdataOptionHandler;
import io.soracom.krypton.cli.KryptonOptionHandler;
import io.soracom.krypton.common.KryptonClientRuntimeException;

public class SORACOMKryptonCLI {

	private static final List<KryptonOptionHandler<?>> kryptonOptionHanderList = new ArrayList<>();
	static {
		kryptonOptionHanderList.add(new BootstrapAwsIotThingOption());
		kryptonOptionHanderList.add(new BootstrapInventoryDeviceOptionHandler());
		kryptonOptionHanderList.add(new GenerateAmazonCognitoOpenIdTokenOptionHandler());
		kryptonOptionHanderList.add(new GenerateAmazonCognitoSessionCredentialsOptionHandler());
		kryptonOptionHanderList.add(new GetSubscriberMetadataOptionHandler());
		kryptonOptionHanderList.add(new GetUserdataOptionHandler());
	}

	public static class KryptonCLIOptions {
		public static final Option helpOption = Option.builder("h").longOpt("help")//
				.desc("Display this help message and stop").build();
		public static final Option provisioningApiEndpointUrlOption = Option.builder("pe").hasArg()
				.longOpt("provisioningApiEndpointUrl")//
				.desc("Set provisioning api endpoint url").build();
		public static final Option requestParameterOption = Option.builder("rp").longOpt("requestParameters").hasArg()//
				.desc("Set request parameter to invoke provisioning api.").build();

		public static final Option debugOption = Option.builder().longOpt("debug").desc("Set debug mode on").build();
		public static final Option versionOption = Option.builder().longOpt("version").desc("Display version").build();
	}

	private static Options initOptions() {
		final Options options = new Options();
		// add provisioing APIs
		for (KryptonOptionHandler<?> provisioningApiHandler : kryptonOptionHanderList) {
			options.addOption(provisioningApiHandler.getOption());
		}
		options.addOption(KryptonCLIOptions.requestParameterOption);
		options.addOption(EndorseCLIOptions.interfaceOption);
		options.addOption(EndorseCLIOptions.portNameOption);
		options.addOption(EndorseCLIOptions.baudRateOption);
		options.addOption(EndorseCLIOptions.dataBitOption);
		options.addOption(EndorseCLIOptions.stopBitOption);
		options.addOption(EndorseCLIOptions.parityBitOption);
		options.addOption(EndorseCLIOptions.modemManagerIndexOption);
		options.addOption(KryptonCLIOptions.provisioningApiEndpointUrlOption);
		options.addOption(EndorseCLIOptions.keysEndpointUrlOption);

		options.addOption(EndorseCLIOptions.listComPortsOption);
		options.addOption(EndorseCLIOptions.deviceInfoOption);

		options.addOption(EndorseCLIOptions.disableKeyCacheOption);
		options.addOption(EndorseCLIOptions.clearKeyCacheOption);

		options.addOption(KryptonCLIOptions.helpOption);
		options.addOption(KryptonCLIOptions.debugOption);
		options.addOption(KryptonCLIOptions.versionOption);
		return options;
	}

	private static void displayHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptionComparator(new Comparator<Option>() {
			public int compare(Option o1, Option o2) {
				return 0;
			}
		});
		formatter.setWidth(200);
		StringBuilder helpText = new StringBuilder();
		helpText.append("\r\n");
		helpText.append("To invoke generateAmazonCognitoSessionCredentials : \r\n");
		helpText.append("soracom-krypton --generateAmazonCognitoSessionCredentials \r\n");
		helpText.append("\r\n");
		helpText.append("To invoke execute bootstrapInventoryDevice using card reader:\r\n");
		helpText.append("soracom-krypton --bootstrapInventoryDevice -if iso7816 \r\n");
		helpText.append("\r\n");

		formatter.printHelp("soracom-krypton --provisioningAPIname [-if interface] [--help] [--debug]",
				helpText.toString(), options, "");
	}

	private static void displayVersion() {
		List<String> lines = Utilities.readResource("/soracom-krypton-version");
		for (String line : lines) {
			System.out.println(line);
		}
	}

	public static void main(String[] args) {
		Options options = initOptions();

		CommandLine line = null;
		try {
			CommandLineParser parser = new DefaultParser();
			line = parser.parse(options, args);
		} catch (ParseException exp) {
			displayHelp(options);
			System.exit(-1);
		}
		if (line.hasOption(EndorseCLIOptions.helpOption.getLongOpt())) {
			displayHelp(options);
			System.exit(0);
		}
		if (line.hasOption(EndorseCLIOptions.versionOption.getLongOpt())) {
			displayVersion();
			System.exit(0);
		}

		SORACOMEndorseClientConfig clientConfig = SORACOMEndorseCLI.createSORACOMEndorseClientConfig(line);
		SORACOMKryptonClientConfig kryptonClientConfig = new SORACOMKryptonClientConfig();

		if (line.hasOption(KryptonCLIOptions.debugOption.getLongOpt())) {
			clientConfig.setDebug(true);
			kryptonClientConfig.setDebug(true);
		}

		kryptonClientConfig.setEndorseClientConfig(clientConfig);

		if (line.hasOption(KryptonCLIOptions.provisioningApiEndpointUrlOption.getLongOpt())) {
			String apiEndpointUrl = line
					.getOptionValue(KryptonCLIOptions.provisioningApiEndpointUrlOption.getLongOpt());
			kryptonClientConfig.setApiEndpointUrl(apiEndpointUrl);
		}

		try {
			String paramJson = null;
			if (line.hasOption(KryptonCLIOptions.requestParameterOption.getLongOpt())) {
				paramJson = line.getOptionValue(KryptonCLIOptions.requestParameterOption.getLongOpt());
			}
			for (KryptonOptionHandler provisioningApiHandler : kryptonOptionHanderList) {
				Option supportedOption = provisioningApiHandler.getOption();
				if (line.hasOption(supportedOption.getLongOpt())) {
					Object result = provisioningApiHandler.invoke(kryptonClientConfig, paramJson);
					if (result == null) {
						throw new KryptonClientRuntimeException("server response is empty.");
					}
					if (result instanceof CLIOutput) {
						System.out.println(((CLIOutput) result).toCLIOutput());
					} else {
						System.out.println(Utilities.toJson(result));
					}
					System.exit(0);
				}
			}
			displayHelp(options);
			System.exit(0);
		} catch (Exception ex) {
			TextLog.error(ex.getMessage());
			System.exit(-1);
		}
	}
}
