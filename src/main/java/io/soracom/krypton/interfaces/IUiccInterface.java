package io.soracom.krypton.interfaces;

public interface IUiccInterface {

	/**
	 * Set the debug mode to verbose uicc interface communication
	 * @param debug -true to enable debug, false otherwise
	 */
	public void setDebug(boolean debug);
	
	/**
	 * Proceed to read the IMSI and return it in the byte encoded form
	 * @return IMSI in binary format
	 */
	public String readImsi();
	
	/**
	 * Proceed to Authenticate with the USIM application and returned the response
	 * @param rand
	 * @param autn
	 * @return
	 * //"Successful 3G authentication" tag = 'DB'
	 * RES, CK, IK if Service no27 is "not available".
	 * or
	 * RES, CK, IK, KC if Service no27 is "available".
	 * //"Synchronization failure" tag = 'DC'
	 * AUTS.
	 */
    public byte[] authenticate(byte[] rand, byte[] autn);
    
    public boolean disconnect();
}
