package client.uiLogic;

import java.awt.CardLayout;
import javax.swing.JPanel;
import client.SocketClient;
import client.ui.FramePopUpWindow;
import client.ui.PanelInquireRecord;
import client.ui.PanelInquireRecordResult;
import share.instruction.InstUpDelInsRecord;
import share.instruction.InstQuireRecord;
import share.message.InstructionMsg;
import share.message.QuiryResultMsg;
import share.message.SimpleFeedbackMsg;

/**
 * 查询/修改记录的界面逻辑
 * @author huang
 * @date 2020-06-14
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
	/**
	 * 查询功能
	 * @param quiryContent 查询内容
	 */
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
	
	/**
	 * 删除功能
	 * @param rowIndex 需要删除的记录在表格中的行号
	 */
	public void delete(int rowIndex) {
		int length = quiryResultMsg.getColumnCount();
		Object[] items = new Object[length];
		for(int i=0; i<length; i++) {
			items[i] = quiryResultMsg.getValueAt(rowIndex, i);
		}
		InstUpDelInsRecord delete = new InstUpDelInsRecord(quiryResultMsg.getQuiryType(), items);
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
	
	public void getIntoUpdatePanel(int rowIndex) {
		
		int length = quiryResultMsg.getColumnCount();
		
		String[] attrName = new String[length];
		Object[] oldRecord = new Object[length];
		
		for(int i=0; i<length; i++) {
			attrName[i] = quiryResultMsg.getColumnName(i);
			oldRecord[i] = quiryResultMsg.getValueAt(rowIndex, i);
		}
		
		uiControllerTwo = new PanelInquireRecordResult(attrName, oldRecord);
		uiControllerTwo.setLogicController(this);
		backgroundPanel.add("update", uiControllerTwo);
		card.show(backgroundPanel, "update");
		
	}
	
	public void update(Object[] oldAndNewRecord) {
		
		Boolean isSame = true;
		int attrNumber = oldAndNewRecord.length/2;
		for(int i=0; i<attrNumber; i++) {
			if(quiryResultMsg.getAttrType()[i] == 0) {
				try {
					oldAndNewRecord[i+attrNumber] = (short)Integer.parseInt((String)oldAndNewRecord[i+attrNumber]);
					if((short)oldAndNewRecord[i] != (short)oldAndNewRecord[i+attrNumber]) {
						isSame = false;
					}
				}catch(NumberFormatException e) {
					uiControllerTwo.showPopUpWindow("系统提示", "修改失败！第" + (i+1) +"个属性的格式错误，应为数字！");
					return;
				}
			}else if(quiryResultMsg.getAttrType()[i] == 1) {
				if(oldAndNewRecord[i] == null)
					oldAndNewRecord[i] = "";
				else
					oldAndNewRecord[i] = (String)oldAndNewRecord[i];
				if(((String)oldAndNewRecord[i+attrNumber]).equals("null"))
					oldAndNewRecord[i+attrNumber] = "";
				else
					oldAndNewRecord[i+attrNumber] = (String)oldAndNewRecord[i+attrNumber];
				if(!oldAndNewRecord[i].equals(oldAndNewRecord[i+attrNumber])) {
					isSame = false;
				}
			}
		}
		
		if(isSame) {
			uiControllerTwo.showPopUpWindow("系统提示", "您没有改变任何内容！");
		}else {
			InstUpDelInsRecord update = new InstUpDelInsRecord(quiryResultMsg.getQuiryType(), oldAndNewRecord);
			socket.sendData(new InstructionMsg(InstructionMsg.UPDATE_RECORD, update));
			/**
			 * 获取服务器的反馈信息并处理
			 */
			SimpleFeedbackMsg feedbackMsg = (SimpleFeedbackMsg)socket.recvDataObj();
			FramePopUpWindow popUp = new FramePopUpWindow("系统提示", feedbackMsg.getTextMsg());
			popUp.setVisible(true);
			if(feedbackMsg.getPermission()) {
				quire(quiryContent);
				returnToInquire();
			}
		}
		
	}

	public void getIntoInsertPanel() {
		
		if(quiryResultMsg == null) {
			uiController.showPopUpWindow("系统提示", "请先查询，然后再添加记录");
			return;
		}
		
		int length = quiryResultMsg.getColumnCount();
		
		String[] attrName = new String[length];
		
		for(int i=0; i<length; i++) {
			attrName[i] = quiryResultMsg.getColumnName(i);
		}
		
		uiControllerTwo = new PanelInquireRecordResult(attrName, null);
		uiControllerTwo.setLogicController(this);
		backgroundPanel.add("insert", uiControllerTwo);
		card.show(backgroundPanel, "insert");
		
	}
	
	public void insert(Object[] newRecord) {
		
		int attrNumber = newRecord.length;
		
		for(int i=0; i<attrNumber; i++) {
			if(quiryResultMsg.getAttrType()[i] == 0) {
				try {
					newRecord[i] = (short)Integer.parseInt((String)newRecord[i]);
				}catch(NumberFormatException e) {
					uiControllerTwo.showPopUpWindow("系统提示", "添加失败！第" + (i+1) +"个属性的格式错误，应为数字！");
					return;
				}
			}else if(quiryResultMsg.getAttrType()[i] == 1) {
				if(((String)newRecord[i]).equals("null"))
					newRecord[i] = "";
				else
					newRecord[i] = (String)newRecord[i];
			}
		}
		
		InstUpDelInsRecord insert = new InstUpDelInsRecord(quiryResultMsg.getQuiryType(), newRecord);
		socket.sendData(new InstructionMsg(InstructionMsg.INSERT_RECORD, insert));
		/**
		 * 获取服务器的反馈信息并处理
		 */
		SimpleFeedbackMsg feedbackMsg = (SimpleFeedbackMsg)socket.recvDataObj();
		FramePopUpWindow popUp = new FramePopUpWindow("系统提示", feedbackMsg.getTextMsg());
		popUp.setVisible(true);
		if(feedbackMsg.getPermission()) {
			quire(quiryContent);
			returnToInquire();
		}
		
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
	
	public void returnToInquire() {
		card.show(backgroundPanel, "Inquire");
		backgroundPanel.remove(uiControllerTwo);
	}
	
	
}
