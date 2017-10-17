package server;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

import javax.swing.ImageIcon;

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

	private String text;
	private ImageIcon image;
	private Point pos;
	private int drawType; //1: fill, 2: line, 3: text, 4: eraser, 5: verText, 6:image
	private int strokeValue;
	private int eraserSize;
	

	public RemoteWBItem(IRemoteClient client, Shape shape, Color color, int drawType, int strokeValue) throws RemoteException {
		super();
		this.client = client;
		this.shape = shape;
		this.creationDate = new Date();
		this.drawType = drawType;
		this.colour = color;
		this.text = "";
		this.strokeValue = strokeValue;
	}
	
	public RemoteWBItem(IRemoteClient client, Shape shape, String text, Color color, int drawType, int strokeValue, Point pos) throws RemoteException {
		super();
		this.client = client;
		this.shape = shape;
		this.creationDate = new Date();
		this.drawType = drawType;
		this.colour = color;
		this.text = text;
		this.strokeValue = strokeValue;
		this.pos = pos;
	}
	
	public RemoteWBItem(IRemoteClient client, ImageIcon image, int drawType) throws RemoteException {
		super();
		this.client = client;
		this.image = image;
		this.drawType = drawType;
	}
	
	public void setText(String text) {
			this.text = text;
	}

		

	public void setPos(Point pos) {
			this.pos = pos;
		}


	public void setDrawType(int drawType) {
			this.drawType = drawType;
		}


	public void setStrokeValue(int strokeValue) {
			this.strokeValue = strokeValue;
		}


	public void setEraserSize(int eraserSize) {
			this.eraserSize = eraserSize;
		}

	public void setShape(Shape shape) {
			this.shape = shape;
		}

	@Override
	public int getDrawType() throws RemoteException {
			return this.drawType;
		}

	@Override
	public Point getPos() throws RemoteException{
		return pos;
	}	
	@Override
	public int getStrokeValue() throws RemoteException {
			return this.strokeValue;
		}
	
	@Override
	public int getEraserSize() throws RemoteException{
			return this.eraserSize;
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
	@Override
	public String getText()throws RemoteException{
			return this.text;
		}

	public ImageIcon getImage() throws RemoteException {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

}
