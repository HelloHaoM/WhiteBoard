package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import server.RemoteClient.ClientLevel;
/**
 * 
 * @author tianzhangh
 *
 */
public interface IRemoteClient extends Remote{
	public static final String LOOKUP_NAME = "Client";

	public void setClientId(int id) throws RemoteException;
	public int getClientId() throws RemoteException;
	public String getClientName() throws RemoteException;
	public void setClientName(String clientName) throws RemoteException;
	public void setClientLevel(ClientLevel lv) throws RemoteException;
	public ClientLevel getClietnLevel() throws RemoteException;
	public void alert(String msg) throws RemoteException;
	
	public void retrieveShape(IRemoteWBItem shape) throws RemoteException;
	public void removeShape(IRemoteWBItem shape) throws RemoteException;
	public void updateShapes(Set<IRemoteWBItem> shapes) throws RemoteException;
	
}
