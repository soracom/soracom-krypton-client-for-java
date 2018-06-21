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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.soracom.krypton.KryptonClientConfig.CommunicationDeviceConfig;
import io.soracom.krypton.beans.AppkeyBean;
import io.soracom.krypton.beans.KeyDistributionBean;
import io.soracom.krypton.beans.MilenageParamsBean;
import io.soracom.krypton.common.AuthenticationResponse;
import io.soracom.krypton.common.AuthenticationResponse.ResultState;
import io.soracom.krypton.common.ITextLogListener;
import io.soracom.krypton.common.TextLogItem;
import io.soracom.krypton.interfaces.AutoDetectManager;
import io.soracom.krypton.interfaces.CommManager;
import io.soracom.krypton.interfaces.IUiccInterface;
import io.soracom.krypton.interfaces.Iso7816Manager;
import io.soracom.krypton.interfaces.MmcliManager;
import io.soracom.krypton.interfaces.UiccInterfaceType;
import io.soracom.krypton.utils.Utilities;

public class KryptonClient implements ITextLogListener  {

	private static KeyCache keyCache = new KeyCache(System.getProperty("user.home")+ File.separator + ".kcache");
	
	public enum RunLevel {
		NORMAL, LIST_COM_PORTS, DEVICE_INFO;
	}

	private static void displayHelp(){
		
		//System.out.println(getFile("help.txt"));
		StringBuilder helpText = new StringBuilder();
		helpText.append("Soracom Krypton Client - Version 0.3.1\r\n");

		helpText.append("\r\n");
		helpText.append("USAGE: \r\n");
		helpText.append("krypton [--help | --listComPorts | --deviceInfo] [-i interface]  \r\n");
		helpText.append("The other parameters depend on command issued \r\n");
		helpText.append("\r\n");
		helpText.append("EXAMPLES: \r\n");
		helpText.append("To generate application key using a card reader: \r\n");
		helpText.append("	endorse -i iso7816 -kl 16 -ka SHA-256 \r\n");
		
		helpText.append("To generate application token using endorse key distribution service: \r\n");
		helpText.append("	endorse -i iso7816 -sp https://keyurl.soracom.io/keyservice/{keyid}/endorse \r\n");
		
		helpText.append("To generate application key using modem:\r\n");
		helpText.append("	endorse -i comm -c /dev/tty1 \r\n");
		
		helpText.append("To use modem manager on some linux distros:\r\n");
		helpText.append("	endorse -i mmcli -m 0 \r\n");
		
		helpText.append("\r\n");
		helpText.append("  -sp				Use external service to generate an application key\r\n");
		helpText.append("  				   	Eg: -sp https://keyurl.soracom.io/keyservice/{keyid}/\r\n");
		helpText.append("  -au		   		Override the default authentication URL API with this switch\r\n");
		helpText.append("  				   	Eg: -au=https://keyurl.soracom.io/keyservice/\r\n");
		helpText.append("  -rp				Pass additional json parameters to the to the service request \r\n");
		helpText.append("  				   	Eg: -rp  \"{\\\"thingName\\\":\\\"living Light\\\",\\\"policyName\\\":\\\"myRoom Policy\\\"}\"\r\n");
		helpText.append("  -kl		   		Length of key to generate in bytes (default is 16) \r\n");
		helpText.append("  -ka		   		Algorithm to use for key generation (default is SHA-256) \r\n");
		helpText.append("  -i			   	UICC Interface to use. Valid values are iso7816, comm, mmcli or autoDetect \r\n");
		helpText.append("  -c			   	Port name of communication device (eg -c COM1 or -c /dev/tty1)\r\n");
		helpText.append("  -b			   	Baud rate for communication device (eg -b 57600)\r\n");
		helpText.append("  -d			   	Data bits for communication device (eg -d 8)\r\n");
		helpText.append("  -s			   	Stop bits for communication device (eg -s 1)\r\n");
		helpText.append("  -p			   	Parity bits for communication device (eg -p 0)\r\n");
		helpText.append("  -m              	Modem manager index if mmcli flag is set (eg -m 0)\r\n");
		helpText.append("  --listComPorts  	List All available Communication devices and exit\r\n");
		helpText.append("  --deviceInfo    	Query the Communication device and print the information\r\n");
		helpText.append("  --endorse	   	Set the key URL to consume Soracom Endorse service (to get JWT token)\r\n");
		helpText.append("  --applicationKey	Output applicationKey\r\n");
		helpText.append("  --clearCache	   	Clear key cache\r\n");
		helpText.append("  --debug		   	Set debug mode on\r\n");
		helpText.append("  --help          	Display this help message and stop\r\n");
		helpText.append("\r\n");
		log(helpText.toString());
	}
	
