package share.message;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

/**
 * 用于返回查询请求的结果的类
 * @author huang
 * @date 2020-06-12
 *
 */
public class QuiryResultMsg extends AbstractTableModel implements Serializable{

	private String quiryType = null;
	private String quiryContent = null;
	
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
	
	private int[] grouping = null;
	private ArrayList<Integer> groupingCount = null;
	private ArrayList<Integer> groupingStart = null;
	private Boolean[] isChangeable = null;
	private Boolean[] isBigText = null;
	private String[] groupTitle = null;
	
	private ArrayList<ArrayList<ArrayList<Integer>>> resolveCube = new ArrayList<ArrayList<ArrayList<Integer>>>();
	
	
	public QuiryResultMsg(String errorMsg) {
		quirySuccess = false;
		this.errorMsg = errorMsg;
	}

	public QuiryResultMsg(String quiryType, String quiryContent, ResultSet resultSet, int[] grouping) throws Exception {
		
		quirySuccess = true;
		this.quiryType = quiryType;
		this.grouping = grouping;
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
		//当结果集非空时，处理多值问题 放弃的设计
		//multiValueHandle();
		
	}
	
	/* 放弃的设计
	private void multiValueHandle() throws Exception {
		if(recordNumber != 0) {
			
			//判断grouping参数的长度和属性数是否一致
			if(grouping.length!=attributeNumber) throw new Exception("grouping length does not equal to attribute number");
			
			//判断属性中是否有多值属性
			Boolean oneInGrouping = false;
			for(int i=0; i<grouping.length; i++) {
				if(grouping[i] == 1) oneInGrouping = true;
			}
			
			if(!oneInGrouping) {
				//若属性中没有多值属性，则运行这个分支
				for(int i=0; i<recordNumber; i++) {
					resolveCube.add(new ArrayList<ArrayList<Integer>>());
					resolveCube.get(i).add(new ArrayList<Integer>());
					resolveCube.get(i).get(0).add(i);
				}
			}else {
				//若属性中存在多值属性，则运行这个分支
				int maxGroup = grouping[grouping.length-1];
				groupingCount = new ArrayList<Integer>(maxGroup+1);
				groupingStart = new ArrayList<Integer>(maxGroup+1);
				for(int i=0; i<maxGroup+1; i++) {
					groupingCount.add(0);
					groupingStart.add(-1);
				}
				for(int i=0; i<grouping.length; i++) {
					if(groupingStart.get(grouping[i])==-1) {
						groupingStart.set(grouping[i], i);
					}
					groupingCount.set(grouping[i], groupingCount.get(grouping[i]) + 1);
				}
				
				//设定第1个G0对象数组
				ArrayList<Object> g0 = new ArrayList<Object>(groupingCount.get(0));
				for(int i=0; i<groupingCount.get(0); i++) {
					g0.add(getOriginalValueAt(0, i));
				}
				//设定第1个G1-n对象数组
				ArrayList<ArrayList<ArrayList<Object>>> gx = new ArrayList<ArrayList<ArrayList<Object>>>(maxGroup);
				for(int i=0; i<maxGroup; i++) {
					gx.add(new ArrayList<ArrayList<Object>>());
					gx.get(i).add(new ArrayList<Object>());
					for(int j=0; j<groupingCount.get(i+1); j++) {
						gx.get(i).get(0).add(getOriginalValueAt(0, groupingStart.get(i+1)+j));
					}
				}
				//初始化resolveCube
				resolveCube.add(new ArrayList<ArrayList<Integer>>());
				for(int i=0; i<maxGroup+1; i++) {
					resolveCube.get(0).add(new ArrayList<Integer>());
					resolveCube.get(0).get(i).add(0);
				}
				//循环处理从第2个记录开始的所有记录
				for(int i=1; i<recordNumber; i++) {
					//获取newg0
					ArrayList<Object> newg0 = new ArrayList<Object>(groupingCount.get(0));
					for(int j=0; j<groupingCount.get(0); j++) {
						newg0.add(getOriginalValueAt(i, j));
					}
					//获取newgx
					ArrayList<ArrayList<Object>> newgx = new ArrayList<ArrayList<Object>>(maxGroup);
					for(int j=0; j<maxGroup; j++) {
						newgx.add(new ArrayList<Object>());
						for(int k=0; k<groupingCount.get(j+1); k++) {
							newgx.get(j).add(getOriginalValueAt(i, groupingStart.get(j+1)+k));
						}
					}
					//判断newg0和g0是否相等
					if(g0.equals(newg0)) {
						//当newg0和g0相等时，即单值/主要属性相同时，进入此分支以进行多值属性整合
						for(int j=0; j<maxGroup; j++) {
							if(!gx.get(j).contains(newgx.get(j))){
								gx.get(j).add(newgx.get(j));
								//第一次更新resolveCube
								resolveCube.get(resolveCube.size()-1).get(j+1).add(i);
							}
						}
					}else {
						///当newg0和g0不等时，即更换了单值/主要属性时，进入此分支以更新g0和gx
						g0 = newg0;
						//第二次更新resolveCube的0位置
						resolveCube.add(new ArrayList<ArrayList<Integer>>());
						int pos = resolveCube.size()-1;
						resolveCube.get(pos).add(new ArrayList<Integer>());
						resolveCube.get(pos).get(0).add(i);
						gx = new ArrayList<ArrayList<ArrayList<Object>>>(maxGroup);
						for(int j=0; j<maxGroup; j++) {
							gx.add(new ArrayList<ArrayList<Object>>());
							gx.get(j).add(newgx.get(j));
							//第二次更新resolveCube的非0位置
							resolveCube.get(pos).add(new ArrayList<Integer>());
							resolveCube.get(pos).get(j+1).add(i);
						}
					}
				}
			}
		}
	}
	*/
	
