package client.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

import client.uiLogic.LogicInquireRecord;
import client.uiLogic.LogicMain;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 * 查询/修改记录的界面
 * @author huang
 * @date 2020-05-25
 *
 */
public class PanelInquireRecord extends JPanel {
	
	private LogicInquireRecord logicController = null;
	
	private JTextField textFieldQuiry = null;  //文本输入框
	private JButton buttonInquiry = null;  //查询按钮
	private JList listShowInquiry = null;  //查询结果列表
	private JComboBox comboBoxCategorySelect = null;  //查询类型选框

	/**
	 * Create the panel.
	 */
	public PanelInquireRecord() {
		
		super();
		
		//布局使用SpringLayout
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		//给界面组件赋值
		textFieldQuiry = new JTextField();
		buttonInquiry = new JButton("搜索");
		listShowInquiry = new JList();
		comboBoxCategorySelect = new JComboBox();
		
		//查询结果列表设定
		springLayout.putConstraint(SpringLayout.SOUTH, listShowInquiry, -40, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.NORTH, listShowInquiry, 123, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, listShowInquiry, 23, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, listShowInquiry, -23, SpringLayout.EAST, this);
		add(listShowInquiry);
		
		//查询按钮设定
		springLayout.putConstraint(SpringLayout.NORTH, buttonInquiry, 0, SpringLayout.NORTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.SOUTH, buttonInquiry, -40, SpringLayout.NORTH, listShowInquiry);
		springLayout.putConstraint(SpringLayout.WEST, buttonInquiry, 5, SpringLayout.EAST, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.EAST, buttonInquiry, 0, SpringLayout.EAST, listShowInquiry);
		add(buttonInquiry);
		
		//文本输入框设定
		springLayout.putConstraint(SpringLayout.NORTH, textFieldQuiry, 40, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, textFieldQuiry, 123, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, textFieldQuiry, -40, SpringLayout.NORTH, listShowInquiry);
		springLayout.putConstraint(SpringLayout.EAST, textFieldQuiry, -123, SpringLayout.EAST, this);
		add(textFieldQuiry);
		textFieldQuiry.setColumns(10);
		
		//查询类型选框设定
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxCategorySelect, 0, SpringLayout.NORTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxCategorySelect, 0, SpringLayout.WEST, listShowInquiry);
		springLayout.putConstraint(SpringLayout.SOUTH, comboBoxCategorySelect, -40, SpringLayout.NORTH, listShowInquiry);
		springLayout.putConstraint(SpringLayout.EAST, comboBoxCategorySelect, -5, SpringLayout.WEST, textFieldQuiry);
		comboBoxCategorySelect.addItem("教师");
		comboBoxCategorySelect.addItem("教学安排");
		comboBoxCategorySelect.addItem("课程");
		comboBoxCategorySelect.addItem("班级");
		comboBoxCategorySelect.addItem("特殊教室");
		add(comboBoxCategorySelect);
		
	}
	
	public void setLogicController(LogicInquireRecord logicController) {
		this.logicController = logicController;
	}
}
