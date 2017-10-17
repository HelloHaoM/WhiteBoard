package remote;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

import javax.swing.ImageIcon;
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
	int getEraserSize() throws RemoteException;
	Point getPos() throws RemoteException;
	int getStrokeValue() throws RemoteException;
	int getDrawType() throws RemoteException;
	public String getText() throws RemoteException;
	public ImageIcon getImage() throws RemoteException;

}
