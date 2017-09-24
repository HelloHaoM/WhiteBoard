package client;
import java.rmi.AlreadyBoundException;
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
 * This class is for testing the rmi architecture
 * @author tianzhangh
 *
 */
public class MathClient {

	public static void main(String[] args) {
		
		try {

			IRemoteClient remoteClient = new RemoteClient();
			remoteClient.setClientName("guest1");
			remoteClient.setClientLevel(RemoteClient.ClientLevel.USER);
		    
		    
		    
			//Retrieve the stub/proxy for the remote math object from the registry
			Registry registry = LocateRegistry.getRegistry("localhost");
			
			IRemoteMath remoteMath = (IRemoteMath) registry.lookup("Compute");
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
			
			System.out.println("Room: "+ roomname);
			System.out.println("Manager: "+ remoteserver.getManager().getClientName());
			
			
			//add some clients for test
			//remoteserver.addClient(remoteClient);
			//System.out.println("Client No. :" + remoteClient.getClientId());
			//System.out.println("Client UserName: "+remoteClient.getClientName());
			
			
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
			
		} catch (RemoteException | NotBoundException | AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
}
