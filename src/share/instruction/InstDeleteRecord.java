package share.instruction;

import java.io.Serializable;

/**
 * 最简单的删除记录类
 * @author huang
 * @date 2020-06-13
 *
 */
public class InstDeleteRecord implements Serializable{
	
	private String deleteType = null;
	private Object[] recordItems = null;
	
	public InstDeleteRecord(String deleteType, Object[] recordItems) {
		
		this.deleteType = deleteType;
		this.recordItems = recordItems;
		
	}
	
	public String getDeleteType() {
		return this.deleteType;
	}
	
	public Object[] getRecordItems() {
		return this.recordItems;
	}

}
