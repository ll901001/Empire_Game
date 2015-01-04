package empirestate.gameCore;
import java.util.HashMap;
import java.util.Map;

public class Block {
    public Map<Property, Integer> equip;
    public int life;
    public int totalLife;
    public Block(int ii){
        life = ii;
        totalLife = ii;
        equip = new HashMap();
    }

    public Block(Block block){
        equip = new HashMap();
        life = block.life;
        totalLife = block.totalLife;
        // update property for each equipment 
        for (Property attri : block.equip.keySet())
            equip.put(attri, new Integer(((Integer)block.equip.get(attri)).intValue()));
    }
    
    // army's property
    public static enum Property
    {
        ATTACK("Attack"), ATTACK_WIDTH("Attack Width"), RANGE("Range"), DEFENSE("Defense"), MOVEMENT("Movement"), SIGHT("Sight"), WATER_MOVEMENT("Water Movement"), CORE("Core");
      
        private final String name;
        private Property(String str ){
              name = str;
        }
        public String toString() {
            return name;
        }
    } 
}