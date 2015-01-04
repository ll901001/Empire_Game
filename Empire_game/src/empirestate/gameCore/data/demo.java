package empirestate.gameCore.data;

import empirestate.gameAI.demoAI;
import empirestate.gameCore.Army;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.GameEngine;
import java.util.Map;
import java.util.Vector; 

public class demo {
    public String gameMap() {
        return new String("maps/demo.map");
    }
    
    public int startingGold() {
        return 1000;
    }
    
    public int playerArmiesNumber() {
        return 4;
    }
    
    public Player getAI() {
        return new demoAI();
    }
    
    public CheckWinner winCondition() {
        return  new CheckWinner();
    }
    
    public Vector<Equip> equipment() {
        return GameData.getEquip();
    }
    
}