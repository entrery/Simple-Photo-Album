package entrery.rushhour;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class RushHourBoard extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final int CELL_SIZE = 80;

	private static final int ROWS = 6;
	private static final int COLUMNS = 6;

	private Vehicle one = new HorizontalVehicle(0, 0, 240, 80, Color.GREEN);
	private Vehicle two = new HorizontalVehicle(160, 160, 240, 80, Color.GREEN);
	private Vehicle three = new HorizontalVehicle(80, 400, 240, 80, Color.GREEN);
	private Vehicle four = new HorizontalVehicle(320, 400, 160, 80, Color.YELLOW);
	private Vehicle five = new VerticalVehicle(160, 240, 80, 160, Color.RED);
	private Vehicle six = new VerticalVehicle(400, 80, 80, 240, Color.BLUE);
	private Vehicle seven = new VerticalVehicle(320, 240, 80, 160, Color.BLUE);
	private Vehicle eight = new VerticalVehicle(0, 320, 80, 160, Color.BLUE);

	List<Vehicle> vehicles = Arrays.asList(one, two, three, four, five, six, seven, eight);
	private Vehicle selectedVehicle;
	
	private boolean canDrag = false;
	private List<Point> cells = new ArrayList<>();

	public RushHourBoard() {
		setPreferredSize(new Dimension(481, 481));
		setBackground(Color.GRAY);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);		
		drawGrid(g);
		drawVehicles(g);
	}

	private void drawVehicles(Graphics g) {
		for (Vehicle vehicle : vehicles) {
			vehicle.paintCar(g);
		}	
	}

	private void drawGrid(Graphics g) {
		cells.clear();
		g.setColor(Color.MAGENTA);
		int offsetX = 0;
		int offsetY = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				cells.add(new Point(offsetX, offsetY));
				g.drawRect(offsetX, offsetY, CELL_SIZE, CELL_SIZE);
				offsetX += CELL_SIZE;
			}
			offsetX = 0;
			offsetY += CELL_SIZE;
		}
	}

	public void mousePressed(MouseEvent e) {
		int x = e.getX(); 
		int y = e.getY();

		selectedVehicle = getSelectedVehicle(new Point(x, y));

		if (selectedVehicle == null) {
			canDrag = false;
			return;
		}
		
		canDrag = true;		
		int dragFromX = x - selectedVehicle.getX();
		int dragFromY = y - selectedVehicle.getY();
		selectedVehicle.doPress(new Point(dragFromX, dragFromY), vehicles);
	}

	private Vehicle getSelectedVehicle(Point p) {
		Vehicle selected = null;
		for (Vehicle vehicle : vehicles) {
			if(vehicle.contains(p)) {
				selected = vehicle;
				break;
			}
		}
		return selected;
	}

	public void mouseDragged(MouseEvent e) {
		if (canDrag) {
			selectedVehicle.doDrag(vehicles, e.getPoint(), getWidth(), getHeight());
			this.repaint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (selectedVehicle != null) {
			selectedVehicle.adjustDragPosition(cells);
			this.repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
		canDrag = false;
	}

	public void mouseMoved(MouseEvent e) {} 
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {} 
}