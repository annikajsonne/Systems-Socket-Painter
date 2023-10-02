package edu.du.cs.annika.rula.painter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

abstract class PaintingPrimitive implements Serializable {
	private static final long serialVersionUID = 1L;
	Color color;

	public void color(Color color) {
		this.color = color;
	}

	public final void draw(Graphics g) {
		g.setColor(this.color);
		drawGeometry(g);
	}

	public abstract void setCoordinates(int x, int y);

	protected abstract void drawGeometry(Graphics g);
}

public class Painter extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	JPanel holder;
	JPanel leftPanel;
	JPanel topPanel;
	JPanel textBox;
	
	JButton red;
	JButton green;
	JButton blue;
	JButton sendMessage;
	JButton line;
	JButton circle;

	JTextField textInputPanel;
	JTextArea textChatPanel;
	
	String name;
	String message;
	
	PaintingPanel centerPanel;
	
	Color color = Color.RED;
	
	ArrayList<String> textHistory = new ArrayList<String>();
	
	Point mouseClickedPoint = new Point();
	Point mouseReleasedPoint = new Point();
	Point mouseUpdate = new Point();
	
	int shape = 0;
	
	ObjectOutputStream oos;
	ObjectInputStream ois;
	PaintingPrimitive drawing;

	public Painter() {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		holder = new JPanel();
		holder.setLayout(new BorderLayout());
		this.addMouseMotionListener(this);

		// Create the paint colors ::

		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3, 1)); // 3 by 1

		
		// add the red button
		red = new JButton();
		red.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.RED;
			}

		});
		
		red.setBackground(Color.RED);
		red.setOpaque(true);
		red.setBorderPainted(false);
		leftPanel.add(red); // Added in next open cell in the grid

		
		// add the green button
		green = new JButton();
		green.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.GREEN;
			}

		});
		green.addActionListener(this);
		green.setBackground(Color.GREEN);
		green.setOpaque(true);
		green.setBorderPainted(false);
		leftPanel.add(green);
		
		
		// add the blue button
		blue = new JButton();
		blue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				color = Color.BLUE;
			}

		});
		blue.addActionListener(this);
		blue.setBackground(Color.BLUE);
		blue.setOpaque(true);
		blue.setBorderPainted(false);
		leftPanel.add(blue);

		
		// add the panels to the overall panel, holder
		// holder layout set to BorderLayout
		holder.add(leftPanel, BorderLayout.WEST);

		//set top
		topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 2)); // 1 by 2

		
		// line button
		line = new JButton();
		line.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				shape = 0;
			}

		});
		line.setBackground(Color.GRAY);
		line.setText("Line");
		line.setBorderPainted(true);
		topPanel.add(line);

		
		// circle button
		circle = new JButton();
		circle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				shape = 1;
			}

		});
		circle.setBackground(Color.GRAY);
		circle.setText("Circle");
		circle.setBorderPainted(true);
		topPanel.add(circle);

		holder.add(topPanel, BorderLayout.NORTH);

		
		// add a new painting panel as center

		centerPanel = new PaintingPanel();
		holder.add(centerPanel, BorderLayout.CENTER);
		centerPanel.addMouseListener(this);
		centerPanel.addMouseMotionListener(this);

		//add the chat panel to the SOUTH

		textBox = new JPanel(new BorderLayout());
		textInputPanel = new JTextField(15);
		textChatPanel = new JTextArea(5, 15);

		textBox.add(textInputPanel, BorderLayout.CENTER);
		textBox.add(textChatPanel, BorderLayout.SOUTH);


		name = JOptionPane.showInputDialog("Enter your name");
		sendMessage = new JButton();
		sendMessage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String tempMessage = name + ": " + textInputPanel.getText() + "\n";
				textChatPanel.append(tempMessage);
				textInputPanel.setText("");
				try {
					oos.writeObject(tempMessage);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

		sendMessage.setBackground(Color.GRAY);
		sendMessage.setText("Send");
		sendMessage.setBorderPainted(true);
		textBox.add(sendMessage, BorderLayout.EAST);

		holder.add(textBox, BorderLayout.SOUTH);
		holder.setFont(getFont());

		try {
			Socket s = new Socket("localhost", 7004);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			PainterWorker pw = new PainterWorker(s, ois, centerPanel, textChatPanel);
			Thread th = new Thread(pw);
			th.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

//connect the holder to the JFrame
		this.setTitle(name);
		setContentPane(holder);

// visible to put all the components on the screen
		setVisible(true);

	}

	public static void main(String[] args) {
		Painter p = new Painter();
		p.setBounds(200, 200, 500, 500);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		drawing.setCoordinates(e.getX(), e.getY());
		centerPanel.repaint();

	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseClickedPoint = new Point();
		mouseClickedPoint.x = e.getX();
		mouseClickedPoint.y = e.getY();
		mouseUpdate = new Point(e.getX(), e.getY());
		if (shape == 0) {
			Line l = new Line(mouseClickedPoint, mouseUpdate, color);
			drawing = l;
			centerPanel.addPrimitive(l);
		} else {
			Circle c = new Circle(mouseClickedPoint, mouseUpdate, color);
			drawing = c;
			centerPanel.addPrimitive(c);
		}
		centerPanel.repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		try {
			oos.writeObject(drawing);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		centerPanel.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
