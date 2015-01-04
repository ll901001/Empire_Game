
package empirestate.gameCore;

import empirestate.gameCore.data.Action;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

// connect with controlled player
// Control player army in the game board

public class ArmyData
{
    private GameEngine engine;
    private ControlPlayer player;
    private final Map<BoardPoint, Army> armyPoint;
    private final Map<Integer, BoardPoint> idMap;
    private Set<BoardPoint> sight;
    private Integer selectedID;

    public ArmyData(ControlPlayer cp)
    {
        player = cp;
        armyPoint = new HashMap();
        idMap = new HashMap();
    }

    public void start(GameEngine ge) {
        engine = ge;
    }

    public synchronized void setArmy(Map<BoardPoint, Integer> map) {
        armyClear();
        for (Map.Entry entry : map.entrySet()) {
            addArmy((BoardPoint)entry.getKey(), (Integer)entry.getValue(), false);
        }
        if ((selectedID != null) && (!idMap.containsKey(selectedID)))
            selectedID = null;
    }

    public synchronized void setSight(Set<BoardPoint> set) {
        sight = set;
    }

    public synchronized Set<BoardPoint> getSight() {
        return sight;
    }

    private synchronized void armyClear() {
        armyPoint.clear();
        idMap.clear();
    }

    public synchronized void addArmy(BoardPoint bp, Integer Int, boolean bool) {
        Army army = engine.armyG(player, Int.intValue());
        if (army != null) {
          armyPoint.put(bp, army);
          this.idMap.put(Int, bp);
          if (bool)
            this.selectedID = Int;
        }
        else if (bool) {
            this.selectedID = null;
        }
    }

    public synchronized void refreshArmy(int ii) { 
        BoardPoint bp = idMap.get(Integer.valueOf(ii));
        if (bp != null) {
              Army army = engine.armyG(this.player, ii);
              if (army != null) {
                  armyPoint.put(bp, army);
                  idMap.put(Integer.valueOf(ii), bp);
              }
        } 
    }

    public synchronized Integer getSelectedID()
    {
        return selectedID;
    }

    public synchronized Army getSelectedArmy() {
        if (selectedID == null) {
            return null;
        }
        BoardPoint bp = (BoardPoint)idMap.get(selectedID);
        if (bp == null) {
            return null;
        }
        return (Army)armyPoint.get(bp);
    }

    public synchronized BoardPoint getSelectedLocation() {
        if (selectedID != null) {
            return (BoardPoint)idMap.get(selectedID);
        }
        return null;
    }

    public synchronized BoardPoint getMapPoint(Army army) {
        if (army == null) {
            return null;
        }
        return getMapPoint(army.thisArmyId);
    }

    public synchronized BoardPoint getMapPoint(int ii) {
        return idMap.get(Integer.valueOf(ii));
    }

    public synchronized Army getArmy(BoardPoint bp) {
        if (bp == null) {
            return null;
        }
        return armyPoint.get(bp);
    }

    private synchronized Army getArmy(int ii)
    {
        if (idMap.containsKey(Integer.valueOf(ii))) {
            return (Army)armyPoint.get(idMap.get(Integer.valueOf(ii)));
        }
        return null;
    }

    public synchronized void setSelected(int ii) {
        this.selectedID = Integer.valueOf(ii);
    }

    public synchronized void setSelected(Army army) {
        if (army == null)
            deselect();
        else
            setSelected(army.thisArmyId);
    }

    public synchronized void deselect() {
        selectedID = null;
    }

    public synchronized Map<BoardPoint, Action.ActionType> getSelectedActions()
    {
        if (selectedID == null) {
            return null;
        }
        Army army = getArmy(selectedID.intValue());
        if ((army != null) && (army.user == player.playerID())) {
            return engine.armyAction(player, selectedID.intValue());
        }
        return null;
    }

    public synchronized Map<BoardPoint, Army> getAllArmies()
    {
        HashMap hashmap = new HashMap();
        for (Map.Entry entr : this.armyPoint.entrySet())
            hashmap.put(entr.getKey(), entr.getValue());
        return hashmap;
    }
}