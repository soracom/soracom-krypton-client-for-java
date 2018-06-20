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

import io.soracom.krypton.interfaces.UiccInterfaceType;

/**
 * Configuration parameters for KryptonClient class
 * 
 * @author c9katayama
 *
 */
public class KryptonClientConfig {

	private String keyAgreementUrl = "https://key-manager-g-soracom.dyn.soracom.com/v1/keys";
	private String keyDistributionUrl = null;
	private String requestParameters = null;
	private UiccInterfaceType uiccInterfaceType = UiccInterfaceType.autoDetect;
	private int keyLength = 16;
	private String keyAlgorithm = "SHA-256";
	private boolean debug = false;
	private boolean applicationKey = false;
	private boolean clearCache;

	public static class CommunicationDeviceConfig {

		private String portName;
		private Integer baudRate;
		private Integer dataBits;
		private Integer stopBits;
		private Integer parity;
		private String modemIndex;

		public String getPortName() {
			return portName;
		}

		public void setPortName(String portName) {
			this.portName = portName;
		}

		public Integer getBaudRate() {
			return baudRate;
		}

		public void setBaudRate(Integer baudRate) {
			this.baudRate = baudRate;
		}

		public Integer getDataBits() {
			return dataBits;
		}

		public void setDataBits(Integer dataBits) {
			this.dataBits = dataBits;
		}

		public Integer getStopBits() {
			return stopBits;
		}

		public void setStopBits(Integer stopBits) {
			this.stopBits = stopBits;
		}

		public Integer getParity() {
			return parity;
		}

		public void setParity(Integer parity) {
			this.parity = parity;
		}

		public String getModemIndex() {
			return modemIndex;
		}

		public void setModemIndex(String modemIndex) {
			this.modemIndex = modemIndex;
		}
	}

	private CommunicationDeviceConfig communicationDeviceConfig;

	public String getKeyAgreementUrl() {
		return keyAgreementUrl;
	}

	public void setKeyAgreementUrl(String keyAgreementUrl) {
		this.keyAgreementUrl = keyAgreementUrl;
	}

	public String getKeyDistributionUrl() {
		return keyDistributionUrl;
	}

	public void setKeyDistributionUrl(String keyDistributionUrl) {
		this.keyDistributionUrl = keyDistributionUrl;
	}

	public String getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(String requestParameters) {
		this.requestParameters = requestParameters;
	}

	public UiccInterfaceType getUiccInterfaceType() {
		return uiccInterfaceType;
	}

	public void setUiccInterfaceType(UiccInterfaceType uiccInterfaceType) {
		this.uiccInterfaceType = uiccInterfaceType;
	}

	public int getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}

	public String getKeyAlgorithm() {
		return keyAlgorithm;
	}

	public void setKeyAlgorithm(String keyAlgorithm) {
		this.keyAlgorithm = keyAlgorithm;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isApplicationKey() {
		return applicationKey;
	}

	public void setApplicationKey(boolean applicationKey) {
		this.applicationKey = applicationKey;
	}
	
	public void setClearCache(boolean clearCache) {
		this.clearCache = clearCache;
	}
	
	public boolean isClearCache() {
		return clearCache;
	}

	public CommunicationDeviceConfig getCommunicationDeviceConfig() {
		return communicationDeviceConfig;
	}

	public void setCommunicationDeviceConfig(CommunicationDeviceConfig communicationDeviceConfig) {
		this.communicationDeviceConfig = communicationDeviceConfig;
	}
}
