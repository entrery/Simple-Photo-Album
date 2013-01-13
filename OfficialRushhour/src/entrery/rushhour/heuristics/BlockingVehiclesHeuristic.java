package entrery.rushhour.heuristics;

import java.awt.Point;
import java.util.List;
import entrery.rushhour.RushHourBoard;
import entrery.rushhour.Vehicle;
import entrery.rushhour.ai.AStarHeuristic;
import entrery.rushhour.ai.State;
import entrery.rushhour.ai.VehicleType;

public class BlockingVehiclesHeuristic implements AStarHeuristic {
	
    public int distanceToGoal(State state) {
    	
        RushHourBoard board = (RushHourBoard) state;
        if (board.isGoal()) {
            return 0;
        }

        List<Vehicle> vehicles = board.getVehicles();
        Vehicle redCar = findRedCar(vehicles);

        int numBlockingCars = 0;
        if(redCar.getVehicleType().equals(VehicleType.Vertical)) {
        	numBlockingCars = calculateRedVertical(vehicles, redCar);
        } else {
        	numBlockingCars = calculateRedHorizontal(vehicles, redCar);
        }

        return numBlockingCars;
    }

	private int calculateRedHorizontal(List<Vehicle> vehicles, Vehicle redCar) {
		int numBlocking = 0;
		for (Vehicle vehicle : vehicles) {
			int leftX = vehicle.getX();
			if(vehicle.contains(new Point(leftX + 1, redCar.getY())) && redCar.getX() < vehicle.getX() && redCar != vehicle) {
				numBlocking++;
			}
		}
		
		return numBlocking;
	}

	private int calculateRedVertical(List<Vehicle> vehicles, Vehicle redCar) {
		int numBlocking = 0;
		
		for (Vehicle vehicle : vehicles) {
			int downLeftY = vehicle.getY() + vehicle.getHeight();
			if(vehicle.contains(new Point(redCar.getX(), downLeftY - 1)) && redCar.getY() > vehicle.getY() && redCar != vehicle) {
				numBlocking++;
			}
		}
		
		return numBlocking;
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