package client.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
 * @date 2020-05-10
 *
 */
public class FrameMain extends JFrame {

	private SocketBase socket = null;
	private String authorityCode = null;
	
	/**
	 * Create the frame.
	 */
	public FrameMain(SocketBase socket, String authorityCode) {
		
		this.socket = socket;
		this.authorityCode = authorityCode;
		
		setTitle("易排课客户端");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
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
		
		JMenuItem menuItemInquireRecord = new JMenuItem("查询/修改记录");
		menuRecord.add(menuItemInquireRecord);
		
		JMenuItem menuItemAddNewRecord = new JMenuItem("增加新记录");
		menuRecord.add(menuItemAddNewRecord);
		
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
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
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
