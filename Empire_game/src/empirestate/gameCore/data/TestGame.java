package empirestate.gameCore.data;
import empirestate.gameAI.GameAI;
import empirestate.gameCore.Equip;
import java.util.Vector;
public class TestGame 
 {
    public String getMap() {
        return new String("maps/test.map");
    }
    
    public int getStartingGold() {
        return 1000;
    }

    public int getMaxArmies() {
        return 5;
    }

    public Player getAI() {
        return new GameAI();
    }

    public CheckWinner getWinCondition() {
        return new CheckWinner();
    }

    public Vector<Equip> getEquipment() {
        return GameData.getEquip();
    }
}
