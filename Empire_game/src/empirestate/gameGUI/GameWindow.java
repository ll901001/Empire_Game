
package empirestate.gameGUI;

import empirestate.gameCore.ControlPlayer;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.data.Barrier2;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Toolkit;
import javax.swing.BorderFactory;

public class GameWindow extends JFrame{
    private ControlPlayer player;
    private GameEngine engine;
    public AddEquipmentFrame aef;

    public GameWindow(ControlPlayer cp){
        setTitle("Empire State");
        setSize(800, 600);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
        setResizable(false);
        setDefaultCloseOperation(0);
        
        player = cp;  
        aef = new  AddEquipmentFrame();
        aef.setLayout(new BorderLayout());

        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new BorderLayout());

        BoardPanel mapPanel = new BoardPanel(player);
        windowPanel.add(mapPanel,"Center");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());

        buttonPanel.add(player.buttCon, "South");
        windowPanel.add(buttonPanel,"South");
        add(windowPanel);
   
        JPanel miniMap = new JPanel();
        miniMap.setLayout(new BorderLayout());
        mapPanel.miniMap.setPreferredSize(new Dimension(100,100));
        miniMap.add(mapPanel.miniMap,"North");
        windowPanel.add(miniMap,"North");
 
        JPanel addEquipPanel = new JPanel();
        addEquipPanel.setLayout(new BorderLayout());

        AmryPicPanel app = new AmryPicPanel(player);
        app.setPreferredSize(new Dimension(200,160));
        addEquipPanel.add(app,"South");
        EquipPanel ep = new EquipPanel(player, app);
        ep.setPreferredSize(new Dimension(100,200));
        addEquipPanel.add(ep,"North");
        aef.add(addEquipPanel);

        player.setPanel(mapPanel, app, ep);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        addWindowListener(new WindowAdapter() { 
            public void windowClosing(WindowEvent e) { System.exit(0); } 
        });
    }

    public void start(GameEngine ge) {
        engine = ge;
        
    }
    
    public void nextBa(int i) {
        if (isVisible()) {
            if (i == 0){
                JOptionPane.showMessageDialog(this, "Player Win", "Barrier1 Success.", 1);
                JOptionPane.showMessageDialog(this, "Enter Barrier 2", "Barrier1 Success.", 1);
            }
            else
            JOptionPane.showMessageDialog(this, "AI Win", "Game over.", 1);
        }
    }
    
    public void end(int i) {
        if (isVisible()) {
            if (i == 0){
                JOptionPane.showMessageDialog(this, "Player Win", "Barrier1 Success.", 1);
            }
            else
            JOptionPane.showMessageDialog(this, "AI Win", "Game over.", 1);
        }
        System.exit(-1);
    }

    public void quit() 
    {
        System.out.println("quit");
        setVisible(false);
    }
    
    public class AddEquipmentFrame extends JFrame{
        private ControlPlayer player;
        private GameEngine engine;
        
        public AddEquipmentFrame (){
            super("Army Tactics");
            setSize(300, 400);
            setVisible(false);
            double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
            setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE);
        }
    }
}