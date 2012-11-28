package org.rtext.lang.commands;

import static java.util.Collections.emptyList;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ReferenceTargets extends Response {
	public static class Target {
		private String display;
		private String file;
		private int line;
		private String desc;
		
		public String getDisplay() {
			return display;
		}
		public String getFile() {
			return file;
		}
		public int getLine() {
			return line;
		}
		public String getDesc() {
			return desc;
		}
		
		public void setDisplay(String display) {
			this.display = display;
		}
	}

	public ReferenceTargets(int invocationId, String type) {
		super(invocationId, type);
	}

	@SerializedName("begin_column") private int endColumn;
	@SerializedName("end_column") private int beginColumn;
	List<Target> targets;
	
	public int getBeginColumn() {
		return beginColumn;
	}
	
	public int getEndColumn() {
		return endColumn;
	}
	
	public List<Target> getTargets() {
		if(targets == null){
			targets = emptyList();
		}
		return targets;
	}
}
