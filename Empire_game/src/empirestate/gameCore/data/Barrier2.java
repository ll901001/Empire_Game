
package empirestate.gameCore.data;

import empirestate.gameAI.Barrier2AI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import empirestate.gameCore.data.Action.ActionType;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.Army;
import java.util.Vector;

public class Barrier2 {
    public String getMap()
    {
         return new String("maps/B2.map");
    }

    public int startingGold(){
         return 2000;
    }

    public int playerArmiesNumber(){
         return 8;
    }

    public Player gameAI(){
         return new Barrier2AI();
    }

    public CheckWinner winCondition(){
         return new Barrier2WinCon();
    }

    public Vector<Equip> equipment(){
        return GameData.getEquip();
    }
    private class Barrier2WinCon extends CheckWinner
    {
        private Barrier2WinCon(){}

        public boolean playerWin(Map<BoardPoint, Army> map, GameEngine ge, int ii){
            return false;
        }

        public boolean playerLost(Map<BoardPoint, Army> map, GameEngine ge, int ii){
            // player win when targets in (17,4) and (17,5) dead
            if (ii == 1)
            {
                if ((!map.containsKey(new BoardPoint(17, 4))) && (!map.containsKey(new BoardPoint(17, 5))))
                    return true;
            }
            if (map.size() == 0)
                 return true;
            return false;
        }

        public String objectives(){
            return new String("Kill the enemy king and queen!");
        }
    }
}