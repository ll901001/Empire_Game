package empirestate.gameCore;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Equip {
    public Map<BoardPoint, Block> tile;
    private int[] _bounds;
    public int cost;
    public String name;
    public String effects;
    
    public Equip(int i1, String str1, String str2){
        cost = i1;
        name = new String(str1);
        effects = new String(str2);
        tile = new HashMap();
    }
    
    public Equip(Equip equip){
        this(equip.tile, equip.cost, equip.name, equip.effects);
    }
    
    public Equip(Map<BoardPoint, Block> tileMap, int i1, String str1, String str2){
        tile = new HashMap();
        // construct the equipment by tiles
        for (Map.Entry entry : tileMap.entrySet()) {
            tile.put(new BoardPoint((BoardPoint)entry.getKey()), new Block((Block) entry.getValue()));
        }
        cost = i1;
        name = str1;
        effects = str2;
    }
}