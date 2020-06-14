package client.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * 系统弹窗类
 * @author huang
 * @date 2020-06-14
 *
 */
public class FramePopUpWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public FramePopUpWindow(String title ,String textMsg) {
		
		setTitle(title);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 180);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel labelText = new JLabel(textMsg);
		labelText.setHorizontalAlignment(SwingConstants.CENTER);
		labelText.setBounds(12, 12, 274, 89);
		contentPane.add(labelText);
		
		JButton buttonOK = new JButton("确认");
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonOK.setBounds(95, 111, 105, 28);
		contentPane.add(buttonOK);
	}

}
