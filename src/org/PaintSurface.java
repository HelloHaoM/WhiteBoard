package org;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
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
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

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
	private IRemoteClient client;
	private IRemoteServer remoteserver;
	private ImageIcon img;
	ArrayList<IRemoteWBItem> shapes ;
	
	Point startDrag, endDrag;
	int[] x = new int[50];
	int[] y = new int [50];
	int[] freex = new int[5000];
	int[] freey = new int[5000];
	int[] eraserx = new int[5000];
	int[] erasery = new int[5000];
	static int xcount = 0;
	static int ycount = 0;
	static int freexCount = 0;
	static int freeyCount = 0;
	static int eraserxCount = 0;
	static int eraseryCount = 0;
	static Color color = Color.BLACK;
	public static int strokeValue = 2;
	public static int eraserSize = 10;
	public static String shapeType = "";
	public static int drawType = 0;
	public static String text = "";
	
	

	public Graphics gg;
	public Shape eraserShape;
	

	public PaintSurface(IRemoteClient client, IRemoteServer remoteserver) throws RemoteException{
		this.client = client;
		this.remoteserver = remoteserver;
		
		this.shapes= new ArrayList<IRemoteWBItem>(remoteserver.getShapes());
	
		
		this.setOpaque(false);
		setBackground(new Color(0,0,0,65));
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e){
				if(SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isRightMouseButton(e)) {
					startDrag = new Point(e.getX(), e.getY());
					endDrag = startDrag;
					repaint();
				}
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				if(SwingUtilities.isLeftMouseButton(e) && !SwingUtilities.isRightMouseButton(e)) {
					freexCount = 0;
					freeyCount = 0;
					Shape r = null;
					IRemoteWBItem item;
					try{			
					
					if(shapeType.equals("Line")){
						r = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
						
						item = new RemoteWBItem(client, r, color, drawType, strokeValue);
						//add shape to the IRemoteWBItem list (Need implements more elements based on MyShape.java)
						shapes.add(item);
						
						// add new shape to the rmi server
						remoteserver.addShape(client, r, color, drawType, strokeValue);
						
						xcount = 0;
						ycount = 0;
					}else if(shapeType.equals("Rectangle")){
						r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
						
						item = new RemoteWBItem(client, r, color, drawType, strokeValue);
						//add shape to the IRemoteWBItem list
						shapes.add(item);
						
						// add new shape to the rmi server
						remoteserver.addShape(client, r, color, drawType, strokeValue);
						
						xcount = 0;
						ycount = 0;
					}else if(shapeType.equals("Circle")){
						r = makeCircle(startDrag.x, startDrag.y, e.getX(), e.getY());
						
						item = new RemoteWBItem(client, r, color, drawType, strokeValue);
						//add shape to the IRemoteWBItem list
						shapes.add(item);
						
						// add new shape to the rmi server
						remoteserver.addShape(client, r, color, drawType, strokeValue);
						xcount = 0;
						ycount = 0;
					}else if(shapeType.equals("Oval")){
						r = makeOval(startDrag.x, startDrag.y, e.getX(), e.getY());
						
						item = new RemoteWBItem(client, r, color, drawType, strokeValue);
						//add shape to the IRemoteWBItem list
						shapes.add(item);
						
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
						
						item = new RemoteWBItem(client, r, color, drawType, strokeValue);
						//add shape to the IRemoteWBItem list
						shapes.add(item);
						
						// add new shape to the rmi server
						remoteserver.addShape(client, r, color, drawType, strokeValue);
					}else if(shapeType.equals("Text")){
						
						TextDialog t1 = new TextDialog();
						IRemoteWBItem myTextShape = new RemoteWBItem(client, r, color, drawType, strokeValue);
						((RemoteWBItem)myTextShape).setPos(startDrag);
						((RemoteWBItem)myTextShape).setText(text);
						shapes.add(myTextShape);
						xcount = 0;
						ycount = 0;
						
						//item = new RemoteWBItem(client, r, color, drawType, strokeValue);
						//add shape to the IRemoteWBItem list
						//shapes.add(item);
						
						// add new shape to the rmi server
						remoteserver.addText(client, r, text, color, drawType, strokeValue, startDrag);
					}
					startDrag = null;
					endDrag = null;
					repaint();
					} catch(RemoteException e1) {
						e1.printStackTrace();
					}	
				}
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				IRemoteWBItem item;
				
				try {
				if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && shapeType.equals("Poly")){
					
					Shape r = makeGeneralPath(x, y, xcount);
					
					item = new RemoteWBItem(client, r, color, drawType, strokeValue);
										
					//add shape to the IRemoteWBItem list
					shapes.add(item);
					
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
					shapes.add(item);
					
					// add new shape to the rmi server
					//remoteserver.addShape(client,eraserShape, color, drawType, strokeValue);
					remoteserver.addShape(client,eraserShape, WhiteBoardClient.getFrame().getBackground(), 4, eraserSize);
					
				}
				if(shapeType.equals("Free")){
					//Shape r = new Rectangle2D.Float(e.getX() - strokeValue/2, e.getY() - strokeValue/2, strokeValue, strokeValue);
					freex[freexCount] = e.getX();
					freey[freeyCount] = e.getY();
					freexCount++;
					freeyCount++;
					Shape r = makeFreePath(freex, freey, freexCount);
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
		
		try {
			
			// example here need to unify the two class: MyShape and RemoteWBItem
		if(item.getDrawType() == 3 || item.getDrawType() == 5) {
			shapes.add(new RemoteWBItem(client,item.getShape(), item.getText(), item.getColour(), item.getDrawType(), item.getStrokeValue(), item.getPos()));
		}else if(item.getDrawType() == 6) {
			shapes.add(item);
		}
		else{
			shapes.add(new RemoteWBItem(client,item.getShape(), item.getColour(), item.getDrawType(), item.getStrokeValue()));
		}
			this.repaint();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
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
			}else if(s.getDrawType() == 5){
				g2.translate(this.getWidth() / 2,this.getHeight() / 2);
				g2.rotate(90 * Math.PI / 180);
				g2.drawString(s.getText(), (s.getPos().y - this.getHeight() / 2), -(s.getPos().x - this.getWidth() / 2));
				g2.rotate(270 * Math.PI / 180);
				g2.translate(-this.getWidth() / 2,-this.getHeight() / 2);
			} else if(s.getDrawType() == 6) {
				Image image = s.getImage().getImage();
				g2.drawImage(image, 0, 0, this);
			}
			else{
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
	
	private GeneralPath makeFreePath(int x[], int y[], int count){
		GeneralPath path = new GeneralPath();
		path.moveTo(x[0], y[0]);
		for(int i = 0; i < count; i++){
			path.lineTo(x[i], y[i]);
		}
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
	
	public ArrayList<IRemoteWBItem> getShpaeList(){
		return this.shapes;
	}
	
	public void cleanList() {
		this.shapes.clear();
	}
}
