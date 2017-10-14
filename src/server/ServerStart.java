package server;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ServerStart {
	JLabel serverName;
	JLabel serverPort;
	JTextField name;
	JTextField port;
	JButton confirmBtn;


	private JFrame frame;
	
    public static String SERVER_NAME = null;
    public static int SERVER_PORT = 0;
    
    public ServerStart() {  
		initialize();
	}

    private void initialize() {
    	frame = new JFrame();

		frame.setLayout(new FlowLayout(FlowLayout.CENTER));

    	serverName = new JLabel("please input the server name: ");
    	serverName.setFont(new Font("Arial", Font.PLAIN, 12));
    	name = new JTextField(12);
    	frame.add(serverName);
    	frame.add(name);

    	serverPort = new JLabel("please input the server port:  ");
    	serverPort.setFont(new Font("Arial", Font.PLAIN, 12));
    	port = new JTextField(11);
    	frame.add(serverPort);
    	frame.add(port);


        confirmBtn = new JButton();
        confirmBtn.setText("Confirm");
        confirmBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        frame.add(confirmBtn);

        
        confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SERVER_NAME = name.getText();
				 String portStr = port.getText();
				 SERVER_PORT = Integer.parseInt(portStr);
				// TODO Auto-generated method stub
				if(SERVER_NAME.length() == 0) {
					JOptionPane.showMessageDialog(null,
	                        "Please input the valid server name", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				if((SERVER_PORT>65535) ||(SERVER_PORT<1024) ) {
					JOptionPane.showMessageDialog(null,
	                        "Please input the valid server port (1024-65535)", "Error",
	                        JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				String args[] = {SERVER_NAME, portStr};
				RMIServer.main(args);
				frame.dispose();
			}
        });
          
       
        frame.setSize(370,128);  
        frame.setTitle("Server Start");  
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerStart window = new ServerStart();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
