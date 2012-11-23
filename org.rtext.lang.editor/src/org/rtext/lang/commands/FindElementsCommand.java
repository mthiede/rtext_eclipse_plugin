package org.rtext.lang.commands;

import com.google.gson.annotations.SerializedName;


public class FindElementsCommand extends Command<Elements> {

	public FindElementsCommand(String searchPattern) {
		super("find_elements", Elements.class);
		this.searchPattern = searchPattern;
	}
	
	@SerializedName("search_pattern") private String searchPattern;
}
