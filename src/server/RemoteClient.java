package server;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.PaintSurface;
import org.WhiteBoardClient;

import remote.IRemoteClient;
import remote.IRemoteWBItem;

/**
 * This is the implement of call back client object multi-clients version v0.1:
 * add the PaintSurface and ChatDialog Object in RemoteClient
 * 
 * @author tianzhangh
 *
 */

@SuppressWarnings("serial")
public class RemoteClient extends UnicastRemoteObject implements IRemoteClient {

	private int clientId;
	private String clientName;
	private ClientLevel lv;
	private PaintSurface paint;
	private WhiteBoardClient whiteBoardClient;
	// private Set<IRemoteWBItem> //s;

	public enum ClientLevel {
		MANAGER(0), USER(1), VISITOR(2);

		private int level;

		ClientLevel(int level) {
			this.level = level;
		}

		public int getLevel() {
			return this.level;
		}
	}

	public RemoteClient() throws RemoteException {
		super();
		// s = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteWBItem,
		// Boolean>());
	}

	public RemoteClient(int id, String name) throws RemoteException {
		super();
		this.clientId = id;
		this.setClientName(name);
		lv = ClientLevel.USER;
		// s = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteWBItem,
		// Boolean>());
	}

	public RemoteClient(int id, String name, ClientLevel lv) throws RemoteException {
		super();
		this.clientId = id;
		this.clientName = name;
		this.lv = lv;
		// s = Collections.newSetFromMap(new ConcurrentHashMap<IRemoteWBItem,
		// Boolean>());
	}

	public void setPaint(PaintSurface paint) {
		this.paint = paint;
	}

	public PaintSurface getPaint() {
		return this.paint;
	}

	public WhiteBoardClient getWhiteBoardClient() {
		return whiteBoardClient;
	}

	public void setWhiteBoardClient(WhiteBoardClient whiteBoardClient) {
		this.whiteBoardClient = whiteBoardClient;
	}

	@Override
	public void alert(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(msg);
		if (whiteBoardClient != null) {
			whiteBoardClient.showOptions(msg);
		}

	}

	@Override
	public void alertClientList(Set<String> clientNameList) throws RemoteException {
		if (whiteBoardClient != null) {
			whiteBoardClient.getDlm().removeAllElements();
			Iterator<String> it = clientNameList.iterator();
			while (it.hasNext()) {
				String name = it.next();
				whiteBoardClient.getDlm().addElement(name);
			}
			whiteBoardClient.getJlist().setModel(whiteBoardClient.getDlm());
		}
	}

	@Override
	public void setClientId(int id) throws RemoteException {
		this.clientId = id;

	}

	@Override
	public int getClientId() throws RemoteException {
		return this.clientId;
	}

	@Override
	public String getClientName() throws RemoteException {
		return clientName;
	}

	@Override
	public void setClientName(String clientName) throws RemoteException {
		this.clientName = clientName;
	}

	@Override
	public void setClientLevel(ClientLevel lv) throws RemoteException {
		this.lv = lv;

	}

	@Override
	public ClientLevel getClientLevel() throws RemoteException {
		return this.lv;
	}

	@Override
	public void retrieveShape(IRemoteWBItem shape) throws RemoteException {
		// global item updating test
		System.out.println("Global item update:" + shape.getOwner().getClientName());
		this.paint.addItem(shape);
		// s.add(shape);
	}

	@Override
	public void removeShape(IRemoteWBItem shape) throws RemoteException {
		this.paint.repaint();
		// s.remove(shape);
	}

	@Override
	public void CleanPaintSurface() throws RemoteException {
		// s.removeAll(this.paint.getShpaeList());
		this.paint.cleanList();
		this.paint.repaint();

	}

	@Override
	public void broadCast(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		if (whiteBoardClient != null) {
			WhiteBoardClient.getShowChat().append(msg);
		}
	}

	@Override
	public void retrieveImg(ImageIcon img) throws RemoteException {
		if (img != null) {
			System.out.println("retrieve img");
			JLabel imgLabel = new JLabel();
			// imgLabel.setOpaque(false);
			this.whiteBoardClient.getFrame().getContentPane().add(imgLabel);
			System.out.println("test 1");
			imgLabel.setIcon(img);
		}

	}

	@Override
	public void removeDialog() throws RemoteException {
		Thread thread = new Thread(()->{
		JOptionPane.showMessageDialog(null, "You have been removed. \n Or the manager closed the room.", "Error",
				JOptionPane.ERROR_MESSAGE);
		
		this.whiteBoardClient.getFrame().dispose();
		System.exit(0);});
		thread.start();
	}

	@Override
	public void closeClient(String name) throws RemoteException {
		this.whiteBoardClient.getFrame().dispose();
	}

	@Override
	public boolean Permission(String name) throws IOException {
		int isAllow = JOptionPane.showConfirmDialog(null, name + " want to join in this room?", "allow",
				JOptionPane.YES_NO_OPTION);
		if (isAllow == JOptionPane.YES_OPTION) {
			System.out.println(name + " join");
			return true;
		}
		return false;
	}

}
