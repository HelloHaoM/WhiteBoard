package org;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import remote.IRemoteClient;
import remote.IRemoteServer;
/**
 * multi-clients version v 0.2
 * @author tianzhangh
 *
 */
public class ChatDialog{
	public JDialog jDialog;
	private JLabel lblInputText;
	private JLabel lblChat;
	private JTextArea chatInput;
	private JTextArea chatShow;
	private JButton btnSend;
	private JButton btnClose;
	
	private IRemoteServer server;
	private IRemoteClient client;
	
	public ChatDialog(IRemoteClient client, IRemoteServer server) throws RemoteException{
		this.client = client;
		this.server = server;
		
		init();
		
		jDialog.setModal(false);
		jDialog.setVisible(false);
		
		
		btnSend.addActionListener(new ActionListener () {

			public void actionPerformed(ActionEvent e) {
				try {
					//add localized time for the chat message
					LocalDateTime date = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					server.sendMessage(client.getClientName()+"["+date.format(formatter)+"]: "+chatInput.getText()+"\n");
					chatInput.setText("");
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnClose.addActionListener(new ActionListener () {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jDialog.setVisible(false);
			}
		});

	}
	
	public void init() throws RemoteException{
		jDialog = new JDialog();
		
		jDialog.setBounds(400, 400, 639, 490);
		jDialog.getContentPane().setLayout(null);
		jDialog.setTitle("Chat Window: "+ client.getClientName());
		
		lblInputText = new JLabel("Input Text");
		lblInputText.setFont(new Font("Times New Roman", Font.BOLD,20));
		lblInputText.setBounds(12, 245, 110, 29);
		jDialog.getContentPane().add(lblInputText);
		
		lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Times New Roman", Font.BOLD,20));
		lblChat.setBounds(12, 13, 61, 16);
		jDialog.getContentPane().add(lblChat);
		
		chatShow = new JTextArea();
		chatShow.setFont(new Font("Monospaced", Font.PLAIN, 15));
		chatShow.setEditable(false);
		chatShow.setBounds(12, 35, 597, 197);
		jDialog.getContentPane().add(chatShow);
		
		chatInput = new JTextArea();
		chatInput.setFont(new Font("Monospaced", Font.PLAIN, 15));
		chatInput.setBounds(12, 274, 468, 156);
		jDialog.getContentPane().add(chatInput);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(492, 401, 117, 29);
		jDialog.getContentPane().add(btnSend);
		
		btnClose = new JButton("Close");
		btnClose.setBounds(492, 356, 117, 29);
		jDialog.getContentPane().add(btnClose);
		
	}
	
	public JTextArea getChatShow() {
		return this.chatShow;
	}	

}
