package entrery.rushhour.ai;

import entrery.rushhour.Vehicle;

public class VehicleMove implements Move {

	private int newX;
	private int newY;
	private boolean isGoal;
	private Vehicle vehicle;
	
	public VehicleMove(int newX, int newY, boolean isGoal, Vehicle vehicle) {
		super();
		this.newX = newX;
		this.newY = newY;
		this.isGoal = isGoal;
		this.vehicle = vehicle;
	}
	
	public int getNewX() {
		return newX;
	}
	public void setNewX(int newX) {
		this.newX = newX;
	}
	public int getNewY() {
		return newY;
	}
	public void setNewY(int newY) {
		this.newY = newY;
	}
	public boolean isGoal() {
		return isGoal;
	}
	public void setGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
}
