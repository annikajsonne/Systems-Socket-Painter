package edu.du.cs.annika.rula.painter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Hub {

	// TODO Auto-generated method stub
	ArrayList<PaintingPrimitive> paintingPrimitives = new ArrayList<PaintingPrimitive>();
	ArrayList<String> textHistory = new ArrayList<String>();
	ArrayList<Socket> clients = new ArrayList<Socket>();
	ArrayList<ObjectOutputStream> oos = new ArrayList<ObjectOutputStream>();

	public Hub() {

		try {
			ServerSocket ss = new ServerSocket(7004);
			while (true) {
				Socket s = ss.accept(); // blocking
				clients.add(s);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				this.oos.add(oos);
				if (!(paintingPrimitives.size() == 0)) {
					for (int i = 0; i < paintingPrimitives.size(); i++) {
						oos.writeObject(paintingPrimitives.get(i));
					}
				}
				if (textHistory.size() != 0) {
					for (int i = 0; i < textHistory.size(); i++) {
						oos.writeObject(textHistory.get(i));
					}
				}

				Worker hubWorker = new Worker(s, oos, this);
				Thread th = new Thread(hubWorker);
				th.start();

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	
	public synchronized void removePainterClient(Socket socket) {
		int index = clients.indexOf(socket);
		oos.remove(index);
		clients.remove(index);

	}
	
	public synchronized void updateAll(Object o, ObjectOutputStream input_oos) throws IOException {

		if (o instanceof PaintingPrimitive) {
			paintingPrimitives.add((PaintingPrimitive) o); 
			//when a new window is created, synchronize old window data
		} else {
			textHistory.add((String) o);

		}
		for (int i = 0; i < clients.size(); i++) {

			try {
				if (!(input_oos == oos.get(i))) {
					oos.get(i).writeObject(o);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	public static void main(String[] args) {
		Hub hub = new Hub();
	}

}
