
package empirestate.gameGUI;

import empirestate.gameCore.data.Action;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;
import empirestate.gameCore.data.Action.ActionType;
import empirestate.gameCore.Block;
import empirestate.gameCore.Block.Property;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.Army;
import empirestate.gameCore.data.GameData;
// most of the methods' name show its function.
public class DrawingComponent
{
    public static  Color plainColor = Color.GRAY;
    public static  Color waterColor = new Color(0, 0, 191);
    public static  Color attackbaseColor = new Color(126, 72, 19);
    public static  Color defenbaseColor = new Color(63, 63, 63);
    public static  Color goldmineColor = new Color(255, 215, 0);
    private static final Color[] playerColor = { Color.BLUE, Color.RED};
    // Set different Color to Player according to its playerID
    public Color getPlayerColor(int i){
        if ((i >= 0) && (i < playerColor.length)) {
            return playerColor[i];
        }
        return Color.WHITE;
    }
    // Set each tile’s color in map according to its terrain reference int.
    public Color getTerrainTileColor(int ii) {
        if (GameData.hasTerrain(ii, 16))
            return Color.BLACK;
        if (GameData.hasTerrain(ii, 1))
            return waterColor;
        if (GameData.hasTerrain(ii, 2))
            return defenbaseColor;
        if (GameData.hasTerrain(ii, 4))
            return attackbaseColor;
        if (GameData.hasTerrain(ii, 8)) {
            return goldmineColor;
        }
        return plainColor;
    }
    // Set equipment colour
    public Color getEquipmentColor(Block.Property equipList) {
        switch (equipList.ordinal()) {
        case 1:
            return Color.YELLOW;
        case 2:
            return new Color(127, 0, 255);
        case 3:
            return new Color(232, 139, 0);
        case 4:
            return new Color(191, 191, 0);
        case 5:
            return Color.LIGHT_GRAY;
        case 6:
            return new Color(150, 75, 0);
        case 7:
            return Color.RED;
        case 8:
            return new Color(245, 245, 220);
        }
        return Color.GREEN;
    }
    // Set symbol’s color to action as design part shown
    public Color getActionTypeColor(Action.ActionType at) {
        if (at == Action.ActionType.MOVE)
            return Color.GREEN;
        if (at == Action.ActionType.ATTACK) 
        {
            return Color.RED;
        }
        return Color.WHITE;
    }
    public Color getTileColor(Block tile){
        int i = 0; int j = 0; int k = 0;
        for (Block.Property proper : tile.equip.keySet()) {
            Color c = getEquipmentColor(proper);
            i += c.getRed();
            j += c.getGreen();
            k += c.getBlue();
        }
        int m = tile.equip.size();
        if (m > 0) {
            i /= m;
            j /= m;
            k /= m;
        }
        return new Color(i, j, k);
    }
    public Color getTileAfterAttackColor(Block tile) {
        Color tileColor = getTileColor(tile);

        double d = tile.life / tile.totalLife;
        if (d < 0.0D)
            d = 0.0D;
        if (d > 1.0D) {
            d = 1.0D;
      }
        return new Color((int)(tileColor.getRed() * d), (int)(tileColor.getGreen() * d), (int)(tileColor.getBlue() * d));
    }
    // draw the special terrains’ graph.  
    //Specially, drawing the triangle need firstly construct a polygon based on three points.
    public void drawTerrain(Graphics gg, int i1, int i2){
        int i = i2;
        int k = i2;

        if (GameData.hasTerrain(i1, 1)) {
            gg.setColor(waterColor);
            gg.fillRect(0, 0, i2, i2);
        }

        if (GameData.hasTerrain(i1, 4)) {
            gg.setColor(attackbaseColor);
            gg.fillPolygon(new Polygon(new int[] { 0,  i / 2, k }, new int[] { k,0 , k }, 3));
        }

        if (GameData.hasTerrain(i1, 2)) {
            gg.setColor(defenbaseColor);
            gg.fillPolygon(new Polygon(new int[] { 0, i / 2, k }, new int[] { 0, k, 0 }, 3));
        }

        if (GameData.hasTerrain(i1, 8)) {
            gg.setColor(goldmineColor);
            gg.fillOval(i/8, i/8, i*3/4, i*3/4);
        }

        if (GameData.hasTerrain(i1, 16)) {
            gg.setColor(new Color(255, 63, 0));
            gg.fillRect(0, 0, i2, i2);
        }
    }
    
    public Color startingAreaColor(int ii, boolean bool) {
        if (bool) {
            Color cc = getPlayerColor(ii);
            return new Color((255 + cc.getRed()) / 2, (255 + cc.getGreen()) / 2, (255 + cc.getBlue()) / 2, 127);
        }
        return new Color(0, 0, 0);
    }
    
