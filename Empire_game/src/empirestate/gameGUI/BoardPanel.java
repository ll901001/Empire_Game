
package empirestate.gameGUI;
import empirestate.gameCore.ControlPlayer;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import empirestate.gameCore.data.Action;
import empirestate.gameCore.data.Action.ActionType;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Army;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;

public class BoardPanel extends JPanel
{
    private ControlPlayer player;
    public  RadarPanel miniMap;
    private int[][] gameBoard;
    private Set<BoardPoint> position;
    private Map<BoardPoint, Army> army;
    private BoardPoint choosePos;
    private Map<BoardPoint, Action.ActionType> chooseAct;
    private Set<BoardPoint> sight;
    //Location in map squares currently displayed by the view
    private double sightX;
    private double sightY;
    private int scaleLvl;

    public BoardPanel(ControlPlayer cp){
        player = cp;
        scaleLvl = 60;
        miniMap = new RadarPanel(cp, this);
        MapListener mouseListener = new MapListener();
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener); 
        setBackground(Color.BLACK);
    }

    public void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        int[][] boardMap = gameBoard;
        Set posiSet = position;
        Map armiesMap = army;
        BoardPoint positionP = choosePos;
        Map map2 = chooseAct;
        Set sightSet = sight;
        int ratio = scaleLvl;
        double viewX = sightX;
        double viewY = sightY;
        double viewWidth = calViewWidth();
        double viewHeight = calViewHeight();
        
        if (boardMap != null){
            int k = (int) (-1* viewX * ratio); 
            int m = (int) (-1.0D * viewY * ratio);
            BoardPoint bp =  new BoardPoint(0, 0);
            for (bp._x = Math.max(0, (int)viewX); (bp._x < boardMap.length) && (bp._x < viewX + viewWidth); bp._x += 1) 
            {
                int n = k + ratio * bp._x;
                // draw game board 
                for (bp._y = Math.max(0, (int)viewY); (bp._y < boardMap[bp._x].length) && (bp._y < viewY + viewHeight); bp._y += 1) 
                {
                    int i1 = m + ratio * bp._y;
                    Graphics gra = gg.create(n, i1, ratio, ratio);
                    player.drawMethods.drawTerrainTile(gra, boardMap[bp._x][bp._y], ratio, true);
                    if (posiSet != null)
                         player.drawMethods.drawStartingFOW(gra, player.playerID(), posiSet, bp, ratio);
                    if (sightSet != null) {
                        player.drawMethods.drawFOW(gra, sightSet.contains(bp), ratio);
                    }
                    // draw army in game board
                    Army army = (Army) armiesMap.get(bp);
                    if (army != null) {
                        player.drawMethods.drawArmy(gra, army, ratio/15, ratio);
                        if ((positionP != null) && (positionP.equals(bp))) 
                        {
                            gg.setColor(Color.WHITE);
                            gg.drawRect(n, i1, ratio - 1, ratio - 1);
                        }
                    }
                    if ((map2 != null) && (map2.containsKey(bp)))
                        player.drawMethods.drawActionSymbol(gra, (Action.ActionType)map2.get(bp), positionP, bp, ratio);
                  }
              }
        }
    }

    public void repaintMap() {
        repaint();
        miniMap.repaint();
    }

    private  boolean inScreen(double dou1, double dou2) {
        return (dou1 >= sightX) && (dou2 >= sightY) && (dou1 < sightX + calViewWidth()) && (dou2 < sightY + calViewHeight());
    }

    private  boolean inScreen(BoardPoint bp) {
        return (bp._x >= sightX) && (bp._y >= sightY) && (bp._x + 1 <= sightX + calViewWidth()) && (bp._y + 1 <= sightY + calViewHeight());
    }

    public  void minimapScroll(double dou1, double dou2)
    {
      if (!inScreen(dou1, dou2))
         setViewPos(dou1 - calViewWidth() / 2.0D, dou2 - calViewHeight() / 2.0D);
    }

    public void positionUp() {
        if (player.mapArea.mapSet == null)
            position = null;
        else {
            position = new HashSet(player.mapArea.mapSet);
        }
        repaintMap();
    }

    public void mapUp() {
        gameBoard = player.mapArea.wholeMap;
        if (player.mapArea.mapSet == null)
            position = null;
        else {
            position = new HashSet(this.player.mapArea.mapSet);
        }
        miniMap.mapUp();
        repaintMap();
    }

    public void selectUpd() {
        choosePos = player.armyData.getSelectedLocation();
        chooseAct = player.armyData.getSelectedActions();
        repaintMap();
    }

    public void armyUp() {
        army = player.armyData.getAllArmies();
        sight = player.armyData.getSight();

        choosePos = player.armyData.getSelectedLocation();
        chooseAct = player.armyData.getSelectedActions();
        miniMap.armyUp();
        repaintMap();
    }

    public  double calViewWidth() {
        return getWidth() / scaleLvl;
    }
    public  double calViewHeight() {
        return getHeight() / scaleLvl;
    }

    public  void setViewPos(double dou1, double dou2) {
        sightX = dou1;
        sightY = dou2;
        if (gameBoard != null) {
          double windowWidth = getWidth() / scaleLvl; double windowHeight = getHeight() / scaleLvl;
          int i = gameBoard.length; int j = gameBoard[0].length;
        }
    }

    private class MapListener extends MouseInputAdapter
    {
        // mouse press point ---scroll map
        private Point pressPoint;
        private double px;
        private double py;

        private MapListener(){}

        public void mousePressed(MouseEvent e)
        {
            if (BoardPanel.this.gameBoard != null) {
                pressPoint = new Point(e.getX(), e.getY());
                px = BoardPanel.this.sightX;
                py = BoardPanel.this.sightY;
            }    
        }

        public  void mouseDragged(MouseEvent e) 
        {
            if (pressPoint != null) {
                setViewPos(px - (e.getX() - pressPoint.x) / BoardPanel.this.scaleLvl, py - (e.getY() - pressPoint.y) / BoardPanel.this.scaleLvl);
            }
            repaintMap();      
        }

        public void mouseReleased(MouseEvent e) {
                 pressPoint = null;
        }

        public void mouseMoved(MouseEvent e){}

        public void mouseClicked(MouseEvent e) {    
            int i = BoardPanel.this.scaleLvl;
            double d1 = BoardPanel.this.sightX; double d2 = BoardPanel.this.sightY;
            int j = Math.round((float)(-1.0D * d1 * i)); 
            int k = Math.round((float)(-1.0D * d2 * i));
            BoardPoint bp = new BoardPoint((e.getX() - j) / i, (e.getY() - k) / i);
            if (e.getClickCount() == 2 && BoardPanel.this.player.armyData.getArmy(bp)!=null){
                player.gameWindow.aef.setVisible(true);
                
            }
            if (BoardPanel.this.gameBoard != null) {
              if (bp != null)
                if ((bp._x >= 0) && (bp._x < BoardPanel.this.gameBoard.length) && (bp._y >= 0) && (bp._y < BoardPanel.this.gameBoard[bp._x].length))
                    BoardPanel.this.player.click(bp);
                else
                    BoardPanel.this.player.clickNot();
            }
        }
    }
    public class RadarPanel extends JPanel
    {
        private ControlPlayer player;
        private BoardPanel boardMap;
        private BufferedImage mapImg; //A predrawn image of the game board
        private Map<BoardPoint, Army> army;
        private BoardPoint newLocation;
        private int miniX;
        private int miniY;

        public RadarPanel(ControlPlayer cp, BoardPanel bp)
        {
              player = cp;
              boardMap = bp;
              setBackground(Color.BLACK);
              addMouseListener(new MouseAdapter(){ 
                public void mouseClicked(MouseEvent  e){        
                    int i = 0;
                    double d1 = 0.0D; 
                    double d2 = 0.0D;
                    if (e.getX() < 370 || e.getX() > 430|| e.getY() > 80 || e.getY() < 20)
                        return;
                    if (mapImg != null) {
                        i = 1;
                        d1 = (e.getX() - miniX) / 3;
                        d2 = (e.getY() - miniY) / 3;
                        System.out.println(e.getX() +"sd"+e.getY());
                        boardMap.minimapScroll(d1, d2);
                        boardMap.repaintMap();
                    }
                }
              });
        }

        public void mapUp() {
              if (player.mapArea.wholeMap == null) {
                  mapImg = null;
              } else {
                  mapImg = new BufferedImage(player.mapArea.wholeMap.length, player.mapArea.wholeMap[0].length, 2);
                  for (int i = 0; i < mapImg.getWidth(); i++) {
                        for (int j = 0; j < mapImg.getHeight(); j++)
                            mapImg.setRGB(i, j, player.drawMethods.getTerrainTileColor(player.mapArea.wholeMap[i][j]).getRGB());
                  }
              }
        }

        public void armyUp() {
              army = player.armyData.getAllArmies();  
              newLocation = player.armyData.getSelectedLocation();
        }
        
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            double mapX = boardMap.sightX;
            double mapY =boardMap.sightY;
            double WindowWidth =boardMap.calViewWidth();
            double windowHeight =boardMap.calViewHeight();

            int i = miniX = (getWidth()-mapImg.getWidth()*3)/2;
            int j = miniY = (getHeight()- mapImg.getHeight()*3)/2;
            BufferedImage map = mapImg;
            Map <BoardPoint, Army> armyMap = army;
            BoardPoint  bp = newLocation;
            
            if (map != null){
                //Draw the minimap
                g.drawImage(map, i, j, 3 * map.getWidth(), 3 * map.getHeight(), null);
                if (armyMap != null)
                {
                    int j1;
                    int l1;
                    for (Iterator it = armyMap.entrySet().iterator(); it.hasNext();)
                    {
                        Map.Entry entry = (Map.Entry) it.next();
                        BoardPoint bp1 = (BoardPoint)entry.getKey();
                        j1 = i + 3 * bp1._x;
                        l1 = j + 3 * bp1._y;
                        g.setColor(player.drawMethods.getPlayerColor(((Army)entry.getValue()).user));
                        g.drawRect(j1, l1, 3, 3);
                        g.setColor(Color.WHITE);
                        g.fillRect(j1+1, l1+1, 2, 2);
                    }
                }             
                // darw the white border
                g.setColor(Color.WHITE);
                int k = Math.round((float)(3 * mapX));
                int l = Math.round((float)(3 * mapY));
                int i1 = Math.round((float)(3 * (mapX + WindowWidth)));
                int k1 = Math.round((float)(3 * (mapY + windowHeight)));
                g.drawRect((i + k) - 1, (j + l) - 1, ((i1 - k) + 2) - 1, ((k1 - l) + 2) - 1);
            }
        }
    }
}