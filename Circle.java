package edu.du.cs.annika.rula.painter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

	public class Circle extends PaintingPrimitive {
		Point c;
		Point radius;

		public Circle(Point cent, Point r, Color color) {
			super.color = color;
			this.c = cent;
			this.radius = r;

		}

		protected void drawGeometry(Graphics g) {
			int r = (int) Math.abs(c.distance(radius));
			g.drawOval(c.x - r, c.y - r, r * 2, r * 2);

		}
		public void setCoordinates(int x, int y) {
			radius.x = x;
			radius.y = y;
		}

	}


