package io.soracom.krypton.beans;

import java.util.List;

public class BootstrapArcResult {

	private String arcClientPeerPrivateKey;
	private String arcClientPeerIpAddress;
	private String arcServerPeerPublicKey;
	private String arcServerEndpoint;
	private List<String> arcAllowedIPs;

	public String getArcClientPeerPrivateKey() {
		return arcClientPeerPrivateKey;
	}

	public void setArcClientPeerPrivateKey(String arcClientPeerPrivateKey) {
		this.arcClientPeerPrivateKey = arcClientPeerPrivateKey;
	}

	public String getArcClientPeerIpAddress() {
		return arcClientPeerIpAddress;
	}

	public void setArcClientPeerIpAddress(String arcClientPeerIpAddress) {
		this.arcClientPeerIpAddress = arcClientPeerIpAddress;
	}

	public String getArcServerPeerPublicKey() {
		return arcServerPeerPublicKey;
	}

	public void setArcServerPeerPublicKey(String arcServerPeerPublicKey) {
		this.arcServerPeerPublicKey = arcServerPeerPublicKey;
	}

	public String getArcServerEndpoint() {
		return arcServerEndpoint;
	}

	public void setArcServerEndpoint(String arcServerEndpoint) {
		this.arcServerEndpoint = arcServerEndpoint;
	}

	public List<String> getArcAllowedIPs() {
		return arcAllowedIPs;
	}

	public void setArcAllowedIPs(List<String> arcAllowedIPs) {
		this.arcAllowedIPs = arcAllowedIPs;
	}
}
