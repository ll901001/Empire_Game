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

public class demoAI implements Player {
    GameEngine engine;
    Map<BoardPoint, Integer> army;
    
    public void start(GameEngine ge, BoardArea sa, int i1, int i2) {
        engine = ge;
        int i3 = engine.armySet(this, new BoardPoint(3,0), new String("ai")); 
        int i4 = engine.armySet(this, new BoardPoint(3,1), new String("ai"));
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
        engine.armySet(this, new BoardPoint(3,2), new String("ai"));
        engine.armySet(this, new BoardPoint(3,3), new String("ai"));
        engine.gameReady(this);
    }
 
        
    public void equipUp(List<Equip> equipList) {}

    public void end(int i) {}
    
    public void lost() {}

    // attack once in each turn
    public void turnStart() {        
        engine.turnEnd(this);     
    }

    public void mapDataUp(BoardArea mapData) {}

    
    public void mapUp(Map<BoardPoint, Integer> map, Set<BoardPoint> set, Action action) {
        army = new HashMap();
        for (BoardPoint bp : map.keySet())
        {
            if (engine.armyG(this, ((Integer)map.get(bp)).intValue()).user == 1)
            {
                army.put(new BoardPoint(bp), new Integer(((Integer)map.get(bp)).intValue()));
            }
        }  
    }
    public void updateTurn(int i) {}
}

