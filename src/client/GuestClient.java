package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeUnit;

import remote.IRemoteClient;
import remote.IRemoteMath;
import remote.IRemoteServer;
import remote.IRemoteWBService;
import server.RemoteClient;
/**
 * Guest Client Class for test
 * @author tianzhangh
 *
 */
public class GuestClient {
public static void main(String[] args) {
		
		try {

			IRemoteClient remoteClient = new RemoteClient();
			remoteClient.setClientName("guest1");
			remoteClient.setClientLevel(RemoteClient.ClientLevel.USER);
		    
		    
		    
			//Retrieve the stub/proxy for the remote math object from the registry
			Registry registry = LocateRegistry.getRegistry("localhost");
			
			IRemoteMath remoteMath = (IRemoteMath) registry.lookup("Compute");
			IRemoteWBService remoteWB = (IRemoteWBService) registry.lookup(IRemoteWBService.LOOKUP_NAME);
			
			String roomname ="WhiteBoard1";
			IRemoteServer remoteserver = remoteWB.getRoom(remoteClient, roomname);
			
			System.out.println("Room: "+ roomname);
			System.out.println("Manager: "+ remoteserver.getManager().getClientName());
			
			//add some clients for test
			remoteserver.addClient(remoteClient);
			System.out.println("Client No. :" + remoteClient.getClientId());
			System.out.println("Client UserName: "+remoteClient.getClientName());
			
			
			// do some jobs in start method
			double result = remoteMath.subtract(5, 4);
			System.out.println("5 - 4 = " + result);
			
			//keep it running for the test 
			while(true) {
				try {
					TimeUnit.SECONDS.sleep(30);
					double result1 = remoteMath.subtract(5, 4);
					System.out.println("5 - 4 = " + result1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
