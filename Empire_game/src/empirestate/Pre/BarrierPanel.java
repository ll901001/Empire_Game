package empirestate.Pre;

import empirestate.gameAI.Barrier1AI;
import empirestate.gameCore.ControlPlayer;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.data.Barrier1;
import empirestate.gameCore.data.Barrier2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BarrierPanel extends JFrame{
    private JButton returnBtn;
    private Image level1;
    private Image level2;
    private Image lock;
    private Image backGroud;
    public BarrierPanel(){
        super("Barrier-- SELECT LEVEL");
        setSize(500, 400);
        setLayout(new BorderLayout());
        addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent  e){       
                if (e.getX()>30 && e.getX()< 160 && e.getY() <230 && e.getY()>130){
                    ControlPlayer controlPlayer = new ControlPlayer();
                    Barrier1 b1 = new Barrier1();
                    Barrier1AI biAI = new Barrier1AI();
                    GameEngine engine = new GameEngine(b1.gameMap(), b1.winCondition(), b1.equipment());
                    engine.addPlayer(controlPlayer, "Local Player", b1.playerArmiesNumber(), b1.startingGold());
                    engine.addPlayer(biAI, "Test Player 1", 20, 20000);
                    engine.start();
                    setVisible(false);
                }
                if (e.getX()>190 && e.getX()< 320 && e.getY() <230 && e.getY()>130){
                    ControlPlayer cp = new ControlPlayer();
                    Barrier2 b2 = new Barrier2();
                    GameEngine ge = new GameEngine(b2.getMap(), b2.winCondition(), b2.equipment());
                    ge.addPlayer(cp, "Local Player", b2.playerArmiesNumber(), b2.startingGold());
                    ge.addPlayer(b2.gameAI(), "AI Player", 10, 11111111);
                    ge.start();
                    setVisible(false);
                }
                if (e.getX()>360 && e.getX()< 440 && e.getY() <230 && e.getY()>130){
                    JOptionPane.showMessageDialog(null,"Do not Support","Locked",1);
                }
                
                System.out.println(e.getX()+" y  "+e.getY());
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e){}
            public void mouseEntered(MouseEvent e) {
      
            }
            public void mouseExited(MouseEvent e) {}
        });
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
        JLabel l1 = new JLabel("Barrier1");
        l1.setFont(new Font("Times", Font.BOLD, 18));
        l1.setForeground(Color.WHITE);
        l1.setBounds(55,200,100,50);
        JLabel l2 = new JLabel("Barrier2");
        l2.setFont(new Font("Times", Font.BOLD, 18));     
        l2.setForeground(Color.WHITE);
        l2.setBounds(210,200,100,50);
        JLabel l3 = new JLabel("Barrier3");
        l3.setFont(new Font("Times", Font.BOLD, 18));
        l3.setForeground(Color.WHITE);
        l3.setBounds(355,200,100,50);
        add(l1);
        add(l2);
        add(l3);
        try
        {
            level1 = Toolkit.getDefaultToolkit().createImage("pic/level1.jpg");
            level2 = Toolkit.getDefaultToolkit().createImage("pic/level2.jpg");
            lock = Toolkit.getDefaultToolkit().createImage("pic/lock.jpg");
        } catch (Exception e) {}
        JPanel levelPic = new JPanel(){  
            public void paint (Graphics g){
                super.paint(g);
                setBackground(Color.BLACK);
                g.drawImage(level1,20,100,null);
                g.drawImage(level2,180,100,null);
                g.drawImage(lock,350,100,null);
            }
        };    
        levelPic.setPreferredSize(new Dimension(400,400));
        add(levelPic,"Center");
        returnBtn = new JButton("Return");
        returnBtn.addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent  e){
                setVisible(false);
                MainMenu MainMeun = new MainMenu();
                MainMeun.setVisible(true);
            }
            public void mousePressed(MouseEvent e) {
            }
            public void mouseReleased(MouseEvent e) {
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        });
        returnBtn.setLocation(400, 400);
        returnBtn.setPreferredSize(new Dimension(100,30));
        JPanel but = new JPanel();
        but.add(returnBtn);
        add(but,"South");
        levelPic.setBackground(Color.BLACK);
        but.setBackground(Color.BLACK);
    }
}
