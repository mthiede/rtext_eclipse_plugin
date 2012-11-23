package org.rtext.lang.commands;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Elements extends Response {

	public static class Element {
		String display;
		String file;
		int line;
		String desc;
		
		public String getDesc() {
			return desc;
		}
		
		public String getDisplay() {
			return display;
		}
		
		public String getFile() {
			return file;
		}
		
		public int getLine() {
			return line;
		}
		
		@Override
		public String toString() {
			return getDisplay();
		}
	}
	public Elements() {
		super(0, "response");
	}
	
	@SerializedName("total_elements") private int totalElements;
	private List<Element> elements = new ArrayList<Element>();

	public List<Element> getElements() {
		return elements;
	}
	
	public int getTotalElements() {
		return totalElements;
	}
}
