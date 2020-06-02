package client.uiLogic;

import client.SocketClient;
import client.ui.FrameMain;
import client.ui.PanelInquireRecord;

/**
 * 程序主界面逻辑
 * @author huang
 * @date 2020-05-28
 *
 */
public class LogicMain {
	
	private FrameMain uiController = null;
	private SocketClient socket = null;
	
	public LogicMain(SocketClient socket) {
		this.socket = socket;
	}
	
	public void setUIController(FrameMain uiController) {
		this.uiController = uiController;
	}
	
	public void inquireOrUpdateRecord() {
		LogicInquireRecord logicInquireRecord = new LogicInquireRecord();
		PanelInquireRecord panelInquireRecord = new PanelInquireRecord(logicInquireRecord);
		logicInquireRecord.setUIController(panelInquireRecord);
		logicInquireRecord.setSocket(socket);
		uiController.addTabbedPanel("查/改记录", null, panelInquireRecord, null);
	}
	
	public SocketClient getSocket() {
		return socket;
	}

}