	public Boolean getQuirySuccess() {
		return quirySuccess;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String getQuiryType() {
		return quiryType;
	}
	
	public String getQuiryContent() {
		return this.quiryContent;
	}
	
	public void setIsChangeable(Boolean[] changeableArray) {
		this.isChangeable = changeableArray;
	}
	
	public void setIsBigText(Boolean[] bigTextArray) {
		this.isBigText = bigTextArray;
	}
	
	public void setGroupTitle(String[] groupTitle) {
		this.groupTitle = groupTitle;
	}
	
	public Boolean[] getIsChangeable() {
		return isChangeable;
	}
	
	public Boolean[] getIsBigText() {
		return isBigText;
	}
	
	public String[] getGroupTitle() {
		return groupTitle;
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
	
	public int[] getGrouping() {
		return grouping;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnName.get(columnIndex);
	}
	
	@Override
	public int getColumnCount() {
		return attributeNumber;
	}

	@Override
	public int getRowCount() {
		return recordNumber;
	}
	
	/* 放弃的设计
	@Override
	public int getRowCount() {
		return resolveCube.size();
	}
	*/

	/* 放弃的设计
	@Override
	public Object getValueAt(int rindex, int cindex) {
		
		int realRIndex = resolveCube.get(rindex).get(0).get(0);
		int type = relocalizeTable.get(cindex)[0];
		Object value = null;
		switch(type) {
		case 0:
			value = columnSmallInt.get(relocalizeTable.get(cindex)[1]).get(realRIndex);
			break;
		case 1:
			value = columnString.get(relocalizeTable.get(cindex)[1]).get(realRIndex);
			break;
		}
		return value;
		
	}
	*/
	
	@Override
	public Object getValueAt(int rindex, int cindex) {
		
		int type = relocalizeTable.get(cindex)[0];
		Object value = null;
		switch(type) {
		case 0:
			value = columnSmallInt.get(relocalizeTable.get(cindex)[1]).get(rindex);
			break;
		case 1:
			value = columnString.get(relocalizeTable.get(cindex)[1]).get(rindex);
			break;
		}
		return value;
		
	}
	
	public Object getOriginalValueAt(int rindex, int cindex) {
		
		int type = relocalizeTable.get(cindex)[0];
		Object value = null;
		switch(type) {
		case 0:
			value = columnSmallInt.get(relocalizeTable.get(cindex)[1]).get(rindex);
			break;
		case 1:
			value = columnString.get(relocalizeTable.get(cindex)[1]).get(rindex);
			break;
		}
		return value;
		
	}
	
}