	protected void start(RunLevel runlevel, KryptonClientConfig kryptonClientConfig){
		switch (runlevel)
    	{
        	case NORMAL: //Normal operation Generate App key
        		try {
        			if (kryptonClientConfig.getKeyDistributionUrl()==null || kryptonClientConfig.getKeyDistributionUrl().isEmpty()){ //Generate Application key use case
        				AppkeyBean result = generateApplicationKey(kryptonClientConfig);
        				log(result.toJson());
        			}else {
        				KeyDistributionBean result = invokeKeyDistributionService(kryptonClientConfig);
        				log(result.toJson());        				
        			}
        		}catch(Exception e) {
        			logError(e.getMessage());
        		}
        		break;
        	case LIST_COM_PORTS://Just list comm ports and exit
        		List<String> ports = listComPorts(kryptonClientConfig);
	        	if (ports.size() == 0){
	        		log("No serial ports detected!");
	        	}
	        	for (String port:ports){
	        		log(port);
	        	}
	        	break;
        	case DEVICE_INFO://Connect to device and query manufacturer info
        		log(getDeviceInfo(kryptonClientConfig));
        		break;
        	default:
        		throw new IllegalArgumentException("Unsupported runlevel. runlevel="+runlevel.toString());
        
    	}		
	}

	public AppkeyBean generateApplicationKey(KryptonClientConfig kryptonClientConfig) throws Exception{
		AuthResult authResult = doAuthentication(kryptonClientConfig);
		long currentTimeStamp = System.currentTimeMillis();
		
		//Execute Key Distribution Service Call or Generate Application key by default
		//Calculate Application key
		byte[] appKey = KryptonAPI.calculateApplicationKey(authResult.nonce,currentTimeStamp, authResult.ck, kryptonClientConfig.getKeyLength(), kryptonClientConfig.getKeyAlgorithm());
		AppkeyBean appKeyBean = new AppkeyBean();
		appKeyBean.setApplicationKey(Utilities.bytesToBase64(appKey));
		return appKeyBean;
		
		//Request application key
		//DEBUG PURPOSES - REMOVE ON PROD 
		/*
		String appKeyContents = KryptonAPI.requestService( keyAgreementUrl+"/"+keyId+"/generate_app_key", ck, nonce,currentTimeStamp, keyLength, keyAlgorithm); 
		if (appKeyContents!=null){
			//log(appKeyContents);
			AppkeyBean servAppKeyBean = AppkeyBean.fromJson(appKeyContents);
			if (servAppKeyBean!=null && servAppKeyBean.getApplicationKey()!=null){
				byte[] servAppKey = Utilities.base64toBytes(servAppKeyBean.getApplicationKey());
				if (Utilities.arrayCompare(servAppKey, 0, appKey, 0, appKey.length)==0){
					log("keys match!");
				}
			}
		}
		*/
		//END DEBUG
	}
	
