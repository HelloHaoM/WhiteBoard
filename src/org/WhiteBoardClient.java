package org;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
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

import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WhiteBoardClient {

	private JFrame frame;
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WhiteBoardClient window = new WhiteBoardClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}

	/**
	 * Create the application.
	 */
	public WhiteBoardClient() {
		initialize();
		
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		//frame.setBounds(100, 100, 450, 300);
		frame.setSize(1000, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PaintSurface paintSurface = new PaintSurface();
		paintSurface.setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().add(paintSurface, BorderLayout.CENTER);
		
		titlePanel = new JPanel();
		frame.getContentPane().add(titlePanel, BorderLayout.NORTH);
		
		lblWhiteboard = new JLabel("WhiteBoard");
		lblWhiteboard.setFont(new Font("Times New Roman", Font.BOLD, 40));
		titlePanel.add(lblWhiteboard);	
		
		functionPanel = new JPanel();
		frame.getContentPane().add(functionPanel, BorderLayout.WEST);
		GridBagLayout gbl_functionPanel = new GridBagLayout();
		gbl_functionPanel.columnWidths = new int[]{0, 0};
		gbl_functionPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_functionPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_functionPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		functionPanel.setLayout(gbl_functionPanel);
		
		lblDrawType = new JLabel("Draw Type");
		lblDrawType.setFont(new Font("Times New Roman", Font.BOLD, 20));
		GridBagConstraints gbc_lblDrawType = new GridBagConstraints();
		gbc_lblDrawType.insets = new Insets(0, 0, 5, 0);
		gbc_lblDrawType.gridx = 0;
		gbc_lblDrawType.gridy = 0;
		functionPanel.add(lblDrawType, gbc_lblDrawType);
		
		rdbtnFill = new JRadioButton("Fill");
		GridBagConstraints gbc_rdbtnFill = new GridBagConstraints();
		gbc_rdbtnFill.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnFill.gridx = 0;
		gbc_rdbtnFill.gridy = 1;
		functionPanel.add(rdbtnFill, gbc_rdbtnFill);
		
		lblShapeType = new JLabel(" Shape Type ");
		lblShapeType.setFont(new Font("Times New Roman", Font.BOLD, 20));
		GridBagConstraints gbc_lblShapeType = new GridBagConstraints();
		gbc_lblShapeType.insets = new Insets(0, 0, 5, 0);
		gbc_lblShapeType.gridx = 0;
		gbc_lblShapeType.gridy = 2;
		functionPanel.add(lblShapeType, gbc_lblShapeType);
		
		btnLine = new JButton(" Line ");
		GridBagConstraints gbc_btnLine = new GridBagConstraints();
		gbc_btnLine.insets = new Insets(0, 0, 5, 0);
		gbc_btnLine.gridx = 0;
		gbc_btnLine.gridy = 3;
		functionPanel.add(btnLine, gbc_btnLine);
		
		btnRectangle = new JButton(" Rect ");
		GridBagConstraints gbc_btnRectangle = new GridBagConstraints();
		gbc_btnRectangle.insets = new Insets(0, 0, 5, 0);
		gbc_btnRectangle.gridx = 0;
		gbc_btnRectangle.gridy = 4;
		functionPanel.add(btnRectangle, gbc_btnRectangle);
		
		btnCircle = new JButton("Circle");
		GridBagConstraints gbc_btnCircle = new GridBagConstraints();
		gbc_btnCircle.insets = new Insets(0, 0, 5, 0);
		gbc_btnCircle.gridx = 0;
		gbc_btnCircle.gridy = 5;
		functionPanel.add(btnCircle, gbc_btnCircle);
		
		btnOval = new JButton(" Oval ");
		GridBagConstraints gbc_btnOval = new GridBagConstraints();
		gbc_btnOval.insets = new Insets(0, 0, 5, 0);
		gbc_btnOval.gridx = 0;
		gbc_btnOval.gridy = 6;
		functionPanel.add(btnOval, gbc_btnOval);
		
		btnFree = new JButton(" Free ");
		GridBagConstraints gbc_btnFree = new GridBagConstraints();
		gbc_btnFree.insets = new Insets(0, 0, 5, 0);
		gbc_btnFree.gridx = 0;
		gbc_btnFree.gridy = 7;
		functionPanel.add(btnFree, gbc_btnFree);
		
		btnText = new JButton("Text");
		GridBagConstraints gbc_btnText = new GridBagConstraints();
		gbc_btnText.insets = new Insets(0, 0, 5, 0);
		gbc_btnText.gridx = 0;
		gbc_btnText.gridy = 8;
		functionPanel.add(btnText, gbc_btnText);
		
		btnErase = new JButton("Erase");
		GridBagConstraints gbc_btnEarse = new GridBagConstraints();
		gbc_btnEarse.insets = new Insets(0, 0, 5, 0);
		gbc_btnEarse.gridx = 0;
		gbc_btnEarse.gridy = 9;
		functionPanel.add(btnErase, gbc_btnEarse);
		
		lblColor = new JLabel("Color");
		lblColor.setFont(new Font("Times New Roman", Font.BOLD, 20));
		GridBagConstraints gbc_lblColor = new GridBagConstraints();
		gbc_lblColor.insets = new Insets(0, 0, 5, 0);
		gbc_lblColor.gridx = 0;
		gbc_lblColor.gridy = 11;
		functionPanel.add(lblColor, gbc_lblColor);
		
		btnColorChoosing = new JButton("Choose");
		GridBagConstraints gbc_btnColorChoosing = new GridBagConstraints();
		gbc_btnColorChoosing.insets = new Insets(0, 0, 5, 0);
		gbc_btnColorChoosing.gridx = 0;
		gbc_btnColorChoosing.gridy = 12;
		functionPanel.add(btnColorChoosing, gbc_btnColorChoosing);
		
		lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Times New Roman", Font.BOLD,20));
		GridBagConstraints gbc_lblChat = new GridBagConstraints();
		gbc_lblChat.insets = new Insets(0, 0, 5, 0);
		gbc_lblChat.gridx = 0;
		gbc_lblChat.gridy = 14;
		functionPanel.add(lblChat, gbc_lblChat);
		
		btnOpenChat = new JButton("Open Chat");
		btnOpenChat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnOpenChat = new GridBagConstraints();
		gbc_btnOpenChat.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpenChat.gridx = 0;
		gbc_btnOpenChat.gridy = 15;
		functionPanel.add(btnOpenChat, gbc_btnOpenChat);
		
	}
	
	private void showChatWindow(){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try{
					ChatFrame window = new ChatFrame();
					window.frame.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}
		});
	}
	

}
