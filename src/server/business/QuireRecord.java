package server.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import server.SocketServer;
import share.instruction.InstQuireRecord;
import share.message.QuiryResultMsg;

/**
 * 服务器中用来处理查询记录事务的类，目前尚不完善
 * @author huang
 * @date 2020-06-02
 *
 */
public class QuireRecord {

	private SocketServer socketServer = null;
	private Connection conn = null;
	
	private String category;
	private String quiryContent;
	
	public QuireRecord(InstQuireRecord instruction, SocketServer socketServer, Connection conn) {
		
		this.socketServer = socketServer;
		this.conn = conn;
		this.category = instruction.getCategorySelect();
				
		if(instruction.getUseAdvancedQuiry()) {
			//TODO 使用高级搜索
			
		}else {
			this.quiryContent = instruction.getQuiryContent();
			ordinaryQuiry();
		}
		
	}
	
	/*
	 * 普通查询函数
	 */
	private void ordinaryQuiry() {
		
		ResultSet resultSet = null;
		QuiryResultMsg quiryResultMsg = null;
		try {
			resultSet = stmtQuiryNumResolve();
			quiryResultMsg = new QuiryResultMsg(resultSet);
		}catch(NumberFormatException e) {
			try {
				resultSet = stmtQuiryNonNumResolve();
				quiryResultMsg = new QuiryResultMsg(resultSet);
			} catch (SQLException e1) {
				quiryResultMsg = new QuiryResultMsg("数据库异常，请稍后再试");
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			quiryResultMsg = new QuiryResultMsg("数据库异常，请稍后再试");
			e.printStackTrace();
		}
		socketServer.sendData(quiryResultMsg);
		
	}

	private ResultSet stmtQuiryNumResolve() throws SQLException {
		short numEqual = (short)Integer.parseInt(quiryContent);
		String pattern = "%" + quiryContent + "%";
		PreparedStatement pStmt = null;
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"select teacher.*, phone_no.phone_no, able_to_teach.course_name, able_to_teach.ability, "
					+ "teaches.grade, teaches.class_no, teaches.course_name, teaches.amount_per_week "
					+ "from ((teacher left outer join phone_no on (teacher.id = phone_no.teacher_id)) "
					+ "left outer join able_to_teach on (teacher.id = able_to_teach.teacher_id)) "
					+ "left outer join teaches on (teacher.id = teaches.teacher_id) "
					+ "where ((teacher.id like ?) or (teacher.name like ?) or (teacher.sex like ?) or (phone_no.phone_no like ?) "
					+ "or (able_to_teach.course_name like ?) or (teaches.grade = ?) or (teaches.class_no = ?) or (teaches.course_name like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			pStmt.setString(4, pattern);
			pStmt.setString(5, pattern);
			pStmt.setShort(6, numEqual);
			pStmt.setShort(7, numEqual);
			pStmt.setString(8, pattern);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"select teaches.grade, teaches.class_no, teaches.course_name, teaches.teacher_id, teacher.name, teaches.amount_per_week "
					+ "from teaches join teacher on (teaches.teacher_id = teacher.id) "
					+ "where ((teaches.grade = ?) or (teaches.class_no = ?) or (teaches.course_name like ?) or (teaches.teacher_id like ?)"
					+ "or (teacher.name like ?) or (teaches.amount_per_week = ?))");
			pStmt.setShort(1, numEqual);
			pStmt.setShort(2, numEqual);
			pStmt.setString(3, pattern);
			pStmt.setString(4, pattern);
			pStmt.setString(5, pattern);
			pStmt.setShort(6, numEqual);
			break;
		case "课程":
			pStmt = conn.prepareStatement(
					"select * from course where ((name like ?) or (category like ?) or (special_classroom_category like ?)"
					+ "or (special_classroom_no = ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			pStmt.setShort(4, numEqual);
			break;
		case "班级":
			pStmt = conn.prepareStatement(
					"select class.*, teaches.course_name, teaches.teacher_id, teacher.name, teaches.amount_per_week "
					+ "from class natural left outer join (teaches join teacher on (teaches.teacher_id = teacher.id)) "
					+ "where ((class.grade = ?) or (class.class_no = ?) or (teaches.teacher_id like ?) or (teacher.name like ?))");
			pStmt.setShort(1, numEqual);
			pStmt.setShort(2, numEqual);
			pStmt.setString(3, pattern);
			pStmt.setString(4, pattern);
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"select * from special_classroom where ((category like ?) or (room_no = ?))");
			pStmt.setString(1, pattern);
			pStmt.setShort(2, numEqual);
			break;
		}
		return pStmt.executeQuery();
	}
	
	private ResultSet stmtQuiryNonNumResolve() throws SQLException {
		String pattern = "%" + quiryContent + "%";
		PreparedStatement pStmt = null;
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"select teacher.*, phone_no.phone_no, able_to_teach.course_name, able_to_teach.ability, "
					+ "teaches.grade, teaches.class_no, teaches.course_name, teaches.amount_per_week "
					+ "from ((teacher left outer join phone_no on (teacher.id = phone_no.teacher_id)) "
					+ "left outer join able_to_teach on (teacher.id = able_to_teach.teacher_id)) "
					+ "left outer join teaches on (teacher.id = teaches.teacher_id) "
					+ "where ((teacher.id like ?) or (teacher.name like ?) or (teacher.sex like ?) or (phone_no.phone_no like ?) "
					+ "or (able_to_teach.course_name like ?) or (teaches.course_name like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			pStmt.setString(4, pattern);
			pStmt.setString(5, pattern);
			pStmt.setString(6, pattern);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"select teaches.grade, teaches.class_no, teaches.course_name, teaches.teacher_id, teacher.name, teaches.amount_per_week "
					+ "from teaches join teacher on (teaches.teacher_id = teacher.id) "
					+ "where ((teaches.course_name like ?) or (teaches.teacher_id like ?) or (teacher.name like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		case "课程":
			pStmt = conn.prepareStatement(
					"select * from course where ((name like ?) or (category like ?) or (special_classroom_category like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		case "班级":
			pStmt = conn.prepareStatement(
					"select class.*, teaches.course_name, teaches.teacher_id, teacher.name, teaches.amount_per_week "
					+ "from class natural left outer join (teaches join teacher on (teaches.teacher_id = teacher.id)) "
					+ "where ((teaches.teacher_id like ?) or (teacher.name like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"select * from special_classroom where category like ?");
			pStmt.setString(1, pattern);
			break;
		}
		return pStmt.executeQuery();
	}
	
	
	
	//TODO 此方法被用于早期调试，现在已经被抛弃。在本模块功能完备之后，应删去此方法。
	private void oQuirySpecialClassroom() {
		
		ResultSet resultSet = null;
		QuiryResultMsg quiryResultMsg = null;
		try {
			int numEqual = Integer.parseInt(quiryContent);
			String pattern = "%" + quiryContent + "%";
			PreparedStatement pStmt = conn.prepareStatement(
					"select * from special_classroom where ((category like ?) or (room_no = ?))");
			pStmt.setString(1, pattern);
			pStmt.setInt(2, numEqual);
			resultSet = pStmt.executeQuery();
			quiryResultMsg = new QuiryResultMsg(resultSet);
		}catch(NumberFormatException e) {
			String pattern = "%" + quiryContent + "%";
			try {
				PreparedStatement pStmt = conn.prepareStatement(
						"select * from special_classroom where category like ?");
				pStmt.setString(1, pattern);
				resultSet = pStmt.executeQuery();
				quiryResultMsg = new QuiryResultMsg(resultSet);
			} catch (SQLException e1) {
				quiryResultMsg = new QuiryResultMsg("数据库异常，请稍后再试");
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			quiryResultMsg = new QuiryResultMsg("数据库异常，请稍后再试");
			e.printStackTrace();
		}
		socketServer.sendData(quiryResultMsg);
		
	}
	
}
