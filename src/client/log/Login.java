package client.log;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import client.socket.SocketBase;

/**
 * 用于处理登录事务的类，包含UI和逻辑
 * @author huang
 * @date 2020-05-07
 *
 */
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField accountField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton exitButton;
	private JLabel portLabel;
	private JTextField portField;
	private JLabel userImgLabel;
	private SocketBase socket;
	private String permission;
	private String authority;

	/**
	 * 创建窗口
	 */
	public Login() {
		
		setFrame();
		setIpAndPortField();
		setIpAndPortLabel();
		setAccountLabel();
		setAccountField();
		setPasswordLabel();
		setPasswordField();
		setLoginButton();
		setExitButton();
		setUserImgLabel();
		permission = null;
		authority = null;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public SocketBase getSocket() {
		return socket;
	}
	
	public String getAuthority() {
		return authority;
	}
	
	private void setLoginButton() {
		loginButton = new JButton("登录");
		/*
		 * 按下登录按钮后获取三个文本框中的内容并尝试和服务器建立联系
		 */
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * 获取端口号、账户、密码
				 */
				String portString = portField.getText();
				int port = Integer.parseInt(portString);
				String account = accountField.getText();
				String password = String.valueOf(passwordField.getPassword());
				byte[] loginMsg = (account + " " + password).getBytes();
				socket = new SocketBase(port);
				socket.sendData(loginMsg);
				byte[] recvByte = socket.recvData();
				String recvMsg = new String(recvByte, 0, recvByte.length);
				int sep = recvMsg.indexOf(' ');
				if(sep == -1) {
					permission = recvMsg;
				}else {
					permission = recvMsg.substring(0, sep);
					authority = recvMsg.substring(sep+1);
				}
			}
		});
		loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		loginButton.setBounds(114, 295, 105, 28);
		contentPane.add(loginButton);
	}
	
	private void setFrame() {
		setResizable(false);
		setTitle("易排课客户端-登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 370);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
	
	private void setUserImgLabel() {
		userImgLabel = new JLabel(new ImageIcon(Login.class.getResource("/img/user.png")));
		userImgLabel.setBounds(173, 15, 105, 124);
		contentPane.add(userImgLabel);
	}
	
	private void setIpAndPortLabel() {
		portLabel = new JLabel("端口号");
		portLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		portLabel.setBounds(101, 150, 51, 36);
		contentPane.add(portLabel);
	}
	
	private void setIpAndPortField() {
		portField = new JTextField();
		portField.setText("1200");  //TODO 此处应记录上一次登录时的IP和端口号
		portField.setColumns(10);
		portField.setBounds(170, 155, 180, 30);
		contentPane.add(portField);
	}
	
	private void setAccountLabel() {
		JLabel accountLabel = new JLabel("账户");
		accountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		accountLabel.setBounds(113, 200, 51, 36);
		contentPane.add(accountLabel);
	}
	
	private void setAccountField() {
		accountField = new JTextField();
		accountField.setBounds(170, 202, 180, 30);
		contentPane.add(accountField);
		accountField.setColumns(10);
	}
	
	private void setPasswordLabel() {
		JLabel passwordLabel = new JLabel("密码");
		passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		passwordLabel.setBounds(113, 255, 59, 18);
		contentPane.add(passwordLabel);
	}
	
	private void setPasswordField() {
		passwordField = new JPasswordField();
		passwordField.setBounds(170, 250, 180, 30);
		contentPane.add(passwordField);
	}
	
	private void setExitButton() {
		exitButton = new JButton("退出");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		exitButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		exitButton.setBounds(243, 295, 105, 28);
		contentPane.add(exitButton);
	}
	
}
