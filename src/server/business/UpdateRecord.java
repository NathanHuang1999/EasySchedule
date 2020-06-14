package server.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import share.instruction.InstUpDelInsRecord;
import share.message.SimpleFeedbackMsg;

/**
 * 服务器中用来处理更新记录事务的类
 * @author huang
 * @date 2020-06-14
 *
 */
public class UpdateRecord {
	
	private Connection conn = null;
	
	private String category;
	private Object[] oldAndNewRecord;
	
	private SimpleFeedbackMsg feedBackMsg = null;
	
	public UpdateRecord(InstUpDelInsRecord instruction, Connection conn, Boolean authority) {
		
		this.conn = conn;
		category = instruction.getRecordType();
		oldAndNewRecord = instruction.getRecordItems();
		
		if(authority) {
			try {
				doUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				feedBackMsg = new SimpleFeedbackMsg(false ,"修改失败！数据库异常或者有非法的外键内容修改");
			}
		}else {
			feedBackMsg = new SimpleFeedbackMsg(false, "修改失败！该账户没有修改数据库记录的权限");
		}
		
	}
	
	private void doUpdate() throws SQLException {
		
		PreparedStatement pStmt = null;
		
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"update teacher set id = ? , name = ? , sex = ? , introduction = ? "
					+ "where id = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.TEACHER ,pStmt, oldAndNewRecord, 0, 4);
			pStmt.setString(5, (String)oldAndNewRecord[0]);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"update teaches set grade = ? , class_no = ? , course_name = ? , teacher_id = ? , amount_per_week = ? "
					+ "where grade = ? and class_no = ? and course_name = ? and teacher_id = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.TEACHES, pStmt, oldAndNewRecord, 0, 5);
			pStmt.setShort(6, (short)oldAndNewRecord[0]);
			pStmt.setShort(7, (short)oldAndNewRecord[1]);
			pStmt.setString(8, (String)oldAndNewRecord[2]);
			pStmt.setString(9, (String)oldAndNewRecord[3]);
			break;
		case "课程":
			pStmt = conn.prepareStatement(
					"update course set name = ? , category = ? , special_classroom_category = ? , special_classroom_no = ? "
					+ "where name = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.COURSE, pStmt, oldAndNewRecord, 0, 4);
			pStmt.setString(5, (String)oldAndNewRecord[0]);
			break;
		case "班级":
			pStmt = conn.prepareStatement(
					"update class set grade = ? , class_no = ? where grade = ? and class_no = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.CLASS, pStmt, oldAndNewRecord, 0, 2);
			pStmt.setShort(3, (short)oldAndNewRecord[0]);
			pStmt.setShort(4, (short)oldAndNewRecord[1]);
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"update special_classroom set category = ? , room_no = ? where category = ? and room_no = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.SPECIAL_CLASSROOM, pStmt, oldAndNewRecord, 0, 2);
			pStmt.setString(3, (String)oldAndNewRecord[0]);
			pStmt.setShort(4, (short)oldAndNewRecord[1]);
			break;
		case "电话号码":
			pStmt = conn.prepareStatement(
					"update phone_no set phone_no = ? , teacher_id = ? where phone_no = ? and teacher_id = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.PHONE_NO, pStmt, oldAndNewRecord, 0, 2);
			pStmt.setString(3, (String)oldAndNewRecord[0]);
			pStmt.setString(4, (String)oldAndNewRecord[1]);
			break;
		case "老师能够教学的课程":
			pStmt = conn.prepareStatement(
					"update able_to_teach set teacher_id = ? , course_name = ? , ability = ? "
					+ "where teacher_id = ? and course_name = ?");
			QuireRecord.prepStmtSetWithNull(QuireRecord.ABLE_TO_TEACH, pStmt, oldAndNewRecord, 0, 3);
			pStmt.setString(4, (String)oldAndNewRecord[0]);
			pStmt.setString(5, (String)oldAndNewRecord[1]);
			break;
		}
		System.out.println(pStmt);
		int sqlReturn = pStmt.executeUpdate();
		if(sqlReturn > 0) {
			feedBackMsg = new SimpleFeedbackMsg(true, "修改成功！此次操作影响到共" + sqlReturn + "条记录");
		}else {
			feedBackMsg = new SimpleFeedbackMsg(false, "修改失败！");
		}
	}

	public SimpleFeedbackMsg getFeedBackMsg() {
		return feedBackMsg;
	}
	
}
