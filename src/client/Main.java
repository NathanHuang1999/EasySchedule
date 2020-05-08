package client;

import java.awt.EventQueue;

import client.log.Login;
import client.ui.FrameLogin;

/**
 * 客户端的程序入口类
 * @author huang
 * @date 2020-05-08
 * 
 */
public class Main {

	/**
	 * 客户端的程序入口函数
	 * @param args 并不需要的参数列表
	 */	  
	  public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						FrameLogin login = new FrameLogin();
						login.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	 

}
