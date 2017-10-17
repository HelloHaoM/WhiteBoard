package remote;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
/**
 * 
 * @author tianzhangh
 *
 */

public interface IRemoteServer extends Remote{
	
	public int getListSize() throws RemoteException;
	public String getRoomName() throws RemoteException;
	public void addClient(IRemoteClient client) throws RemoteException;
	public void addClient(String clientname, IRemoteClient client) throws RemoteException;
	//public void removeClient(String clientname) throws RemoteException;
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
	public void updateAllClientsWithClientName() throws RemoteException;
	
	//manage and update the room's white board 
	public void addShape(IRemoteClient client, Shape shape, Color colour,int Drawtype ,int Stroke) throws RemoteException;
	public void addText(IRemoteClient client, Shape shape, String text, Color colour,int Drawtype ,int Stroke, Point Pos) throws RemoteException;
	public void addImg(IRemoteClient client, ImageIcon img) throws RemoteException;
	public void addImage(IRemoteClient client, ImageIcon img, int Drawtype) throws RemoteException;
	public Set<IRemoteWBItem> getShapes() throws RemoteException;
	public void removeItemsByClient(IRemoteClient client) throws RemoteException;
	public void removeItem(IRemoteClient client, IRemoteWBItem item) throws RemoteException;
	public void removeAllItems() throws RemoteException;
	
	//for multi-clients chat
	public void sendMessage(String msg) throws RemoteException;
	
	public ImageIcon getImg() throws RemoteException;
	
	//join in the room
	boolean permission(String name) throws IOException;
	
	//update client list
	void updateList(IRemoteClient client) throws RemoteException;
	// remove the client
	public void removeHints(IRemoteClient client) throws RemoteException;
	//public void closeWindow(IRemoteClient client) throws RemoteException;
	void removeClient(IRemoteClient client) throws RemoteException;
	void removeClient(String clientname) throws RemoteException;
	void cleanAll() throws RemoteException;
	void removeAllClient(IRemoteClient client) throws RemoteException;
	
}
