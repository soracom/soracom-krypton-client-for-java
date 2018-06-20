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
