package edu.du.cs.annika.rula.painter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Worker implements Runnable {
	ObjectOutputStream oos;
	Socket s;
	Hub hub;

	public Worker(Socket s, ObjectOutputStream oos, Hub h) {
		this.s = s;
		this.oos = oos;
		this.hub = h;

	}

	@Override
	public void run() {
		int temp = 0;
		
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			while (temp == 0) {
				try {
					Object o = ois.readObject();
					hub.updateAll(o, oos);
				} catch (Exception e) {
					temp = 1;
					hub.removePainterClient(s);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
