package client.uiLogic;

import java.awt.CardLayout;
import javax.swing.JPanel;
import client.SocketClient;
import client.ui.FramePopUpWindow;
import client.ui.PanelInquireRecord;
import client.ui.PanelInquireRecordResult;
import share.instruction.InstDeleteRecord;
import share.instruction.InstQuireRecord;
import share.message.InstructionMsg;
import share.message.QuiryResultMsg;
import share.message.SimpleFeedbackMsg;

/**
 * 查询/修改记录的界面逻辑
 * @author huang
 * @date 2020-06-04
 *
 */
public class LogicInquireRecord {
	
	private JPanel backgroundPanel = null;
	private CardLayout card = null;
	
	private LogicMain logicFather = null;
	private PanelInquireRecord uiController = null;
	private PanelInquireRecordResult uiControllerTwo = null;
	
	private SocketClient socket = null;
	
	//TODO 完善高级搜索类型
	private String[] AdvancedInquiry_SpecialClassroom = {"类别", "编号"};
	
	private String categorySelect = null; 
	private String quiryContent = null;
	private QuiryResultMsg quiryResultMsg = null;

	public LogicInquireRecord(LogicMain logicFather) {
		this.logicFather = logicFather;
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
		if(categorySelect.equals("-请选择-")) return;
		InstQuireRecord quiry = new InstQuireRecord(categorySelect, quiryContent);
		socket.sendData(new InstructionMsg(InstructionMsg.QUIRE_RECORD, quiry));
		/**
		 * 获取服务器的反馈信息并处理
		 */
		quiryResultMsg = (QuiryResultMsg)socket.recvDataObj();
		
		//TODO 待修改
		if(quiryResultMsg.getQuirySuccess()) {
			uiController.fillTable(quiryResultMsg);
		}
		
	}
	
	public void delete(int rowIndex) {
		int length = quiryResultMsg.getColumnCount();
		Object[] items = new Object[length];
		for(int i=0; i<length; i++) {
			items[i] = quiryResultMsg.getValueAt(rowIndex, i);
		}
		InstDeleteRecord delete = new InstDeleteRecord(quiryResultMsg.getQuiryType(), items);
		socket.sendData(new InstructionMsg(InstructionMsg.DELETE_RECORD, delete));
		/**
		 * 获取服务器的反馈信息并处理
		 */
		SimpleFeedbackMsg feedbackMsg = (SimpleFeedbackMsg)socket.recvDataObj();
		FramePopUpWindow popUp = new FramePopUpWindow("系统提示", feedbackMsg.getTextMsg());
		popUp.setVisible(true);
		if(feedbackMsg.getPermission()) 
			quire(quiryContent);
		
	}
	
	public void setBackgroundPanel(JPanel backgroundPanel) {
		this.backgroundPanel = backgroundPanel;
		this.card = (CardLayout) backgroundPanel.getLayout();
	}
	
	public void setUIController(PanelInquireRecord uiController) {
		this.uiController = uiController;
	}
	
	public void setSocket(SocketClient socket) {
		this.socket = socket;
	}
	
	public void removeThisTab() {
		logicFather.removeTabbedPanel(backgroundPanel);
	}
	
	public void switchPanel(String name) {
		card.show(backgroundPanel, name);
	}
	
}
