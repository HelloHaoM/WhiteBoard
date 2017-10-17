package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.WhiteBoardClient;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;
import server.RemoteClient;

/**
 * multi-clients version v0.2 ManagerClient and GuestClient implement the main
 * function, which separates it from the GUI class
 * 
 * @author tianzhangh
 *
 */
public class ManagerClient {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JOptionPane.showMessageDialog(null, "Connecting....\n Pleas wait. ", "Hints", JOptionPane.INFORMATION_MESSAGE);
					// Retrieve the stub/proxy for the remote object from the registry
					//Registry registry = LocateRegistry.getRegistry("localhost");
					Registry registry = LocateRegistry.getRegistry(args[4]);
					
					IRemoteWBService remoteWB = (IRemoteWBService) registry.lookup(IRemoteWBService.LOOKUP_NAME);

					// String roomname = "whiteboard1";
					String roomname = args[2];
					// IRemoteClient manager = new RemoteClient(0, "tianzhangh");
					IRemoteClient manager = new RemoteClient(Integer.parseInt(args[0]), args[1]);

					manager.setClientLevel(RemoteClient.ClientLevel.MANAGER);

					IRemoteServer remoteserver = remoteWB.createRoom(manager, roomname);
					if (remoteserver != null) {
						remoteserver.setManager(manager);
					} else {
						JOptionPane.showMessageDialog(null, "Room name exist.", "Error", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}

					// add manager
					remoteserver.addClient(manager);

					WhiteBoardClient window = new WhiteBoardClient(manager, remoteserver);
					window.frame.setVisible(true);
					window.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

					Set<String> nameList = remoteserver.getClientNameList();
					Iterator<String> it = nameList.iterator();
					while (it.hasNext()) {
						String name = it.next();
						System.out.println(name);
						window.getDlm().addElement(name);
						window.getJlist().setModel(window.getDlm());
					}

					((RemoteClient) manager).setWhiteBoardClient(window);

					// remove the client from the list
					window.getJlist().addListSelectionListener(new ListSelectionListener() {
						boolean flag = false;

						public void valueChanged(ListSelectionEvent e) {
							String name = (String) window.getJlist().getSelectedValue();
							if (name != null) {
								// the manager should not remove himself/herself.
								if (!name.equals(args[1])) {
									if ((e.getValueIsAdjusting() == false) && (flag == false)) {
										flag = true;
										int isRemove = JOptionPane.showConfirmDialog(null,
												"Do you want to kick out this client?", "Confirm",
												JOptionPane.YES_NO_OPTION);
										if (isRemove == JOptionPane.YES_OPTION) {

											window.getDlm().removeElement(name);
											try {
												remoteserver.removeClient(remoteserver.getClient(name));
												remoteserver.removeRequest(manager, name);
												manager.alertClientList(remoteserver.getClientNameList());
												remoteserver.updateAllClientsWithClientName();

												// remoteserver.closeWindow(remoteserver.getClient(name));

											} catch (RemoteException e1) {
												e1.printStackTrace();
											}
										}
										flag = false;
									}
								} else {
									int op = JOptionPane.showConfirmDialog(null,
											"You are not allow to remove yourself. \n"
													+ "Do you want to close this room?  ",
											"Warning", JOptionPane.YES_NO_OPTION);
									if (op == JOptionPane.YES_OPTION) {
										try {
											remoteWB.removeRoom(manager, roomname);
										} catch (RemoteException | NotBoundException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										window.getFrame().dispose();
										System.exit(0);
									}

								}
							}
						}
					});

					// remove the room
					window.getFrame().addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent we) {
							int i = JOptionPane.showConfirmDialog(null, "Room will be removed.", "Warning",
									JOptionPane.YES_NO_OPTION);
							if (i == JOptionPane.YES_OPTION) {
								try {
									remoteWB.removeRoom(manager, roomname);
								} catch (RemoteException | NotBoundException e1) {
									// TODO Auto-generated catch block
									System.out.println("Connection reset");
								}
								window.getFrame().dispose();
								System.exit(0);
							}
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Invalid request. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		});

	}

}
