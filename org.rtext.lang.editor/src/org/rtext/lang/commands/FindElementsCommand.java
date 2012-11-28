package org.rtext.lang.commands;

import com.google.gson.annotations.SerializedName;


public class FindElementsCommand extends Command<Elements> {

	public FindElementsCommand(String searchPattern) {
		super("find_elements", Elements.class);
		this.searchPattern = searchPattern;
	}
	
	@SerializedName("search_pattern") private String searchPattern;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((searchPattern == null) ? 0 : searchPattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FindElementsCommand other = (FindElementsCommand) obj;
		if (searchPattern == null) {
			if (other.searchPattern != null)
				return false;
		} else if (!searchPattern.equals(other.searchPattern))
			return false;
		return true;
	}
	
	
}
