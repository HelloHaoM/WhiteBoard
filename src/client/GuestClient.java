package client;

import java.awt.EventQueue;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IRemoteClient remoteClient = new RemoteClient();
					//remoteClient.setClientName("client456");
					remoteClient.setClientName(args[0]);
					remoteClient.setClientLevel(RemoteClient.ClientLevel.USER);

					// Retrieve the stub/proxy for the remote object from the registry
					Registry registry = LocateRegistry.getRegistry("localhost");

					IRemoteWBService remoteWB = (IRemoteWBService) registry.lookup(IRemoteWBService.LOOKUP_NAME);

					//String roomname = "whiteboard1";
					String roomname = args[1];
					IRemoteServer remoteserver = remoteWB.getRoom(remoteClient, roomname);

					System.out.println("Room: " + roomname);
					System.out.println("Manager: " + remoteserver.getManager().getClientName());

					WhiteBoardClient window = new WhiteBoardClient(remoteClient, remoteserver);
					window.frame.setVisible(false);
					
					//remoteserver.requestAdd(remoteClient.getClientName(), remoteClient);

					boolean join = remoteserver.permission(remoteClient.getClientName());
					if (join) {
						
						window.frame.setVisible(true);
						// add some clients for test
						remoteserver.addClient(remoteClient);
						System.out.println("Client No. :" + remoteClient.getClientId());
						System.out.println("Client UserName: " + remoteClient.getClientName());
						
						Set<String> nameList = remoteserver.getClientNameList();
						Iterator<String> it = nameList.iterator();
						while (it.hasNext()) {
							String name = it.next();
							System.out.println(name);
							window.getDlm().addElement(name);
							window.getJlist().setModel(window.getDlm());
						}

						((RemoteClient) remoteClient).setWhiteBoardClient(window);
						// Synchoronize the file 
						
					}

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
