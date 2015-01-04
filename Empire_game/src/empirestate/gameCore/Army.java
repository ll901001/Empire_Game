package empirestate.gameCore;
import empirestate.gameCore.Block.Property;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Army {
    public String name;
    public int thisArmyId;
    public int user;
    public HashMap<BoardPoint, Block> tile;
    public HashMap<Block.Property, Integer> stats;

    public Army(String str, int i1, int armyId)
    {
        thisArmyId = armyId;
        name = new String(str);
        tile = new HashMap();
        stats = new HashMap();
        user = i1;
        // initialize property
        for (Block.Property attri : Block.Property.values()){
            stats.put(attri, 0);
        }
    }

    public Army(Army army)
    {   
        stats = new HashMap();
        tile = new HashMap();
        name = new String(army.name);
        user = army.user;
        thisArmyId = army.thisArmyId;
        // update property according equipment
        for (Iterator it = army.stats.keySet().iterator(); it.hasNext(); ) { 
            Property property = (Block.Property)it.next();
            stats.put(property,army.stats.get(property));
        }
        // update equipment 
        for (Iterator it2 = army.tile.keySet().iterator(); it2.hasNext(); ) 
        { 
            BoardPoint bp = (BoardPoint)it2.next();
            tile.put(new BoardPoint(bp), new Block((Block)army.tile.get(bp)));
        }
    }

    public boolean equals(Object ob){
          return ((ob instanceof Army)) && (thisArmyId == ((Army)ob).thisArmyId);
    }
}