package client.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Font;
import javax.swing.JButton;

/**
 * 客户端UI的参考界面
 * @author huang
 * @date 2020-05-06
 *
 */
public class FrameTemplete extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton button;
	private JButton button_1;

	/**
	 * Create the frame.
	 */
	public FrameTemplete() {
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
		label.setBounds(130, 145, 51, 36);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("密码");
		label_1.setFont(new Font("SansSerif", Font.BOLD, 16));
		label_1.setBounds(130, 198, 59, 18);
		contentPane.add(label_1);
		
		textField = new JTextField();
		textField.setBounds(180, 143, 170, 40);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(180, 192, 170, 40);
		contentPane.add(passwordField);
		
		button = new JButton("登录");
		button.setFont(new Font("SansSerif", Font.BOLD, 16));
		button.setBounds(114, 255, 105, 28);
		contentPane.add(button);
		
		button_1 = new JButton("退出");
		button_1.setFont(new Font("SansSerif", Font.BOLD, 16));
		button_1.setBounds(243, 255, 105, 28);
		contentPane.add(button_1);
	}
}
