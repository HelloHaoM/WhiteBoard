package remote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * 
 * @author tianzhangh
 *
 */
public interface IRemoteWBService extends Remote {
	public static final String LOOKUP_NAME = "RoomManage";

	public IRemoteServer createRoom(IRemoteClient manager, String roomname) throws RemoteException, AlreadyBoundException;
	public IRemoteServer getRoom(IRemoteClient client, String roomname) throws RemoteException;
	public boolean removeRoom(IRemoteClient manager, String roomname) throws RemoteException, NotBoundException;
	public void clearAllRoom() throws RemoteException;
	public boolean checkLv(IRemoteClient manager) throws RemoteException;

}
