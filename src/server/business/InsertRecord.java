package server.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import share.instruction.InstUpDelInsRecord;
import share.message.SimpleFeedbackMsg;

/**
 * 服务器中用来处理插入记录事务的类
 * @author huang
 * @date 2020-06-15
 *
 */
public class InsertRecord {
	
private Connection conn = null;
	
	private String category;
	private Object[] newRecord;
	
	private SimpleFeedbackMsg feedBackMsg = null;
	
	public InsertRecord(InstUpDelInsRecord instruction, Connection conn, Boolean authority) {
		
		this.conn = conn;
		category = instruction.getRecordType();
		newRecord = instruction.getRecordItems();
		
		if(authority) {
			try {
				doInsertion();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				feedBackMsg = new SimpleFeedbackMsg(false, "添加失败！数据库异常或者添加了非法的外键内容");
			}
		}else {
			feedBackMsg = new SimpleFeedbackMsg(false, "添加失败！该账户没有修改数据库记录的权限");
		}
		
	}
	
	private void doInsertion() throws  SQLException {
		
		PreparedStatement pStmt = null;
		
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"insert into teacher values (?,?,?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.TEACHER ,pStmt, newRecord, 0, 0);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"insert into teaches values (?,?,?,?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.TEACHES, pStmt, newRecord, 0, 0);
			break;
		case "课程":
			pStmt = conn.prepareStatement(
					"insert into course values (?,?,?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.COURSE, pStmt, newRecord, 0, 0);
			break;
		case "班级":
			pStmt = conn.prepareStatement(
					"insert into class values (?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.CLASS, pStmt, newRecord, 0, 0);
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"insert into special_classroom values (?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.SPECIAL_CLASSROOM, pStmt, newRecord, 0, 0);
			break;
		case "电话号码":
			pStmt = conn.prepareStatement(
					"insert into phone_no values (?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.PHONE_NO, pStmt, newRecord, 0, 0);
			break;
		case "老师能够教学的课程":
			pStmt = conn.prepareStatement(
					"insert into able_to_teach values (?,?,?)");
			QuireRecord.prepStmtSetWithNull(QuireRecord.ABLE_TO_TEACH, pStmt, newRecord, 0, 0);
			break;
		
		}
		
		int sqlReturn = pStmt.executeUpdate();
		if(sqlReturn > 0) {
			feedBackMsg = new SimpleFeedbackMsg(true, "添加成功！此次操作影响到共" + sqlReturn + "条记录");
		}else {
			feedBackMsg = new SimpleFeedbackMsg(false, "修改失败！");
		}
		
	}
	
	public SimpleFeedbackMsg getFeedBackMsg() {
		return feedBackMsg;
	}

}
