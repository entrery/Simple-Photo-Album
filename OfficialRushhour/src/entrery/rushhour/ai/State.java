package entrery.rushhour.ai;

import java.util.List;

public interface State {
    public List<Move> getMoves();

    public State applyMove(Move move);
    
    public boolean isGoal();
}
