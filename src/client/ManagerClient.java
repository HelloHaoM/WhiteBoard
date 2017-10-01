package client;
import java.awt.EventQueue;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import javax.swing.UIManager;

import org.WhiteBoardClient;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;
import server.RemoteClient;

/**
 * multi-clients version v0.2
 * ManagerClient and GuestClient implement the main function,
 * which separates it from the GUI class
 * @author tianzhangh
 *
 */
public class ManagerClient {

	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Retrieve the stub/proxy for the remote object from the registry
					Registry registry = LocateRegistry.getRegistry("localhost");
					
					IRemoteWBService remoteWB = (IRemoteWBService) registry.lookup(IRemoteWBService.LOOKUP_NAME);
					
					String roomname ="whiteboard1";
					IRemoteClient manager = new RemoteClient(0, "tianzhangh");
					manager.setClientLevel(RemoteClient.ClientLevel.MANAGER);
					
					IRemoteServer remoteserver = remoteWB.createRoom(manager, roomname);
					if(remoteserver != null) {
						remoteserver.setManager(manager);
					}
					
				    //add manager
					remoteserver.addClient(manager);
					
					
					WhiteBoardClient window = new WhiteBoardClient(manager, remoteserver);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}
	
}
