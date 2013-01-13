package entrery.rushhour.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarSolutionFinder {
	
    private final AStarHeuristic heuristic;
    
    public AStarSolutionFinder(AStarHeuristic heuristic) {
        this.heuristic = heuristic;
    }
      
    public List<State> solve(State state) {

    	if (state.isGoal()) {
        	return Collections.emptyList();
        }

        Set<AStarState> closed = new HashSet<>();
        PriorityQueue<AStarState> prioQueue = new PriorityQueue<AStarState>();

        AStarState startState = new AStarState(state);
        startState.setGScore(0);
        int stateHScore = heuristic.distanceToGoal(state);
        if (stateHScore == Integer.MAX_VALUE) {
            return null; // No possible solutions
        }
        startState.setHScore(stateHScore);
        prioQueue.add(startState);
        
        while (!prioQueue.isEmpty()) {
         
        	AStarState bestAStarState = prioQueue.poll();
            if(closed.contains(bestAStarState)) {
            	continue;
            }
            
            State bestState = bestAStarState.getState();
            if (bestState.isGoal()) {
            	return reconstructSolution(bestAStarState);
            }
            
            closed.add(bestAStarState);
            
			for (Move move : bestState.getMoves()) {
				State nextState = bestState.applyMove(move);
				if (closed.contains(new AStarState(nextState))) {
					continue;
				}

				if (nextState != null) {
					int nextStateGScore = bestAStarState.getGScore() + 1;
					int nextStateHScore = heuristic.distanceToGoal(nextState);

					AStarState nextAStarState = new AStarState(nextState);
					nextAStarState.setGScore(nextStateGScore);
					nextAStarState.setHScore(nextStateHScore);
					nextAStarState.setPrevState(bestAStarState, move);

					prioQueue.add(nextAStarState);
				}
            }
        }

        return Collections.emptyList();
    }

    private List<State> reconstructSolution(AStarState goalState) {
        List<State> solution = new ArrayList<State>();
        
        AStarState current = goalState;
        while (true) {
            solution.add(0, current.getState());
        	
            AStarState prev = current.getPrevState();
            if (prev == null) {
                break;
            }
      
            current = prev;
        }     
        return solution;
    }
}