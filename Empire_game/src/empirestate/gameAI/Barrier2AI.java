package empirestate.gameAI;

import empirestate.gameCore.data.Action;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.data.Player;
import empirestate.gameCore.BoardArea;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Barrier2AI implements Player {
    GameEngine engine;
    Map<BoardPoint, Integer> army;
    //set defenders and objectives 
    public void start(GameEngine ge, BoardArea start, int i1, int i2)
    {
        engine = ge;

        int i3 = engine.armySet(this, new BoardPoint(17, 4), new String("Objective")); 
        int i4 = engine.armySet(this, new BoardPoint(17, 5), new String("Objective"));
        int k;
        // update objectives' equipment
        for (int j = 0; j < 7; j += 2)
        {
            for (k = 0; k < 7; k += 2)
            {
                engine.updateArmy(this, i3, 4, new BoardPoint(j, k));
                engine.updateArmy(this, i4, 4, new BoardPoint(j, k));
            }
        }
        //set defenders
        placeDefender(new BoardPoint(9, 0));
        placeDefender(new BoardPoint(9, 9));
        placeDefender(new BoardPoint(8, 4));
        placeDefender(new BoardPoint(8, 5));
        placeDefender(new BoardPoint(12, 3));
        placeDefender(new BoardPoint(12, 6));
        placeDefender(new BoardPoint(16, 3));
        placeDefender(new BoardPoint(16, 6));
        
        engine.gameReady(this);
    }
    // update defenders equipment
    private void placeDefender(BoardPoint bp)
    {
        int i = engine.armySet(this, bp, new String("Royal Guard"));
        for (int j = 0; j < 7; j += 2)
        {
            engine.updateArmy(this, i, 4, new BoardPoint(0, j));
        }
        engine.updateArmy(this, i, 2, new BoardPoint(2, 0));
        engine.updateArmy(this, i, 2, new BoardPoint(2, 5));
        engine.updateArmy(this, i, 0, new BoardPoint(6, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 1));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 3));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 5));
    }
    
    public void equipUp(List<Equip> list) {}
    public void end(int i) {}

    public void lost() {}
    //attack once each turn
    public void turnStart() 
    {
        
        Set armySet = army.keySet();
        BoardPoint bp1;
        Map map;
        for (Iterator it = armySet.iterator(); it.hasNext(); ) 
        { 
            bp1 = (BoardPoint)it.next();  
            map = engine.armyAction(this, ((Integer)army.get(bp1)).intValue());
            if (map != null)
            {
                for (Iterator it1 = map.keySet().iterator(); it1.hasNext();) 
                {
                    BoardPoint point = (BoardPoint) it1.next();
                    if (map.get(point) == Action.ActionType.ATTACK)
                    {
                        engine.armyAction(this, ((Integer)army.get(bp1)).intValue(), point);
                    }
                }
            }
        }
        engine.turnEnd(this);      
    }

    public void mapDataUp(BoardArea mapArea) {}
    public void mapUp(Map<BoardPoint, Integer> map, Set<BoardPoint> set, Action action) 
    {
        army = new HashMap();
        for (BoardPoint bp : map.keySet())
        {
            if (engine.armyG(this, ((Integer)map.get(bp)).intValue()).user == 1)
            {
                army.put(new BoardPoint(bp), new Integer(((Integer)map.get(bp)).intValue()));
            }
        }
    }
    public void updateTurn(int ii) {}
}