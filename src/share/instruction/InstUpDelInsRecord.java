package share.instruction;

import java.io.Serializable;

/**
 * 最简单的更新/删除/新增记录类
 * @author huang
 * @date 2020-06-14
 *
 */
public class InstUpDelInsRecord implements Serializable{
	
	private String recordType = null;
	private Object[] recordItems = null;
	
	public InstUpDelInsRecord(String recordType, Object[] recordItems) {
		
		this.recordType = recordType;
		this.recordItems = recordItems;
		
	}
	
	public String getRecordType() {
		return this.recordType;
	}
	
	public Object[] getRecordItems() {
		return this.recordItems;
	}

}
