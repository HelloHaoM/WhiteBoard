package remote;

import java.awt.Color;
import java.awt.Shape;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
/**
 * 
 * @author tianzhangh
 *
 */
public interface IRemoteWBItem extends Remote {
    public Shape getShape() throws RemoteException;
    public Date getCreationTime() throws RemoteException;
    public IRemoteClient getOwner() throws RemoteException;
    public Color getColour() throws RemoteException;
}
