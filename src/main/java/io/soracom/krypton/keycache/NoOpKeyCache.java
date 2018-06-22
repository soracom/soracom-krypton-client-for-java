package io.soracom.krypton.keycache;

import java.security.Key;

import io.soracom.krypton.KryptonClient.AuthResult;
import io.soracom.krypton.common.KryptonClientRuntimeException;

public class NoOpKeyCache implements KeyCache {

	@Override
	public AuthResult getAuthResultFromCache(String imsi) {
		return null;
	}

	@Override
	public boolean isStillValid(String alias) {
		return false;
	}

	@Override
	public void initKeyStore(String path) {

	}

	@Override
	public String[] listKeyAliases() {
		return null;
	}

	@Override
	public Key getKey(String alias) {
		return null;
	}

	@Override
	public void setKey(String alias, Key key) {

	}

	@Override
	public void unsetKey(String alias) {

	}

	@Override
	public void clear() {

	}

	@Override
	public void save() {

	}

	@Override
	public byte[] getKeyBytes(String alias) {
		return null;
	}

	@Override
	public void setKeyBytes(String alias, byte[] value) {
	}
}
