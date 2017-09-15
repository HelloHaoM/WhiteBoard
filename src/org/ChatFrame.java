package org;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ChatFrame {
	public JFrame frame;
	private JLabel lblInputText;
	private JLabel lblChat;
	private JTextArea chatInput;
	private JTextArea chatShow;
	private JButton btnSend;
	private JButton btnClose;
	
	
	public ChatFrame() {
		initialize();
		
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
			}
		});
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(400, 400, 400, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Chat Window");
		
		lblInputText = new JLabel("Input Text");
		lblInputText.setFont(new Font("Times New Roman", Font.BOLD,20));
		lblInputText.setBounds(57, 18, 110, 16);
		frame.getContentPane().add(lblInputText);
		
		lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Times New Roman", Font.BOLD,20));
		lblChat.setBounds(256, 18, 61, 16);
		frame.getContentPane().add(lblChat);
		
		chatInput = new JTextArea();
		chatInput.setBounds(204, 46, 173, 186);
		frame.getContentPane().add(chatInput);
		
		chatShow = new JTextArea();
		chatShow.setBounds(19, 46, 173, 186);
		frame.getContentPane().add(chatShow);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(50, 258, 117, 29);
		frame.getContentPane().add(btnSend);
		
		btnClose = new JButton("Close");
		btnClose.setBounds(225, 258, 117, 29);
		frame.getContentPane().add(btnClose);

		
	}

}
