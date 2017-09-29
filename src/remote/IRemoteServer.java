package remote;

import java.awt.Color;
import java.awt.Shape;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * 
 * @author tianzhangh
 *
 */

public interface IRemoteServer extends Remote{
	
	public int getListSize() throws RemoteException;
	public void addClient(IRemoteClient client) throws RemoteException;
	public void addClient(String clientname, IRemoteClient client) throws RemoteException;
	public void removeClient(String clientname) throws RemoteException;
	public Set<String> getClientNameList() throws RemoteException;
	public IRemoteClient getClient(String clientname) throws RemoteException;
	public int getClientId(String clientname) throws RemoteException;
	
	
	public void requestAdd(String clientname, IRemoteClient client) throws RemoteException;
	// manage the client requests
	public boolean setManager(IRemoteClient manager) throws RemoteException;
	public IRemoteClient getManager() throws RemoteException;
	Set<String> getRequestList() throws RemoteException;
	public void approveRequest(IRemoteClient manager, String clientname) throws RemoteException;
	public void clearRequestList(IRemoteClient manager) throws RemoteException;
	public void removeRequest(IRemoteClient manager, String clientname) throws RemoteException;
	
	public void updateAllClients(String msg) throws RemoteException;
	
	//manage and update the room's white board 
	public void addShape(IRemoteClient client, Shape shape, Color colour) throws RemoteException;
	public Set<IRemoteWBItem> getShapes() throws RemoteException;
	public void removeItemsByClient(IRemoteClient client) throws RemoteException;
	public void removeItem(IRemoteClient client, IRemoteWBItem item) throws RemoteException;
	public void removeAllItems() throws RemoteException;
	
	public void sendMessage(String msg) throws RemoteException;
}
