package org;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBService;
import server.RemoteClient;
import server.RemoteServer;
/**
 * multi-clients version v0.2
 * @author tianzhangh
 *
 */
public class WhiteBoardClient {

	public static JFrame frame;
	private JPanel titlePanel;
	private JLabel lblWhiteboard;
	private JPanel functionPanel;
	private JLabel lblShapeType;
	private JButton btnLine;
	private JButton btnRectangle;
	private JButton btnCircle;
	private JButton btnOval;
	private JButton btnFree;
	private JLabel lblColor;
	private JButton btnErase;
	private JLabel lblDrawType;
	private JRadioButton rdbtnFill;
	private JButton btnColorChoosing;
	private JButton btnText;
	private JLabel lblChat;
	private JButton btnOpenChat;
	private JButton btnPaintSize;
	private JMenuBar menuBar;
	private JButton btnPoly;
	private JScrollPane scrollPane;
	private static JTextArea textArea;
	private JLabel lblOptions;
	
	private ChatDialog chatDialog;
	private PaintSurface paintSurface;
	private IRemoteClient client;
	private IRemoteServer server;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Retrieve the stub/proxy for the remote object from the registry
					Registry registry = LocateRegistry.getRegistry("localhost");
					
					IRemoteWBService remoteWB = (IRemoteWBService) registry.lookup(IRemoteWBService.LOOKUP_NAME);
					
					String roomname ="whiteboard1";
					IRemoteClient manager = new RemoteClient(0, "tianzhangh");
					manager.setClientLevel(RemoteClient.ClientLevel.MANAGER);
					
					IRemoteServer remoteserver = remoteWB.createRoom(manager, roomname);
					if(remoteserver != null) {
						remoteserver.setManager(manager);
					}
					
