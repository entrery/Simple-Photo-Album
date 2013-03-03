package entrery.rushhour.heuristics;

import java.awt.Point;
import java.util.List;

import entrery.rushhour.RushHourBoard;
import entrery.rushhour.Vehicle;
import entrery.rushhour.ai.AStarHeuristic;
import entrery.rushhour.ai.State;
import entrery.rushhour.ai.VehicleType;

public class EuclideHeuristic implements AStarHeuristic {

	@Override
	public int distanceToGoal(State nextState) {
		RushHourBoard board = (RushHourBoard) nextState;
		Vehicle redCar = findRedCar(board.getVehicles());
		Point goal = getGoal(redCar);
		
		if(redCar.getVehicleType().equals(VehicleType.Horizontal)) {
			return goal.x - (redCar.getX() + redCar.getWidth() / RushHourBoard.CELL_SIZE);
		}
		else {
			return redCar.getY() - goal.y;
		}
	}


	private Point getGoal(Vehicle redCar) {
		if(redCar.getVehicleType().equals(VehicleType.Horizontal)) {
			return new Point(5, redCar.getY());
		}
		return new Point(redCar.getX(), 0);
	}


	public Vehicle findRedCar(List<Vehicle> vehicles) {
		Vehicle redCar = null;
        for (Vehicle vehicle : vehicles) {
			if(vehicle.isRed()) {
				redCar = vehicle;
				break;
			}
		}
        return redCar;
	}
	
}
