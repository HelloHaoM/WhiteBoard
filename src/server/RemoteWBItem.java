package server;

import java.awt.Color;
import java.awt.Shape;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import remote.IRemoteClient;
import remote.IRemoteWBItem;
/**
 * The implement of the remote Item for maintain the shape in the IRemoteServer 
 * and update it to other IRemoteClient objects in this IRemoteServer(room) 
 * @author tianzhangh
 *
 */
public class RemoteWBItem extends UnicastRemoteObject implements IRemoteWBItem {
    private Shape shape;
    private IRemoteClient client;
    private Date creationDate;
    private Color colour;

	public RemoteWBItem(IRemoteClient client, Shape shape, Color colour) throws RemoteException {
		super();
		this.client = client;
		this.shape = shape;
		this.creationDate = new Date();
		this.colour = colour;
	}

	@Override
	public Shape getShape() throws RemoteException {
		return this.shape;
	}

	@Override
	public Date getCreationTime() throws RemoteException {
		return this.creationDate;
	}

	@Override
	public IRemoteClient getOwner() throws RemoteException {
		return this.client;
	}

	@Override
	public Color getColour() throws RemoteException {
		return this.colour;
	}

}
