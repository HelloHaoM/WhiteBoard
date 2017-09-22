package server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import remote.IRemoteClient;
import remote.IRemoteMath;
import remote.IRemoteServer;
import remote.IRemoteWBItem;
/**
 * This is the implement of call back client object
 * @author tianzhangh
 *
 */
public class RemoteClient extends UnicastRemoteObject implements IRemoteClient {
	
	private int clientId;
	private String clientName;
	private ClientLevel lv;
	
	private Set<IRemoteWBItem> shapes;
	
	public enum ClientLevel{
		MANAGER(0), USER(1), VISITOR(2);
		
		private int level;
		ClientLevel(int level){
			this.level = level;
		}
		
		public int getLevel() {
			return this.level;
		}
	} 

	public RemoteClient() throws RemoteException {
		super();
		shapes = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteWBItem, Boolean>());
	}
	
	public RemoteClient(int id, String name) throws RemoteException{
		super();
		this.clientId = id;
		this.setClientName(name);
		lv = ClientLevel.USER;
		shapes = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteWBItem, Boolean>());
	}
	
	public RemoteClient(int id, String name, ClientLevel lv) throws RemoteException{
		super();
		this.clientId = id;
		this.clientName = name;
		this.lv = lv;
		shapes = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteWBItem, Boolean>());
	}

	@Override
	public void alert(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(msg);
		
	}
	

	@Override
	public void setClientId(int id) throws RemoteException {
		this.clientId = id;
		
	}

	@Override
	public int getClientId() throws RemoteException {
		return this.clientId;		
	}
	
	@Override
	public String getClientName() throws RemoteException{
		return clientName;
	}
	
	@Override
	public void setClientName(String clientName) throws RemoteException {
		this.clientName = clientName;
	}

	@Override
	public void setClientLevel(ClientLevel lv) throws RemoteException {
		this.lv = lv;
		
	}

	@Override
	public ClientLevel getClietnLevel() throws RemoteException {
		return this.lv;
	}

	@Override
	public void retrieveShape(IRemoteWBItem shape) throws RemoteException {
		this.shapes.add(shape);
	}

	@Override
	public void updateShapes(Set<IRemoteWBItem> shapes) throws RemoteException {
		this.shapes.addAll(shapes);		
	}

	@Override
	public void removeShape(IRemoteWBItem shape) throws RemoteException {
		this.shapes.remove(shape);
	}

}
