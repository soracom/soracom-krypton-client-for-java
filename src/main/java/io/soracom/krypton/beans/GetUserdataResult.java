package io.soracom.krypton.beans;

import io.soracom.krypton.cli.CLIOutput;

public class GetUserdataResult implements CLIOutput {
	private String userData;

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	@Override
	public String toCLIOutput() {
		return userData;
	}
}
