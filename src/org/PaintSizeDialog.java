package org;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class PaintSizeDialog {
	public JDialog jDialog;
	
	public PaintSizeDialog(){
		init();
		jDialog.setModal(true);
		jDialog.setVisible(true);
	}
	
	private void init(){
		jDialog = new JDialog();
		jDialog.setBounds(100, 100, 200, 80);
		jDialog.setTitle("Paint Size");
		jDialog.getContentPane().setLayout(new GridLayout(1, 3));
		JButton btnSize1 = new JButton();
		URL urlSmall = this.getClass().getResource("/small.png");
		Icon small = new ImageIcon(urlSmall);
		btnSize1.setIcon(small);
		btnSize1.setBackground(Color.white);
		jDialog.getContentPane().add(btnSize1);
		btnSize1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.strokeValue = 2;
				jDialog.dispose();
			}
		});
		JButton btnSize2 = new JButton();
		URL urlMid = this.getClass().getResource("/mid.png");
		Icon mid = new ImageIcon(urlMid);
		btnSize2.setIcon(mid);
		btnSize2.setBackground(Color.white);
		jDialog.getContentPane().add(btnSize2);
		btnSize2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.strokeValue = 6;
				jDialog.dispose();
			}
		});
		
		JButton btnSize3 = new JButton();
		URL urlBig = this.getClass().getResource("/big.png");
		Icon big = new ImageIcon(urlBig);
		btnSize3.setIcon(big);
		btnSize3.setBackground(Color.white);
		jDialog.getContentPane().add(btnSize3);
		btnSize3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.strokeValue = 10;
				jDialog.dispose();
			}
		});
	}

}
