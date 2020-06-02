package share.instruction;

import java.io.Serializable;

/**
 * 最简单的查询记录类，需要完善
 * @author huang
 * @date 2020-05-28
 *
 */
public class InstQuireRecord implements Serializable{
	
	private Boolean useAdvancedQuiry = null;
	private String categorySelect = null; 
	private String quiryContent = null;
	
	public InstQuireRecord(String categorySelect, String quiryContent) {
		this.useAdvancedQuiry = false;
		this.categorySelect = categorySelect;
		this.quiryContent = quiryContent;
	}
	
	public Boolean getUseAdvancedQuiry() {
		return useAdvancedQuiry;
	}
	
	public String getCategorySelect() {
		return categorySelect;
	}
	
	public String getQuiryContent() {
		return quiryContent;
	}

}
