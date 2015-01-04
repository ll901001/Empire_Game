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

public class Barrier1AI implements Player {
    GameEngine engine;
    Map<BoardPoint, Integer> army;
    
    //set attackers 
    public void start(GameEngine ge, BoardArea sa, int i1, int i2) {
        engine = ge;
        System.out.print("set AI");
        placeAttacker1(new BoardPoint(6,0));
        placeAttacker1(new BoardPoint(10,3));
        placeAttacker1(new BoardPoint(11,1));
        placeAttacker1(new BoardPoint(12,4));
        placeAttacker1(new BoardPoint(13,1));
        placeAttacker1(new BoardPoint(14,5));
        placeAttacker2(new BoardPoint(3,0));
        placeAttacker2(new BoardPoint(8,0));
        placeAttacker2(new BoardPoint(4,5));
        placeAttacker3(new BoardPoint(6,5));
        engine.gameReady(this);
    }
    // attackers with 1 attack range 
    private void placeAttacker1(BoardPoint bp){
        int i = engine.armySet(this, bp, new String("Attacker"));
        engine.updateArmy(this, i, 0, new BoardPoint(0, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(0, 4));
        engine.updateArmy(this, i, 0, new BoardPoint(2, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 4));
        engine.updateArmy(this, i, 2, new BoardPoint(3, 0));
    }
    // attackers with 2 attack range
    private void placeAttacker2 (BoardPoint bp){
        int i = engine.armySet(this, bp, new String("Attacker"));
        engine.updateArmy(this, i, 0, new BoardPoint(0, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(0, 4));
        engine.updateArmy(this, i, 0, new BoardPoint(2, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 4));
        engine.updateArmy(this, i, 2, new BoardPoint(3, 0));
        engine.updateArmy(this, i, 2, new BoardPoint(6, 0));
    }
    // attackers with 0 attack range
    private void placeAttacker3 (BoardPoint bp){
        int i = engine.armySet(this, bp, new String("Attacker"));
        engine.updateArmy(this, i, 0, new BoardPoint(0, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(0, 4));
        engine.updateArmy(this, i, 0, new BoardPoint(2, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(2, 4));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 0));
        engine.updateArmy(this, i, 0, new BoardPoint(5, 4));
    }
        
    public void equipUp(List<Equip> equipList) {}

    public void end(int i) {}
    
    public void lost() {}

    // attack once in each turn
    public void turnStart() {
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

