
package empirestate.gameCore.data;

import java.util.Map;
import empirestate.gameCore.Block;
import empirestate.gameCore.Block.Property;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import java.util.Vector;

public class GameData
{
    // terrains
    public static final int PLAIN   = 0x00;
    public static final int WATER   = 0x01;
    public static final int DEFENSE = 0x02;
    public static final int ATTACK  = 0x04;
    public static final int GOLD    = 0x08;

    public static boolean hasTerrain(int i1, int i2)
    {
        if (i2 == 0 )
            return true;
        return (i1 & i2) != 0;
    }
    // contruct different tpyes of equipment
    public static Vector<Equip> getEquip()
    {
        Vector EquipV = new Vector();

        Block attackTile = new Block(50);
        attackTile.equip.put(Block.Property.ATTACK, Integer.valueOf(50));

        Block moveTile = new Block(50);
        moveTile.equip.put(Block.Property.MOVEMENT, Integer.valueOf(100));

        Block rangeTile = new Block(50);
        rangeTile.equip.put(Block.Property.RANGE, Integer.valueOf(30));

        Block sightTile = new Block(50);
        sightTile.equip.put(Block.Property.SIGHT, Integer.valueOf(200));

        Block defenseTile=  new Block(100);
        defenseTile.equip.put(Block.Property.DEFENSE, Integer.valueOf(5));

        Block waterTile = new Block(50);
        waterTile.equip.put(Block.Property.WATER_MOVEMENT, Integer.valueOf(1));

        Block godTile = new Block(50);
        godTile.equip.put(Block.Property.SIGHT, Integer.valueOf(20));
        godTile.equip.put(Block.Property.ATTACK, Integer.valueOf(10));
        godTile.equip.put(Block.Property.DEFENSE, Integer.valueOf(1));

        Equip weapon = new Equip(50, "Weapon", "Provides +120 damage (50 hp per block)");
        weapon.tile.put(new BoardPoint(0, 0), new Block(attackTile));
        weapon.tile.put(new BoardPoint(1, 1), new Block(attackTile));
        weapon.tile.put(new BoardPoint(0, 1), new Block(attackTile));
        weapon.tile.put(new BoardPoint(0, 2), new Block(attackTile));
        weapon.tile.put(new BoardPoint(0, 3), new Block(attackTile));
        EquipV.add(weapon);

        Equip transport = new Equip(40, "Transportation", "Provides +120 movement (50 hp per block)"); 
        transport.tile.put(new BoardPoint(0, 1), new Block(moveTile));
        transport.tile.put(new BoardPoint(1, 0), new Block(moveTile));
        transport.tile.put(new BoardPoint(1, 1), new Block(moveTile));
        EquipV.add(transport);

        Equip support = new Equip(30, "Support", "Provides +1 attack range(50 hp per block)");
        support.tile.put(new BoardPoint(0, 0), new Block(rangeTile));
        support.tile.put(new BoardPoint(1, 0), new Block(rangeTile));
        support.tile.put(new BoardPoint(1, 1), new Block(rangeTile));
        support.tile.put(new BoardPoint(1, 2), new Block(rangeTile));
        EquipV.add(support);

        Equip telescope = new Equip(20, "Telescope", "Provides +300 sight  (50 hp per block)");
        telescope.tile.put(new BoardPoint(1, 0), new Block(sightTile));
        telescope.tile.put(new BoardPoint(0, 1), new Block(sightTile));
        telescope.tile.put(new BoardPoint(1, 2), new Block(sightTile));
        telescope.tile.put(new BoardPoint(2, 1), new Block(sightTile));
        telescope.tile.put(new BoardPoint(1, 1), new Block(sightTile));
        EquipV.add(telescope);

        Equip shield = new Equip(30, "Shield", "Provides +20 defense (100 hp per block)");
        shield.tile.put(new BoardPoint(0, 0), new Block(defenseTile));
        shield.tile.put(new BoardPoint(0, 1), new Block(defenseTile));
        shield.tile.put(new BoardPoint(1, 0), new Block(defenseTile));
        shield.tile.put(new BoardPoint(1, 1), new Block(defenseTile));
        EquipV.add(shield);

        Equip boat = new Equip(100, "Boat", "Provides the water movement ability (50 hp per block)");
        boat.tile.put(new BoardPoint(0, 0), new Block(waterTile));
        boat.tile.put(new  BoardPoint(0, 1), new Block(waterTile));
        boat.tile.put(new BoardPoint(1, 1), new Block(waterTile));
        boat.tile.put(new BoardPoint(2, 1), new Block(waterTile));
        boat.tile.put(new BoardPoint(3, 1), new Block(waterTile));
        boat.tile.put(new BoardPoint(3, 0), new Block(waterTile));
        EquipV.add(boat);

        Equip Almighty = new Equip(50,"Almighty", "Provides +20 attack, 5 defense and 30 sight(50 hp per block)");
        Almighty.tile.put(new BoardPoint(0, 0), new Block(godTile));
        EquipV.add(Almighty);
        return EquipV;
    }
}