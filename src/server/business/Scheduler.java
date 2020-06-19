package server.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 排课事务类
 * @author huang
 * @date 2020-06-17
 *
 */
public class Scheduler {
	
	private Connection conn = null;
	
	private int class_count = 0;
	private int days_count = 0;
	private int period_count = 0; 
	
	private String[][][] schedule = null; 
	private String[][][] hit_wall = null;
	
	private int pointer = 0;
	
	public Scheduler(Connection conn) {
		
		this.conn = conn;
		
		try {
			init();
			do_schedule();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void init() throws SQLException {
		
		//获取班级总数
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select count(*) from class");
		rs.first();
		class_count = rs.getInt(1);
		
		//TODO 这段代码暂时使每周上课天数和每天课程数固定，未来的迭代会使其可变化
		days_count = 5;
		period_count = 8;
		
		schedule = new String[class_count][days_count][period_count];
		hit_wall = new String[class_count][days_count][period_count];
		
	}
	
	private void do_schedule() {
		
		class_count = 0;
		prepare();
		
	}
	
	private void prepare() {
		
	}

}
