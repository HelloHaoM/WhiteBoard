package server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import remote.IRemoteClient;
import remote.IRemoteMath;
import remote.IRemoteServer;
import remote.IRemoteWBService;

/**
 * This class is for testing the rmi architecture.
 * Creates instances of the RemoteMath (test), RemoteClient(call back obejct),
 * RemoteWBService (management) class and
 * publishes them in the rmiregistry
 * 
 * @author tianzhangh
 */
public class RMIServer {
    public static final String SERVER_NAME = "Whiteboard";
    public static final int SERVER_PORT = 1099;
    
    

	public static void main(String[] args)  {
		
		try {
			
			//Export the remote math object to the Java RMI runtime so that it
			//can receive incoming remote calls.
			//Because RemoteMath extends UnicastRemoteObject, this
			//is done automatically when the object is initialized.
			
			IRemoteMath remoteMath = new RemoteMath();
			IRemoteWBService remoteWB = new RemoteWBService();
			IRemoteClient remoteClient = new RemoteClient();
			
            
            //Publish the remote object's stub in the registry under the name "Compute"
            Registry registry = LocateRegistry.createRegistry(SERVER_PORT);
            registry.bind("Compute", remoteMath);
            registry.bind(IRemoteClient.LOOKUP_NAME, remoteClient);
            registry.bind(IRemoteWBService.LOOKUP_NAME, remoteWB);
            
            
            
            System.out.println("Math server ready");
            
            System.out.println("WhiteBoard-Management server ready");
            
            //The server will continue running as long as there are remote objects exported into
            //the RMI runtime, to remove remote objects from the
            //RMI runtime so that they can no longer accept RMI calls you can use:
           // UnicastRemoteObject.unexportObject(remoteMath, false);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
