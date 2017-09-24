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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PaintSurface extends JComponent{
	ArrayList<MyShape> shapes = new ArrayList<MyShape>();
	
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
	
	
	public PaintSurface(){
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
				MyShape myShape = new MyShape(r, drawType, color, strokeValue);
				if(shapeType.equals("Line")){
					r = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
					myShape.setShape(r);
					myShape.setColor(color);
					myShape.setStrokeValue(strokeValue);
					shapes.add(myShape);
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Rectangle")){
					r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
					myShape.setShape(r);
					myShape.setColor(color);
					myShape.setStrokeValue(strokeValue);
					shapes.add(myShape);
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Circle")){
					r = makeCircle(startDrag.x, startDrag.y, e.getX(), e.getY());
					myShape.setShape(r);
					myShape.setColor(color);
					myShape.setStrokeValue(strokeValue);
					shapes.add(myShape);
					xcount = 0;
					ycount = 0;
				}else if(shapeType.equals("Oval")){
					r = makeOval(startDrag.x, startDrag.y, e.getX(), e.getY());
					myShape.setShape(r);
					myShape.setColor(color);
					myShape.setStrokeValue(strokeValue);
					shapes.add(myShape);
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
					myShape.setShape(r);
					myShape.setColor(color);
					myShape.setStrokeValue(strokeValue);
					shapes.add(myShape);
				}else if(shapeType.equals("Text")){
					showTextDialog();
					MyShape myTextShape = new MyShape(r, 3, color, strokeValue);
					myTextShape.setPos(startDrag);
					myTextShape.setText(text);
					shapes.add(myTextShape);
					xcount = 0;
					ycount = 0;
				}
				startDrag = null;
				endDrag = null;
				repaint();
			}
			
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					Shape r = makeGeneralPath(x, y, xcount);
					MyShape myShape = new MyShape(r, drawType, color, strokeValue);
					myShape.setColor(color);
					myShape.setStrokeValue(strokeValue);
					shapes.add(myShape);
					xcount = 0;
					ycount = 0;
				}
				repaint();
			}
			
			
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e){
				endDrag = new Point(e.getX(), e.getY());
				if(shapeType.equals("Eraser")){
					eraserShape = new Rectangle2D.Float(e.getX() - eraserSize/2, e.getY() - eraserSize/2, eraserSize, eraserSize);
					MyShape myShape = new MyShape(eraserShape, 4, color, strokeValue);
					myShape.setColor(WhiteBoardClient.frame.getBackground());
					myShape.setStrokeValue(eraserSize);
					shapes.add(myShape);
				}
				if(shapeType.equals("Free")){
					Shape r = new Rectangle2D.Float(e.getX() - strokeValue/2, e.getY() - strokeValue/2, strokeValue, strokeValue);
					MyShape myShape = new MyShape(r, 1, color, strokeValue);
					shapes.add(myShape);
				}
				repaint();
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
		
		for(MyShape s : shapes){
			//g2.setPaint(colors[s.getMyColorIndex()]);
			g2.setPaint(s.getColor());
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
					g2.setPaint(s.getColor());
					g2.fill(s.getShape());
				}
			}
			
		}	
		
//		for(Shape s : shapes){
//			g2.setPaint(colors[0]);
//			g2.draw(s);
//			g2.setPaint(colors[colorIndex]);
//			g2.fill(s);
//		}
		
		if (startDrag != null && endDrag != null) {
	        g2.setPaint(Color.LIGHT_GRAY);
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
