package empirestate.Pre;

import empirestate.gameAI.GameAI;
import empirestate.gameAI.demoAI;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.data.TestGame;
import empirestate.gameCore.ControlPlayer;
import empirestate.gameCore.data.demo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class MainMenu extends JFrame 
{
    private JButton barrierBtn;
    private JButton quickBtn;
    private JButton demoBut;
    private JButton quitBtn;
    private Image   backGroud;
    
    public MainMenu ()
    {
        super("Main Menu");
        setSize(480,410);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setLocation( (int) (width - this.getWidth()) / 2,(int) (height - this.getHeight()) / 2);
        try
        {
            backGroud = Toolkit.getDefaultToolkit().createImage("pic/backgroud.jpg");
        } catch (Exception e) {
            System.err.println("Could not load campaign icons.");
        }
        JPanel mainJp = new JPanel(){
            public void paint(Graphics g) {
                g.drawImage(backGroud, 0, 0, null);     
            }
        };
        mainJp.setLayout(new BoxLayout(mainJp, 1));
        
        JPanel buttonJP = new JPanel(); 
        buttonJP.setBackground(Color.BLACK);
        barrierBtn = new JButton("Barrier Mode ");
        quickBtn = new JButton("Normal Mode");
        demoBut = new JButton("Tutorial");
        quitBtn = new JButton("Quit");
        
        barrierBtn.setAlignmentX(1F);
        barrierBtn.addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent  e){
                setVisible(false);
                BarrierPanel barrier = new BarrierPanel();
                barrier.setVisible(true);
                System.out.println("鼠标点击");
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
        quickBtn.setAlignmentX(1F);
        quickBtn.addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent  e){                
                setVisible(false);
                startRandom();
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
        demoBut.setAlignmentX(1F);
        demoBut.addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent  e){
                setVisible(false);
                startDemo();
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
        quitBtn.setAlignmentX(1F);
        quitBtn.addMouseListener(new MouseListener(){ 
            public void mouseClicked(MouseEvent  e){
                System.exit(-1);
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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
        addWindowListener(new WindowAdapter() { 
            public void windowClosing(WindowEvent e) { System.exit(0); } 
        });
        barrierBtn.setPreferredSize(new Dimension(120, 30));
        quickBtn.setPreferredSize(new Dimension(120, 30));
        demoBut.setPreferredSize(new Dimension(80, 30));
        quitBtn.setPreferredSize(new Dimension(80, 30));

        buttonJP.add(Box.createVerticalGlue());
        buttonJP.add(barrierBtn);
        buttonJP.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonJP.add(quickBtn);
        buttonJP.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonJP.add(demoBut);
        buttonJP.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonJP.add(quitBtn);
        buttonJP.add(Box.createVerticalGlue());
        buttonJP.add(Box.createRigidArea(new Dimension(0, 50)));

        add(mainJp, "Center");
        add(buttonJP, "South");
        
    }
    public void startRandom()
    {
        ControlPlayer localGUIPlayer = new ControlPlayer();
        TestGame randomGame = new TestGame();
        GameAI ai = new GameAI();
        GameEngine ge = new GameEngine(randomGame.getMap(), randomGame.getWinCondition(), randomGame.getEquipment());
        ge.addPlayer(localGUIPlayer, "Local Player", randomGame.getMaxArmies(), randomGame.getStartingGold());
        ge.addPlayer(ai, "Test Player 1", 5, 2000);
        ge.start();
    }
    public void startDemo(){
        ControlPlayer localGUIPlayer = new ControlPlayer();
        demo gg = new demo();
        demoAI ai = new demoAI();
        GameEngine ge = new GameEngine(gg.gameMap(), gg.winCondition(), gg.equipment());
        ge.addPlayer(localGUIPlayer, "Local Player", gg.playerArmiesNumber(), gg.startingGold());
        ge.addPlayer(ai, "Test Player 1", 5, 2000);
        ge.start();
    }
    
    public static void main(String[] paramArrayOfString)
    {
        MainMenu MainMeun = new MainMenu();
        MainMeun.setVisible(true);
    }
}