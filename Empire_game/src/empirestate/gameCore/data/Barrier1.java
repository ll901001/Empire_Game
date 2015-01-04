package empirestate.gameCore.data;

import empirestate.gameAI.Barrier1AI;
import empirestate.gameAI.Barrier2AI;
import empirestate.gameCore.Army;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.GameEngine;
import java.util.Map;
import java.util.Vector;

public class Barrier1 {
    public String gameMap() {
        return new String("maps/B1.map");
    }
    
    public int startingGold() {
        return 200;
    }
    
    public int playerArmiesNumber() {
        return 1;
    }
    
    public Player getAI() {
        return new Barrier1AI();
    }
    
    public CheckWinner winCondition() {
        return new Barrier1WinCon();
    }
    
    public Vector<Equip> equipment() {
        return GameData.getEquip();
    }
    
    private class Barrier1WinCon extends CheckWinner{
        private Barrier1WinCon(){}
        // player win when gold > 200
        public boolean playerWin(Map<BoardPoint, Army> armyMap, GameEngine ge, int ii){
            return (ii == 0) && (ge.playerDataV.get(0).gold > 200);
        }
        public boolean playerLost(Map<BoardPoint, Army> map,GameEngine ge, int ii){
            if (map.size() == 0)
                 return true;
            return false;
        }
        public String objectives(){
            return new String("Make gold exceed 200!");
        }
    }
}