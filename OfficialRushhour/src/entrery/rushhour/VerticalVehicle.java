package entrery.rushhour;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import entrery.rushhour.ai.VehicleType;

public class VerticalVehicle extends Vehicle {
	private int upperYBound;
	private int downYBound;
	
	public VerticalVehicle(int x, int y, int w, int h, Color color, VehicleType type, boolean isRed, int index) {
		super(x, y, w, h, color, type, isRed, index);
	}

	public void adjustDragPosition(List<Point> cells) {
		Point rectOfTopLeft = belongsTo(cells, getX(), getY());
		Point rectOfDownLeft = belongsTo(cells, getX(), getY() + getHeight());

		int topYdist = getY() - rectOfTopLeft.y;
		int downYdist = rectOfDownLeft.y + CELL_SIZE - getY() - getHeight();

		if (topYdist > downYdist) {
			this.setY(rectOfTopLeft.y + CELL_SIZE);
		} else {
			this.setY(rectOfTopLeft.y);
		}
	}
	
	@Override
	public void doDrag(List<Vehicle> vehicles, Point point, int panelWidth, int panelHeight) {
		setY(point.y - getDragFromY());
		setY(Math.max(upperYBound, getY()));
		setY(Math.min(downYBound - getHeight(), getY()));
		setY(Math.max(getY(), 0));
		setY(Math.min(getY(), (panelHeight - 1) - getHeight()));
	}
	
	private int findUpperYBound(List<Vehicle> vehicles) { 
		int minYBound = 0;
		for (Vehicle vehicle : vehicles) {
			int downLeftY = vehicle.getY() + vehicle.getHeight();
			if(vehicle.contains(new Point(getX(), downLeftY - 1)) && getY() > vehicle.getY() && this != vehicle) {
				if(minYBound < downLeftY) {
					minYBound = downLeftY;
				}
			}
		}
		return minYBound; 
	}
	
	private int findDownYBound(List<Vehicle> vehicles) {
		int maxYBound = RushHourBoard.CELL_SIZE * RushHourBoard.ROWS;
		for (Vehicle vehicle : vehicles) {
			int downLeftY = vehicle.getY();
			if(vehicle.contains(new Point(getX(), downLeftY + 1)) && getY() < vehicle.getY() && this != vehicle) {
				if(maxYBound > downLeftY) {
					maxYBound = downLeftY;
				}
			}
		}
		return maxYBound; 
	}

	@Override
	public void doPress(Point dragPoint, List<Vehicle> vehicles) {
		setDragFromX(dragPoint.x);
		setDragFromY(dragPoint.y);
		
		calculateBounds(vehicles);
	}

	public void calculateBounds(List<Vehicle> vehicles) {
		downYBound = findDownYBound(vehicles);
		upperYBound = findUpperYBound(vehicles);
	}
	
	public int getUpperYBound() {
		return upperYBound;
	}

	public int getDownYBound() {
		return downYBound;
	}
}