    public void drawTerrainTile(Graphics gg, int i1, int i2, boolean bool) {
        gg.setColor(plainColor);
        gg.fillRect(0, 0, i2, i2);
        drawTerrain(gg, i1, i2);
        if (bool) {
            gg.setColor(new Color(128, 128, 128));
            gg.drawRect(0, 0, i2 - 1, i2 - 1);
        }
    }

    public Color fogofWarColor(boolean bool) {
        if (bool) {
            return new Color(0, 0, 0, 0);
        }
        return new Color(0, 0, 0, 255);
    }
    // draw fog of war
    public void drawFOW(Graphics gg, boolean bool, int ii) {
        gg.setColor(fogofWarColor(bool));
        gg.fillRect(0, 0, ii, ii);
    }

    public void drawTile(Graphics gg, Block tile, int ii) {
        gg.setColor(getTileAfterAttackColor(tile));
        gg.fillRect(0, 0, ii, ii);
    }
    // starting area shadow
    public void drawStartingFOW(Graphics gg, int i1, Set<BoardPoint> set1, BoardPoint bp, int i2) {
        if (set1.contains(bp)) {
          gg.setColor(startingAreaColor(i1, true));
          int i = 5;
        } else {
            gg.setColor(startingAreaColor(i1, false));
            gg.fillRect(0, 0, i2, i2);
        }
    }
        
    public void drawBlockFeature(Graphics gg, int ii) {
        int i = ii/6;
        System.out.println(i);
        if (i > 0)
        {
            gg.setColor(new Color(0, 0, 0, 100));
            for (int j = 0; j < i; j++) {
                  gg.drawLine(j + 1, ii - 1 - j, ii - 1, ii - 1 - j);
                  gg.drawLine(ii - 1 - j, j + 1, ii - 1 - j, ii - 1 - i);
            }
        }
    }

    public BufferedImage getTileShadow(int ii) {
        BufferedImage bi = new BufferedImage(ii, ii, 2);
        drawBlockFeature(bi.getGraphics(), ii);
        return bi;
    }

    public void drawArmy(Graphics gg, Army army, int i1, int i2) {
        int i = 10 * i1;
        int j = (i2 - i) / 2;
        gg.setColor(Color.WHITE);
        gg.fillRect(j, j, i, i);

        for (BoardPoint bp : army.tile.keySet()) {
            Block ti = (Block)army.tile.get(bp);
            gg.setColor(getTileAfterAttackColor(ti));
            gg.fillRect(j + (bp._x + 1) * i1, j + (bp._y + 1) * i1, i1, i1);
        }
        // border drawing 
        gg.setColor(getPlayerColor(army.user));
        gg.fillRect(j, j, i, i1);
        gg.fillRect(j, j + i - i1, i, i1);
        gg.fillRect(j, j + i1, i1, i - 2 * i1);
        gg.fillRect(j + i - i1, j + i1, i1, i - 2 * i1);
    }
    
    public void drawEquip(Graphics gg, Equip equip, int ii) {
        
        int k = 1;  int m = 1 ;
        BufferedImage bi = new BufferedImage(ii, ii, 2);
        drawBlockFeature(bi.getGraphics(), ii);

        for (BoardPoint bp : equip.tile.keySet()) {
            Block block = (Block)equip.tile.get(bp);
            int n = (k + bp._x) * ii; int i1 = (m + bp._y) * ii;
            gg.setColor(getTileColor(block));
            gg.fillRect(n, i1, ii, ii);
            gg.drawImage(bi, n, i1, ii, ii, null);
        }
    }
    // draw action symbol based on direction and action type
    public void drawActionSymbol(Graphics gg, Action.ActionType at, BoardPoint bp1, BoardPoint bp2, int ii) {
        gg.setColor(getActionTypeColor(at));
        int i = bp2._x - bp1._x; int j = bp2._y - bp1._y;
        double d1;
        if (i == 0) 
        {
            if (j > 0)
              d1 = Math.PI/2;
            else
              d1 = 3 * Math.PI/2;
          } else {
              d1 = Math.atan(j / i);
              if (i < 0)
                  d1 += Math.PI;
        }
        double d2 = d1 + 2 * Math.PI/3;
        double d3 = d1 - 2 * Math.PI/3;

        double d4 = ii / 2.0D;
        int[] a1 = { Math.round((float)(Math.cos(d1) * d4 + d4)), Math.round((float)(Math.cos(d2) * d4 + d4)), Math.round((float)(Math.cos(d3) * d4 + d4))};
        int[] a2 = { Math.round((float)(Math.sin(d1) * d4 + d4)), Math.round((float)(Math.sin(d2) * d4 + d4)),Math.round((float)(Math.sin(d3) * d4 + d4))};
        gg.fillPolygon(new Polygon(a1, a2, 3));
    }
}