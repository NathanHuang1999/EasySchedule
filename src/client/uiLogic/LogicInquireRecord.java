package client.uiLogic;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import client.SocketClient;
import client.ui.PanelInquireRecord;
import share.instruction.InstQuireRecord;
import share.message.InstructionMsg;
import share.message.QuiryResultMsg;

/**
 * 查询/修改记录的界面逻辑
 * @author huang
 * @date 2020-06-02
 *
 */
public class LogicInquireRecord {
	
	private PanelInquireRecord uiController = null;
	private SocketClient socket = null;
	
	//TODO 完善高级搜索类型
	private String[] AdvancedInquiry_SpecialClassroom = {"类别", "编号"};
	
	private String categorySelect = null; 
	private String quiryContent = null;

	public LogicInquireRecord() {
		
	}
	
	public void setCategorySelect(String categorySelect) {
		this.categorySelect = categorySelect;
		//TODO 完善高级搜索类型
		switch(categorySelect) {
		case "特殊教室":
			uiController.setAdvancedQuiryItem(AdvancedInquiry_SpecialClassroom);break;
		case "-请选择-":
			break;
		}
	}
	
	//TODO 目前没有增加高级搜索功能
	public void quire(String quiryContent) {
		this.quiryContent = quiryContent;
		InstQuireRecord quiry = new InstQuireRecord(categorySelect, quiryContent);
		socket.sendData(new InstructionMsg(InstructionMsg.QUIRE_RECORD, quiry));
		/**
		 * 获取服务器的反馈信息并处理
		 */
		QuiryResultMsg quiryResultMsg = (QuiryResultMsg)socket.recvDataObj();
		
		//TODO 待修改
		if(quiryResultMsg.getQuirySuccess()) {
			try {
				quiryResultMsg.beforeFirst();
				System.out.print(">\n");
				while(quiryResultMsg.next()) {
					System.out.print(quiryResultMsg.getString(0) + "\t");
					System.out.print(quiryResultMsg.getString(1) + "\t");
					System.out.print(quiryResultMsg.getString(2) + "\t");
					System.out.print(quiryResultMsg.getString(3) + "\t");
					System.out.print(quiryResultMsg.getString(4) + "\t");
					System.out.print(quiryResultMsg.getString(5) + "\t");
					System.out.print(quiryResultMsg.getString(6) + "\t");
					System.out.print(quiryResultMsg.getShort(7) + "\t");
					System.out.print(quiryResultMsg.getShort(8) + "\t");
					System.out.print(quiryResultMsg.getString(9) + "\t");
					System.out.print(quiryResultMsg.getShort(10) + "\t");
					System.out.print("\n");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void setUIController(PanelInquireRecord uiController) {
		this.uiController = uiController;
	}
	
	public void setSocket(SocketClient socket) {
		this.socket = socket;
	}
}
