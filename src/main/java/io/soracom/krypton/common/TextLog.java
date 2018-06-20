package io.soracom.krypton.common;

import java.util.List;
import java.util.ArrayList;


public class TextLog {

	private List<ITextLogListener> listeners = new ArrayList<ITextLogListener>();
	
	private List<TextLogItem> items = new ArrayList<TextLogItem>();
	
	public void addListener(ITextLogListener toAdd) {
        listeners.add(toAdd);
    }
	
	public void removeListener(ITextLogListener toRemove) {
        listeners.remove(toRemove);
    }
	
	
	public void add(TextLogItem item){
		items.add(item);
		itemAdded(item);
	}
	
	public void clear(){
		items.clear();
		itemsCleared();
	}
	
	private void itemAdded(TextLogItem item){
		 for (ITextLogListener hl : listeners){
	            hl.itemAdded(item);
	    }
	}
	
	private void itemsCleared(){
		 for (ITextLogListener hl : listeners){
	            hl.itemsCleared();
	    }
	}
	
	public void setItems(List<TextLogItem> value){ items = value; }
	public List<TextLogItem> getItems(){ return items; }
	
	public int size(){
		if (items!=null) {
			return items.size();
		}
		else{
			return 0;
		}
	}
	
	public TextLogItem get(int i){
		if (items!=null) {
			return items.get(i);
		}
		else{
			return null;
		}
	}
}
