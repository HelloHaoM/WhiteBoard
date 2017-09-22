package org;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class TextDialog {
	public JDialog jDialog;
	
	public TextDialog(){
		init();
		jDialog.setModal(true);
		jDialog.setVisible(true);
	}
	
	public void init(){
		jDialog = new JDialog();
		jDialog.setBounds(320, 180, 260, 100);
		jDialog.setTitle("Text Input");
		jDialog.getContentPane().setLayout(new GridLayout(1, 1));
		JTextField jTextField = new JTextField(80);
		jDialog.add(jTextField);
		JButton btnOk = new JButton("OK");
		jDialog.add(btnOk);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PaintSurface.text = jTextField.getText();
				jDialog.dispose();
			}
		});
	}

}
