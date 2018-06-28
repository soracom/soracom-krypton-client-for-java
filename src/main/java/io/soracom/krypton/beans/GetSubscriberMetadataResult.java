package io.soracom.krypton.beans;

import com.google.gson.JsonObject;

import io.soracom.endorse.utils.Utilities;
import io.soracom.krypton.cli.CLIOutput;

public class GetSubscriberMetadataResult implements CLIOutput {
	private JsonObject subscriber;

	@Override
	public String toCLIOutput() {
		return Utilities.toJson(subscriber);
	}

	public JsonObject getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(JsonObject subscriber) {
		this.subscriber = subscriber;
	}
}
