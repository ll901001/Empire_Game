/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package empirestate.gameCore.data;

import java.util.Map;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Army;
import empirestate.gameCore.Army;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.GameEngine;

public class CheckWinner
{
    public boolean playerWin(Map<BoardPoint, Army> armyMap, GameEngine ge, int ii){
          return false;
    }

    public boolean playerLost(Map<BoardPoint, Army> armyMap, GameEngine ge, int ii){
          if (armyMap.size() == 0)
              return true;
          return false;
    }
    public String objectives(){
        return new String("Kill all enemies!");
    }

}