	public KeyDistributionBean invokeKeyDistributionService(KryptonClientConfig kryptonClientConfig) throws Exception{
		AuthResult authResult = doAuthentication(kryptonClientConfig);
		long currentTimeStamp = System.currentTimeMillis();
		
		if (kryptonClientConfig.getKeyDistributionUrl()==null || kryptonClientConfig.getKeyDistributionUrl().isEmpty()){
			throw new IllegalArgumentException("KeyDistributionUrl is null");
		}
		String uri = kryptonClientConfig.getKeyDistributionUrl().replace("{keyid}", authResult.keyId);
		String serviceResponse = KryptonAPI.requestService(uri, authResult.ck, authResult.nonce,currentTimeStamp, authResult.keyId, kryptonClientConfig.getKeyLength(), kryptonClientConfig.getKeyAlgorithm(),kryptonClientConfig.getRequestParameters());
		if (serviceResponse!=null){
			KeyDistributionBean result = new KeyDistributionBean();
			result.setServiceProviderResponse(new Gson().fromJson(serviceResponse,JsonObject.class));
			if (kryptonClientConfig.isApplicationKey()) {
				byte[] appKey = KryptonAPI.calculateApplicationKey(authResult.nonce, currentTimeStamp, authResult.ck, kryptonClientConfig.getKeyLength(), kryptonClientConfig.getKeyAlgorithm());
				result.setApplicationKey(Utilities.bytesToBase64(appKey));
			}
			return result;
		}else {
			throw new Exception("Service response is empty.");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listComPorts(KryptonClientConfig kryptonClientConfig){
    	String[] ports = CommManager.getAvailablePorts();
    	if (ports==null || ports.length==0){
    		return Collections.EMPTY_LIST;
    	}
    	return Arrays.asList(ports);
	}
	
	public String getDeviceInfo(KryptonClientConfig kryptonClientConfig){
		CommManager	commManager= createCommManager(kryptonClientConfig.getCommunicationDeviceConfig());
		return commManager.queryDevice();
	}
	
	public void clearCache() {
		keyCache.clear();
		log("key cache has been cleared.");
	}
	
	private static class AuthResult{
		byte[] nonce;
		byte[] ck =null;
		String keyId;
	}
	protected AuthResult doAuthentication(KryptonClientConfig kryptonClientConfig) {
		AuthResult authResult = new AuthResult();
		IUiccInterface uiccInterface = createUiccInterface(kryptonClientConfig);
		if (kryptonClientConfig.isDebug()){ //Only set debug mode if it is true
			uiccInterface.setDebug(kryptonClientConfig.isDebug());
			KryptonAPI.setDebug(kryptonClientConfig.isDebug());
		}
		
		String imsi=uiccInterface.readImsi();
		if (imsi==null || imsi.isEmpty()){
			throw new RuntimeException("IMSI not retrieved! Halting key agreement negociation!");
		}
		//DEBUG
		//imsi = "900000000000001";
		//
		
		//Verify if cached key exist
		for (String alias:keyCache.listKeyAliases()){
			if (keyCache.isStillValid(alias)){
				if (alias.startsWith(imsi)){
					String[] aliasParts = alias.split("_");//Used since introduction of a composite alias
					if (aliasParts.length>1){
						authResult.keyId = aliasParts[1]; //Key ID is second part
						authResult.ck=keyCache.getKeyBytes(alias);
						log("retrieve keyId and ck from key cache. keyId=" + authResult.keyId);
						break;
					}
					else
					{
						keyCache.unsetKey(alias);
					}
				}
				else{
					keyCache.unsetKey(alias);
				}
			}
			else
			{
				keyCache.unsetKey(alias);
			}
		}
		if (authResult.ck==null) //Key cache did not return a key, proceed with authentication
		{
			//First step - Create master key
			MilenageParamsBean milenageParams = KryptonAPI.initKeyAgreement(kryptonClientConfig.getKeyAgreementUrl(),imsi);
			if (milenageParams==null || milenageParams.getAutn()==null || milenageParams.getRand()==null){
				throw new RuntimeException("Error negotiating key agreement for imsi "+((imsi==null)?"":imsi.toString())+"!");
			}
			authResult.keyId =milenageParams.getKeyId();
			byte[] rand = Utilities.base64toBytes(milenageParams.getRand());
			byte[] autn = Utilities.base64toBytes(milenageParams.getAutn());
			if (autn==null || rand==null){
				throw new RuntimeException("Bad parameters detected while negotiating key agreement!");
			}
			byte[] rsp = uiccInterface.authenticate(rand, autn);
			byte[] res =null;				
			if (rsp!=null){
				AuthenticationResponse authResponse = new AuthenticationResponse(rsp);
				switch (authResponse.getResultState())
				{
					case Success:
						res = authResponse.getRes();
						authResult.ck = authResponse.getCk();
						if (authResult.keyId==null){
							throw new RuntimeException("Key ID is null please try authentication one more time!");
						}
						if (KryptonAPI.verifyMasterKey(kryptonClientConfig.getKeyAgreementUrl()+"/"+authResult.keyId+"/verify",  Utilities.bytesToBase64(res))){
							String alias = imsi + "_" +authResult.keyId;//composite alias
							keyCache.setKeyBytes(alias, authResult.ck);
						}
						else
						{
							throw new RuntimeException("Could not verify master key!");
						}
						 break;
					case SynchronisationFailure:
						byte[] auts = authResponse.getAuts();
						milenageParams = KryptonAPI.initKeyAgreement(kryptonClientConfig.getKeyAgreementUrl(), imsi,milenageParams.getRand(),Utilities.bytesToBase64(auts));
						rand = Utilities.base64toBytes(milenageParams.getRand());
	        			autn = Utilities.base64toBytes(milenageParams.getAutn());
	        			authResult.keyId =milenageParams.getKeyId();
	        			rsp = uiccInterface.authenticate(rand, autn);
						
						if (rsp==null){
							throw new RuntimeException("Failure to authenticate during resynchronization procedure!");
						}
						else
						{
							authResponse = new AuthenticationResponse(rsp);
							if (authResponse.getResultState()==ResultState.Success){
								res = authResponse.getRes();
								authResult.ck = authResponse.getCk();
								if (KryptonAPI.verifyMasterKey(kryptonClientConfig.getKeyAgreementUrl()+"/"+authResult.keyId+"/verify",  Utilities.bytesToBase64(res))){
									String alias = imsi + "_" +authResult.keyId;//composite alias
									keyCache.setKeyBytes(alias, authResult.ck);
	    						}
	    						else
	    						{
	    							throw new RuntimeException("Could not verify master key!");
	    						}
							}
							else
							{
								throw new RuntimeException("Unable to resynchronize while negotiating key agreement!");
							}
						}
	
						break;
					default:
						throw new RuntimeException("Authentication failure while negotiating key agreement!");
				}
			}
		}
		authResult.nonce = new byte[8];
		new Random().nextBytes(authResult.nonce);
		//Save key cache
        keyCache.save();		
		return authResult;
	}
	
	protected IUiccInterface createUiccInterface(KryptonClientConfig kryptonClientConfig) {
		UiccInterfaceType uiccInterfaceType = kryptonClientConfig.getUiccInterfaceType();
		switch (uiccInterfaceType){
		case iso7816:{
			return new Iso7816Manager();
		}
		case comm:{
			return createCommManager(kryptonClientConfig.getCommunicationDeviceConfig());
		}
		case mmcli:{
			return createMmcliManager(kryptonClientConfig.getCommunicationDeviceConfig());
		}
		case autoDetect:{
			return new AutoDetectManager(kryptonClientConfig.isDebug());
		}
		default:
			throw new IllegalArgumentException("Unsupported UiccInterfaceType. type:"+uiccInterfaceType.toString());
		}
	}
		
	protected CommManager createCommManager(CommunicationDeviceConfig communicationDeviceConfig) {
		CommManager commManager = new CommManager();
		if(communicationDeviceConfig != null) {
			if  (communicationDeviceConfig.getPortName() != null){
				commManager.setPortName(communicationDeviceConfig.getPortName());
			}
			if (communicationDeviceConfig.getBaudRate() != null){
				commManager.setBaudRate(communicationDeviceConfig.getBaudRate());
			}
			if (communicationDeviceConfig.getDataBits() != null){
				commManager.setDataBits( communicationDeviceConfig.getDataBits());
			}
			if (communicationDeviceConfig.getStopBits() !=null){
				commManager.setStopBits(communicationDeviceConfig.getStopBits());
			}
			if (communicationDeviceConfig.getParity() != null){
				commManager.setParity(communicationDeviceConfig.getParity());
			}
		}
		return commManager;
	}
	protected MmcliManager createMmcliManager(CommunicationDeviceConfig communicationDeviceConfig) {
		MmcliManager mmcliManager = new MmcliManager();
		if(communicationDeviceConfig != null) {
			mmcliManager.setModemIndex(communicationDeviceConfig.getModemIndex());
		}
		return mmcliManager;
	}
	
	public static void main(String[] args) {
		//COLLECT ALL COMMAND LINE ARGUMENTS
		List<String> argsList = new ArrayList<String>(); // List of Command line Arguments
	    HashMap<String, String> optsList = new HashMap<String, String>(); // List of Options
	    List<String> doubleOptsList = new ArrayList<String>();
	
	    for (int i = 0; i < args.length; i++) {
	        switch (args[i].charAt(0)) {
	        case '-':
	            if (args[i].length() < 2)
	                throw new IllegalArgumentException("Not a valid argument: "+args[i]);
	            if (args[i].charAt(1) == '-') {
	                if (args[i].length() < 3)
	                    throw new IllegalArgumentException("Not a valid argument: "+args[i]);
	                // --opt
	                doubleOptsList.add(args[i].substring(2, args[i].length()));
	            } else {
	                if (args.length-1 == i)
	                    throw new IllegalArgumentException("Expected arg after: "+args[i]);
	                // -opt
	
	                optsList.put(args[i], args[i+1]);
	                i++;
	            }
	            break;
	        default:
	            // arg
	            argsList.add(args[i]);
	            break;
	        }
	    }
	    
	          
	    for (String opt:doubleOptsList){
        	if (opt.equals("help")){
             	displayHelp();
             	return;
            }

        }
        
        //Load config from file first - Command line overrides file config
       //loadProperties();
        
        //START COMM DEVICE
       KryptonClientConfig kryptonClientConfig = new KryptonClientConfig();
       try {
    	    if  (optsList.get("-i")!=null){ 
    	    	kryptonClientConfig.setUiccInterfaceType(UiccInterfaceType.valueOf(optsList.get("-i")));
    	    }
    	    if  (optsList.get("-sp")!=null){   
    	    	kryptonClientConfig.setKeyDistributionUrl(optsList.get("-sp"));
     	    }
    	    if  (optsList.get("-au")!=null){   
    	    	kryptonClientConfig.setKeyAgreementUrl(optsList.get("-au")) ;
      	    }
    	    if  (optsList.get("-rp")!=null){   
    	    	kryptonClientConfig.setRequestParameters(optsList.get("-rp")) ;
       	    }
    	    if  (optsList.get("-kl")!=null){   
    	    	kryptonClientConfig.setKeyLength(Integer.parseInt(optsList.get("-kl"))) ;
      	    }
    	    if  (optsList.get("-ka")!=null){  
    	    	kryptonClientConfig.setKeyAlgorithm(optsList.get("-ka")) ;
      	    }
    	    CommunicationDeviceConfig communicationDeviceConfig = new CommunicationDeviceConfig();
			if  (optsList.get("-c")!=null){
				communicationDeviceConfig.setPortName( optsList.get("-c") );
			}
			if (optsList.get("-b")!=null){
				communicationDeviceConfig.setBaudRate( Integer.parseInt(optsList.get("-b")) );
			}
			if (optsList.get("-d")!=null){
				communicationDeviceConfig.setDataBits(  Integer.parseInt(optsList.get("-d")) );
			}
			if (optsList.get("-s")!=null){
				communicationDeviceConfig.setStopBits( Integer.parseInt(optsList.get("-s")) );
			}
			if (optsList.get("-p")!=null){
				communicationDeviceConfig.setParity( Integer.parseInt(optsList.get("-p")) );
			}
			if (optsList.get("-m")!=null){
				int modemIntex =  Integer.parseInt(optsList.get("-m"));
				communicationDeviceConfig.setModemIndex( Integer.toString(modemIntex));
			}
			kryptonClientConfig.setCommunicationDeviceConfig(communicationDeviceConfig);
       }
	   catch (Exception ex){
		   logError("Illegal argument: "+ex.getMessage());
	   }
       try{
	       //Check for execution commands
    	   RunLevel runLevel = RunLevel.NORMAL;
    	   
	        if (doubleOptsList.contains("listComPorts")){
	         	runLevel = RunLevel.LIST_COM_PORTS;
	        }
	    	else if (doubleOptsList.contains("deviceInfo")){
	    		runLevel = RunLevel.DEVICE_INFO;
	        }
	    	else 
	    	{
	    		if (doubleOptsList.contains("endorse")){
	    			kryptonClientConfig.setKeyDistributionUrl(kryptonClientConfig.getKeyAgreementUrl() + "/{keyid}/endorse"); // still runLevel 0
	    		}
	    		
	    		if (doubleOptsList.contains("applicationKey")){
	    			kryptonClientConfig.setApplicationKey(true);
	    		}
	    		
	    		if (doubleOptsList.contains("debug")){
	    			kryptonClientConfig.setDebug(true);
	    		}
	        
	    	}
	        KryptonClient client= new KryptonClient();
    		if (doubleOptsList.contains("clearCache")){
    			client.clearCache();
    		}
	        KryptonAPI.registerLogListener(client);
	        client.start(runLevel, kryptonClientConfig);
	        
	        
       }
	   catch (Exception ex){
		   logError("Error: "+ex.getMessage());
	   }
	    
	}

	private static void log(String line){
    	System.out.println(line);
    }
	
	
	private static void logError(String line){
		System.err.println(line);
    }
	
	@Override
	public void itemAdded(TextLogItem item) {

    	String line = "";
		switch (item.getType()){

		 	case LOG:
				line = item.getReadableTime()+": "+item.getDescription();
				log(line);
				break;
				
			case WARN:
				line = item.getReadableTime()+": [WARNING] "+item.getDescription();
				log(line);
				break;
				
			case ERR:
				line = item.getReadableTime()+": [ERROR] "+item.getDescription();
				logError(line);
				break;
		}
		

	}

	@Override
	public void itemsCleared() {

		
	}
	
	
}
