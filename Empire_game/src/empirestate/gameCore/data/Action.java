
package empirestate.gameCore.data;

import empirestate.gameCore.BoardPoint;

public class Action {
    public ActionType action;
    public BoardPoint base;
    public BoardPoint destna;

    public Action(Action ac){
        this(ac.action, ac.base, ac.destna);
    }
   // set starting point and destination
    public Action(ActionType ac, BoardPoint bp1, BoardPoint bp2)
    {
        action = ac;
        if (bp1 == null){
            base = null;
        }
        else{
            base = new BoardPoint(bp1);
        }
        if (bp2 == null){
            destna = null;
        }
        else{
            destna = new BoardPoint(bp2);
        }
    }
    public static enum ActionType{ ADD_SIGHT, MOVE, ATTACK, ARMY_DIED, GAME_START,}
}