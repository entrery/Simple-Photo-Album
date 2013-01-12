
package entrery.rushhour;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public abstract class Vehicle {

	protected static final int CELL_SIZE = 80;

	private int xPos;
	private int yPos;
	private int width;
	private int height;
	private int dragFromX;
	private int dragFromY;
	Color color;

	Vehicle(int x, int y, int w, int h, Color color) {
		this.xPos = x;
		this.yPos = y;
		this.width = w;
		this.height = h;
		this.color = color;
	}

	public void setX(int xPos) {
		this.xPos = xPos;
	}

	public int getX() {
		return xPos;
	}

	public void setY(int yPos) {
		this.yPos = yPos;
	}

	public int getY() {
		return yPos;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDragFromX() {
		return dragFromX;
	}

	public void setDragFromX(int dragFromX) {
		this.dragFromX = dragFromX;
	}

	public int getDragFromY() {
		return dragFromY;
	}

	public void setDragFromY(int dragFromY) {
		this.dragFromY = dragFromY;
	}

	public boolean contains(Point p) {
		return new Rectangle(xPos, yPos, width, height).contains(p);
	}

	public void paintCar(Graphics g) {
		g.setColor(color);
		g.fillRect(xPos + 5, yPos + 5, width - 10, height - 10);
		g.setColor(Color.BLACK);
		g.drawRect(xPos + 5, yPos + 5, width - 10, height - 10);
	}

	public abstract void doDrag(List<Vehicle> vehicles, Point point, int panelWidth, int panelHeight);
	public abstract void adjustDragPosition(List<Point> cells);
	public abstract void doPress(Point dragPoint, List<Vehicle> vehicles);

	protected Point belongsTo(List<Point> cells, int centerX, int centerY) {
		Point rectangle = null;
		for (Point point : cells) {
			if (containsPoint(centerX, centerY, point)) {
				rectangle = point;
				break;
			}
		}
		return rectangle;
	}

	private boolean containsPoint(int centerX, int centerY, Point point) {
		if ((centerX >= point.x && centerX <= point.x + CELL_SIZE)
				&& (centerY >= point.y && centerY <= point.y + CELL_SIZE)) {
			return true;
		}
		return false;
	}
}