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
package io.soracom.krypton.keycache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import javax.crypto.spec.SecretKeySpec;

import io.soracom.krypton.KryptonClient.AuthResult;
import io.soracom.krypton.common.TextLog;

/**
 * A class to manage the Java Cryptography Extension KeyStore with concept of validity for the keys
 * @author olivier.comarmond
 *
 */
public class JCEKeyCache implements KeyCache{
	private char[] protection;
	private String path;
	private static final String defaultAlgo = "AES";
	private KeyStore store;
	private long validity=3600000L; //milliseconds
	
	
	public JCEKeyCache(String path){
		initKeyStore(path);
	}
	
	public AuthResult getAuthResultFromCache(String imsi) {
		//Verify if cached key exist
		for (String alias:listKeyAliases()){
			if (isStillValid(alias)){
				if (alias.startsWith(imsi)){
					String[] aliasParts = alias.split("_");//Used since introduction of a composite alias
					if (aliasParts.length>1){
						AuthResult authResult = new AuthResult();
						authResult.keyId = aliasParts[1]; //Key ID is second part
						authResult.ck=getKeyBytes(alias);
						TextLog.log("retrieve keyId and ck from key cache. keyId=" + authResult.keyId);
						return authResult;
					}
					else{
						unsetKey(alias);
					}
				}
				else{
					unsetKey(alias);
				}
			}
			else{
				unsetKey(alias);
			}
		}
		return null;
	}
	
	public boolean isStillValid(String alias){
		try {
			Date created = store.getCreationDate(alias);
			Date expiry = new Date( created.getTime() + validity);
			Date now = new Date(System.currentTimeMillis());
			return now.before(expiry);
			
		} catch (KeyStoreException e) {
			return false;
		}
	}
	
	public void initKeyStore(String path) {
		//load key store key from environment 
		String keyStoreKey = System.getenv(ENV_NAME_KRYPTON_KEY_STORE_KEY);
		if(keyStoreKey != null) {
			TextLog.debug("set key store encryption key from env");
		}else {
			keyStoreKey = System.getProperty(ENV_NAME_KRYPTON_KEY_STORE_KEY);
			if(keyStoreKey != null) {
				TextLog.debug("set key store encryption key from system property");
			}else {
				keyStoreKey = "!_S0r4C0m_&";
				TextLog.debug("set default key store encryption key");
			}
		}
		protection = keyStoreKey.toCharArray();
		try{
			this.path = path;
		    File file = new File(path);
		    this.store = KeyStore.getInstance("JCEKS");
		    if (file.exists()) {
		        // if exists, load
		    	this.store.load(new FileInputStream(file), protection);
		    } else {
		        // if not exists, create
		    	this.store.load(null, null);
		    	this.store.store(new FileOutputStream(file), protection);
		    }
		}
		catch (Exception ex){

		}
	}
	
	public String[] listKeyAliases(){
		ArrayList<String> retVal = new ArrayList<String>();
		if (store!=null)
		{
			Enumeration<String> enumeration;
			try {
				enumeration = store.aliases();
				while(enumeration.hasMoreElements()) {
					retVal.add( (String)enumeration.nextElement() );
				}
			} catch (KeyStoreException e) {
				
			}
			
		}
		return retVal.toArray(new String[retVal.size()]);
	}
	
	public Key getKey(String alias){
		try{
			
			return (store==null)?null:store.getKey(alias, protection);
			
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	public void setKey(String alias, Key key){
		try{
			if (store!=null){
				store.setKeyEntry(alias,key,protection,null);
			}
			
		}
		catch (Exception ex)
		{
			return ;
		}
	}
	
	
	public void unsetKey(String alias){
		try{
			if (store!=null){
				store.deleteEntry(alias);
			}
			
		}
		catch (Exception ex)
		{
			return ;
		}
	}
	
	public void clear(){
		for (String alias:listKeyAliases()){
			unsetKey(alias);
		}
	}
	
	public void save(){
		try{
		    File file = new File(path);
		    this.store.store(new FileOutputStream(file), protection);
		}
		catch (Exception ex){

		}
	}
	public byte[] getKeyBytes(String alias){
		try{
			
			return (store==null)?null:store.getKey(alias, protection).getEncoded();
			
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	public void setKeyBytes(String alias, byte[] value){
		try{
			if (store!=null){
				Key key = new SecretKeySpec(value, defaultAlgo);
				store.setKeyEntry(alias,key,protection,null);
			}
			
		}
		catch (Exception ex)
		{
			return ;
		}
	}
	
	
}
