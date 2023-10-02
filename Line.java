package edu.du.cs.annika.rula.painter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Line extends PaintingPrimitive {
	Point start;
	Point end;

	public Line(Point s, Point e, Color color) {
		super.color = color;
		this.start = s;
		this.end = e;
	}

	protected void drawGeometry(Graphics g) {
		g.drawLine(start.x, start.y, end.x, end.y);
	}
	
	public void setCoordinates(int x, int y) {
		end.x = x;
		end.y = y;
		
	}

}
