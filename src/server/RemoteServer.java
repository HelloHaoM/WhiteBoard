package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import remote.IRemoteClient;
import remote.IRemoteServer;
/**
 * This is the implement of RemoteServer Object
 * @author tianzhangh
 *
 */

public class RemoteServer extends UnicastRemoteObject implements IRemoteServer{
	private Map<String, IRemoteClient> clients;
	private Map<String, IRemoteClient> requestClients;
	private static int clientNum; //synchronized it later
	private static int requestNum;
	private static IRemoteClient roomManager;
	private static int LOOKUP_NAME;
	
	protected RemoteServer() throws RemoteException {
		super();
		clientNum = 0;
		requestNum = 0;
		// initialize added clients and requesting clients map
		this.clients = new ConcurrentHashMap<String, IRemoteClient>();
		//this.requestClients = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteClient, Boolean>());
		this.requestClients = new ConcurrentHashMap<String, IRemoteClient>();
		
		// add client watch dog
		Thread t = new Thread(()->this.clientWatchDog());
		t.start();
	}
	
	@Override
	public boolean setManager(IRemoteClient manager) throws RemoteException{
		if(manager.getClietnLevel() == RemoteClient.ClientLevel.MANAGER) {
			roomManager = manager;
			manager.alert("room creation succeed");
			return true;
		}else {
			manager.alert("room creation denied");
			return false;
		}
	}

	@Override
	public int getListSize() throws RemoteException {
		// TODO Auto-generated method stub
		return this.clients.size();
	}

	@Override
	public void removeClient(String clientname) throws RemoteException {
		// TODO Auto-generated method stub
		if(clients.containsKey(clientname)){
			this.clients.remove(clientname);
			String msg = clientname + "removed.";
			System.out.println(msg);
			this.updateAllClients(msg);
			clientNum--;
		}
		else {
			System.out.println(clientname + "not exist");
		}
	}
	
	@Override
	public void addClient(IRemoteClient client) throws RemoteException {
		
		int id = clientNum;
		// not thread save right now, just for the test
		String clientname = client.getClientName();
		client.setClientId(id);
		if(clientname == null) {
			clientname = "c"+id;
			client.setClientName(clientname);
		}
			
		if(!clients.containsKey(clientname)) {
			clientNum++; //synchronized it later
		    this.clients.put(clientname, client);
		    String msg = clientname + "added.";
		    System.out.println(msg);
		    this.updateAllClients(msg);
		}
		else {
			client.alert("client name exit");
		}
		
	}
	@Override
	public void addClient(String clientname, IRemoteClient client) throws RemoteException {
		
		int id = clientNum;
		client.setClientId(id);
		if(!clients.containsKey(clientname)) {
			clientNum++;
			this.clients.put(clientname, client);
			String msg = clientname+ " added.";
			System.out.println(msg);
			this.updateAllClients(msg);
		}
		else {
			client.alert("client name exist");
		}
	}
	
	public Map<String, IRemoteClient> getClients(){
		return this.clients;
	}
	
	public void clientWatchDog() {
		try {
			while(true){
				TimeUnit.MILLISECONDS.sleep(10);
				int size = getListSize();
				if(size == 3) {
					String msg ="3 clients now!";
					updateAllClients(msg);
					break;
				}
			}		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateAllClients(String msg) throws RemoteException {
		Map<String, IRemoteClient> clients = this.getClients();
		for( Map.Entry<String, IRemoteClient> entry: clients.entrySet() ) {
			entry.getValue().alert(msg);
		}
	}

	@Override
	public int getClientId(String clientname) throws RemoteException {
		return this.getClients().get(clientname).getClientId();
	}



	@Override
	public IRemoteClient getClient(String clientname) throws RemoteException {		
		return this.getClients().get(clientname);
	}

	@Override
	public Set<String> getClientNameList() throws RemoteException {
		return this.clients.keySet();
	}

	@Override
	public Set<String> getRequestList() throws RemoteException {	
		return this.requestClients.keySet();
	}

	@Override
	public void requestAdd(String clientname, IRemoteClient client) throws RemoteException {
		if(!clients.containsKey(clientname)) {
			if(!requestClients.containsKey(clientname)) {
				requestNum++;
				requestClients.put(clientname, client);
				client.alert("Please waiting for Room Manager Approve....");
			}else {
				client.alert(clientname + "name exist. Request Denied");
			}
		}else {
			client.alert(clientname + "name exist. Request Denied");
		}
		
	}

	@Override
	public void approveRequest(IRemoteClient manager, String clientname) throws RemoteException {
		requestNum--;
		IRemoteClient client = this.requestClients.get(clientname);
		if(!clients.containsKey(client.getClientName())) {
			this.addClient(client.getClientName(), client);
		}else {
			client.alert("Request Denied");
			manager.alert(client.getClientName()+" Already Exist");
		}
		
		
	}

	@Override
	public void clearRequestList(IRemoteClient manager) throws RemoteException {
		requestNum = 0;
		
	}

	@Override
	public void removeRequest(IRemoteClient manager, String clientname) throws RemoteException {
		requestNum--;
		
	}

	@Override
	public IRemoteClient getManager() throws RemoteException {
		return roomManager;
	}


}
