package entrery.rushhour;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import entrery.rushhour.ai.Move;
import entrery.rushhour.ai.State;
import entrery.rushhour.ai.VehicleMove;
import entrery.rushhour.ai.VehicleType;
import entrery.rushhour.main.RushHourMain;

public class RushHourBoard extends JPanel implements MouseListener, MouseMotionListener, State {

	private static final long serialVersionUID = 1L;
	public static final int CELL_SIZE = 80;

	public static final int ROWS = 6;
	public static final int COLUMNS = 6;
	
	private List<Vehicle> vehicles;
	private Vehicle selectedVehicle;
	private boolean canDrag = false;
	private List<Point> cells = new ArrayList<Point>();
	private boolean isGoal = false;

	public RushHourBoard(List<Vehicle> vehicles, boolean isGoal) {
		this.vehicles = vehicles;
		this.isGoal = isGoal;
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
		RushHourMain.hideButtons();
		
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

	@Override
	public List<Move> getMoves() {
		List<Move> availableMovesForAllVehicles = new ArrayList<Move>();
		
		for(Vehicle vehicle : vehicles) {
			addMovesForVehicle(vehicle, availableMovesForAllVehicles);
		}
		
		return availableMovesForAllVehicles;
	}

	private void addMovesForVehicle(Vehicle vehicle, List<Move> moves) {
		vehicle.calculateBounds(vehicles);
		switch (vehicle.getVehicleType()) {
		case Horizontal:
			handleLeftMoves(vehicle, moves);
			handleRightMoves(vehicle, moves);
			break;
		case Vertical:
			handleUpMoves(vehicle, moves);
			handleDownMoves(vehicle, moves);
			break;
		default:
			break;
		}
	}

	private void handleUpMoves(Vehicle vehicle, List<Move> moves) {
		VerticalVehicle verticalVehicle = (VerticalVehicle) vehicle;
		
		int movesUp = (verticalVehicle.getY() - verticalVehicle.getUpperYBound()) / CELL_SIZE;

		for (int i = 0; i < movesUp; i++) {
			int upOffset = (i + 1) * CELL_SIZE;
			VehicleMove move = new VehicleMove(vehicle.getX(), vehicle.getY() - upOffset, false, verticalVehicle);		
			moves.add(move);
		}
	}

	private void handleDownMoves(Vehicle vehicle, List<Move> moves) {
		VerticalVehicle verticalVehicle = (VerticalVehicle) vehicle;
		
		int movesDown = (verticalVehicle.getDownYBound() - (verticalVehicle.getY() + vehicle.getHeight() )) / CELL_SIZE;

		for (int i = 0; i < movesDown; i++) {
			int upOffset = (i + 1) * CELL_SIZE;
			VehicleMove move = new VehicleMove(vehicle.getX(), vehicle.getY() + upOffset, false, verticalVehicle);		
			moves.add(move);
		}	
	}

	private void handleRightMoves(Vehicle vehicle, List<Move> moves) {
		HorizontalVehicle horizontalVehicle = (HorizontalVehicle) vehicle;
		
		int movesDown = (horizontalVehicle.getRightXBound() - (horizontalVehicle.getX() + vehicle.getWidth() )) / CELL_SIZE;

		for (int i = 0; i < movesDown; i++) {
			int upOffset = (i + 1) * CELL_SIZE;
			VehicleMove move = new VehicleMove(vehicle.getX() + upOffset, vehicle.getY(), false, horizontalVehicle);		
			moves.add(move);
		}		
	}

	private void handleLeftMoves(Vehicle vehicle, List<Move> moves) {
		HorizontalVehicle horizontalVehicle = (HorizontalVehicle) vehicle;
		
		int movesLeft = (horizontalVehicle.getX() - horizontalVehicle.getLeftXBound()) / CELL_SIZE;

		for (int i = 0; i < movesLeft; i++) {
			int leftOffset = (i + 1) * CELL_SIZE;
			VehicleMove move = new VehicleMove(vehicle.getX() - leftOffset, vehicle.getY(), false, horizontalVehicle);		
			moves.add(move);
		}
	}

	@Override
	public State applyMove(Move vehicleMove) {
		VehicleMove move = (VehicleMove) vehicleMove;
		Vehicle moved = move.getVehicle();
		//vehicles.remove(moved);
				
		Vehicle newVehicle = null;
		if(moved.getVehicleType() == VehicleType.Vertical) {
			newVehicle = new VerticalVehicle(move.getNewX(), move.getNewY(), moved.getWidth(), moved.getHeight(), moved.getColor(), moved.getVehicleType(), moved.isRed(), moved.getIndex());
		} else {
			newVehicle = new HorizontalVehicle(move.getNewX(), move.getNewY(), moved.getWidth(), moved.getHeight(), moved.getColor(), moved.getVehicleType(), moved.isRed(), moved.getIndex());
		}
	
		//vehicles.add(newVehicle);
	
		List<Vehicle> newVehicleList = new ArrayList<>(vehicles);
		newVehicleList.remove(moved);
		newVehicleList.add(newVehicle);
		
		Collections.sort(newVehicleList, new Comparator<Vehicle>() {

			@Override
			public int compare(Vehicle o1, Vehicle o2) {
				return o1.getIndex() < o2.getIndex() ? -1 : o1.getIndex() > o2.getIndex() ? 1 : 0;
			}
		});
		
		return new RushHourBoard(newVehicleList, isGoalState(newVehicle));
	}

	private boolean isGoalState(Vehicle newVehicle) {
		if(newVehicle.isRed()) {
			if(newVehicle.getVehicleType().equals(VehicleType.Vertical)) {
				return newVehicle.getY() == 0;
			} else {
				return newVehicle.getX() == COLUMNS * CELL_SIZE - 2 * CELL_SIZE;
			}
		}
		return false;
	}

	@Override
	public boolean isGoal() {
		return isGoal;
	} 
	
	public List<Vehicle> getVehicles() {
		return vehicles;
	}
	
	
	public void printMatrix() {
		char[][] matrix = new char[6][6];
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = 'O';
			}
		}
		
		for (Vehicle vehicle : vehicles) {
				for (int i = 0; i < vehicle.getWidth() / CELL_SIZE ; i++) {
					matrix[(vehicle.getX() / CELL_SIZE) + i][vehicle.getY() / CELL_SIZE] = getSymbol(vehicle.isRed(), vehicle.getVehicleType());
				}
				for (int i = 0; i < vehicle.getHeight() / CELL_SIZE ; i++) {
					matrix[(vehicle.getX() / CELL_SIZE)][(vehicle.getY() / CELL_SIZE) + i] = getSymbol(vehicle.isRed(), vehicle.getVehicleType());
				}
		}
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[j][i]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static char getSymbol(boolean isRed, VehicleType type) {
		if(isRed) {
			return 'R';
		} else if(type.equals(VehicleType.Vertical)) {
			return 'V';
		} else {
			return 'H';
		}
	}
	
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof RushHourBoard)) {
            return false;
        }
        RushHourBoard otherBoard = (RushHourBoard)other;
        
        return Arrays.equals(vehicles.toArray(new Vehicle[0]), otherBoard.vehicles.toArray(new Vehicle[0]));
//        for (int i = 0; i < vehicles.size(); i++) {
//			if(!vehicles.get(i).equals(otherBoard.vehicles.get(i))) {
//				return false;
//			}
//		}
//        
//        return true;
    }
}