package org;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;

public class MyShape {
	private Shape s;
	private Color color;
	private String text;
	private Point pos;
	private int drawType; //1: fill, 2: line, 3: text, 4: eraser, 5: verText
	private int strokeValue;
	private int eraserSize;
	
	public MyShape(Shape s, int drawType, Color color, int strokeValue){
		this.s = s;
		this.drawType = drawType;
		this.color = color;
		this.text = "";
		this.strokeValue = strokeValue;
	}
	
	public Shape getShape(){
		return this.s;
	}
	
	public int getDrawType(){
		return this.drawType;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public String getText(){
		return this.text;
	}
	
	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public void setShape(Shape s){
		this.s = s;
	}
	
	public void setDrawType(int drawType){
		this.drawType = drawType;
	}
	
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setText(String text){
		this.text = text;
	}

	public int getStrokeValue() {
		return strokeValue;
	}

	public void setStrokeValue(int strokeValue) {
		this.strokeValue = strokeValue;
	}
	
	public int getEraserSize() {
		return eraserSize;
	}

	public void setEraserSize(int eraserSize) {
		this.eraserSize = eraserSize;
	}
	
}
