package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;
/**
 * This is the implement of WhiteBoard Service Object
 * it manages several whiteBoard rooms (RemoteServer)
 * @author tianzhangh
 *
 */
public class RemoteWBService extends UnicastRemoteObject implements IRemoteWBService {
	
	private static int roomNum;
	private Map<String ,IRemoteServer> roomMap;
	private Registry registry;
	public static final String SERVER_NAME = "Whiteboard";
	public static final int SERVER_PORT = 1099;

	protected RemoteWBService() throws RemoteException {
		super();
		roomNum = 0;
		roomMap = new ConcurrentHashMap<String, IRemoteServer>();
		registry = LocateRegistry.getRegistry("localhost");
	}
	
	@Override
	public IRemoteServer createRoom(IRemoteClient manager, String roomname) throws RemoteException, AlreadyBoundException {
		if(this.checkLv(manager)) {
		if(roomMap.containsKey(roomname)) {
			String msg = "room name exist";
			manager.alert(msg);
			return null;
		}else {
			roomNum++;
			IRemoteServer remoteserver = new RemoteServer();
			// bind this remote server by using room name
			registry.bind(roomname, remoteserver);
			roomMap.put(roomname, remoteserver);
			String msg =roomname + " created";
			manager.alert(msg);
			return remoteserver;
		}
		}else {
			manager.alert("unauthorized");
			return null;
		}
	}
	
	@Override
	public IRemoteServer getRoom(IRemoteClient manager, String roomname) throws RemoteException {
		if(this.checkLv(manager) ==  false)
			return null;
		if(roomMap.containsKey(roomname)) {
			manager.alert("Succeed: access to "+ roomname);
			return roomMap.get(roomname);
		}else {
			manager.alert(roomname+" does not exist.");
			return null;
		}
	}
	
	@Override
	public boolean removeRoom(IRemoteClient manager, String roomname) throws RemoteException {
		if(this.checkLv(manager) == false) {
			manager.alert("unauthorized");
			return false;
		}
		if(!roomMap.containsKey(roomname)) {
			manager.alert(roomname + "does not exist");
			return false;
		}else {
			roomNum--;
			IRemoteServer roomserver = roomMap.get(roomname);
			String managerName = roomserver.getManager().getClientName();
			if(manager.getClientName().equalsIgnoreCase(managerName)) {
				manager.alert("room has been removed");
				roomserver.updateAllClients("room has been removed by manager");
				roomMap.remove(roomname);				
				return true;
			}else {
				// not the manager who created this room
				manager.alert("unauthorized");
				return false;
			}
		}
		
	}
	
	@Override
	public void clearAllRoom() throws RemoteException {
		//iterate all room and clients in it
		Set<Entry<String, IRemoteServer>> roomset = this.roomMap.entrySet();
		for(Entry<String, IRemoteServer> room: roomset) {
			room.getValue().updateAllClients("all rooms have been removed");
		}
		roomNum = 0;
	}
	
	@Override
	public boolean checkLv(IRemoteClient manager) throws RemoteException {
		if(manager.getClietnLevel() == RemoteClient.ClientLevel.MANAGER) {
			return true;
		}			
		else {
			return false;
		}
	}

}
