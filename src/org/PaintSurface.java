package org;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import remote.IRemoteClient;
import remote.IRemoteServer;
import remote.IRemoteWBItem;
import server.RemoteWBItem;

/**
 * multi-clients version v 0.1
 * @author tianzhangh
 *
 */
public class PaintSurface extends JComponent{
	private List<IRemoteWBItem> remoteshapes;
	private IRemoteClient client;
	private IRemoteServer remoteserver;
	
	ArrayList<IRemoteWBItem> shapes = new ArrayList<IRemoteWBItem>();
	
	Point startDrag, endDrag;
	int[] x = new int[50];
	int[] y = new int [50];
	static int xcount = 0;
	static int ycount = 0;
	static Color color = Color.BLACK;
	public static int strokeValue = 2;
	public static int eraserSize = 10;
	public static String shapeType = "";
	public static int drawType = 0;
	public static String text = "";
	
	

	public Graphics gg;
	public Shape eraserShape;
	
	public List<IRemoteWBItem> getShapes(){
		return this.remoteshapes;
	}
	public PaintSurface(IRemoteClient client, IRemoteServer remoteserver) throws RemoteException{
		this.client = client;
		this.remoteserver = remoteserver;
		remoteshapes = new ArrayList<IRemoteWBItem>();
		 for (IRemoteWBItem remoteshape : remoteserver.getShapes()   ) {
			 	this.addItem(remoteshape);
				
	        }
		
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				Shape r = null;
				IRemoteWBItem item;
				try{
					
				item = new RemoteWBItem(client, r, color, drawType, strokeValue);
			
				
				if(shapeType.equals("Line")){
					r = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
					
					
					//add shape to the IRemoteWBItem list (Need implements more elements based on MyShape.java)
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
					
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Rectangle")){
					r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
					
					
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
					
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Circle")){
					r = makeCircle(startDrag.x, startDrag.y, e.getX(), e.getY());
					
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Oval")){
					r = makeOval(startDrag.x, startDrag.y, e.getX(), e.getY());
					
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Poly")){
					x[xcount] = startDrag.x;
					y[ycount] = startDrag.y;
					xcount++;
					ycount++;
					x[xcount] = e.getX();
					y[xcount] = e.getY();
					xcount++;
					ycount++;
					r = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
					
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
				}else if(shapeType.equals("Text")){
					
					TextDialog t1 = new TextDialog();
					IRemoteWBItem myTextShape = new RemoteWBItem(client, r, color, 3, strokeValue);
					((RemoteWBItem)myTextShape).setPos(startDrag);
					((RemoteWBItem)myTextShape).setText(text);
					shapes.add(myTextShape);
					xcount = 0;
					ycount = 0;
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addText(client, r, text, color, 3, strokeValue, startDrag);
				}
				startDrag = null;
				endDrag = null;
				repaint();
				} catch(RemoteException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				IRemoteWBItem item;
				
				try {
				if(e.getButton() == MouseEvent.BUTTON3){
					
					Shape r = makeGeneralPath(x, y, xcount);
					
					item = new RemoteWBItem(client, r, color, drawType, strokeValue);
					
					
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
					xcount = 0;
					ycount = 0;
				}
				repaint();} 
				
				catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			IRemoteWBItem item;
			
			public void mouseDragged(MouseEvent e){
				endDrag = new Point(e.getX(), e.getY());
				try {
					
				if(shapeType.equals("Eraser")){
					eraserShape = new Rectangle2D.Float(e.getX() - eraserSize/2, e.getY() - eraserSize/2, eraserSize, eraserSize);
					 item = new RemoteWBItem(client,eraserShape, WhiteBoardClient.getFrame().getBackground(), 4, eraserSize);
					
					//add shape to the IRemoteWBItem list
					remoteshapes.add(item);
					
					// add new shape to the rmi server
					//remoteserver.addShape(client,eraserShape, color, drawType, strokeValue);
					remoteserver.addShape(client,eraserShape, WhiteBoardClient.getFrame().getBackground(), 4, eraserSize);
					
				}
				if(shapeType.equals("Free")){
					Shape r = new Rectangle2D.Float(e.getX() - strokeValue/2, e.getY() - strokeValue/2, strokeValue, strokeValue);
					item = new RemoteWBItem(client, r, color, drawType, strokeValue);
					
					//add shape to the IRemoteWBItem list
					addItem(item);
					
					// add new shape to the rmi server
					remoteserver.addShape(client, r, color, drawType, strokeValue);
				}
				repaint();}
				catch(Exception ex) {ex.printStackTrace();}
				
			}
			
			@Override
			public void mouseMoved(MouseEvent e){
				if(shapeType.equals("Eraser")){
					eraserShape = new Rectangle2D.Float(e.getX() - eraserSize/2, e.getY() - eraserSize/2, eraserSize, eraserSize);
					repaint();
				}
			}
		});
		
	}
	
	public void addItem(IRemoteWBItem item) {
		remoteshapes.add(item);
		
		try {
			
			// example here need to unify the two class: MyShape and RemoteWBItem
		if(item.getDrawType() == 3){
			shapes.add(new RemoteWBItem(client,item.getShape(), item.getText(), item.getColour(), item.getDrawType(), item.getStrokeValue(), item.getPos()));
		}else{
			shapes.add(new RemoteWBItem(client,item.getShape(), item.getColour(), item.getDrawType(), item.getStrokeValue()));
		}
			this.repaint();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/*public void paintComponent(Graphics g) {
	       try {
			super.paintComponent(g);
	        gg = g;
	        Graphics2D g2 = (Graphics2D)g;
	        System.out.println("Inside painter");
	        for(IRemoteWBItem item : this.remoteshapes){
	            
	                System.out.printf("Drawing shape by %s at %s%n", item.getOwner(), item.getCreationTime());
	                g2.setColor(item.getColour());
	                g2.fill(item.getShape());
	                g2.draw(item.getShape());
	            
	        }
	       }
	       catch(RemoteException rem) {
	    	   rem.printStackTrace();
	       }
	    }*/
	
	private void paintBackground(Graphics2D g2){
		g2.setPaint(Color.LIGHT_GRAY);
	}
	
	public void paintEraser(Shape r){
		Graphics2D g2 = (Graphics2D) gg;
		g2.setColor(Color.BLACK);
		g2.fill(r);
	}
	
	public void paint(Graphics g){
		gg = g;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		paintBackground(g2);
		
		//g2.setStroke(new BasicStroke(strokeValue));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
		try {
		for(IRemoteWBItem s : shapes){
			//g2.setPaint(colors[s.getMyColorIndex()]);
			g2.setPaint(s.getColour());
			if(s.getDrawType() == 4){
				g2.setStroke(new BasicStroke(s.getEraserSize()));
			}else{
				g2.setStroke(new BasicStroke(s.getStrokeValue()));
			}
			if(s.getDrawType() == 3){
				g2.drawString(s.getText(), s.getPos().x, s.getPos().y);
			}else{
				g2.draw(s.getShape());
				if(s.getDrawType() == 1 || s.getDrawType() == 4){
					//g2.setPaint(colors[s.getMyColorIndex()]);
					g2.setPaint(s.getColour());
					g2.fill(s.getShape());
				}
			}
		}}
		catch(RemoteException re) { re.printStackTrace();}
		
		if (startDrag != null && endDrag != null) {
	        g2.setPaint(Color.LIGHT_GRAY);
	        g2.setStroke(new BasicStroke(strokeValue));
	        Shape r = null;
	        if(shapeType.equals("Line")){
	        	r = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
	        	g2.draw(r);
	        }else if(shapeType.equals("Rectangle")){
	        	r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
	        	g2.draw(r);
	        }else if(shapeType.equals("Circle")){
	        	r = makeCircle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
	        	g2.draw(r);
	        }else if(shapeType.equals("Oval")){
	        	r = makeOval(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
	        	g2.draw(r);
	        }else if(shapeType.equals("Poly")){
	        	r = makeLine(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
	        	g2.draw(r);
	        }	
	      }
		if(shapeType.equals("Eraser")){
			paintEraser(eraserShape);
		}

	}
	
	private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2){
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}
	    
	private Line2D.Float makeLine(int x1, int y1, int x2, int y2){
	    return new Line2D.Float(x1, y1, x2, y2);
	}
	
	private Ellipse2D.Double makeCircle(double x1, double y1, double x2, double y2){
		double d2 = (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
		double r = Math.sqrt(d2);
		return new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), r, r);
	}
	
	private Ellipse2D.Double makeOval(double x1, double y1, double x2, double y2){
		double weight = Math.abs(x1 - x2);
		double height = Math.abs(y1 - y2);
		return new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), weight, height);
		
	}
	
	private GeneralPath makeGeneralPath(int x[], int y[], int count){
		GeneralPath path = new GeneralPath();
		path.moveTo(x[0], y[0]);
		for(int i = 0; i < count; i++){
			path.lineTo(x[i], y[i]);
		}
		path.closePath();
		return path;
	}
	
	private Rectangle2D.Float erase(int x1, int y1, int x2, int y2){
		return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
	}
	
	private Line2D.Float eraser(int x1, int y1, int x2, int y2){
		return new Line2D.Float(x1,y1,x2,y2);
	}
	
	private void showTextDialog(){
		TextDialog textDialog = new TextDialog();
		
	}
}
