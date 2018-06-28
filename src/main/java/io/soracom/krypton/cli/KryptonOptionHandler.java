package io.soracom.krypton.cli;

import org.apache.commons.cli.Option;

import io.soracom.krypton.SORACOMKryptonClientConfig;

public interface KryptonOptionHandler<T> {

	Option getOption();

	T invoke(SORACOMKryptonClientConfig kryptonClientConfig,String requestParamJson);
}
