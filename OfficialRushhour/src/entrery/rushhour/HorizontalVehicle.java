package entrery.rushhour;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import entrery.rushhour.ai.VehicleType;

public class HorizontalVehicle extends Vehicle {
	private int leftXBound;
	private int rightXBound;
	
	public HorizontalVehicle(int x, int y, int w, int h, Color color, VehicleType type, boolean isRed, int index) {
		super(x, y, w, h, color, type, isRed, index);
	}

	@Override
	public void adjustDragPosition(List<Point> cells) {
		Point rectOfLeftTop = belongsTo(cells, getX(), getY());
		Point rectOfRightTop = belongsTo(cells, getX() + getWidth(), getY());

		int leftXdist = getX() - rectOfLeftTop.x;
		int rightXdist = rectOfRightTop.x + CELL_SIZE - getX() - getWidth();

		if (leftXdist > rightXdist) {
			this.setX(rectOfLeftTop.x + CELL_SIZE);
		} else {
			this.setX(rectOfLeftTop.x);
		}
	}

	public int getLeftXBound() {
		return leftXBound;
	}

	public int getRightXBound() {
		return rightXBound;
	}

	@Override
	public void doDrag(List<Vehicle> vehicles, Point point, int panelWidth, int panelHeight) {
		setX(point.x - getDragFromX());
		setX(Math.max(getX(), leftXBound));
		setX(Math.min(getX(), rightXBound - getWidth()));
		setX(Math.max(getX(), 0));
		setX(Math.min(getX(), (panelWidth - 1) - getWidth()));
	}

	@Override
	public void doPress(Point dragPoint, List<Vehicle> vehicles) {
		setDragFromX(dragPoint.x);
		setDragFromY(dragPoint.y);
		
		calculateBounds(vehicles);
	}

	private int findRightXBound(List<Vehicle> vehicles) {
		int maxXBound = RushHourBoard.CELL_SIZE * RushHourBoard.COLUMNS;
		for (Vehicle vehicle : vehicles) {
			int leftX = vehicle.getX();
			if(vehicle.contains(new Point(leftX + 1, getY())) && getX() < vehicle.getX() && this != vehicle) {
				if(maxXBound > leftX) {
					maxXBound = leftX;
				}
			}
		}
		return maxXBound; 
	}

	private int findLeftXBound(List<Vehicle> vehicles) {
		int minXBound = 0;
		for (Vehicle vehicle : vehicles) {
			int rightX = vehicle.getX() + vehicle.getWidth();
			if(vehicle.contains(new Point(rightX - 1, getY())) && getX() > vehicle.getX() && this != vehicle) {
				if(minXBound < rightX) {
					minXBound = rightX;
				}
			}
		}
		return minXBound; 
	}

	@Override
	public void calculateBounds(List<Vehicle> vehicles) {
		leftXBound = findLeftXBound(vehicles);
		rightXBound = findRightXBound(vehicles);	
	}
}
