package io.soracom.krypton.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextLogItem {

	public enum TextLogItemType{
		LOG,
		WARN,
		ERR
	}
	private TextLogItemType type; 
	private String desc;
	private long time;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");    
;
	
	

	public TextLogItem(){
		time = System.currentTimeMillis();
		type = TextLogItemType.LOG;
		desc = "";

	}
	
	public TextLogItem(TextLogItemType itemType, String description){
		time = System.currentTimeMillis();
		type = itemType;
		desc = description;
	}
	
	public TextLogItem(long timeStamp, TextLogItemType itemType, String description){
		time = timeStamp;
		type = itemType;
		desc = description;
	}
	public void  setType( TextLogItemType value ){ type = value; }
	public TextLogItemType getType(){ return type; }
	
    public void setDescription(String value) { desc = value; }
	public String getDescription(){ return desc; }
	
	public void setTime(long value) { time = value; }
	public long getTime(){ return time; }
	public String getReadableTime(){ 
		Date resultdate = new Date(time);
		return sdf.format(resultdate);
	}
		
}
