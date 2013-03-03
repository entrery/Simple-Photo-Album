package entrery.rushhour.heuristics;

import entrery.rushhour.ai.AStarHeuristic;
import entrery.rushhour.ai.State;

public class NullHeuristics implements AStarHeuristic {

	@Override
	public int distanceToGoal(State nextState) {
		return 0;
	}

}
