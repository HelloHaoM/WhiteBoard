package org;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ChatDialog {
	public JDialog jDialog;
	private JLabel lblInputText;
	private JLabel lblChat;
	private JTextArea chatInput;
	private JTextArea chatShow;
	private JButton btnSend;
	private JButton btnClose;
	
	public ChatDialog(){
		init();
		jDialog.setModal(true);
		jDialog.setVisible(true);
	}
	
	public void init(){
		jDialog = new JDialog();
		jDialog.setBounds(400, 400, 400, 350);
		jDialog.getContentPane().setLayout(null);
		jDialog.setTitle("Chat Window");
		
		lblInputText = new JLabel("Input Text");
		lblInputText.setFont(new Font("Times New Roman", Font.BOLD,20));
		lblInputText.setBounds(57, 18, 110, 16);
		jDialog.getContentPane().add(lblInputText);
		
		lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Times New Roman", Font.BOLD,20));
		lblChat.setBounds(256, 18, 61, 16);
		jDialog.getContentPane().add(lblChat);
		
		chatInput = new JTextArea();
		chatInput.setBounds(204, 46, 173, 186);
		jDialog.getContentPane().add(chatInput);
		
		chatShow = new JTextArea();
		chatShow.setBounds(19, 46, 173, 186);
		jDialog.getContentPane().add(chatShow);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(50, 258, 117, 29);
		jDialog.getContentPane().add(btnSend);
		
		btnClose = new JButton("Close");
		btnClose.setBounds(225, 258, 117, 29);
		jDialog.getContentPane().add(btnClose);
	}
	

}
