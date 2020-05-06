package client.log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * 用于处理登录事务的类，包含UI和逻辑
 * @author huang
 * @date 2020-05-06
 *
 */
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton button;
	private JButton button_1;

	/**
	 * Create the frame.
	 */
	public Login() {
		setResizable(false);
		setTitle("易排课客户端-登录");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("账户");
		label.setFont(new Font("SansSerif", Font.BOLD, 16));
		label.setBounds(113, 145, 51, 36);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("密码");
		label_1.setFont(new Font("SansSerif", Font.BOLD, 16));
		label_1.setBounds(113, 205, 59, 18);
		contentPane.add(label_1);
		
		textField = new JTextField();
		textField.setBounds(170, 143, 180, 40);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(170, 192, 180, 40);
		contentPane.add(passwordField);
		
		button = new JButton("登录");
		button.setFont(new Font("SansSerif", Font.BOLD, 16));
		button.setBounds(114, 255, 105, 28);
		contentPane.add(button);
		
		button_1 = new JButton("退出");
		button_1.setFont(new Font("SansSerif", Font.BOLD, 16));
		button_1.setBounds(243, 255, 105, 28);
		contentPane.add(button_1);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
