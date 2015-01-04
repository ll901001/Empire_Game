
package empirestate.gameGUI;

import empirestate.gameCore.ControlPlayer;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;
import empirestate.gameCore.Equip;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import empirestate.gameCore.Equip;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;

public class EquipPanel extends JPanel{
    private EquipList equipList;
    private JButton previous;
    private JButton next;
    private JTextArea equipInf;
    private int gold;
    private AmryPicPanel armyPic;

    public EquipPanel(ControlPlayer cp, AmryPicPanel app){
        setLayout(new BorderLayout());

        armyPic = app;

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        equipList = new EquipList(cp);
        listPanel.add(equipList, "Center");

        EquipChangeListener equipChange = new EquipChangeListener();

        previous = new JButton("Previous");
        previous.addActionListener(equipChange);
        listPanel.add(previous, "North");

        next = new JButton("Next");
        next.addActionListener(equipChange);
        listPanel.add(next, "South");
        listPanel.setPreferredSize(new Dimension(150,100));
        add(listPanel,"West");

        TextEquip equiptext = textDisplay(5);
        equipInf = equiptext.textArea;
        equiptext.jsp.setBorder(BorderFactory.createTitledBorder(null, "Equipments", 2, 0));
        equiptext.jsp.setPreferredSize(new Dimension(130,100));
        add(equiptext.jsp,"East");

        previous.setEnabled(false);
        next.setEnabled(false);
    }

    public void setEquip(List<Equip> listE) {
    
        equipList.addEquipment(listE);

        equipInf.setText(getEquipString(this.equipList.getEquipment()));
        equipInf.setCaretPosition(0);

        previous.setEnabled((listE != null) && (listE.size() > 1));
        next.setEnabled((listE != null) && (listE.size() > 1));
        updateArmyIma();
        repaint();
    }

    public void updateGold(int ii) {
        gold = ii;
        updateArmyIma();
    }

    private void updateArmyIma() {
        Equip equip = equipList.getEquipment();
        if ((equip != null) && (equip.cost <= this.gold))
            armyPic.equipmentSelected(equipList.getEquipmentID().intValue(), equipList.getEquipment());
        else
            armyPic.noSelected();
    }

    private String getEquipString(Equip equip)
    {
        if (equip == null) {
            return "";
        }
        return equip.name + "\nCost: " + equip.cost + "\n" + equip.effects;
    }
    
    public  class TextEquip {
        public JScrollPane jsp;
        public JTextArea textArea;

        public TextEquip(JScrollPane JSpane, JTextArea text) {
            jsp = JSpane;
            textArea = text;
        }
    }
    
    private class EquipChangeListener implements ActionListener
    {
        private EquipChangeListener()
        {
        }

        public void actionPerformed(ActionEvent ae)
        {
            Object event = ae.getSource();
            if (event == EquipPanel.this.previous)
                EquipPanel.this.equipList.scrollUp();
            else if (event == EquipPanel.this.next)
                EquipPanel.this.equipList.scrollDown();
            else  return;
            EquipPanel.this.equipInf.setText(EquipPanel.this.getEquipString(EquipPanel.this.equipList.getEquipment()));
            EquipPanel.this.equipInf.setCaretPosition(0);
            EquipPanel.this.updateArmyIma();
            EquipPanel.this.repaint();
        }
    }
    public TextEquip textDisplay(int ii)
    {
        JTextArea textArea = new JTextArea();
        textArea.setRows(ii);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane jsp = new JScrollPane(textArea, 20, 31);
        return new TextEquip(jsp, textArea);
    }
    
    public class EquipList extends JPanel
    {
        ControlPlayer player;
        private Equip[] equipArray;
        private int index;

        public EquipList(ControlPlayer cp)
        {
            player = cp;
            setBackground(Color.BLACK);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Equip[] equipA;
            int k;
            synchronized (this)
            {
                equipA = equipArray;
                k = index;
            }
            if (equipA != null && equipA.length > 0)
            {
                Equip equip = equipA[k];
                int l = Math.min(getWidth() / 5, getHeight() / 5);
                int k1 = (getWidth() - 150) / 2;
                int l1 = (getHeight() - 150) / 2;
                player.drawMethods.drawEquip(g.create(k1, l1, 200, 200), equip, l);
                
            }
        }

        public  void addEquipment(List<Equip> equipL)
        {
            int k;
            if (equipL == null) {
                equipArray = null;
                index = 0;
            } else {
                equipArray = new Equip[equipL.size()];
                index = 0;
                k = 0;
                for (Equip eq : equipL) {
                  equipArray[k++]  = eq;
                }
            }
        }

        public  Equip getEquipment() {
            if ((equipArray == null) || (equipArray.length == 0)) {
                return null;
            }
            return equipArray[index];
        }

        public  Integer getEquipmentID() {
            if ((equipArray == null) || (equipArray.length == 0)) {
                return null;
            }
            return Integer.valueOf(index);
        }

        public  void scrollUp() {
            scroll(index - 1);
        }

        public  void scrollDown() {
            scroll(index + 1);
        }

        public  void scrollTo(int ii) {
            scroll(ii);
        }

        public  void scrollTo(double dou) {
            if ((equipArray != null) && (equipArray.length > 0))
                scroll(Math.round((float)dou * equipArray.length));
        }
        // scroll the equipment accoring to index
        private  void scroll(int ii) {
            if ((equipArray != null) && (equipArray.length > 0)) {
                index = (ii % equipArray.length);
                if (index < 0)
                    index += equipArray.length;
            }
        }
    }
}