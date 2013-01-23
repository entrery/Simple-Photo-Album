package entrery.rushhour.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import entrery.rushhour.RushHourBoard;

public class AStarSolutionFinder {
	
    private final AStarHeuristic heuristic;
    
    public AStarSolutionFinder(AStarHeuristic heuristic) {
        this.heuristic = heuristic;
    }
      
    public List<State> solve(State state) {

    	if (state.isGoal()) {
        	return Collections.emptyList();
        }

        List<AStarState> closed = new ArrayList<>();
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
        	System.out.println("Closed size " + closed.size());
        	
        	if(closed.size() == 31) {
        		System.out.println("stop it");
        		System.out.println(closed.contains(bestAStarState));
        		
        		AStarState firstState = closed.get(0);
        		
        		firstState.equals(bestAStarState);
        	}
        	
            if(closed.contains(bestAStarState)) {
            	System.out.println("Once");
            	continue;
            }
            
            State bestState = bestAStarState.getState();
            if (bestState.isGoal()) {
            	return reconstructSolution(bestAStarState);
            }
            
            ((RushHourBoard) bestState).printMatrix();
            
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