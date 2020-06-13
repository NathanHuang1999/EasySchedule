package server.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import share.instruction.InstDeleteRecord;
import share.message.QuiryResultMsg;
import share.message.SimpleFeedbackMsg;

/**
 * 服务器中用来处理删除记录事务的类，目前尚不完善
 * @author huang
 * @date 2020-06-13
 *
 */
public class DeleteRecord {
	
	private Connection conn = null;
	
	private String category;
	private Object[] record;
	
	private SimpleFeedbackMsg feedBackMsg = null;
	
	public DeleteRecord(InstDeleteRecord instruction, Connection conn, Boolean authority) {
		
		this.conn = conn;
		category = instruction.getDeleteType();
		record = instruction.getRecordItems();
		
		if(authority) {
			try {
				doDeletion();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				feedBackMsg = new SimpleFeedbackMsg(false ,"删除失败！数据库异常，请稍后再试");
			}
		}else {
			feedBackMsg = new SimpleFeedbackMsg(false, "删除失败！该账户没有修改数据库记录的权限");
		}
		
	}
	
	private void doDeletion() throws SQLException {
		
		PreparedStatement pStmt = null;
		
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"delete from teacher "
					+ "where id = ? and name = ? and sex = ? and introduction = ?");
			pStmt.setString(1, (String)record[0]);
			pStmt.setString(2, (String)record[1]);
			pStmt.setString(3, (String)record[2]);
			pStmt.setString(4, (String)record[3]);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"delete from teaches "
					+ "where grade = ? and class_no = ? and course_name = ? and "
					+ "teacher_id = ? and amount_per_week = ?");
			pStmt.setShort(1, (short)record[0]);
			pStmt.setShort(2, (short)record[1]);
			pStmt.setString(3, (String)record[2]);
			pStmt.setString(4, (String)record[3]);
			pStmt.setShort(5, (short)record[4]);			
			break;
		case "课程":
			pStmt = conn.prepareStatement(
					"delete from course where name = ? and category = ? "
					+ "and special_classroom_category = ? and special_classroom_no = ?");
			pStmt.setString(1, (String)record[0]);
			pStmt.setString(2, (String)record[1]);
			pStmt.setString(3, (String)record[2]);
			pStmt.setShort(4, (short)record[3]);
			break;
		case "班级":
			pStmt = conn.prepareStatement(
					"delete from class where grade = ? and class_no = ?");
			pStmt.setShort(1, (short)record[0]);
			pStmt.setShort(2, (short)record[1]);
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"delete from special_classroom where category = ? and room_no = ?");
			pStmt.setString(1, (String)record[0]);
			pStmt.setShort(2, (short)record[1]);
			break;
		case "电话号码":
			pStmt = conn.prepareStatement(
					"delete from phone_no where phone_no = ? and teacher_id = ?");
			pStmt.setString(1, (String)record[0]);
			pStmt.setString(2, (String)record[1]);
			break;
		case "老师能够教学的课程":
			pStmt = conn.prepareStatement(
					"delete from able_to_teach where teacher_id = ? and "
					+ "course_name = ? and ability = ?");
			pStmt.setString(1, (String)record[0]);
			pStmt.setString(2, (String)record[1]);
			pStmt.setString(3, (String)record[2]);
			break;
		}
		int sqlReturn = pStmt.executeUpdate();
		if(sqlReturn > 0) {
			feedBackMsg = new SimpleFeedbackMsg(true, "删除成功！此次操作影响到共" + sqlReturn + "条记录");
		}else {
			feedBackMsg = new SimpleFeedbackMsg(false, "删除失败！不过该记录可能已经被他人删除，您只是慢了一点点:)");
		}
	}
	
	public SimpleFeedbackMsg getFeedBackMsg() {
		return feedBackMsg;
	}

	
}
