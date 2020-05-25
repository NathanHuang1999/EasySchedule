package client.uiLogic;

import client.SocketClient;
import client.ui.FrameMain;
import client.ui.PanelInquireRecord;

/**
 * 程序主界面逻辑
 * @author huang
 * @date 2020-05-24
 *
 */
public class LogicMain {
	
	private FrameMain uiController = null;
	private SocketClient socket = null;
	private String authorityCode = null;
	
	public LogicMain(SocketClient socket, String authorityCode) {
		this.socket = socket;
		this.authorityCode = authorityCode;
	}
	
	public void setUIController(FrameMain uiController) {
		this.uiController = uiController;
	}
	
	public void inquireOrUpdateRecord() {
		PanelInquireRecord panelInquireRecord = new PanelInquireRecord();
		LogicInquireRecord logicInquireRecord = new LogicInquireRecord();
		panelInquireRecord.setLogicController(logicInquireRecord);
		logicInquireRecord.setUIController(panelInquireRecord);
		uiController.addTabbedPanel("查/改记录", null, panelInquireRecord, null);
	}
	
	public SocketClient getSocket() {
		return socket;
	}

}
