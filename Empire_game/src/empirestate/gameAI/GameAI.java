 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empirestate.gameAI;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import empirestate.gameCore.data.Action;
import empirestate.gameCore.data.Action.ActionType;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.BoardArea;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.data.Player;
import empirestate.gameCore.Equip;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;

public class GameAI implements Player
{
    BoardPoint best;
    public GameEngine engine;
    public int[][] mapPOINT;
    public int id;
    public int totalPlayers;
    public boolean endBol;
    public LinkedList linkList;

    public void start(GameEngine coreThis, BoardArea mapDataThis, int playerID, int PlayerNum)
    {
        engine = coreThis;
        mapPOINT = coreThis.boardMap;
        totalPlayers = PlayerNum;
        endBol = false;
        linkList = new LinkedList();
        best = bestBase(mapPOINT);
        // random set the AI armies in setting area 
        while (engine.restArmies(this) > 0) 
        {
            int i = (int)(Math.random()*10);
            BoardPoint mapPoint = null;
            for (BoardPoint startPoint : mapDataThis.mapSet) {
                if (i == 0) {
                      mapPoint = startPoint;
                      break;
                }
                i--;
            }
            setArmy(mapPoint);
            if (mapPoint == null) {
                  throw new IllegalStateException("Unable to find a placement point");
            }
        } 
        engine.gameReady(this);
    }
    // update map according to the action
    public void mapUp(Map<BoardPoint, Integer> armyMap, Set<BoardPoint> set, Action ac) {
        linkList.removeAll(linkList);
        for (BoardPoint bp : armyMap.keySet())
        {
            if (engine.armyG(this, ((Integer)armyMap.get(bp)).intValue()).user == 1)
            {
                linkList.add(new Integer(((Integer)armyMap.get(bp)).intValue()));
            }
        }
        if ((ac == null) || (ac.destna == null) || (ac.base == null))
            return;
        if (ac.action == Action.ActionType.ARMY_DIED) 
        {
            int i = -1;
            Collection co = armyMap.values();
            for (Iterator it = linkList.iterator(); it.hasNext();)
            {
                i = ((Integer)it.next()).intValue();
                if ((i != -1) && (!co.contains(Integer.valueOf(i))))
                    it.remove();
            }
        }
    }
    // AI turn action 
    public void turnStart()
    {
        LinkedList linkedL = new LinkedList(linkList); 
        BoardPoint bp1;
        BoardPoint bp2;
        HashMap map1;
        HashMap map2;
        BoardPoint movePoint = null;
        BoardPoint mp = null;
        BoardPoint userPosition = null;
        BoardPoint within = null;
        int iValue;
        int count = 0;
        // AI's sight area
        Set sightArea = engine.sight(1, engine.armyV, engine.armyInfV, mapPOINT);
        for (Iterator it1 = linkedL.iterator(); it1.hasNext(); ) 
        {           
            try{
            Thread.sleep(300);
            } catch (Exception e) {}
            iValue = ((Integer)it1.next()).intValue();
            // Scan sight area
            for (int i = 5; i< engine.armyInfV.size();i++){
                userPosition = engine.armyInfV.get(i).position;
                for (Iterator it = sightArea.iterator(); it.hasNext();) {
                    mp = (BoardPoint) it.next();
                    if(userPosition.toString().equals(mp.toString()))                    
                    {
                        setAdvance(iValue);
                        within = mp;
                        break;
                    }
                }
            }
            mp = engine.armyInfV.get(iValue).position;
            map1 = engine.armyAction(this, iValue);
            // Move to targets
            if (map1 != null)
            {
                if (distance(mp,within) > 1 || within == null ){
                    HashSet moveSet = new HashSet();
                    // area which can move to. 
                    for (Iterator it2 = map1.keySet().iterator(); it2.hasNext();) {
                        bp2 = (BoardPoint) it2.next();
                        if (map1.get(bp2) == Action.ActionType.MOVE) 
                            moveSet.add(bp2);
                    }
                    // greedy algorithm
                    double max = 1111111D;
                    if (within!= null){
                        for (Iterator moveIt =moveSet.iterator(); moveIt.hasNext(); ){
                            bp2 = (BoardPoint) moveIt.next();
                            double distanceA = distance(bp2, within);
                            if (distanceA<max)
                            {
                                movePoint = bp2;
                                max = distanceA;
                            }
                        }
                        if (movePoint!=null)
                            engine.armyAction(this, iValue, movePoint);
                    }
                    else{
                        for (Iterator moveIt =moveSet.iterator(); moveIt.hasNext(); ){
                            bp2 = (BoardPoint) moveIt.next();
                            double distanceD = distance(bp2, best);
                            if (distanceD<max)
                            {
                                movePoint = bp2;
                                max = distanceD;
                            }
                        }
                        if(distance(best, movePoint) > distance (best, mp))
                        {
                            movePoint = null;
                        }
                        if ((movePoint!=null))
                        {
                            engine.armyAction(this, iValue, movePoint);
                        }
                    }
                }
                //attack 
                for (Iterator it2 = map1.keySet().iterator(); it2.hasNext();) {
                    bp2 = (BoardPoint) it2.next();
                    if (map1.get(bp2) == Action.ActionType.ATTACK)
                    {
                        engine.armyAction(this, iValue, bp2);
                        break;
                    }
                }
            }
        }
        engine.turnEnd(this);
    }
    
