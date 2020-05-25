package client;

import java.awt.EventQueue;

import client.ui.FrameLogin;
import client.uiLogic.LogicLogin;

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
						FrameLogin frameLogin = new FrameLogin();
						LogicLogin logicLogin = new LogicLogin();
						frameLogin.setLogicController(logicLogin);
						logicLogin.setUIController(frameLogin);
						frameLogin.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	 

}
