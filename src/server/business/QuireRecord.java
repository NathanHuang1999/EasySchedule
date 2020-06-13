package server.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import server.SocketServer;
import share.instruction.InstQuireRecord;
import share.message.QuiryResultMsg;

/**
 * 服务器中用来处理查询记录事务的类，目前尚不完善
 * @author huang
 * @date 2020-06-13
 *
 */
public class QuireRecord {

	private Connection conn = null;
	
	private String category;
	private String quiryContent;
	public static int[][] groupingResolve = {{0,0,0,0,1,2,2,3,3,3,3}, {0,0,0,0,0,0}, {0,0,0,0}, {0,0,1,1,1,1}, {0,0}};
	private int[] grouping = null;
	
	private QuiryResultMsg quiryResultMsg = null;
	
	public QuireRecord(InstQuireRecord instruction, Connection conn) {
		
		this.conn = conn;
		this.category = instruction.getCategorySelect();
		
		switch(category) {
		case "教师":
			grouping = groupingResolve[0];break;
		case "教学安排":
			grouping = groupingResolve[1];break;
		case "课程":
			grouping = groupingResolve[2];break;
		case "班级":
			grouping = groupingResolve[3];break;
		case "特殊教室":
			grouping = groupingResolve[4];break;
		}
		
		if(instruction.getUseAdvancedQuiry()) {
			//TODO 使用高级搜索。如果时间充裕会添加此部分
			
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
		try {
			resultSet = stmtQuiryNumResolve();
			quiryResultMsg = new QuiryResultMsg(category, quiryContent, resultSet, grouping);
		}catch(NumberFormatException e) {
			try {
				resultSet = stmtQuiryNonNumResolve();
				quiryResultMsg = new QuiryResultMsg(category, quiryContent, resultSet, grouping);
			} catch (SQLException e1) {
				quiryResultMsg = new QuiryResultMsg("数据库异常，请稍后再试");
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			quiryResultMsg = new QuiryResultMsg("数据库异常，请稍后再试");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//socketServer.sendData(quiryResultMsg);
		
	}

	public QuiryResultMsg getQuiryResultMsg() {
		return quiryResultMsg;
	}
	
	/* 
	 * 将查询内容解析成数字进行查询的函数
	 */
	private ResultSet stmtQuiryNumResolve() throws SQLException {
		short numEqual = (short)Integer.parseInt(quiryContent);
		String pattern = "%" + quiryContent + "%";
		PreparedStatement pStmt = null;
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"select * "
					+ "from teacher "
					+ "where ((teacher.id like ?) or (teacher.name like ?) or (teacher.sex like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"select * "
					+ "from teaches "
					+ "where ((teaches.grade = ?) or (teaches.class_no = ?) or (teaches.course_name like ?) or (teaches.teacher_id like ?)"
					+ "or (teaches.amount_per_week = ?))");
			pStmt.setShort(1, numEqual);
			pStmt.setShort(2, numEqual);
			pStmt.setString(3, pattern);
			pStmt.setString(4, pattern);
			pStmt.setShort(5, numEqual);
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
					"select * "
					+ "from class "
					+ "where ((class.grade = ?) or (class.class_no = ?)) "
					+ "order by class.grade, class.class_no");
			pStmt.setShort(1, numEqual);
			pStmt.setShort(2, numEqual);
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"select * from special_classroom where ((category like ?) or (room_no = ?))");
			pStmt.setString(1, pattern);
			pStmt.setShort(2, numEqual);
			break;
		case "电话号码":
			pStmt = conn.prepareStatement(
					"select * from phone_no where ((phone_no like ?) or (teacher_ID like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			break;			
		case "老师能够教学的课程":
			pStmt = conn.prepareStatement(
					"select * from able_to_teach where ((teacher_id like ?) or (course_name like ?) or (ability like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		}
		return pStmt.executeQuery();
	}
	/* 放弃的设计
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
					+ "or (able_to_teach.course_name like ?) or (teaches.grade = ?) or (teaches.class_no = ?) or (teaches.course_name like ?)) "
					+ "order by teacher.id, teacher.name, teacher.sex, teacher.introduction");
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
					+ "where ((class.grade = ?) or (class.class_no = ?) or (teaches.teacher_id like ?) or (teacher.name like ?)) "
					+ "order by class.grade, class.class_no");
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
	*/
	
	
	/*
	 * 不将查询内容解析成数字进行查询的函数
	 */
	private ResultSet stmtQuiryNonNumResolve() throws SQLException {
		String pattern = "%" + quiryContent + "%";
		PreparedStatement pStmt = null;
		switch(category) {
		case "教师":
			pStmt = conn.prepareStatement(
					"select teacher.* "
					+ "from teacher "
					+ "where ((teacher.id like ?) or (teacher.name like ?) or (teacher.sex like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		case "教学安排":
			pStmt = conn.prepareStatement(
					"select teaches.grade, teaches.class_no, teaches.course_name, teaches.teacher_id, teaches.amount_per_week "
					+ "from teaches "
					+ "where ((teaches.course_name like ?) or (teaches.teacher_id like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			break;
		case "课程":
			pStmt = conn.prepareStatement(
					"select * from course where ((name like ?) or (category like ?) or (special_classroom_category like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		case "班级":
			if(pattern.equals("%%"))
				pStmt = conn.prepareStatement("select * from class");
			else
				pStmt = conn.prepareStatement("select * from class where class.grade = -1");
			break;
		case "特殊教室":
			pStmt = conn.prepareStatement(
					"select * from special_classroom where (category like ?)");
			pStmt.setString(1, pattern);
			break;
		case "电话号码":
			pStmt = conn.prepareStatement(
					"select * from phone_no where ((phone_no like ?) or (teacher_ID like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			break;			
		case "老师能够教学的课程":
			pStmt = conn.prepareStatement(
					"select * from able_to_teach where ((teacher_id like ?) or (course_name like ?) or (ability like ?))");
			pStmt.setString(1, pattern);
			pStmt.setString(2, pattern);
			pStmt.setString(3, pattern);
			break;
		}
		return pStmt.executeQuery();
	}
	/* 放弃的设计
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
					+ "or (able_to_teach.course_name like ?) or (teaches.course_name like ?)) "
					+ "order by teacher.id, teacher.name, teacher.sex, teacher.introduction");
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
					+ "where ((teaches.teacher_id like ?) or (teacher.name like ?)) "
					+ "order by class.grade, class.class_no");
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
	*/
	
}