    private void setArmy(BoardPoint bp)
    {
        int ids = engine.armySet(this, bp, new String("I am AI!"));
        setBasic(ids);
    }
    private void setBasic (int ids) {
        engine.updateArmy(this, ids, 0, new BoardPoint(0, 0));
        engine.updateArmy(this, ids, 2, new BoardPoint(1, 0));
        engine.updateArmy(this, ids, 4, new BoardPoint(3, 0));
        engine.updateArmy(this, ids, 5, new BoardPoint(2, 4));
        engine.updateArmy(this, ids, 1, new BoardPoint(0, 3));
        engine.updateArmy(this, ids, 1, new BoardPoint(6, 2));
        engine.updateArmy(this, ids, 3, new BoardPoint(4, 1));

    }
    private void setAdvance(int ids)
    {         
            setBasic(ids);
            engine.updateArmy(this, ids, 4, new BoardPoint(3, 0));
            engine.updateArmy(this, ids, 4, new BoardPoint(0, 5));
            engine.updateArmy(this, ids, 4, new BoardPoint(2, 6));
            engine.updateArmy(this, ids, 4, new BoardPoint(6, 0));
    }
    private double distance(BoardPoint bp1, BoardPoint bp2)
    {
        if (bp1 !=null &&bp2 !=null)  
            return Math.pow(bp1._x - bp2._x,2) + Math.pow(bp1._y - bp2._y,2);
        return 0;
    }
    // scan map to find thebestbase
    private BoardPoint bestBase (int[][] Map)
    {
        int bestNum = 0;
        int point = 0;
        BoardPoint mp1 = new BoardPoint (23,23);
        BoardPoint bestPoint = mp1;
        BoardPoint selectPoint = mp1;
        
        for (int i = 1 ; i < Map.length -1; i++){
            for (int j = 1; j <Map[0].length -1; j ++)
            {
                point = Map[i][j] + Map[i+1][j]+ Map[i-1][j] + Map[i][j+1] + Map[i][j-1];
                if (point > bestNum)
                {
                    bestNum = point;
                    bestPoint._x = i;
                    bestPoint._y = j;
                }
                else if (point == bestNum)
                {
                    selectPoint._x = i;
                    selectPoint._y = j;
                    if (distance (bestPoint,mp1) > distance(selectPoint, mp1))
                        bestPoint = selectPoint;
                }
            }
        }
        return bestPoint;
    }
    public void end(int i) {
        endBol = true;
    }
    public void equipUp(List<Equip> list) {}
    public void lost() {}
    public void mapDataUp(BoardArea ba) {}
    public void updateTurn(int ii) {}
}