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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.soracom.krypton.beans.AppkeyBean;
import io.soracom.krypton.beans.KeyRequestBean;
import io.soracom.krypton.beans.MilenageParamsBean;
import io.soracom.krypton.beans.NonceBean;
import io.soracom.krypton.beans.SessionDataBean;
import io.soracom.krypton.beans.TokenBean;
import io.soracom.krypton.beans.XresBean;
import io.soracom.krypton.common.HttpResponse;
import io.soracom.krypton.common.ITextLogListener;
import io.soracom.krypton.common.TextLog;
import io.soracom.krypton.common.TextLogItem;
import io.soracom.krypton.utils.Utilities; 

public class KryptonAPI {
	
	private static boolean debug;
	private static TextLog log = new TextLog();
	
	/**
	 * Register a client to receive internal log messages
	 * @param listener - an ITextLogListener interface
	 */
	public static void registerLogListener(ITextLogListener listener){
		log.addListener(listener);
	}
	
	/**
	 * Deregister a client to receive internal log messages
	 * @param listener - an ITextLogListener interface
	 */
	public void deregisterLogListener(ITextLogListener listener) {
        log.removeListener(listener);
    }
	
	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		KryptonAPI.debug = debug;
	}

	/**
	 * Internal function to handle HTTP Post to the selected URL
	 * @param url - full URL uncluding http:// or https://
	 * @param postParameters - Parameters in JSON format
	 * @return The full http response object
	 * @throws Exception
	 */
	private static HttpResponse sendPost(String url,String postParameters) throws Exception {
		HttpResponse retVal = new HttpResponse(url);
		try
		{
			URL obj = new URL(url);

			HttpURLConnection con = (url.toLowerCase().startsWith("https"))?((HttpsURLConnection) obj.openConnection()):((HttpURLConnection) obj.openConnection());
			
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-type", "application/json;  charset=utf-8");
			// Send post request
			con.setDoOutput(true);
			if (postParameters!=null){
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(postParameters);
				wr.flush();
				wr.close();
			}
			retVal.setCode(con.getResponseCode());
		
		
			BufferedReader in = new BufferedReader(
			        new InputStreamReader((retVal.getCode()<400)?con.getInputStream():con.getErrorStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
		
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			retVal.setContents(response.toString());

		}
		catch (Exception Ex)
		{
			retVal.setError(Ex.getMessage());
		}
		return retVal;
	}
		
	// HTTP POST request
	/**
	 * Internal function to handle HTTP Post to the key distribution API
	 * @param url - full URL uncluding http:// or https://
	 * @param keyRequestBean - The object containing the necessary input parameters
	 * @param signature - The calculated signature
	 * @return The full http response object
	 * @throws Exception
	 */
	public static HttpResponse postKeyRequest(String url, String body, String timestamp, String algorithm, String signature) throws Exception {
		
		HttpResponse retVal = new HttpResponse(url);
		try
		{
			URL obj = new URL(url);

			HttpURLConnection con = (url.toLowerCase().startsWith("https"))?((HttpsURLConnection) obj.openConnection()):((HttpURLConnection) obj.openConnection());
			
			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Content-type", "application/json;  charset=utf-8");
			con.setRequestProperty("x-soracom-timestamp", timestamp);
			con.setRequestProperty("x-soracom-digest-algorithm", algorithm);
			con.setRequestProperty("x-soracom-signature", signature);
			// Send post request
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(body);
			wr.flush();
			wr.close();
			
			retVal.setCode(con.getResponseCode());
		
		
			BufferedReader in = new BufferedReader(
			        new InputStreamReader((retVal.getCode()<400)?con.getInputStream():con.getErrorStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
		
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			retVal.setContents(response.toString());

		}
		catch (Exception Ex)
		{
			retVal.setError(Ex.getMessage());
		}
		return retVal;
	}

	/**
	 * Create master key - This is the first API call in the key agreement service API message sequence
	 * @param url - The URL to send the inital API call
	 * @param imsi - The IMSI retrieved from the SIM
	 * @return - The input parameters required to run the authenticate algorithm on the SIM
	 */
	public static MilenageParamsBean initKeyAgreement(String url, String imsi){
		
		return initKeyAgreement(url, imsi, null, null);
	}
	
	/**
	 * Create master key with rand and auts parameters too (use in case of resync) 
	 * @param url - The URL to send the inital API call
	 * @param imsi - The IMSI retrieved from the SIM
	 * @return - The input parameters required to re-run the authenticate algorithm on the SIM
	 */
	public static MilenageParamsBean initKeyAgreement(String url, String imsi, String rand, String auts){
		
		try{
			MilenageParamsBean retVal = new MilenageParamsBean();
			SessionDataBean content = new SessionDataBean();
			if (imsi!=null){
				content.setImsi(imsi);	
			}
			if (rand!=null){
				content.setRand(rand);
			}
			if (auts!=null){
				content.setAuts(auts);
			}
			if (debug) {
				System.out.println("invoke KeyAgreement. params="+content.toJson());
			}
			HttpResponse response = sendPost(url, content.toJson());
			if ((response.getCode()==200 || response.getCode()==401) && response.getContents()!=null){
				retVal = MilenageParamsBean.fromJson(response.getContents());
			}
			else
			{
				if (debug)
				{
					logError("While calling key agreement URL "+url+", received http response: "+ Integer.toString(response.getCode()));
					if (response.getContents()!=null && !response.getContents().isEmpty()){
						logError("Body: "+ response.getContents());	
					}
					
				}
			}
			return retVal;
		}
		catch (Exception ex){
			logError(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Verify master key -  This is the second API call in the key agreement service API message sequence to validate the master key before use
	 * @param url - The URL to send the verify call(The url must contain the key Id returned in first step eg: keyAgreementUrl+"/"+keyId+"/verify")
	 * @param xres - the signed response
	 * @return - True if the master key is correctly verified, false otherwise
	 */
	public static boolean verifyMasterKey(String url, String xres){
		
		try{
			XresBean content = new XresBean();
			content.setXres(xres);
			HttpResponse response = sendPost(url, content.toJson());
			if (response.getCode()==200 ){
				return true;
			}
			else
			{
				if (debug)
				{
					logError("While calling verify URL "+url+", received http response: "+ Integer.toString(response.getCode()));
					if (response.getContents()!=null && !response.getContents().isEmpty()){
						logError("Body: "+ response.getContents());	
					}
					
				}
			}
		}
		catch (Exception ex){
			logError (ex.getMessage());
		}
		return false;
	}
	
	/**
	 * @deprecated  As of release 0.0.3, replaced by requestService
	 * Third step - Generate the application key
	 * @param url - The key Id returned in first step
	 * @param nonce - the challenge
	 * @return - the application key 
	 */
	@Deprecated public static AppkeyBean generateAppKey(String url, String nonce){
		
		try{
			AppkeyBean retVal = new AppkeyBean();
			NonceBean content = new NonceBean();
			content.setNonce(nonce);
			HttpResponse response = sendPost(url, content.toJson());
			if (response.getCode()==200 && response.getContents()!=null){
				retVal = AppkeyBean.fromJson(response.getContents());
			}
			return retVal;
		}
		catch (Exception ex){
			logError (ex.getMessage());
			return null;
		}
	}
	
	/**
	 * @deprecated  As of release 0.0.3, replaced by requestService
	 * Last step - Get Token
	 * @param url - The url to post the request to
	 * @param nonce - the challenge
	 * @return - The JWT Token
	 */
	@Deprecated public static TokenBean getToken(String url, String nonce){
		
		try{
			TokenBean retVal = new TokenBean();
			NonceBean content = new NonceBean();
			content.setNonce(nonce);
			HttpResponse response = sendPost(url, content.toJson());
			if (response.getCode()==200 && response.getContents()!=null){
				retVal = TokenBean.fromJson(response.getContents());
			}
			return retVal;
		}
		catch (Exception ex){
			logError (ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Internal Error Local logging function - logs to internal TextLog object
	 * Register (registerLogListener) to the log for client to receive the messages
	 * @param line
	 */
	
	private static void logError(String line){
    	log.add(new TextLogItem(TextLogItem.TextLogItemType.ERR, line));
    }
	/**
	 * Method used to calculate the signature to append to key request messages
	 * @param message - body contents of the http request
	 * @param timestamp - unix timestamp in milliseconds
	 * @param secretKey - the criptographic key used (typically ck)
	 * @param algorithm - the hashing algorithm to use (eg:  
	 * @return - The Signature to append to key request messages
	 * @throws NoSuchAlgorithmException
	 */
	public static String calculateSignature(String message, long timestamp, byte[] secretKey, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(message.getBytes(StandardCharsets.UTF_8));
        digest.update(Long.toString(timestamp).getBytes(StandardCharsets.UTF_8));
        digest.update(secretKey);
        byte[] hash = digest.digest();
        return Utilities.bytesToBase64(hash);
    }
	
	/**
	 * Method used to calculate the shared ApplicationKey 
	 * @param nonce - the challenge
	 * @param timestamp - unix timestamp in milliseconds
	 * @param secretKey - the criptographic key used (typically ck)
	 * @param keyLength - The required length of key to be generated
	 * @param algorithm - Algorithm to use eg: SHA-256
	 * @return The shared key for further communications
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] calculateApplicationKey(byte[] nonce, long timestamp, byte[] secretKey, int keyLength, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(nonce);
        digest.update(Long.toString(timestamp).getBytes(StandardCharsets.UTF_8));
        digest.update(secretKey);
        byte[] encodedhash = digest.digest();
        return Utilities.arraySplice(encodedhash, 0, keyLength);
    }
	
	/**
	 * Merge two json strings. Merge "source" into "target". If fields have equal name, merge them recursively. 
	 * @return the merged object (target). 
	 */ 
	private static JsonObject jsonMerge(JsonObject source, JsonObject target) { 
	    for (String key: source.keySet()) { 
            JsonElement value = source.get(key); 
            if (!target.has(key)) { 
                // new value for "key": 
                target.add(key, value); 
            }
	    } 
	    return target; 
	} 
	
	/**
	 * Communicate with key distribution service - This is the third call in the key agreement service API message sequence
	 * @param url - The URL to which the call should be posted (eg: keyAgreementUrl+"/"+keyId+"/generate_app_key")  
	 * @param ck - The Cryptographic Key return from Milenage authentication run on the SIM
	 * @param nonce - the challenge
	 * @param timestamp - The current Timestamp in miliseconds
	 * @param keyId - The Key reference ID on server
	 * @param keyLength - The required length of key to be generated
	 * @param algorithm - Algorithm to use eg: SHA-256
	 * @param jsonParameters - Any additional parameters to include in the body
	 * 
	 * @return The result of the API call (HTTP body content)
	 */
	public static String requestService( String url, byte[] ck,  byte[] nonce, long timestamp, String keyId, int keyLength,  String algorithm, String jsonParameters){
		
		try{
			String retVal = "";
			KeyRequestBean content = new KeyRequestBean();
			content.setNonce(Utilities.bytesToBase64(nonce));
			content.setKeyId(keyId);
			content.setLength(keyLength);
			content.setTimestamp(Long.toString(timestamp));
			content.setAlgorithm(algorithm);
			
			//Merge json objects
			String body="";
			if (jsonParameters==null){
				body=content.toJson();
			}
			else
			{ 
				Gson gson = new Gson();
				JsonObject contentJson = gson.fromJson(content.toJson(), JsonObject.class);
				JsonObject paramJson = gson.fromJson(jsonParameters, JsonObject.class);
				JsonObject jsonResult = jsonMerge(contentJson,paramJson);
				body = gson.toJson(jsonResult);
			}
 					
			String sig = calculateSignature(body, timestamp, ck, algorithm);
			HttpResponse response = postKeyRequest(url, body, content.getTimestamp(), content.getAlgorithm(), sig);
			if (response.getCode()==200 && response.getContents()!=null){
				retVal = response.getContents();
			}
			else
			{
				if (debug)
				{
					logError("While calling key distribution service URL "+url+", received http response: "+ Integer.toString(response.getCode()));
					if (response.getContents()!=null && !response.getContents().isEmpty()){
						logError("Body: "+ response.getContents());	
					}
					
				}
			}
			return retVal;
		}
		catch (Exception ex){
			logError (ex.getMessage());
			return null;
		}
	}

	
	
	
}
