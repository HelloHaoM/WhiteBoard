package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.WhiteBoardClient;

import client.ClientStart;
import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;
import server.RemoteClient;

public class ClientStart {

	private JFrame frame;

	JComboBox jcb_level;
	JLabel level;
	JLabel client;
	JLabel room;
	JLabel clientID;
	JLabel serverPort;
	JLabel serverIP;

	JTextField nameClient;
	JTextField nameRoom;
	JTextField idClient;
	JTextField port;
	JTextField address;

	JButton confirmBtn;

	JPanel jp1;
	JPanel jp2;

	public ClientStart() {

		frame = new JFrame();

		jp1 = new JPanel();
		jp1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

		clientID = new JLabel("please input the ID of the client: ");
		clientID.setFont(new Font("Arial", Font.PLAIN, 12));
		idClient = new JTextField(12);
		jp1.add(clientID);
		jp1.add(idClient);

		client = new JLabel("please input the name of the client: ");
		client.setFont(new Font("Arial", Font.PLAIN, 12));
		nameClient = new JTextField(10);
		jp1.add(client);
		jp1.add(nameClient);

		room = new JLabel("please input the name of the room: ");
		room.setFont(new Font("Arial", Font.PLAIN, 12));
		nameRoom = new JTextField(10);
		jp1.add(room);
		jp1.add(nameRoom);
		
		serverPort = new JLabel("please input the sever port:         ");
		serverPort.setFont(new Font("Arial", Font.PLAIN, 12));
		port = new JTextField(12);
		jp1.add(serverPort);
		jp1.add(port);
		
		serverIP = new JLabel("Please input the IP address:    ");
		serverIP.setFont(new Font("Arial", Font.PLAIN, 12));
		address = new JTextField(12);
		jp1.add(serverIP);
		jp1.add(address);
		
		level = new JLabel("Client Level: ");
		level.setFont(new Font("Arial", Font.PLAIN, 12));
		String str1[] = { "Manager", "User"};
		jcb_level = new JComboBox(str1);
		jp1.add(level);
		jp1.add(jcb_level);

		jp2 = new JPanel();
		confirmBtn = new JButton();
		confirmBtn.setText("Confirm");
		confirmBtn.setFont(new Font("Arial", Font.PLAIN, 12));
		jp2.add(confirmBtn);

		frame.add(jp1);
		frame.add(jp2, BorderLayout.SOUTH);
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String idStr = idClient.getText();
				String clientStr = nameClient.getText();
				String roomStr = nameRoom.getText();
				String portStr = port.getText();
				String ipStr = address.getText();


				// handle the exceptions of wrong inputs
				// client ID
				if(idStr.length() == 0 && jcb_level.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null,
	                        "The client ID must be the number", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				for (int i = 0; i < idStr.length(); i++) {
					if (idStr.charAt(i) < 48 || idStr.charAt(i) > 57) {
						JOptionPane.showMessageDialog(null,
		                        "The client ID must be the number", "Error",
		                        JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				}
				// client name
				if (clientStr.length() == 0) {
					JOptionPane.showMessageDialog(null,
	                        "Please input the valid client name", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				// room name
				if (roomStr.length() == 0) {
					JOptionPane.showMessageDialog(null,
	                        "Please input the valid room name", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				//server port
				int portInt = Integer.parseInt(portStr);
				if((portInt>65535) ||(portInt<1024) ) {
					JOptionPane.showMessageDialog(null,
	                        "Please input the valid server port (1024-65535)", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				//IP address
				if(ipStr.length() != 0) {
					String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+
		                      "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
		                      "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+
		                      "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
					if (!ipStr.matches(regex)) {
						JOptionPane.showMessageDialog(null,
		                        "Please input the valid ip address", "Error",
		                        JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
					
				}else {
					JOptionPane.showMessageDialog(null,
	                        "Please input the valid ip address", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				if (jcb_level.getSelectedIndex() == 0) { // Manager
					String[] args = { idStr, clientStr, roomStr, portStr, ipStr };
					ManagerClient.main(args);
				} else if (jcb_level.getSelectedIndex() == 1) { // User (Guest)
					String[] args = { clientStr, roomStr, portStr, ipStr };
					GuestClient.main(args);
				}

				frame.dispose();
			}
		});

		frame.setSize(400, 300);
		frame.setTitle("Client Start");
		frame.setLocationRelativeTo(null);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientStart window = new ClientStart();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
