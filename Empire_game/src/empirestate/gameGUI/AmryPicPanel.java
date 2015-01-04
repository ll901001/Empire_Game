package empirestate.gameGUI;
import empirestate.gameCore.ControlPlayer;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import empirestate.gameCore.Block;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.Army;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class AmryPicPanel extends JPanel{
    private ControlPlayer player;
    private Army selectedArmy;
    private int armyNO;
    private int scale;    
    private int _ox;
    private int _oy;
    private boolean shadow;
    private Equip equipment;
    private int index;
    private BoardPoint position;

    public AmryPicPanel(ControlPlayer cp){
        player = cp;
        position = new BoardPoint(0, 0);
        addMouseListener(new MouseAdapter(){    
            public void mouseClicked(MouseEvent e) {
                if ((shadow) && (equipment != null))
                       player.setEquip(selectedArmy, index, position);
            }
            public void mouseExited(MouseEvent e) {
                shadow = false;
                repaint();
            }
        });
        addMouseMotionListener(new MouseAdapter(){
            public void mouseMoved(MouseEvent e) {
                equipmentPosition(e);
            }
        });
        setBackground(Color.BLACK);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int i = 8;
        int j = scale = Math.min(getWidth() / 12, getHeight() / 12);
        int k = j * i;
        int l = _ox = (getWidth() - k) / 2;
        int i1 = _oy = (getHeight() - k) / 2;
        boolean moving = shadow;
        Equip eq = equipment;
        BoardPoint bp = position;
        Army army = selectedArmy;
  
        if (army != null){
            g.setColor(Color.WHITE);
            g.fillRect(l, i1, k, k);
            BoardPoint bp1;
            for (Iterator it = army.tile.entrySet().iterator(); it.hasNext();)
            { 
                Entry entry = (Entry)it.next();
                bp1 = (BoardPoint)entry.getKey();
                g.setColor(player.drawMethods.getTileAfterAttackColor((Block)entry.getValue()));
                g.fillRect(l + bp1._x * j, i1 + bp1._y * j, j, j); 
                //fangge
                g.drawImage(player.drawMethods.getTileShadow(j), l + bp1._x * j, i1 + bp1._y * j, j, j, this);
            }
            // show equipment moving in the army's internal space 
            if (army.user == player.playerID() && eq != null && moving)
            {
                Iterator it1 = eq.tile.entrySet().iterator();
                do{
                    if (!it1.hasNext())
                            break;
                    Entry entry1 = (Entry)it1.next();
                    BoardPoint bp2 = new BoardPoint((BoardPoint)entry1.getKey());
                    bp2._x += bp._x;
                    bp2._y += bp._y;
                    if (bp2._x >= 0 && bp2._x < i && bp2._y >= 0 && bp2._y < i){
                        g.setColor(player.drawMethods.getTileAfterAttackColor((Block)entry1.getValue()));
                        g.fillRect(l + bp2._x * j, i1 + bp2._y * j, j, j);
                    }
                } while (true);
            }
            // draw border
            g.setColor(player.drawMethods.getPlayerColor(army.user));
            g.fillRect(l - j, i1 - j, k + 2 * j, j);
            g.fillRect(l - j, i1 + k, k + 2 * j, j);
            g.fillRect(l - j, i1, j, k);
            g.fillRect(l + k, i1, j, k);
        }
    }

    public void selectUpd() {

        selectedArmy = player.armyData.getSelectedArmy();
        repaint();
    }

    public  void noSelected() {
        equipment = null;
        repaint();
    }

    public  void equipmentSelected(int ii, Equip eq) {
        equipment = eq;
        index = ii;
        repaint();
    }

    private void equipmentPosition(MouseEvent e) {
        shadow = true;
        if ((selectedArmy != null) && (equipment != null)) {
            position._x = (int) Math.floor((e.getX() - _ox) / scale);
            position._y = (int) Math.floor((e.getY() - _oy) / scale);
            repaint();
        }
    }
}