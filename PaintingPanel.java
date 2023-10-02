package edu.du.cs.annika.rula.painter;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class PaintingPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	ArrayList<PaintingPrimitive> paintingPrimitives = new ArrayList<PaintingPrimitive>();

	public PaintingPanel() {
		super.setBackground(Color.WHITE);
	}

	public void addPrimitive(PaintingPrimitive obj) {
		this.paintingPrimitives.add(obj);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (PaintingPrimitive obj : paintingPrimitives) {
	
			obj.draw(g);
		}
	}
}
