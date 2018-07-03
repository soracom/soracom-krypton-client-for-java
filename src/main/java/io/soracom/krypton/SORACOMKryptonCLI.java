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
import io.soracom.krypton.cli.BootstrapAwsIotThingOperationHandler;
import io.soracom.krypton.cli.BootstrapInventoryDeviceOperationHandler;
import io.soracom.krypton.cli.CLIOutput;
import io.soracom.krypton.cli.GenerateAmazonCognitoOpenIdTokenOperationHandler;
import io.soracom.krypton.cli.GenerateAmazonCognitoSessionCredentialsOperationHandler;
import io.soracom.krypton.cli.GetSubscriberMetadataOperationHandler;
import io.soracom.krypton.cli.GetUserdataOperationHandler;
import io.soracom.krypton.cli.KryptonOperationHandler;
import io.soracom.krypton.cli.OperationInfo;
import io.soracom.krypton.common.KryptonClientRuntimeException;

/**
 * Command line for SORACOM Krypton
 * 
 * @author c9katayama
 *
 */
public class SORACOMKryptonCLI {

	private static final List<KryptonOperationHandler<?>> kryptonOptionHanderList = new ArrayList<>();
	static {
		kryptonOptionHanderList.add(new GetSubscriberMetadataOperationHandler());
		kryptonOptionHanderList.add(new GetUserdataOperationHandler());
		kryptonOptionHanderList.add(new BootstrapInventoryDeviceOperationHandler());
		kryptonOptionHanderList.add(new GenerateAmazonCognitoSessionCredentialsOperationHandler());
		kryptonOptionHanderList.add(new GenerateAmazonCognitoOpenIdTokenOperationHandler());
		kryptonOptionHanderList.add(new BootstrapAwsIotThingOperationHandler());
	}

	public static class KryptonCLIOptions {
		public static final Option execOption;
		static {
			StringBuilder optionList = new StringBuilder();
			optionList.append("Set operation of provisioning API(required).\nFollowing operations are supported:\n");
			for (KryptonOperationHandler<?> provisioningApiHandler : kryptonOptionHanderList) {
				OperationInfo info = provisioningApiHandler.getOperationInfo();
				optionList.append("[ " + info.getOperation() + " ]: " + info.getDescription() + "\n");
			}
			optionList.append("(eg. -s getSubscriberMetadata )");
			execOption = Option.builder("o").longOpt("operation").hasArg().required().desc(optionList.toString())
					.valueSeparator().build();
		}

		public static final Option helpOption = Option.builder("h").longOpt("help")//
				.desc("Display this help message and stop").build();
		public static final Option provisioningApiEndpointUrlOption = Option.builder().hasArg()
				.longOpt("provisioning-api-endpoint-url")//
				.desc("Set provisioning api endpoint url. Default value is "
						+ ProvisioningApiEndpoint.getDefault().getApiEndpoint())
				.build();
		public static final Option requestParameterOption = Option.builder("p").longOpt("params").hasArg()//
				.desc("Set request parameter for invoking provisioning api.\n (eg. -p \"{'foo':'bar'}\" ").build();

		public static final Option debugOption = Option.builder().longOpt("debug").desc("Set debug mode on").build();
		public static final Option versionOption = Option.builder().longOpt("version").desc("Display version").build();
	}

	private static Options initOptions() {
		final Options options = new Options();
		// add provisioing APIs
		options.addOption(KryptonCLIOptions.execOption);
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

		formatter.printHelp("soracom-krypton -o [operation] -p [params]\n", "options:", options, "");
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

			if (line.hasOption(KryptonCLIOptions.execOption.getLongOpt())) {
				String execName = line.getOptionValue(KryptonCLIOptions.execOption.getLongOpt()).toLowerCase();
				for (KryptonOperationHandler<?> provisioningApiHandler : kryptonOptionHanderList) {
					OperationInfo info = provisioningApiHandler.getOperationInfo();
					if (info.getOperation().toLowerCase().equals(execName)) {
						Object result = provisioningApiHandler.invoke(kryptonClientConfig, paramJson);
						if (result == null) {
							System.exit(0);
						}
						if (result instanceof CLIOutput) {
							System.out.println(((CLIOutput) result).toCLIOutput());
						} else if (result instanceof String) {
							System.out.println(result);
						} else {
							System.out.println(Utilities.toJson(result));
						}
						System.exit(0);
					}
				}
				throw new KryptonClientRuntimeException(execName + " is not supported.");
			} else {
				throw new KryptonClientRuntimeException(KryptonCLIOptions.execOption.getOpt() + " option is required.");
			}
		} catch (Exception ex) {
			TextLog.error(ex.getMessage());
			System.exit(-1);
		}
	}
}
