package client.ui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import client.uiLogic.LogicInquireRecord;
import share.message.QuiryResultMsg;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 用于详细展示查询结果的类
 * @author huang
 * @date 2020-06-13
 *
 */
public class PanelInquireRecordResult extends JPanel {

	private LogicInquireRecord logicController = null;
	
	private String[] attrName = null;
	private Object[] oldRecord = null;
	
	private JLabel[] labels = null;
	private JTextField[] textFields = null;
	private JButton buttonReturn = null;
	private JButton buttonOK = null;
	
	private int attrNumber = 0;
	
	/**
	 * Create the panel.
	 */
	public PanelInquireRecordResult(String[] attrName, Object[] oldRecord) {
		
		this.attrNumber = attrName.length;
		this.attrName = attrName;
		this.oldRecord = oldRecord;
		
		this.setLayout(new GridLayout(attrNumber+1, 2));
		
		labels = new JLabel[attrNumber];
		textFields = new JTextField[attrNumber];
		
		for(int i=0; i<attrNumber; i++) {
			
			labels[i] = new JLabel(attrName[i]);
			labels[i].setHorizontalAlignment(SwingConstants.CENTER);
			add(labels[i]);
			
			textFields[i] = new JTextField();
			textFields[i].setText(String.valueOf(oldRecord[i]));
			add(textFields[i]);
			
		}
		
		buttonReturn = new JButton("返回");
		buttonReturn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logicController.returnToInquire();
			}
		});
		add(buttonReturn);
		
		buttonOK = new JButton("确认修改");
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Object[] oldAndNewRecord = new Object[attrNumber*2];
				for(int i=0; i<attrNumber; i++) {
					oldAndNewRecord[i] = oldRecord[i];
				}
				for(int i=attrNumber; i<attrNumber*2; i++) {
					oldAndNewRecord[i] = textFields[i-attrNumber].getText();
				}
				
				logicController.update(oldAndNewRecord);
				
			}
		});
		add(buttonOK);
		
	}
	
	public void setLogicController(LogicInquireRecord logicController) {
		this.logicController = logicController;
	}
	
	public void showPopUpWindow(String windowTitle, String textMsg) {
		FramePopUpWindow popUp = new FramePopUpWindow(windowTitle, textMsg);
		popUp.setVisible(true);
	}

}
