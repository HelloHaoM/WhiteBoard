package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

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
	public ClientLevel getClientLevel() throws RemoteException;
	//for whiteboard status output (need to be modified to GUI version)
	public void alert(String msg) throws RemoteException;
	public void alertClientList(Set<String> clientNameList) throws RemoteException;
	
	public void retrieveShape(IRemoteWBItem shape) throws RemoteException;
	public void removeShape(IRemoteWBItem shape) throws RemoteException;
	//ublic void updateShapes(Set<IRemoteWBItem> shapes) throws RemoteException;
	
	public void retrieveImg(ImageIcon img) throws RemoteException;
	//for multi-clients chat only
	public void broadCast(String msg) throws RemoteException;

	boolean Permission(String name) throws IOException;

	void closeClient(String name) throws RemoteException;
	void CleanPaintSurface() throws RemoteException;
	void removeDialog() throws RemoteException;
	
}
