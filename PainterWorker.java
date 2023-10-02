package edu.du.cs.annika.rula.painter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JTextArea;

public class PainterWorker implements Runnable {
	Socket s;
	ObjectInputStream ois; 
	PaintingPanel centerPanel;
	JTextArea text;


	public PainterWorker(Socket s, ObjectInputStream ois, PaintingPanel c, JTextArea t) {
		this.s = s;
		this.ois = ois;
		this.centerPanel = c;
		this.text = t;
	}
	

	@Override
	public void run() {
		while (true) {
			try {
				Object o = ois.readObject();
				if(o instanceof PaintingPrimitive) {
					this.centerPanel.addPrimitive((PaintingPrimitive)o);
					centerPanel.repaint();
				} else {
					this.text.append((String)o);
					
				}
			} catch (ClassNotFoundException | IOException e) {
				
				e.printStackTrace();
			}
		}

	}

}
