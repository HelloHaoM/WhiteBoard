package org;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class TextDirDialog {
public JDialog jDialog;
	
	public TextDirDialog(){
		init();
		jDialog.setModal(true);
		jDialog.setVisible(true);
	}
	
	private void init(){
		jDialog = new JDialog();
		jDialog.setBounds(100, 100, 200, 80);
		jDialog.setTitle("Text Direction");
		jDialog.getContentPane().setLayout(new GridLayout(1, 3));
		JButton btnSize1 = new JButton("Hor");
		btnSize1.setBackground(Color.white);
		jDialog.getContentPane().add(btnSize1);
		btnSize1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.drawType = 3;
				jDialog.dispose();
			}
		});
		JButton btnSize2 = new JButton("Ver");
		btnSize2.setBackground(Color.white);
		jDialog.getContentPane().add(btnSize2);
		btnSize2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.drawType = 5;
				jDialog.dispose();
			}
		});
	}


}
