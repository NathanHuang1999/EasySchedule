package client.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import client.uiLogic.LogicInquireRecord;
import client.uiLogic.LogicMain;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * 查询/修改记录的界面
 * @author huang
 * @date 2020-06-12
 *
 */
public class PanelInquireRecord extends JPanel {
	
	private LogicInquireRecord logicController = null;
	
	private SpringLayout springLayout = null;
	private JTextField textFieldQuiry = null;  //文本输入框
	private JButton buttonInquiry = null;  //查询按钮
	private JTable tableShowInquiry = null;
	private JPopupMenu popupMenu = null;
	private JTableHeader tableHeader = null;
	private JComboBox comboBoxCategorySelect = null;  //查询类型选框
	private JLabel labelAdvancedQuiry = null;  //高级查询选项
	private JButton buttonCloseThisTab = null;
	
	private int focusRow = -1;
	
	private String[] AdvancedQuiryItem = null;

	/**
	 * Create the panel.
	 */
	public PanelInquireRecord(LogicInquireRecord logicController) {
		
		super();
		this.logicController = logicController;
		
		//布局使用SpringLayout
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		//给界面组件赋值
		textFieldQuiry = new JTextField();
		buttonInquiry = new JButton("搜索");
		buttonInquiry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logicController.quire(textFieldQuiry.getText());
			}
		});
		tableShowInquiry = new JTable();
		tableHeader = tableShowInquiry.getTableHeader();
		comboBoxCategorySelect = new JComboBox();
		
		//为查询类型选框添加选择事件
		comboBoxCategorySelect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(arg0.getStateChange() == arg0.SELECTED){
				    logicController.setCategorySelect((String)comboBoxCategorySelect.getSelectedItem());
				}
			}
		});
		
		
		//查询结果列表设定
		springLayout.putConstraint(SpringLayout.SOUTH, tableShowInquiry, -40, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.NORTH, tableShowInquiry, 143, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, tableShowInquiry, 23, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, tableShowInquiry, -23, SpringLayout.EAST, this);
		tableShowInquiry.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	mouseRightButtonClick(evt);
            }
		});
		add(tableShowInquiry);
		createPopupMenu();

		//文本输入框设定
		springLayout.putConstraint(SpringLayout.NORTH, textFieldQuiry, 40, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, textFieldQuiry, 123, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, textFieldQuiry, -60, SpringLayout.NORTH, tableShowInquiry);
		springLayout.putConstraint(SpringLayout.EAST, textFieldQuiry, -123, SpringLayout.EAST, this);
		add(textFieldQuiry);
		textFieldQuiry.setColumns(10);
		
		//查询结果列表表头设定
		springLayout.putConstraint(SpringLayout.SOUTH, tableHeader, 0, SpringLayout.NORTH, tableShowInquiry);
		springLayout.putConstraint(SpringLayout.NORTH, tableHeader, 40, SpringLayout.SOUTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.WEST, tableHeader, 0, SpringLayout.WEST, tableShowInquiry);
		springLayout.putConstraint(SpringLayout.EAST, tableHeader, 0, SpringLayout.EAST, tableShowInquiry);
		add(tableHeader);
				
		//查询按钮设定
		springLayout.putConstraint(SpringLayout.NORTH, buttonInquiry, 0, SpringLayout.NORTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.SOUTH, buttonInquiry, 0, SpringLayout.SOUTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.WEST, buttonInquiry, 5, SpringLayout.EAST, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.EAST, buttonInquiry, 0, SpringLayout.EAST, tableShowInquiry);
		add(buttonInquiry);
		
		//查询类型选框设定
		springLayout.putConstraint(SpringLayout.NORTH, comboBoxCategorySelect, 0, SpringLayout.NORTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.WEST, comboBoxCategorySelect, 0, SpringLayout.WEST, tableShowInquiry);
		springLayout.putConstraint(SpringLayout.SOUTH, comboBoxCategorySelect, 0, SpringLayout.SOUTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.EAST, comboBoxCategorySelect, -5, SpringLayout.WEST, textFieldQuiry);
		/* 放弃的设计
		comboBoxCategorySelect.addItem("-请选择-");
		comboBoxCategorySelect.addItem("教师");
		comboBoxCategorySelect.addItem("教学安排");
		comboBoxCategorySelect.addItem("课程");
		comboBoxCategorySelect.addItem("班级");
		comboBoxCategorySelect.addItem("特殊教室");
		*/
		comboBoxCategorySelect.addItem("-请选择-");
		comboBoxCategorySelect.addItem("教师");
		comboBoxCategorySelect.addItem("教学安排");
		comboBoxCategorySelect.addItem("课程");
		comboBoxCategorySelect.addItem("班级");
		comboBoxCategorySelect.addItem("特殊教室");
		comboBoxCategorySelect.addItem("电话号码");
		comboBoxCategorySelect.addItem("老师能够教学的课程");
		add(comboBoxCategorySelect);
		
		//高级查询选项设定
		labelAdvancedQuiry = new JLabel(">高级搜索");
		labelAdvancedQuiry.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO 高级查询模块
			}
		});
		labelAdvancedQuiry.setForeground(new Color(0, 0, 139));
		springLayout.putConstraint(SpringLayout.NORTH, labelAdvancedQuiry, 5, SpringLayout.SOUTH, buttonInquiry);
		springLayout.putConstraint(SpringLayout.WEST, labelAdvancedQuiry, 0, SpringLayout.WEST, buttonInquiry);
		springLayout.putConstraint(SpringLayout.SOUTH, labelAdvancedQuiry, -20, SpringLayout.NORTH, tableShowInquiry);
		add(labelAdvancedQuiry);
		
		buttonCloseThisTab = new JButton("关闭选项卡");
		springLayout.putConstraint(SpringLayout.WEST, buttonCloseThisTab, 0, SpringLayout.WEST, comboBoxCategorySelect);
		springLayout.putConstraint(SpringLayout.SOUTH, buttonCloseThisTab, -10, SpringLayout.NORTH, comboBoxCategorySelect);
		springLayout.putConstraint(SpringLayout.EAST, buttonCloseThisTab, 0, SpringLayout.EAST, comboBoxCategorySelect);
		springLayout.putConstraint(SpringLayout.NORTH, buttonCloseThisTab, -30, SpringLayout.NORTH, comboBoxCategorySelect);
		buttonCloseThisTab.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logicController.removeThisTab();
			}
		});
		add(buttonCloseThisTab);
		
	}
	
	public void setAdvancedQuiryItem(String[] items) {
		this.AdvancedQuiryItem = items;
	}
	
	public void setLogicController(LogicInquireRecord logicController) {
		this.logicController = logicController;
	}
	
	public void fillTable(TableModel tableModel) {
		tableShowInquiry.setModel(tableModel);
		remove(tableHeader);
		tableHeader = tableShowInquiry.getTableHeader();
		springLayout.putConstraint(SpringLayout.SOUTH, tableHeader, 0, SpringLayout.NORTH, tableShowInquiry);
		springLayout.putConstraint(SpringLayout.NORTH, tableHeader, 40, SpringLayout.SOUTH, textFieldQuiry);
		springLayout.putConstraint(SpringLayout.WEST, tableHeader, 0, SpringLayout.WEST, tableShowInquiry);
		springLayout.putConstraint(SpringLayout.EAST, tableHeader, 0, SpringLayout.EAST, tableShowInquiry);
		add(tableHeader);
	}
	
	//鼠标右键点击事件
    private void mouseRightButtonClick(MouseEvent evt) {
        //判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            //通过点击位置找到点击为表格中的行
            int focusedRowIndex = tableShowInquiry.rowAtPoint(evt.getPoint());
            if (focusedRowIndex == -1) {
                return;
            }
            focusRow = focusedRowIndex;
            //将表格所选项设为当前右键点击的行
            tableShowInquiry.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
            //弹出菜单
            popupMenu.show(tableShowInquiry, evt.getX(), evt.getY());
        }
 
    }
	
	private void createPopupMenu() {
		
		popupMenu = new JPopupMenu();
		
		JMenuItem updateMenItem = new JMenuItem();
        updateMenItem.setText(" 修改 ");
        updateMenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	logicController.getIntoUpdatePanel(focusRow);
            }
        });
        popupMenu.add(updateMenItem);
        
        JMenuItem delMenItem = new JMenuItem();
        delMenItem.setText(" 删除 ");
        delMenItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	logicController.delete(focusRow);
            }
        });
        popupMenu.add(delMenItem);
    }
}
