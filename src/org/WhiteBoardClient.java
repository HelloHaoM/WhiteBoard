package org;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.html.HTMLDocument.Iterator;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBItem;
import remote.IRemoteWBService;
import server.RemoteClient;
import server.RemoteServer;
import server.RemoteWBItem;

/**
 * multi-clients version v0.2
 * 
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
	private JLabel lblClientList;
	private JLabel lblChat_1;
	private static JTextArea txtrShowchat;
	private JLabel lblInput;
	private JButton btnSend;
	private JScrollPane scrollPane_3;
	private static JList list;
	private static DefaultListModel<String> dlm;
	private JPanel panel;
	private JTextArea txtrInputchat;

	private PaintSurface paintSurface;
	private IRemoteClient client;
	private IRemoteServer server;

	private String fileName;
	private String filePath;
	private boolean isOpenFile;

	private JMenu fileMenu;
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem saveAsMenuItem;

	private boolean isFill;

	/**
	 * Create the application.
	 * 
	 * @throws RemoteException
	 */
	public WhiteBoardClient(IRemoteClient client, IRemoteServer server) throws RemoteException {
		this.client = client;
		this.server = server;
		
		isOpenFile = false;

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
				if (choose.isSelected()) {
					PaintSurface.drawType = 1;
					isFill = true;
				} else {
					PaintSurface.drawType = 0;
					isFill = false;
				}

			}
		});

		btnLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Line";
				if (isFill) {
					PaintSurface.drawType = 1;
				} else {
					PaintSurface.drawType = 0;
				}
			}
		});
		btnRectangle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Rectangle";
				if (isFill) {
					PaintSurface.drawType = 1;
				} else {
					PaintSurface.drawType = 0;
				}
			}
		});

		btnCircle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Circle";
				if (isFill) {
					PaintSurface.drawType = 1;
				} else {
					PaintSurface.drawType = 0;
				}
			}
		});

		btnPoly.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Poly";
				if (isFill) {
					PaintSurface.drawType = 1;
				} else {
					PaintSurface.drawType = 0;
				}
			}
		});

		btnFree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Free";
				PaintSurface.drawType = 0;
			}
		});

		btnOval.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Oval";
				if (isFill) {
					PaintSurface.drawType = 1;
				} else {
					PaintSurface.drawType = 0;
				}
			}
		});

		btnErase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Eraser";
				showEraserSize();
			}
		});

		btnText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.shapeType = "Text";
				showTextDir();
			}
		});

		btnColorChoosing.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.color = JColorChooser.showDialog(null, "Color Choosing", PaintSurface.color);
			}
		});

		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					LocalDateTime date = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
					server.sendMessage(client.getClientName() + "[" + date.format(formatter) + "]: " + "\n"
							+ txtrInputchat.getText() + "\n");
					txtrInputchat.setText("");
				} catch (RemoteException re) {
					re.printStackTrace();
				}

			}
		});

	}

	public static JFrame getFrame() {
		return frame;
	}

	public static JTextArea getShowChat() {
		return txtrShowchat;
	}

	public static DefaultListModel<String> getDlm() {
		return dlm;
	}

	public static JList getJlist() {
		return list;
	}
	

	public void showOptions(String msg) {
		System.out.println("Show");
		String showMsg = msg + "\n\n";
		textArea.append(showMsg);
		textArea.setCaretPosition(textArea.getText().length());
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws RemoteException
	 */
	private void initialize() throws RemoteException {
		frame = new JFrame();
		// frame.setBounds(100, 100, 450, 300);
		frame.setSize(1000, 800);
		//Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		//frame.setSize((int)(screenSize.width*0.8),(int)(screenSize.height*0.8));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setTitle(this.client.getClientName());

		// set PaintSurface to the RemoteClient
		paintSurface = new PaintSurface(client, server);
		((RemoteClient) this.client).setPaint(paintSurface);
		
		for (IRemoteWBItem remoteshape : server.getShapes()) {
			this.paintSurface.addItem(remoteshape);
		}
		
		ImageIcon img = this.server.getImg();
		this.server.addImg(client,img);
		

		frame.getContentPane().add(paintSurface, BorderLayout.CENTER);

		titlePanel = new JPanel();
		frame.getContentPane().add(titlePanel, BorderLayout.NORTH);

		lblWhiteboard = new JLabel(this.server.getRoomName());
		lblWhiteboard.setFont(new Font("Times New Roman", Font.BOLD, 40));
		titlePanel.add(lblWhiteboard);

		functionPanel = new JPanel();
		frame.getContentPane().add(functionPanel, BorderLayout.WEST);
		GridBagLayout gbl_functionPanel = new GridBagLayout();
		gbl_functionPanel.columnWidths = new int[] { 0, 0 };
		gbl_functionPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_functionPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_functionPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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

		lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Times New Roman", Font.BOLD, 20));
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
		textArea.setBackground(new Color(214, 217, 223));
		textArea.setBorder(null);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Times New Roman", Font.BOLD, 10));
		scrollPane.setViewportView(textArea);

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);

		panel.setPreferredSize(new Dimension(250, 1000));
		panel.setLayout(null);

		lblClientList = new JLabel("Client List");
		lblClientList.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblClientList.setBounds(87, 6, 92, 16);
		panel.add(lblClientList);

		lblChat_1 = new JLabel("Chat");
		lblChat_1.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblChat_1.setBounds(110, 270, 61, 16);
		panel.add(lblChat_1);

		lblInput = new JLabel("Input");
		lblInput.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblInput.setBounds(6, 523, 61, 16);
		panel.add(lblInput);

		btnSend = new JButton("Send");
		btnSend.setBounds(75, 603, 117, 29);
		panel.add(btnSend);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 298, 238, 214);
		panel.add(scrollPane_1);

		txtrShowchat = new JTextArea();
		scrollPane_1.setViewportView(txtrShowchat);
		txtrShowchat.setLineWrap(true);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 551, 238, 34);
		panel.add(scrollPane_2);

		txtrInputchat = new JTextArea();
		scrollPane_2.setViewportView(txtrInputchat);
		txtrInputchat.setLineWrap(true);

		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(6, 34, 238, 214);
		panel.add(scrollPane_3);

		dlm = new DefaultListModel<String>();
		list = new JList();
		list.setModel(dlm);
		//this.server.updateList(client);
		scrollPane_3.setViewportView(list);

		fileMenu = new JMenu("File");
		newMenuItem = new JMenuItem("New");
		newMenuItem.setActionCommand("New");
		openMenuItem = new JMenuItem("Open");
		openMenuItem.setActionCommand("Open");
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setActionCommand("Save");
		saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.setActionCommand("Save As");

		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);

		menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		frame.setJMenuBar(menuBar);

		// listen file operations
		setUpEventListener();

	}

	private void showPaintSize() {
		PaintSizeDialog paintSizeDialog = new PaintSizeDialog();
	}

	private void showTextDir() {
		TextDirDialog textDirDialog = new TextDirDialog();
	}

	private void showEraserSize() {
		EraserSizeDialog eraserSizeDialog = new EraserSizeDialog();
	}

	public void newFile() {
		frame.setTitle("");
		int wantSave = JOptionPane.showConfirmDialog(null, "Do you want to save current file?", "Hints", 0);

		if (wantSave == 0) {
			try {
				saveFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			paintSurface.removeAll();
			if (isOpenFile == true) {
				frame.getContentPane().remove(imgLabel);
			}
			paintSurface.shapes.removeAll(paintSurface.shapes);
			paintSurface.repaint();*/
			
			// Clean all canvas
			try {
				server.cleanAll();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (wantSave == 1) {
			
			// Clean all canvas
			try {
				server.cleanAll();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void openFile() throws IOException {
		// File file = null;
		server.cleanAll();
		JFileChooser chooseFile = new JFileChooser();
		chooseFile.setMultiSelectionEnabled(false);
		chooseFile.setDialogTitle("Pleash choose the file");
		// Specify the file type
		chooseFile.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				}
				String name = file.getName().toLowerCase();
				if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp")
						|| name.endsWith(".ico") || name.endsWith(".jpeg")) {
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				return "Image Files";
			}

		});

		int returnVal = chooseFile.showOpenDialog(null);
		chooseFile.setVisible(true);

		switch (returnVal) {
		case JFileChooser.APPROVE_OPTION:
			File file = chooseFile.getSelectedFile().getAbsoluteFile();
			fileName = file.getName();
			filePath = file.getPath();
			frame.setTitle(fileName);
			
			// Clean all canvas
			

			isOpenFile = true;
			ImageIcon img = new ImageIcon(filePath);
			IRemoteWBItem item = new RemoteWBItem(this.client, img, 6);
			this.paintSurface.addItem(item);
			server.addImage(this.client, img, 6);
			
			break;
		case JFileChooser.CANCEL_OPTION:

			break;
		case JFileChooser.ERROR_OPTION:
			break;
		default:
			break;
		}

	}

	public void saveFile() throws IOException {
		//String name = frame.getTitle();
		//if (name.equals("") || name.equals(this.client.getClientName())) { // save as a new file
			saveAsFile();
		/*} else {

			// if (isOpenFile == true)
			// frame.getContentPane().remove(imgLabel);
			// paintSurface.shapes.removeAll(paintSurface.shapes);
			// paintSurface.repaint();

			FileOutputStream fos = new FileOutputStream(filePath);

			/*
			BufferedImage imgNew = null;
			try {
				imgNew = new Robot().createScreenCapture(new Rectangle(paintSurface.getLocationOnScreen().x,
						paintSurface.getLocationOnScreen().y, paintSurface.getWidth(), paintSurface.getHeight()));
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			ImageIO.write(imgNew, "jpg", bos);
			bos.flush();
			bos.close();*/
			/*
			Component component = paintSurface;
			BufferedImage bufferedImage = (BufferedImage) component.createImage(component.getWidth(),
					component.getHeight());
			component.paint(bufferedImage.getGraphics());
			BufferedOutputStream out = new BufferedOutputStream(fos);
			ImageIO.write(bufferedImage, "jpg", out);
			out.flush();
			out.close();
			
			JOptionPane.showMessageDialog(null, "The file was successfully saved.", "Hints",
					JOptionPane.INFORMATION_MESSAGE);
			
		}*/
	}

	public void saveAsFile() throws IOException {
		FileDialog saveDialog = new FileDialog(frame, "Save as...", FileDialog.SAVE);
		saveDialog.setVisible(true);

		fileName = saveDialog.getFile();
		filePath = saveDialog.getDirectory();

		FileOutputStream fos = new FileOutputStream(filePath + fileName);
		
		/*
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = new Robot().createScreenCapture(new Rectangle(paintSurface.getLocationOnScreen().x,
					paintSurface.getLocationOnScreen().y, paintSurface.getWidth(), paintSurface.getHeight()));
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedOutputStream output = new BufferedOutputStream(fos);*/

		String saveFormat = fileName.split("\\.")[1].toString();
		//ImageIO.write(bufferedImage, saveFormat, output);
		
		
		Component component = paintSurface;
		BufferedImage bufferedImage = (BufferedImage) component.createImage(component.getWidth(),
				component.getHeight());
		component.paint(bufferedImage.getGraphics().create(0, 0, component.getWidth(), component.getHeight()));
		BufferedOutputStream output = new BufferedOutputStream(fos);

		ImageIO.write(bufferedImage, "jpg", output);

		output.flush();
		output.close();
		
		//output.flush();
		//output.close();

		frame.setTitle(fileName);

	}
	
	private void setUpEventListener() {
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile();
			}
		});

		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		saveAsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveAsFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}
