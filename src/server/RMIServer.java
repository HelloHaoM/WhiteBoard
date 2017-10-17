package server;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.ClientStart;
import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;

/**
 * This class is for testing the rmi architecture. Creates instances of
 * RemoteWBService (management) class and publishes them in the rmiregistry
 * 
 * IRemoteClient do not need to be bind in registry, because it transfer itself
 * to IRemoteServer
 * 
 * @author tianzhangh
 */
public class RMIServer {

	//public static String SERVER_NAME = null;
	//public static int SERVER_PORT = 1099;

	public static void main(String[] args) {

		try {

			// Export the remote object to the Java RMI runtime so that it
			// can receive incoming remote calls.
			// Because RemoteMath extends UnicastRemoteObject, this
			// is done automatically when the object is initialized.

			//args[0] servername, args[1] server port, args[2] ip address
			IRemoteWBService remoteWB = new RemoteWBService(args[0], args[1], args[2]);

			// Publish the remote object's stub in the registry
			try {
				
				String ip = args[2];
				int ServerPort = Integer.parseInt(args[1]);
				
				Registry registry = LocateRegistry.getRegistry(ip);
				registry.bind(IRemoteWBService.LOOKUP_NAME, remoteWB);
			} catch (ConnectException e) {
				JOptionPane.showMessageDialog(null, "Connection Error. \n", "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

			System.out.println(args[0] + " server ready");
			JOptionPane.showMessageDialog(null, args[0] + " server ready!", "Hints", JOptionPane.INFORMATION_MESSAGE);

			// The server will continue running as long as there are remote objects exported
			// into
			// the RMI runtime, to remove remote objects from the
			// RMI runtime so that they can no longer accept RMI calls you can use:
			// UnicastRemoteObject.unexportObject(remoteMath, false);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
