package entrery.rushhour.ai;

public class AStarState implements Comparable<AStarState> {
    private State state;
    private int gScore;
    private int hScore;
    private AStarState prevAStarState;
    private Move prevStateMove;
	
    public AStarState(State state) { 
        this.state = state;
    }
    
    public State getState() {
        return state;
    }
    
    public int getGScore() {
        return gScore;
    }
    
    public void setGScore(int gScore) {
        this.gScore = gScore;
    }
    
    public int getHScore() {
        return hScore;
    }
    
    public void setHScore(int hScore) {
        this.hScore = hScore;
    }
    
    public AStarState getPrevState() {
        return prevAStarState;
    }
    
    public Move getPrevMove() {
        return prevStateMove;
    }
    
    public void setPrevState(AStarState prevAStarState, Move prevStateMove) {
        this.prevAStarState = prevAStarState;
        this.prevStateMove = prevStateMove;
    }
    
    @Override
    public int compareTo(AStarState other) {
        return (gScore + hScore - other.gScore - other.hScore);
    }
    
    @Override
    public boolean equals(Object obj) {
    	AStarState otherAStarState = (AStarState) obj;
    	State otherState = otherAStarState.getState();
    	
    	return state.equals(otherState);
    }
}
