package server;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBItem;

/**
 * This is the implement of RemoteServer Object, which contains the management
 * of One WhiteBoard Room and its clients and IRemoteWBItem which update the new
 * client and its IRemoteWBItem to other clients
 * 
 * @author tianzhangh
 *
 */
public class RemoteServer extends UnicastRemoteObject implements IRemoteServer {
	private Map<String, IRemoteClient> clients;
	private Map<String, IRemoteClient> requestClients;
	private Set<IRemoteWBItem> shapes;
	private ImageIcon img;

	private static int clientNum; // synchronized it later
	private static int requestNum;
	private static IRemoteClient roomManager;
	private String roomName;
	private static int LOOKUP_NAME;

	protected RemoteServer(String roomName) throws RemoteException {
		super();
		clientNum = 0;
		requestNum = 0;
		// initialize added clients and requesting clients map
		this.clients = new ConcurrentHashMap<String, IRemoteClient>();
		this.shapes = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<IRemoteWBItem, Boolean>())); 
		this.requestClients = new ConcurrentHashMap<String, IRemoteClient>();
		this.roomName = roomName;

		// add client watch dog thread
		Thread t = new Thread(() -> this.clientWatchDog());
		t.start();
	}

	@Override
	public String getRoomName() throws RemoteException {
		return this.roomName;
	}

	@Override
	public boolean setManager(IRemoteClient manager) throws RemoteException {
		if (manager.getClientLevel() == RemoteClient.ClientLevel.MANAGER) {
			roomManager = manager;
			manager.alert(this.roomName + " room root succeed");
			return true;
		} else {
			manager.alert(this.roomName + " room root denied");
			return false;
		}
	}

	@Override
	public int getListSize() throws RemoteException {
		// TODO Auto-generated method stub
		return this.clients.size();
	}

	@Override
	public void removeClient(IRemoteClient client) throws RemoteException {
		// TODO Auto-generated method stub
		String clientname = client.getClientName();
		if (clients.containsKey(clientname)) {
			//client.closeClient(clientname);
			client.removeDialog();
			
			this.clients.remove(clientname);
			String msg = clientname + " removed from " + this.roomName;
			System.out.println(msg);
			this.updateAllClients(msg);
			this.updateAllClientsWithClientName();
			
			clientNum--;

		} else {
			System.out.println(clientname + "not exist");
		}
	}
	
	@Override
	public void removeClient(String clientname) throws RemoteException {
		
		
		if (clients.containsKey(clientname)) {	
			this.clients.remove(clientname);
			String msg = clientname + " removed from " + this.roomName;
			System.out.println(msg);
			this.updateAllClients(msg);
			this.updateAllClientsWithClientName();
			
			clientNum--;

		} else {
			System.out.println(clientname + "not exist");
		}
		
		
	}
	
	// remove client and close the window
	public void removeHints(IRemoteClient client) throws RemoteException{
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			if (!remoteclient.getClientName().equalsIgnoreCase(client.getClientName())) {
				//
			}
		}
	}

	@Override
	public void addClient(IRemoteClient client) throws RemoteException {

		int id = clientNum;
		// not thread save right now, just for the test
		String clientname = client.getClientName();
		client.setClientId(id);
		if (clientname == null) {
			clientname = "c" + id;
			client.setClientName(clientname);
		}

		if (!clients.containsKey(clientname)) {
			clientNum++; // synchronized it later
			this.clients.put(clientname, client);
			String msg = clientname + " added in " + this.roomName;
			System.out.println(msg);
			this.updateAllClients(msg);
			this.updateAllClientsWithClientName();
		} else {
			client.alert("client name exist");
		}

	}

	@Override
	public void addClient(String clientname, IRemoteClient client) throws RemoteException {

		int id = clientNum;
		client.setClientId(id);
		if (!clients.containsKey(clientname)) {
			clientNum++;
			this.clients.put(clientname, client);
			String msg = clientname + " added in room " + this.roomName;
			System.out.println(msg);
			this.updateAllClients(msg);
		} else {
			client.alert("client name exist");
		}
	}

	public Map<String, IRemoteClient> getClients() {
		return this.clients;
	}

	public void clientWatchDog() {
		try {
			while (true) {
				TimeUnit.MILLISECONDS.sleep(10);
				int size = getListSize();
				if (size == 3) {
					String msg = "3 clients now!";
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
		for (Entry<String, IRemoteClient> entry : clients.entrySet()) {
			entry.getValue().alert(msg);
		}
	}

	@Override
	public void updateAllClientsWithClientName() throws RemoteException {
		Map<String, IRemoteClient> clients = this.getClients();
		for (Entry<String, IRemoteClient> entry : clients.entrySet()) {
			entry.getValue().alertClientList(this.getClientNameList());
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
		if (!clients.containsKey(clientname)) {
			if (!requestClients.containsKey(clientname)) {
				requestNum++;
				requestClients.put(clientname, client);
				client.alert("Please waiting for Room Manager Approve....");
				this.getManager().alert("new request");
			} else {
				client.alert(clientname + "name exist. Request Denied");
			}
		} else {
			client.alert(clientname + "name exist. Request Denied");
		}

	}

	@Override
	public void approveRequest(IRemoteClient manager, String clientname) throws RemoteException {
		if (checkManager(manager) == true) {
			requestNum--;
			IRemoteClient client = this.requestClients.get(clientname);
			if (!clients.containsKey(client.getClientName())) {
				this.addClient(client.getClientName(), client);
			} else {
				client.alert("Request Denied");
				manager.alert(client.getClientName() + " Already Exist");
			}
		} else {
			manager.alert("unauthorized");
		}

	}

	@Override
	public void clearRequestList(IRemoteClient manager) throws RemoteException {
		if (checkManager(manager) == true) {
			requestNum = 0;
			Set<Entry<String, IRemoteClient>> set = this.requestClients.entrySet();
			for (Entry<String, IRemoteClient> entry : set) {
				entry.getValue().alert("request denied");
			}
			this.requestClients.clear();
			manager.alert("requestlist cleared");
		} else {
			manager.alert("clearing requestlist failed");
		}
	}

	@Override
	public void removeRequest(IRemoteClient manager, String clientname) throws RemoteException {
		if (checkManager(manager) == true) {
			if (this.requestClients.containsKey(clientname)) {
				requestNum--;
				IRemoteClient client = this.requestClients.get(clientname);
				client.alert("your request has been denied");
				this.requestClients.remove(clientname);
				manager.alert(clientname + " request has been denied");
			} else {
				manager.alert(clientname + " does not exist");
			}
		} else {
			manager.alert("unauthorized");
		}
	}

	@Override
	public IRemoteClient getManager() throws RemoteException {
		return roomManager;
	}

	private boolean checkManager(IRemoteClient manager) throws RemoteException {
		if (this.getManager().getClientName().equalsIgnoreCase(manager.getClientName())) {
			return true;
		} else {
			return false;
		}

	}

	/***
	 * Given an IRemoteClient, and a java.awt.Shape shape, create an IRemoteWBItem,
	 * store it, and distribute it to all clients in this room.
	 *
	 * @param client
	 *            The source client
	 * @param shape
	 *            The shape to be distributed add
	 * @throws RemoteException
	 */
	@Override
	public void addShape(IRemoteClient client, Shape shape, Color colour, int DrawType, int Stroke)
			throws RemoteException {
		IRemoteWBItem itemShape = new RemoteWBItem(client, shape, colour, DrawType, Stroke);
		this.shapes.add(itemShape);
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			//if (remoteclient.getClientName().equalsIgnoreCase(client.getClientName()) == false) {
				remoteclient.retrieveShape(itemShape);
			//}
			remoteclient.alert("new shape added from " + client.getClientName());
		}
	}

	@Override
	public void addText(IRemoteClient client, Shape shape, String text, Color colour, int DrawType, int Stroke,
			Point Pos) throws RemoteException {
		IRemoteWBItem itemTextShape = new RemoteWBItem(client, shape, text, colour, DrawType, Stroke, Pos);
		this.shapes.add(itemTextShape);
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			if (remoteclient.getClientName().equalsIgnoreCase(client.getClientName()) == false) {
				remoteclient.retrieveShape(itemTextShape);
			}
			remoteclient.alert("new Text added from " + client.getClientName());
		}
	}

	/**
	 * Given an IRemoteClient, and an IRemoteWBIitem which need to be globally
	 * removed
	 * 
	 * @param client
	 *            the source client
	 * @param item
	 *            the item need to be removed
	 * @throws RemoteException
	 */
	@Override
	public void removeItem(IRemoteClient client, IRemoteWBItem item) throws RemoteException {
		if (this.shapes.remove(item) == true) {
			for (Entry<String, IRemoteClient> entry : this.clients.entrySet()) {
				IRemoteClient remoteclient = entry.getValue();
				remoteclient.alert(client.getClientName() + " removed a item");
				remoteclient.removeShape(item);
				this.shapes.remove(item);
				if(this.shapes.contains(item)) {System.out.println("remove failed");}
			}
		} else {
			client.alert("item remove failed");
		}

	}
	
	@Override
	public void cleanAll() throws RemoteException{
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			remoteclient.alert(remoteclient.getClientName() + " Try to clean the canva.");
			//this.removeItemsByClient(remoteclient);
			remoteclient.CleanPaintSurface();
			
			//added to remove shapes when u clear screen
			
			
		}
		this.removeAllItems();
		
	}

	@Override
	public Set<IRemoteWBItem> getShapes() throws RemoteException {
		return this.shapes;
	}

	
	@Override
	public void removeItemsByClient(IRemoteClient client) throws RemoteException {
		for (IRemoteWBItem item : this.shapes) {
			if (item.getOwner().getClientName().equalsIgnoreCase(client.getClientName())) {
				this.shapes.remove(item);
			}
		}
		this.removeAllItems();
	//	updateGlobalShapes();
	}
	
	@Override
	public void removeAllClient(IRemoteClient client) throws RemoteException{
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			if (!remoteclient.getClientName().equalsIgnoreCase(client.getClientName())) {
				remoteclient.removeDialog();
			}
		}
	}
	

	@Override
	public void removeAllItems() throws RemoteException {
		this.shapes.clear();
		this.updateAllClients("all items have been cleared");
//		this.updateGlobalShapes();
	}

	/*private void updateGlobalShapes() throws RemoteException {
		for (Entry<String, IRemoteClient> entry : this.clients.entrySet()) {
			entry.getValue().updateShapes(this.shapes);
		}
	}*/

	/**
	 * for multi-clients chat
	 * 
	 * @param msg
	 * @author tianzhangh
	 */
	@Override
	public void sendMessage(String msg) throws RemoteException {
		// System.out.println(msg);
		this.broadMessage(msg);
	}

	/**
	 * for multi-clients chat in the ChatDialog
	 * 
	 * @param msg
	 * @throws RemoteException
	 */
	public void broadMessage(String msg) throws RemoteException {
		Map<String, IRemoteClient> clients = this.getClients();
		for (Entry<String, IRemoteClient> entry : clients.entrySet()) {
			entry.getValue().broadCast(msg);
		}
	}

	// synchronize the image file
	@Override
	public void addImg(IRemoteClient client, ImageIcon img) throws RemoteException {
		this.updateImg(client, img);
	}

	@Override
	public ImageIcon getImg() throws RemoteException {
		return this.img;
	}
	
	public void updateImg(IRemoteClient client, ImageIcon img) throws RemoteException{
		this.img = img;
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			if (!remoteclient.getClientName().equalsIgnoreCase(client.getClientName())) {
				remoteclient.retrieveImg(img);
			}
		}
	}

	@Override
	public void updateList(IRemoteClient client) throws RemoteException {
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			if (!remoteclient.getClientName().equalsIgnoreCase(client.getClientName())) {
				remoteclient.alertClientList(this.getClientNameList());
			}
		}
	}

	// join
	@Override
	public boolean permission(String name) throws IOException {
		return this.getClients().entrySet().iterator().next().getValue().Permission(name);
	}

	@Override
	public void addImage(IRemoteClient client, ImageIcon img, int Drawtype) throws RemoteException {
		IRemoteWBItem image = new RemoteWBItem(client, img, Drawtype);
		this.shapes.add(image);
		Set<Entry<String, IRemoteClient>> clientset = this.getClients().entrySet();
		for (Entry<String, IRemoteClient> entry : clientset) {
			IRemoteClient remoteclient = entry.getValue();
			if (remoteclient.getClientName().equalsIgnoreCase(client.getClientName()) == false) {
				remoteclient.retrieveShape(image);
			}
			remoteclient.alert("new Image added from " + client.getClientName());
		}
		
	}

}
