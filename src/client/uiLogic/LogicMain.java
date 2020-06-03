package client.uiLogic;

import java.awt.CardLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import client.SocketClient;
import client.ui.FrameMain;
import client.ui.PanelInquireRecord;
import client.ui.PanelInquireRecordResult;

/**
 * 程序主界面逻辑
 * @author huang
 * @date 2020-06-02
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
		
		LogicInquireRecord logicInquireRecord = new LogicInquireRecord(this);
		PanelInquireRecord panelInquireRecord = new PanelInquireRecord(logicInquireRecord);
		PanelInquireRecordResult panelInquireRecordResult = new PanelInquireRecordResult();
		
		JPanel panelSwitch = new JPanel(new CardLayout());
		panelSwitch.add("inquire", panelInquireRecord);
		panelSwitch.add("result", panelInquireRecordResult);

		logicInquireRecord.setUIController(panelInquireRecord);
		logicInquireRecord.setBackgroundPanel(panelSwitch);
		logicInquireRecord.setSocket(socket);	
		
		uiController.addTabbedPanel("查/改记录", null, panelSwitch, null);
		
	}
	
	/**
	 * 在主界面上加入标签页面
	 * @param tabName 标签名称
	 * @param icon 标签图标
	 * @param newTabbedPanel 标签页面对象
	 * @param tip 页面注释
	 */
	public void addTabbedPanel(String tabName, Icon icon, JPanel newTabbedPanel, String tip) {
		uiController.addTabbedPanel(tabName, icon, newTabbedPanel, tip);
	}
	
	/**
	 * 在主界面上关闭标签页面
	 * @param tabbedPanel 标签页对象
	 */
	public void removeTabbedPanel(JPanel tabbedPanel) {
		uiController.removeTabbedPanel(tabbedPanel);
	}
	
	public SocketClient getSocket() {
		return socket;
	}

}
