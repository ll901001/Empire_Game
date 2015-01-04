package empirestate.gameCore;
import empirestate.gameCore.data.TurnThread;
import empirestate.gameCore.data.Action;
import empirestate.gameCore.data.Player;
import empirestate.gameCore.data.CheckWinner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import empirestate.gameCore.data.Action.ActionType;
import empirestate.gameCore.Block.Property;
import empirestate.gameCore.data.GameData;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameEngine
{
    public int[][] boardMap;
    public Vector <HashSet<BoardPoint>> startingPoint;
    public Vector<Army> armyV;
    public Vector<ArmyInformation> armyInfV;
    public Vector<Player> playerV;
    public Vector<PlayerData> playerDataV;
    public Vector<Equip> equip;
    public CheckWinner winner; 
    public int turns;
    public int maxPlayer;
    public String mapStr;
    //Constructor 
    public GameEngine(String str, CheckWinner winCondition, Vector<Equip> equipVec){
        mapStr = str;
        startingPoint = new Vector();
        armyV = new Vector();
        armyInfV = new Vector();
        playerV = new Vector();
        playerDataV = new Vector();
        equip = equipVec;
        winner = winCondition;
        turns = -1;
        maxPlayer = 0;  
    }
    
    public  void addPlayer (Player pp,String str, int armyNum, int startGold ) {
        playerV.add(pp);
        playerDataV.add(new PlayerData(new String(str), armyNum, startGold));   
    }

    private int playerId(Player pp){
        for (int i = 0; i < playerV.size();i++)
        {
            if (playerV.get(i) == pp)
                return i;
        }
        return -1;
    }
    
    public void start(){
        loadFile(mapStr);
        if (playerV.size() == 0)
            return;
        BoardArea startArea;
        Vector EquipV;

        for (int i = 0; i < playerV.size(); i++)
        {
            startArea = new BoardArea(boardMap, startingPoint.get(i));
            EquipV = new Vector();
            for (int j = 0; j < equip.size(); j++)
            {
                EquipV.add(new Equip(equip.get(j)));
            }    

            playerV.get(i).start(this, startArea, i,  playerV.size());
            playerV.get(i).equipUp(EquipV);
            System.out.println(playerV.size());
        }
    }
    
    private void loadFile (String str){
        try {
            FileReader fr = new FileReader(str);
            BufferedReader bf = new BufferedReader (fr);
            
            HashSet waterSet = new HashSet();
            HashSet defenseSet = new HashSet();
            HashSet attackSet = new HashSet();
            HashSet goldSet = new HashSet();
            int width = 0;          
            int height = 0;
            String string;

            while ((string = bf.readLine()) != null)
            {
                if (string.startsWith("height|"))
                    height = new Integer(string.split("\\|")[1]).intValue();
                else if (string.startsWith("width|"))
                    width = new Integer(string.split("\\|")[1]).intValue();
                else if (string.startsWith("players|"))
                    maxPlayer = new Integer(string.split("\\|")[1]).intValue();             
                else
                {
                    String[] stringArr1;
                    String[] stringArr2;
                    if (string.startsWith("start|"))
                    {
                        HashSet startArea = new HashSet();
                        stringArr1 = string.split("\\|");
                        for (int k = 1; k < stringArr1.length; k++)
                        {
                            stringArr2 = stringArr1[k].split(",");
                            if (stringArr2.length != 2)
                            {
                               throw new RuntimeException();
                            }
                            startArea.add(new BoardPoint(new Integer(stringArr2[0]).intValue(), new Integer(stringArr2[1]).intValue()));
                        }
                        startingPoint.add(startArea);
                        System.out.println(startingPoint);
                    }
                    else if (string.startsWith("water|"))
                    {
                        stringArr1 = string.split("\\|");
                        for (int k = 1; k < stringArr1.length; k++)
                        {
                            stringArr2 = stringArr1[k].split(",");
                            waterSet.add(new BoardPoint(new Integer(stringArr2[0]).intValue(), new Integer(stringArr2[1]).intValue()));
                        }
                    }
                    else if (string.startsWith("defense|"))
                    {
                        stringArr1 = string.split("\\|");
                        for (int k = 1; k < stringArr1.length; k++)
                        {
                            stringArr2 = stringArr1[k].split(",");
                            defenseSet.add(new BoardPoint(new Integer(stringArr2[0]).intValue(), new Integer(stringArr2[1]).intValue()));
                        }
                    }
                    else if (string.startsWith("attack|"))
                    {
                        stringArr1 = string.split("\\|");
                        for (int k = 1; k < stringArr1.length; k++)
                        {
                            stringArr2 = stringArr1[k].split(",");
                            attackSet.add(new BoardPoint(new Integer(stringArr2[0]).intValue(), new Integer(stringArr2[1]).intValue()));
                      }
                    }
                    else if (string.startsWith("goldmine|"))
                    {
                        stringArr1 = string.split("\\|");
                        for (int k = 1; k < stringArr1.length; k++)
                        {
                            stringArr2 = stringArr1[k].split(",");
                            goldSet.add(new BoardPoint(new Integer(stringArr2[0]).intValue(), new Integer(stringArr2[1]).intValue()));
                        }
                    }
                    else
                    {
                       throw new RuntimeException();
                    }
                }
        }
        // judgment
        if ((width == 0) || (height == 0) || (maxPlayer == 0) || (maxPlayer != startingPoint.size()))
            throw new RuntimeException();
        boardMap = new int[width][height];
        for (int k = 0; k < width; k++){
            for (int m = 0; m < height; m++){
                boardMap[k][m] = 0;
            }
        }
        BoardPoint bp;
        Iterator it;
        for (it = waterSet.iterator(); it.hasNext(); ) { 
            bp = (BoardPoint)it.next();

            if ((bp._x < 0) || (bp._x >= width) || (bp._y < 0) || (bp._y >= height))
                throw new RuntimeException();

            boardMap[bp._x][bp._y] =  boardMap[bp._x][bp._y] + 1;
        }
        
        for (it = defenseSet.iterator(); it.hasNext(); ) { 
            bp = (BoardPoint)it.next();

            if ((bp._x < 0) || (bp._x >= width) || (bp._y < 0) || (bp._y >= height))
                throw new RuntimeException();
            boardMap[bp._x][bp._y] = boardMap[bp._x][bp._y] + 2;
        }

        for (it = attackSet.iterator(); it.hasNext(); ) { 
            bp = (BoardPoint)it.next();
            if ((bp._x < 0) || (bp._x >= width) || (bp._y < 0) || (bp._y >= height))
               throw new RuntimeException();

            boardMap[bp._x][bp._y] = boardMap[bp._x][bp._y] + 4;
        }

        for (it = goldSet.iterator(); it.hasNext(); ) { 
            bp = (BoardPoint)it.next();
            if ((bp._x < 0) || (bp._x >= width) || (bp._y < 0) || (bp._y >= height))
                throw new RuntimeException();

            boardMap[bp._x][bp._y] = boardMap[bp._x][bp._y] + 8;
        } 
        fr.close();
        } catch (Exception e) {} 
    }
        
    public  void gameReady(Player pp){
        int i = playerId(pp);
        if (turns != -1)
            return;
        playerDataV.get(i).ready = true;
        for (int j = 0; j < playerDataV.size(); j++)
        {
            if (!(playerDataV.get(j).ready) && (!(playerDataV.get(j)).quit))
                return;
        }
        mapUp(new Action(Action.ActionType.GAME_START, null, null));
        turnNext();
    }
    
    public void turnNext(){
        int i = checkWin();
        if (i != -1)
        {
            gameEnd(i);
            return;
        }
        for (int j = 0; j < armyInfV.size(); j++)
        {
            armyInfV.get(j).moveBol = false;
            armyInfV.get(j).attackBol = false;
        }
        for (int j = 0; j < playerV.size() - 1; j++)
        {
            turns += 1;
            if (turns == 2)
                turns = 0;
            if (!(playerDataV.get(turns).quit) && (!playerDataV.get(turns).lost))
            {
                int turnGold = 0;
                for (int m = 0; m < armyInfV.size(); m++)
                {
                    BoardPoint bp = armyInfV.get(m).position;
                    if ((armyV.get(m).user == turns) && (!bp.equals(new BoardPoint(-1, -1))) && (GameData.hasTerrain(boardMap[bp._x][bp._y], 0x08)))
                        turnGold += 10;
            }
            playerDataV.get(turns).gold += turnGold;  
            TurnThread turn = new TurnThread(playerV.get(turns));
            turn.start();
            //playerV.get(turns).turnStart();
            for (int n = 0; n < playerV.size(); n++)
                playerV.get(n).updateTurn(turns);
            return;
            }
        }
        gameEnd(-1);
    }
    
    public  void turnEnd(Player pp){
        int i = playerId(pp);
        if (i == turns)
            turnNext();
    } 
    
    private  void moveAction(int i, int armyId, BoardPoint destina, BoardPoint position){
        Map map1;
        if (armyInfV.get(armyId).moveBol)
            return;
        map1 = availableAction(armyId, i, armyV, armyInfV, boardMap);
        if (map1.get(destina) != Action.ActionType.MOVE)
            return;
        armyInfV.get(armyId).moveBol = true;
        armyInfV.get(armyId).position = destina;
        mapUp(new Action(Action.ActionType.MOVE, position, destina)); //local from. para to
    }
    
    private  void attackAction(int i, int k, int armyId, BoardPoint attackBp, BoardPoint beAttBp){
        Map map1;
        if (armyInfV.get(armyId).attackBol)
                return;
        map1 = availableAction(armyId, i, armyV, armyInfV, boardMap);
        if (map1.get(attackBp) != Action.ActionType.ATTACK)
            return;
        armyInfV.get(armyId).attackBol = true;
        armyInfV.get(armyId).moveBol = true;

        Army attacker = armyV.get(armyId);
        Army defender = armyV.get(k);

        double d1 = beAttBp._x + 0.5D;
        double d2 = beAttBp._y + 0.5D;
        double d3 = attackBp._x + 0.5D;
        double d4 = attackBp._y + 0.5D;

        double d5 = Math.atan2(d4 - d2, d3 - d1);

        double d6 = d5 + Math.PI/2;

        int m = Math.min(8, 2 + Math.abs(attackBp._x - beAttBp._x) + Math.abs(attackBp._y - beAttBp._y));
        // m attack lines 
        AttackPoint[] attackPoint = new AttackPoint[m];

        double d7 = (m - 1) / 2.0D;
        double d11 = 1.0D / 8;

        for (int n = 0; n < m; n++) {
            double d10 = d7 * d11;
            double d8 = d10 * Math.cos(d6);
            double d9 = d10 * Math.sin(d6);
            attackPoint[n] = new AttackPoint(d3 + d8, d4 + d9);
            d7 -= 1.0D;
        }

        Vector Vector1 = new Vector();
        for (int i1 = 0; i1 < m; i1++) {
            Vector1.add(attackDirection(8, new AttackPoint (d1,d2), attackPoint[i1]));
        }

        int attackPower = ((Integer)attacker.stats.get(Block.Property.ATTACK)).intValue();
        int defensePower = ((Integer)defender.stats.get(Block.Property.DEFENSE)).intValue() + (GameData.hasTerrain(boardMap[attackBp._x][attackBp._y], 0x02) ? 75 : 0);
        // Distribute the damage
        attackPower = damage(attackPower, defensePower);
        int damageLeft;
        for (int i4 = 0; i4 < m; i4++)
        {
            damageLeft = attackPower / m;
            for (Iterator it = ((List)Vector1.get(i4)).iterator(); it.hasNext();) {
                Map map2 = (Map) it.next();
                int damageDistr = 0;
                for (Iterator it1 =map2.keySet().iterator(); it1.hasNext();) {
                    BoardPoint bp2 = (BoardPoint) it1.next();

                    Block defenTile = (Block)defender.tile.get(bp2);
                    if (defenTile != null){
                          int damage = (int)(damageLeft * ((Double)map2.get(bp2)).doubleValue() + 0.5D);
                          defenTile.life -= damage;
                          if (defenTile.life <= 0) {
                                damage += defenTile.life;
                                defender.tile.remove(bp2);
                          }
                          damageDistr += damage;
                    }
                }
                damageLeft -= damageDistr;
                if (damageLeft == 0)
                    break;
            }
        }
        updateProperty(defender);
        mapUp(new Action(Action.ActionType.ATTACK, beAttBp, attackBp));

        if (((Integer)defender.stats.get(Block.Property.CORE)).intValue() == 0) {
            playerDataV.get(attacker.user).gold += 100;
            armyInfV.get(k).position = new BoardPoint(-1, -1);
            mapUp(new Action(Action.ActionType.ARMY_DIED, attackBp, attackBp));
        }
    } 
    
    public  void armyAction(Player pp, int armyId, BoardPoint mapPoint){
        int i = playerId(pp);
        if ((turns != i) || (armyId < 0) || (armyId >= armyV.size()))
            return;
        BoardPoint position = new BoardPoint(armyInfV.get(armyId).position);
        int k = basicActionInt(mapPoint, armyInfV);
        if (k == -1)
             moveAction(i, armyId, mapPoint, position);     
        else
            attackAction(i, k, armyId, mapPoint, position);
    }
    // define attack line according to Wu Xiaolin's line algrithom
    private List<Map<BoardPoint, Double>> attackDirection(int ii, AttackPoint rp1, AttackPoint rp2){
       double x1 = rp1._x; double y1 = rp1._y;
        double x2 = rp2._x; double y2 = rp2._y;

        double dx = rp2._x - rp1._x;
        double dy = rp2._y - rp1._y;
   
        boolean bool1 = Math.abs(dx) < Math.abs(dy);
        double buff;
        if (bool1)
        {
            buff = x1;
            x1 = y1;
            y1 = buff;

            buff = x2;
            x2 = y2;
            y2 = buff;

            buff = dx;
            dx = dy;
            dy = buff;
        }
        // Make the line go left to right
        boolean bool2 = dx < 0.0D;
        if (bool2)
        {
            buff = x1;
            x1 = x2;
            x2 = buff;

            buff = y1;
            y1 = y2;
            y2 = buff;
        }
        double grad = dy / dx;
        int i;
        int j;
        int k;
        int m;
        if (bool1) {
            i = (int)Math.floor((int)rp2._y * ii);
            j = (int)Math.floor(((int)rp2._y + 1) * ii) - 1;
            k = (int)Math.floor((int)rp2._x * ii);
            m = (int)Math.floor(((int)rp2._x + 1) * ii) - 1;
        } else {
            i = (int)Math.floor((int)rp2._x * ii);
            j = (int)Math.floor(((int)rp2._x + 1) * ii) - 1;
            k = (int)Math.floor((int)rp2._y * ii);
            m = (int)Math.floor(((int)rp2._y + 1) * ii) - 1;
        }
        int n = (int)Math.floor((int)x1 * ii);

        LinkedList linkedlist = new LinkedList();
       
        double d9 = y1 * ii + grad * (n - x1 * ii);
        // main loop
        for (int i1 = n; i1 <= j; i1++) {
             int i2 = i1 >= i ? 1 : 0;

            if (i2 != 0) {
                HashMap mapH = new HashMap();
                int i3 = (int)Math.floor(d9);
                double d10 = d9 - i3;

                if ((k <= i3) && (i3 <= m)) {
                    putPoint(mapH, i1 % ii, i3 % ii, 1.0D - d10, bool1, bool2);
                }

                i3++;
                if ((k <= i3) && (i3 <= m)) {
                    putPoint(mapH, i1 % ii, i3 % ii, d10, bool1, bool2);
                }
              if (mapH.size() > 0) {
                    if (bool2)
                        linkedlist.addFirst(mapH);
                    else {
                        linkedlist.addLast(mapH);
                    }
                }
            }
            d9 += grad;
        }
        return linkedlist;
    }
    //Puts a point in the given map.
    private void putPoint(Map<BoardPoint, Double> map, int i1, int i2, double dou, boolean bool1, boolean bool2){
        if (bool1) 
            map.put(new BoardPoint(i2, i1), Double.valueOf(dou));
        else 
            map.put(new BoardPoint(i1, i2), Double.valueOf(dou));   
    }

    private int damage(int i1, int i2){
        return (int)(i1 / (1.0D + 9.0D * Math.tanh(Math.pow(i2, 1.2D) / 5000.0D)));
    }

    private void updateProperty(Army army){
        Block.Property abonustype[] = Block.Property.values();
        int i = abonustype.length;
        for (int j = 0; j < i; j++)
        {
            Block.Property attri = abonustype[j];
            army.stats.put(attri, 0);
        }
        for (Iterator it = army.tile.values().iterator(); it.hasNext();)
        {
            Block tile = (Block)it.next();
            Iterator it2 = tile.equip.keySet().iterator();
            while (it2.hasNext()) 
            {
                Block.Property attri = (Block.Property)it2.next();
                army.stats.put(attri, Integer.valueOf(((Integer)army.stats.get(attri)).intValue() + ((Integer)tile.equip.get(attri)).intValue()));
            }
        }
    }

    public HashMap<BoardPoint, Action.ActionType> armyAction(Player pp, int ii){
        int i = playerId(pp);
        return availableAction(ii, i, armyV, armyInfV, boardMap);
    }

    private HashMap<BoardPoint, Integer> areaForArmy(int ii, HashSet<BoardPoint> set){
        HashMap ArmyPosionton = new HashMap();
        for (int i = 0; i < armyInfV.size(); i++)
        {
            if (set.contains(armyInfV.get(i).position))
                ArmyPosionton.put(armyInfV.get(i).position,i);
        }
        return ArmyPosionton;
    }
    //Generates new map information and sends it to all players.
    private void mapUp(Action ac){
        HashSet mapSet = null ;
        for (int i = 0; i < playerV.size(); i++)
        {
            if (!playerDataV.get(i).quit)
            {
                mapSet = sight(i, armyV, armyInfV, boardMap);
                if (mapSet.contains(ac.destna) || ac.action == Action.ActionType.GAME_START)
                    playerV.get(i).mapUp(areaForArmy(i, mapSet), mapSet, new Action(ac));
                else if ((mapSet).contains(ac.base))
                {
                    BoardPoint newBase = new BoardPoint(ac.base);
                    for (Iterator it = mapSet.iterator(); it.hasNext(); ) 
                    { 
                        BoardPoint bp = (BoardPoint)it.next();
                        if (Math.abs(bp._x - ac.destna._x) + Math.abs(bp._y - ac.destna._y) < Math.abs(newBase._x - ac.destna._x) + Math.abs(newBase._y - ac.destna._y))
                              newBase = new BoardPoint(bp);
                    }
                    playerV.get(i).mapUp(areaForArmy(i, mapSet), mapSet, new Action(ac.action, ac.base, newBase));
                }
                else{
                    playerV.get(i).mapUp(areaForArmy(i, mapSet), mapSet, null);
                }
            }
        }
        
    }

    public  void quit(Player pp){
        int i = playerId(pp);
        playerDataV.get(i).lost = true;
        playerDataV.get(i).quit = true;
        if (turns == i)
            turnNext();
        else{
            int j = checkWin();
            if (j != -1){
                  gameEnd(j);
                  return;
            }
        }
    }

    private int checkWin(){
        for (int i = 0; i < playerV.size(); i++)
        {
            HashMap hashMap = new HashMap();
            for (int j = 0; j < this.armyV.size(); j++)
            {
                if ((armyV.get(j).user == i) && (!armyInfV.get(j).position.equals(new BoardPoint(-1, -1))))
                    hashMap.put(new BoardPoint(armyInfV.get(j).position), new Army(this.armyV.get(j)));
            }
            if ((winner.playerWin(hashMap, this, i)) && (!playerDataV.get(i).lost) && (!playerDataV.get(i).quit))
                return i;
            if ((winner.playerLost(hashMap, this, i)) && (!playerDataV.get(i).lost) && (!playerDataV.get(i).quit)){
                playerDataV.get(i).lost = true;
                playerV.get(i).lost();
            }
        }
        int i = 0;
        int j = -1;
        for (int k = 0; k < playerDataV.size(); k++){
              if ((!playerDataV.get(k).lost) && (!playerDataV.get(k).quit)){
                    i++;
                    j = k;
              }
        }
        if (i == 1)
            return j;
        return -1;
    }
    
    private void gameEnd(int ii){
        System.out.println(ii);
        turns = -2;
        for (int i = 0; i < this.playerV.size(); i++) {
            if (!(playerDataV.get(i)).quit) {
                ((Player)this.playerV.get(i)).end(ii);
            }
        }
    }

    public  int armySet(Player pp, BoardPoint bp, String str)                         {
        bp = new BoardPoint(bp);
        int i = playerId(pp);
        if (turns != -1 || restArmies(pp) == 0)
            return -1;
        if ((basicActionInt(bp, armyInfV) == -1) && (((HashSet)startingPoint.get(i)).contains(bp)) && (!GameData.hasTerrain(boardMap[bp._x][bp._y], 0x01)))
        {
            Army ar = new Army(new String(str), i, armyV.size());
            Block coreTile = new Block(100);
            coreTile.equip.put(Block.Property.CORE, 1);
            int j;
            int k;
            for (j = 3; j <= 4; j++)
            {
                for (k = 3; k <= 4; k++)
                {
                    ar.tile.put(new BoardPoint(j, k), new Block(coreTile));
                }
            }
            ar.stats.put(Block.Property.CORE, Integer.valueOf(4));
            armyV.add(ar);
            armyInfV.add(new ArmyInformation(bp));
            return armyV.size() - 1;
        }
        return -1;  
    }

    public  boolean updateArmy(Player pp, int armiesId, int equipID, BoardPoint bp)           {
        int i = playerId(pp);
        if (((turns == -1) && (i == 0)) || (equipID < 0) || (equipID >= equip.size()) || (armiesId < 0) || (armiesId >= armyV.size()))
           return false;
        Equip equi = equip.get(equipID);
        Army arm = armyV.get(armiesId);
        int j = playerDataV.get(i).gold;
        if (arm.user != i || armyInfV.get(armiesId).moveBol||equi.cost > j)
            return false; 
        
        for (Iterator it = equi.tile.keySet().iterator(); ((Iterator)it).hasNext(); ) 
        { 
            BoardPoint bp1 = (BoardPoint)(it).next();
            if (arm.tile.containsKey(new BoardPoint(bp._x + ((BoardPoint)bp1)._x, bp._y + ((BoardPoint)bp1)._y)))
                return false;
            if ((bp._x + ((BoardPoint)bp1)._x < 0) || (bp._x + ((BoardPoint)bp1)._x >= 8) || (bp._y + ((BoardPoint)bp1)._y < 0) || (bp._y + ((BoardPoint)bp1)._y >= 8))
                return false;
        }

        for ( Iterator it1 = equi.tile.keySet().iterator(); ((Iterator)it1).hasNext(); ) 
        { 
            BoardPoint bp1 = (BoardPoint)((Iterator)it1).next();
            Block tile = new Block((Block)equi.tile.get(bp1));
            for (Block.Property proper : ((Block)tile).equip.keySet())
            {
                arm.stats.put(proper, Integer.valueOf(((Integer)((Block)tile).equip.get(proper)).intValue() + ((Integer)arm.stats.get(proper)).intValue()));
            }
            arm.tile.put(new BoardPoint(bp._x + bp1._x, bp._y + bp1._y), (Block)tile);
        }
        (playerDataV.get(i)).gold -= equi.cost;
        if (turns != -1)
            mapUp(new Action(Action.ActionType.ADD_SIGHT, new BoardPoint((armyInfV.get(armiesId)).position), new BoardPoint((armyInfV.get(armiesId)).position)));
        return true;
    }

    public String playerName(int i){
        return new String(((PlayerData)playerDataV.get(i)).name);
    }

    public int goldNumber(Player p){
        int i = playerId(p);
        int j = (playerDataV.get(i)).gold;
        return j;
    }

    public Army armyG(Player pp, int ii){
        int i = playerId(pp);
        if ((ii >= this.armyV.size()) && (ii < 0))
        {
            return null;
        }
        Army army = new Army((Army)armyV.get(ii));
        if ((!sight(i, armyV, armyInfV, boardMap).contains(((ArmyInformation)armyInfV.get(ii)).position)) && (i != -2))
        {
            return null;
        }
        return army;
    } 

    public int restArmies(Player pp){
        int i = 0;
        int j = playerId(pp);
        if (j == -2)
            return 0;   
        for (int k = 0; k < armyV.size(); k++)
        {
            if (armyV.get(k).user == j)
                i++;
        }
        return playerDataV.get(j).maxUnits - i;
    }
    //Calculates the available actions for the army
    public  HashMap<BoardPoint, Action.ActionType> availableAction(int i1, int i2, List<Army> armyList, List<ArmyInformation> armyInf, int[][] terrain){
        Set sightSet = sight(i2, armyList, armyInf, terrain);

        HashMap actionMap = new HashMap();
        if ((armyInf.get(i1).moveBol) && (armyInf.get(i1).attackBol))
            return actionMap;

        if (!armyInf.get(i1).moveBol)
        {
            for (Iterator it1 = armyPosition(i1, 0, armyList, armyInf, terrain, sightSet).iterator(); it1.hasNext(); ) 
            { 
                BoardPoint mp1 = (BoardPoint)it1.next();
                if (basicActionInt(mp1, armyInf) == -1)
                    actionMap.put(new BoardPoint(mp1), Action.ActionType.MOVE);
            }
        }
        
        if (!((ArmyInformation)armyInf.get(i1)).attackBol)
        {
             for (Iterator it2 = armyPosition(i1, 1, armyList, armyInf, terrain, sightSet).iterator(); ((Iterator)it2).hasNext(); ) 
            { 
                BoardPoint mp2 = (BoardPoint)it2.next();

                int i = basicActionInt(mp2, armyInf);
                if ((i != -1) && (((Army)armyList.get(i)).user != i2))
                    actionMap.put(new BoardPoint(mp2), Action.ActionType.ATTACK);
            }
        }
        return actionMap;
    }

    public  int basicActionInt(BoardPoint bp, List<ArmyInformation> list){
          for (int i = 0; i < list.size(); i++){
                if ((list.get(i)).position.equals(bp))
                    return i;
          }
          return -1;
    }

    public  HashSet<BoardPoint> armyPosition(int i1, int i2, List<Army> armyList, List<ArmyInformation> ArmyInfList, int[][] arrayInt, Set<BoardPoint> set){
        ArmyInformation armyInf = (ArmyInformation)ArmyInfList.get(i1);
        HashSet area = new HashSet();
        int attRange;
        int k;
        if (i2 == 0)
        {
            Army arm = (Army)armyList.get(i1);
            attRange = 0 + ((Integer)arm.stats.get(Block.Property.MOVEMENT)).intValue() / 120;

            if (((Integer)arm.stats.get(Block.Property.WATER_MOVEMENT)).intValue() > 0)
                k = 1;
            else
                k = 0;
            
            HashSet hashSet = new HashSet();
            Vector vector = new Vector();
            // RECURSION
            vector.add(new MapAndArea(new BoardPoint(armyInf.position._x + 1, armyInf.position._y), attRange));
            vector.add(new MapAndArea(new BoardPoint(armyInf.position._x - 1, armyInf.position._y), attRange));
            vector.add(new MapAndArea(new BoardPoint(armyInf.position._x, armyInf.position._y + 1), attRange));
            vector.add(new MapAndArea(new BoardPoint(armyInf.position._x, armyInf.position._y - 1), attRange));
            while (vector.size() > 0)
            {
                MapAndArea pointNum = (MapAndArea)vector.remove(0);
                if (!hashSet.contains(pointNum.point))
                {
                    hashSet.add(pointNum.point);

                    if ((pointNum.abilityDistance > 0) && (pointNum.point._x >= 0) && (pointNum.point._y >= 0) && (pointNum.point._x < arrayInt.length) 
                            && (pointNum.point._y < arrayInt[0].length) && (basicActionInt(pointNum.point, ArmyInfList) == -1) && (set.contains(pointNum.point)) 
                            && (!GameData.hasTerrain(arrayInt[pointNum.point._x][pointNum.point._y], 0x10)))
                    {
                        if (GameData.hasTerrain(arrayInt[pointNum.point._x][pointNum.point._y], 0x01))
                        {
                          if (k != 0)
                          {
                                area.add(new BoardPoint(pointNum.point));
                                vector.add(new MapAndArea(new BoardPoint(pointNum.point._x + 1, pointNum.point._y), pointNum.abilityDistance - 1));
                                vector.add(new MapAndArea(new BoardPoint(pointNum.point._x - 1, pointNum.point._y), pointNum.abilityDistance - 1));
                                vector.add(new MapAndArea(new BoardPoint(pointNum.point._x, pointNum.point._y + 1), pointNum.abilityDistance - 1));
                                vector.add(new MapAndArea(new BoardPoint(pointNum.point._x, pointNum.point._y - 1), pointNum.abilityDistance - 1));
                          }
                        }
                        else
                        {
                            area.add(new BoardPoint(pointNum.point));
                            vector.add(new MapAndArea(new BoardPoint(pointNum.point._x + 1, pointNum.point._y), pointNum.abilityDistance - 1));
                            vector.add(new MapAndArea(new BoardPoint(pointNum.point._x - 1, pointNum.point._y), pointNum.abilityDistance - 1));
                            vector.add(new MapAndArea(new BoardPoint(pointNum.point._x, pointNum.point._y + 1), pointNum.abilityDistance - 1));
                            vector.add(new MapAndArea(new BoardPoint(pointNum.point._x, pointNum.point._y - 1), pointNum.abilityDistance - 1));
                        }
                    }
                }
            }
        }
        else
        {
            if (i2 == 1)
            {
                attRange = ((Integer)((Army)armyList.get(i1)).stats.get(Block.Property.RANGE)).intValue() / 120;
                if (GameData.hasTerrain(arrayInt[armyInf.position._x][armyInf.position._y], 0x04))
                {
                    attRange += 3;
                }
            }
            else
            {
                attRange = 6 + ((Integer)((Army)armyList.get(i1)).stats.get(Block.Property.SIGHT)).intValue() / 120;
            }

            for (int j = armyInf.position._x - attRange; j < armyInf.position._x + attRange + 1; j++)
            {
                for (k = armyInf.position._y - attRange; k < armyInf.position._y + attRange + 1; k++)
                {
                  if ((j >= 0) && (k >= 0) && (j < arrayInt.length) && (k < arrayInt[0].length) && (Math.abs(armyInf.position._x - j) + Math.abs(armyInf.position._y - k) <= attRange))
                    {
                        if (((i2 == 1) && (set.contains(new BoardPoint(j, k)))) || (i2 != 1)) {
                            area.add(new BoardPoint(j, k));
                        }
                    }
                }
            }
        }
        return area;
    }

    public  HashSet<BoardPoint> sight(int i1, List<Army> armyList, List<ArmyInformation> armyInfList, int[][] arrayInt){
        HashSet sightArea = new HashSet();
        for (int i = 0; i < armyList.size(); i++) {
            if (armyList.get(i).user == i1) {
                sightArea.addAll(armyPosition(i, 2, armyList, armyInfList, arrayInt, sightArea));
            }
        }
        return sightArea;
    }
    
    class MapAndArea {
        BoardPoint point;
        int abilityDistance;

        public MapAndArea(BoardPoint bp, int ii) {
              point = bp;
              abilityDistance = ii;
        }
    }
    private class AttackPoint{
        private double _x;
        private double _y;

        private AttackPoint(double i1, double i2){
            _x = i1;
            _y = i2;
        }

        private AttackPoint(AttackPoint rp){
            _x = rp._x;
            _y = rp._y;
        }
    }
    public class ArmyInformation{
        private boolean moveBol;
        private boolean attackBol;
        public BoardPoint position;
        private ArmyInformation(BoardPoint paramMapPoint)
        {
            position = new BoardPoint(paramMapPoint);
            moveBol = false;
            attackBol = false;
        }
    }
    public class PlayerData {
        public int gold;
        public String name;
        public boolean ready;
        public boolean quit;
        public boolean lost;
        public int maxUnits;

        public PlayerData(String str, int i1, int i2)
        {
            name = new String(str);
            maxUnits = i1;
            gold = i2;
            lost = false;
            ready = false;
            quit = false;
         }
    }
}