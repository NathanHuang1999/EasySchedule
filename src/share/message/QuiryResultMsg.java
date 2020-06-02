package share.message;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 用于返回查询请求的结果的类
 * @author huang
 * @date 2020-06-01
 *
 */
public class QuiryResultMsg implements Serializable{

	private Boolean quirySuccess = null;
	private String errorMsg = null;
	
	private int pointer = -1;
	private int attributeNumber = 0;
	private int recordNumber = 0;
	
	private ArrayList<ArrayList<Short>> columnSmallInt = new ArrayList<ArrayList<Short>>();
	private ArrayList<ArrayList<String>> columnString = new ArrayList<ArrayList<String>>();
	
	private ArrayList<int[]> relocalizeTable = new ArrayList<int[]>();
	private ArrayList<String> columnName = new ArrayList<String>();
	private ArrayList<Integer> columnType = new ArrayList<Integer>();
	
	
	public QuiryResultMsg(String errorMsg) {
		quirySuccess = false;
		this.errorMsg = errorMsg;
	}

	public QuiryResultMsg(ResultSet resultSet) throws SQLException {
		
		quirySuccess = true;
		ResultSetMetaData metaData = resultSet.getMetaData();
		//获取列数
		attributeNumber = metaData.getColumnCount();
		//对每一列进行初始化
		for(int i=0; i<attributeNumber; i++) {
			String attributeName = metaData.getColumnName(i+1);
			columnName.add(attributeName);
			int attributeType = metaData.getColumnType(i+1);
			columnType.add(attributeType);
			switch(attributeType) {
			case java.sql.Types.SMALLINT:
				relocalizeTable.add(new int[]{0, columnSmallInt.size()});
				columnSmallInt.add(new ArrayList<Short>());
				break;
			case java.sql.Types.VARCHAR:
				relocalizeTable.add(new int[]{1, columnString.size()});
				columnString.add(new ArrayList<String>());
				break;
			case java.sql.Types.CHAR:
				relocalizeTable.add(new int[]{1, columnString.size()});
				columnString.add(new ArrayList<String>());
				break;
			}
		}
		//填充数据
		while(resultSet.next()) {
			recordNumber += 1;
			for(int i=0; i<attributeNumber; i++) {
				int type = relocalizeTable.get(i)[0];
				int position = relocalizeTable.get(i)[1];
				switch(type) {
				case 0:
					Object ifNull = resultSet.getObject(i+1);
					if(ifNull == null) {
						columnSmallInt.get(position).add((short)-1);
					}else {
						columnSmallInt.get(position).add(resultSet.getShort(i+1));
					}
					break;
				case 1:
					columnString.get(position).add(resultSet.getString(i+1));
					break;
				}
			}
		}
		
	}
	
	public Boolean getQuirySuccess() {
		return quirySuccess;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public Boolean next() {
		pointer += 1;
		return (pointer < recordNumber);
	}
	
	public void beforeFirst() {
		pointer = -1;
	}
	
	public short getShort(int index) throws SQLException {
		int type = relocalizeTable.get(index)[0];
		if(type == 1) throw new SQLException("Type of the column is not Integer");
		return columnSmallInt.get(relocalizeTable.get(index)[1]).get(pointer);
	}
	
	public String getString(int index) throws SQLException {
		int type = relocalizeTable.get(index)[0];
		if(type == 0) throw new SQLException("Type of the column is not String");
		return columnString.get(relocalizeTable.get(index)[1]).get(pointer);
	}
	
}