				    //add manager
					remoteserver.addClient(manager);
					
					
					WhiteBoardClient window = new WhiteBoardClient(manager, remoteserver);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}*/

	/**
	 * Create the application.
	 * @throws RemoteException 
	 */
	public WhiteBoardClient(IRemoteClient client, IRemoteServer server) throws RemoteException {
		this.client = client;
		this.server = server;
		
		initialize();
		
		btnPaintSize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showPaintSize();
			}
		});
		
		rdbtnFill.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				JRadioButton choose = (JRadioButton) e.getSource();
				if(choose.isSelected()){
					PaintSurface.drawType = 1;
				}else{
					PaintSurface.drawType = 0;
				}
				
			}
		});
		
		btnLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Line";
			}
		});
		btnRectangle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Rectangle";
			}
		});
		
		btnCircle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Circle";
			}
		});
		
		btnPoly.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Poly";
			}
		});
		
		btnFree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Free";
			}
		});
		
		
		btnErase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Eraser";
				showEraserSize();
			}
		});
		
		btnOval.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Oval";
			}
		});
		
		btnText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Text";
			}
		});
		
		
		btnColorChoosing.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.color = JColorChooser.showDialog(null, "Color Choosing", PaintSurface.color);
			}
		});
		
		btnOpenChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showChatWindow();
			}
		});

		
		
	}
	
	public static JFrame getFrame(){
		return frame;
	}
	
	public void showOptions(String msg){
		System.out.println("Show");
		String showMsg = msg + "\n\n";
		textArea.append(showMsg);
		textArea.setCaretPosition(textArea.getText().length());
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws RemoteException 
	 */
	private void initialize() throws RemoteException {
		frame = new JFrame();
		//frame.setBounds(100, 100, 450, 300);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(this.client.getClientName());
		
		//set PaintSurface to the RemoteClient
		paintSurface = new PaintSurface(client, server);
		((RemoteClient) this.client).setPaint(paintSurface);
		
		//set chatDialog to the RemoteClient and the setVisible false
		chatDialog = new ChatDialog(client, server);
		((RemoteClient) client).setChat(chatDialog);
		
		
		frame.getContentPane().add(paintSurface, BorderLayout.CENTER);
		
		titlePanel = new JPanel();
		frame.getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		lblWhiteboard = new JLabel(this.server.getRoomName());
		lblWhiteboard.setFont(new Font("Times New Roman", Font.BOLD, 40));
		titlePanel.add(lblWhiteboard);	
		
		functionPanel = new JPanel();
		frame.getContentPane().add(functionPanel, BorderLayout.WEST);
		GridBagLayout gbl_functionPanel = new GridBagLayout();
		gbl_functionPanel.columnWidths = new int[]{0, 0};
		gbl_functionPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_functionPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_functionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		functionPanel.setLayout(gbl_functionPanel);
		
		lblDrawType = new JLabel("Draw Type");
		lblDrawType.setFont(new Font("Times New Roman", Font.BOLD, 20));
		GridBagConstraints gbc_lblDrawType = new GridBagConstraints();
		gbc_lblDrawType.insets = new Insets(0, 0, 5, 0);
		gbc_lblDrawType.gridx = 0;
		gbc_lblDrawType.gridy = 0;
		functionPanel.add(lblDrawType, gbc_lblDrawType);
		
		btnPaintSize = new JButton("Paint Size");
		GridBagConstraints gbc_btnPaintSize = new GridBagConstraints();
		gbc_btnPaintSize.insets = new Insets(0, 0, 5, 0);
		gbc_btnPaintSize.gridx = 0;
		gbc_btnPaintSize.gridy = 1;
		functionPanel.add(btnPaintSize, gbc_btnPaintSize);
		
		rdbtnFill = new JRadioButton("Fill");
		GridBagConstraints gbc_rdbtnFill = new GridBagConstraints();
		gbc_rdbtnFill.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnFill.gridx = 0;
		gbc_rdbtnFill.gridy = 2;
		functionPanel.add(rdbtnFill, gbc_rdbtnFill);
		
		lblShapeType = new JLabel(" Shape Type ");
		lblShapeType.setFont(new Font("Times New Roman", Font.BOLD, 20));
		GridBagConstraints gbc_lblShapeType = new GridBagConstraints();
		gbc_lblShapeType.insets = new Insets(0, 0, 5, 0);
		gbc_lblShapeType.gridx = 0;
		gbc_lblShapeType.gridy = 3;
		functionPanel.add(lblShapeType, gbc_lblShapeType);
		
		btnLine = new JButton(" Line ");
		GridBagConstraints gbc_btnLine = new GridBagConstraints();
		gbc_btnLine.insets = new Insets(0, 0, 5, 0);
		gbc_btnLine.gridx = 0;
		gbc_btnLine.gridy = 4;
		functionPanel.add(btnLine, gbc_btnLine);
		
		btnRectangle = new JButton(" Rect ");
		GridBagConstraints gbc_btnRectangle = new GridBagConstraints();
		gbc_btnRectangle.insets = new Insets(0, 0, 5, 0);
		gbc_btnRectangle.gridx = 0;
		gbc_btnRectangle.gridy = 5;
		functionPanel.add(btnRectangle, gbc_btnRectangle);
		
		btnCircle = new JButton("Circle");
		GridBagConstraints gbc_btnCircle = new GridBagConstraints();
		gbc_btnCircle.insets = new Insets(0, 0, 5, 0);
		gbc_btnCircle.gridx = 0;
		gbc_btnCircle.gridy = 6;
		functionPanel.add(btnCircle, gbc_btnCircle);
		
		btnOval = new JButton(" Oval ");
		GridBagConstraints gbc_btnOval = new GridBagConstraints();
		gbc_btnOval.insets = new Insets(0, 0, 5, 0);
		gbc_btnOval.gridx = 0;
		gbc_btnOval.gridy = 7;
		functionPanel.add(btnOval, gbc_btnOval);
		
		btnPoly = new JButton("Poly");
		GridBagConstraints gbc_btnPoly = new GridBagConstraints();
		gbc_btnPoly.insets = new Insets(0, 0, 5, 0);
		gbc_btnPoly.gridx = 0;
		gbc_btnPoly.gridy = 8;
		functionPanel.add(btnPoly, gbc_btnPoly);
		
		btnFree = new JButton(" Free ");
		GridBagConstraints gbc_btnFree = new GridBagConstraints();
		gbc_btnFree.insets = new Insets(0, 0, 5, 0);
		gbc_btnFree.gridx = 0;
		gbc_btnFree.gridy = 9;
		functionPanel.add(btnFree, gbc_btnFree);
		
		btnText = new JButton("Text");
		GridBagConstraints gbc_btnText = new GridBagConstraints();
		gbc_btnText.insets = new Insets(0, 0, 5, 0);
		gbc_btnText.gridx = 0;
		gbc_btnText.gridy = 10;
		functionPanel.add(btnText, gbc_btnText);
		
		btnErase = new JButton("Erase");
		GridBagConstraints gbc_btnEarse = new GridBagConstraints();
		gbc_btnEarse.insets = new Insets(0, 0, 5, 0);
		gbc_btnEarse.gridx = 0;
		gbc_btnEarse.gridy = 11;
		functionPanel.add(btnErase, gbc_btnEarse);
		
		lblColor = new JLabel("Color");
		lblColor.setFont(new Font("Times New Roman", Font.BOLD, 20));
		GridBagConstraints gbc_lblColor = new GridBagConstraints();
		gbc_lblColor.insets = new Insets(0, 0, 5, 0);
		gbc_lblColor.gridx = 0;
		gbc_lblColor.gridy = 13;
		functionPanel.add(lblColor, gbc_lblColor);
		
		btnColorChoosing = new JButton("Choose");
		GridBagConstraints gbc_btnColorChoosing = new GridBagConstraints();
		gbc_btnColorChoosing.insets = new Insets(0, 0, 5, 0);
		gbc_btnColorChoosing.gridx = 0;
		gbc_btnColorChoosing.gridy = 14;
		functionPanel.add(btnColorChoosing, gbc_btnColorChoosing);
		
		lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Times New Roman", Font.BOLD,20));
		GridBagConstraints gbc_lblChat = new GridBagConstraints();
		gbc_lblChat.insets = new Insets(0, 0, 5, 0);
		gbc_lblChat.gridx = 0;
		gbc_lblChat.gridy = 16;
		functionPanel.add(lblChat, gbc_lblChat);
		
		btnOpenChat = new JButton("Open Chat");
		btnOpenChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnOpenChat = new GridBagConstraints();
		gbc_btnOpenChat.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenChat.gridx = 0;
		gbc_btnOpenChat.gridy = 17;
		functionPanel.add(btnOpenChat, gbc_btnOpenChat);
		
		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Times New Roman", Font.BOLD,20));
		GridBagConstraints gbc_lblOptions = new GridBagConstraints();
		gbc_lblOptions.insets = new Insets(0, 0, 5, 0);
		gbc_lblOptions.gridx = 0;
		gbc_lblOptions.gridy = 18;
		functionPanel.add(lblOptions, gbc_lblOptions);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(90, 80));
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.VERTICAL;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 19;
		functionPanel.add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(214,217,223));
		textArea.setBorder(null);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Times New Roman", Font.BOLD,10));
		scrollPane.setViewportView(textArea);
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setActionCommand("New");
		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setActionCommand("Open");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setActionCommand("Save");
		JMenuItem saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.setActionCommand("Save As");
		
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		
		frame.setJMenuBar(menuBar);
		
	}
	
	private void showPaintSize(){
		PaintSizeDialog paintSizeDialog = new PaintSizeDialog();
	}
	
	private void showEraserSize(){
		EraserSizeDialog eraserSizeDialog = new EraserSizeDialog();
	}
	
	private void showChatWindow(){
		chatDialog.jDialog.setVisible(true);
	}
	

}
