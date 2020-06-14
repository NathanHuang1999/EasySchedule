package client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.SocketClient;
import client.uiLogic.LogicMain;
import share.SocketBase;

import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;

/**
 * 程序主界面UI
 * @author huang
 * @date 2020-06-15
 *
 */
public class FrameMain extends JFrame {

	private LogicMain logicController = null;
	private JPanel contentPanel = null;
	private JTabbedPane panelTabbedBackground = null;
	
	/**
	 * Create the frame.
	 */
	public FrameMain() {
		
		setTitle("易排课客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 618);
		setLocationRelativeTo(null); 
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menuAccount = new JMenu("账户");
		menuBar.add(menuAccount);
		
		JMenuItem menuItemCheckAccountInfo = new JMenuItem("查看账户信息");
		menuAccount.add(menuItemCheckAccountInfo);
		
		JMenuItem menuItemExit = new JMenuItem("退出系统");
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuAccount.add(menuItemExit);
		
		JMenu menuRecord = new JMenu("记录");
		menuBar.add(menuRecord);
		
		JMenuItem menuItemInquireRecord = new JMenuItem("查询/处理记录");
		menuItemInquireRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logicController.inquireOrUpdateRecord();
			}
		});
		menuRecord.add(menuItemInquireRecord);
		
		JMenu menuSchedule = new JMenu("排课");
		menuBar.add(menuSchedule);
		
		JMenuItem menuItemInquireTable = new JMenuItem("查看/修改课表");
		menuSchedule.add(menuItemInquireTable);
		
		JMenuItem menuItemAddNewTable = new JMenuItem("新建课表");
		menuSchedule.add(menuItemAddNewTable);
		
		JMenuItem menuItemInquireConstraint = new JMenuItem("查看/修改约束");
		menuSchedule.add(menuItemInquireConstraint);
		
		JMenuItem menuItemAddNewConstraint = new JMenuItem("新建约束");
		menuSchedule.add(menuItemAddNewConstraint);
		
		JMenu menuHelp = new JMenu("帮助");
		menuBar.add(menuHelp);
		
		JMenuItem menuItemAbout = new JMenuItem("关于");
		menuHelp.add(menuItemAbout);
		
		//contentPanel = new JLayeredPane();
		//contentPanel = new JPanel();
		//setContentPane(contentPanel);
		//PanelTabbedBackground = new JTabbedPane(JTabbedPane.LEFT);
		//contentPanel.add(PanelTabbedBackground, new Integer(1));
		//contentPanel.add(PanelTabbedBackground);
		
		panelTabbedBackground = new JTabbedPane(JTabbedPane.LEFT);
		getContentPane().add(panelTabbedBackground, BorderLayout.CENTER);
		
	}

	/**
	 * 在主界面上加入标签页面
	 * @param tabName 标签名称
	 * @param icon 标签图标
	 * @param newTabbedPanel 标签页面对象
	 * @param tip 页面注释
	 */
	public void addTabbedPanel(String tabName, Icon icon, JPanel newTabbedPanel, String tip) {
		panelTabbedBackground.addTab(tabName, icon, newTabbedPanel, tip);
	}
	
	/**
	 * 在主界面上关闭标签页面
	 * @param tabbedPanel 标签页对象
	 */
	public void removeTabbedPanel(JPanel tabbedPanel) {
		int i = panelTabbedBackground.indexOfComponent(tabbedPanel);
	    if (i != -1) {
	    	panelTabbedBackground.remove(i);
	    }
	}
	
	public void setLogicController(LogicMain logicController) {
		this.logicController = logicController;
	}
	
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
