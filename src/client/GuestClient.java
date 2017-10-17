package client;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.WhiteBoardClient;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;
import server.RemoteClient;

/**
 * multi-clients version v0.2 ManagerClient and GuestClient implement the main
 * function, which separates it from the GUI class
 * 
 * @author tianzhangh
 *
 */
public class GuestClient {
	/**
	 * Launch the application.
	 */
	static IRemoteClient remoteClient = null;
	static IRemoteServer remoteserver = null;
	static WhiteBoardClient window = null;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					remoteClient = new RemoteClient();
					// remoteClient.setClientName("client456");
					remoteClient.setClientName(args[0]);
					remoteClient.setClientLevel(RemoteClient.ClientLevel.USER);

					// Retrieve the stub/proxy for the remote object from the registry
					// Registry registry = LocateRegistry.getRegistry("localhost");
					Registry registry = null;
					// args[0] client name, args[1]room name, args[2] port, args[3] ip address
					try {
						registry = LocateRegistry.getRegistry(args[3]);
					} catch (ConnectException e) {
						JOptionPane.showMessageDialog(null, "Connection refused. \n", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}

					IRemoteWBService remoteWB = null;
					try {
						remoteWB = (IRemoteWBService) registry.lookup(IRemoteWBService.LOOKUP_NAME);
					} catch (ConnectException e) {
						JOptionPane.showMessageDialog(null, "Connection refused. \n Please check your server port.", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}

					// String roomname = "whiteboard1";
					String roomname = args[1];
					remoteserver = remoteWB.getRoom(remoteClient, roomname);

					// room does not exist
					if (remoteserver == null) {
						JOptionPane.showMessageDialog(null, "The room does not exist", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}

					System.out.println("Room: " + roomname);
					System.out.println("Manager: " + remoteserver.getManager().getClientName());

					window = new WhiteBoardClient(remoteClient, remoteserver);
					window.frame.setVisible(false);

					// name exist
					if (remoteserver.getClientNameList().contains(args[0])) {
						JOptionPane.showMessageDialog(null, "This room already has a manager.", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}

					// remoteserver.requestAdd(remoteClient.getClientName(), remoteClient);

					boolean join = remoteserver.permission(remoteClient.getClientName());
					if (join) {

						window.frame.setVisible(true);
						// client cannot do file operation
						window.frame.getJMenuBar().setVisible(false);
						// add some clients for test
						remoteserver.addClient(remoteClient);
						System.out.println("Client No. :" + remoteClient.getClientId());
						System.out.println("Client UserName: " + remoteClient.getClientName());
						remoteClient.retrieveImg(remoteserver.getImg());
						Set<String> nameList = remoteserver.getClientNameList();
						Iterator<String> it = nameList.iterator();
						while (it.hasNext()) {
							String name = it.next();
							System.out.println(name);
							window.getDlm().addElement(name);
							window.getJlist().setModel(window.getDlm());
						}

						((RemoteClient) remoteClient).setWhiteBoardClient(window);
					} else {
						JOptionPane.showMessageDialog(null, "Your request has benn denied.", "Error",
								JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}

					// quit( Add new )
					window.getFrame().addWindowListener(new WindowAdapter() {
						// IRemoteClient manager = remoteserver.getManager();
						public void windowClosing(WindowEvent we) {
							int i = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Warning",
									JOptionPane.YES_NO_OPTION);

							if (i == JOptionPane.YES_OPTION) {
								try {
									remoteserver.removeClient(remoteClient.getClientName());
								} catch (RemoteException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								window.getFrame().dispose();
								System.exit(0);
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		while (remoteserver != null) {

			try {
				Set<String> clientNameList = remoteserver.getClientNameList();
				remoteClient.alertClientList(clientNameList);
				if (!remoteserver.getClientNameList().contains(args[0])) {
					JOptionPane.showMessageDialog(null, "You have been kicked out!", "Error",
							JOptionPane.ERROR_MESSAGE);
					window.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					System.exit(0);
				}

			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				TimeUnit.SECONDS.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
