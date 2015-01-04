package empirestate.gameCore;
import empirestate.gameAI.Barrier1AI;
import empirestate.gameAI.GameAI;
import empirestate.gameAI.demoAI;
import empirestate.gameCore.data.Action;
import empirestate.gameCore.data.Barrier1;
import empirestate.gameCore.data.Barrier2;
import empirestate.gameCore.data.Player;
import empirestate.gameCore.data.TestGame;
import empirestate.gameCore.data.demo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import empirestate.gameGUI.AmryPicPanel;
import empirestate.gameGUI.BoardPanel;
import empirestate.gameGUI.DrawingComponent;
import empirestate.gameGUI.EquipPanel;
import empirestate.gameGUI.GameWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
// control player army in the game board
// connect player with the user interface
public class ControlPlayer implements Player{
    private int playerId;
    public  DrawingComponent drawMethods;
    public  BoardArea mapArea;
    public  ArmyData armyData;
    private int armyNum;
    private BoardPanel gameMap;
    private AmryPicPanel armyImage;
    private EquipPanel equip;
    public ButtonsControl buttCon;
    public  GameWindow gameWindow;
    private GameEngine engine;
    private boolean ready;

    public ControlPlayer(){
        drawMethods = new DrawingComponent();
        mapArea = new BoardArea();
        armyData = new ArmyData(this);
        armyNum = 1;
        buttCon = new ButtonsControl(this);
        gameWindow = new GameWindow(this);
        ready = false;
    }

    public void setPanel(BoardPanel mp, AmryPicPanel app, EquipPanel ep) {
        gameMap = mp;
        armyImage = app;
        equip = ep;
    }

    public void start(GameEngine core, BoardArea map, int i1, int i2){
        playerId = i1;
        engine = core;

        mapArea.wholeMap = map.wholeMap;
        mapArea.mapSet = map.mapSet;

        armyData.start(engine);

        buttCon.start(engine.restArmies(this));
        gameWindow.setVisible(true);
        goldUp();
        mapUpd();
        armiesUp();
        gameMap.repaintMap();
    }

    public int playerID() {
        return playerId;
    }

    public void equipUp(List<Equip> list){
        equip.setEquip(list);
    }

    public void mapDataUp(BoardArea mapA){
        mapArea.wholeMap = mapA.wholeMap;
        mapArea.mapSet = mapA.mapSet;
        mapUpd();
    }

    public void mapUp(Map<BoardPoint, Integer> map, Set<BoardPoint> set, Action ac){
        armyData.setArmy(map);
        armyData.setSight(set);

        armiesUp();
        gameMap.repaintMap();
        goldUp();
    }

    public void turnStart(){
        goldUp();
        gameMap.repaintMap();
    }

    public void updateTurn(int ii){
        selectUpd();
        buttCon.turn(ii == playerId);
    }

    public void lost(){
        buttCon.lost();
    }

    public void end(int i){
        System.out.println("end");
        if(engine.mapStr.equals("maps/B1.map")&& i == 0 ){
            gameWindow.nextBa(i);
            gameWindow.setVisible(false);
            ControlPlayer cp = new ControlPlayer();
            Barrier2 b2 = new Barrier2();
            GameEngine ge = new GameEngine(b2.getMap(), b2.winCondition(), b2.equipment());
            ge.addPlayer(cp, "Local Player", b2.playerArmiesNumber(), b2.startingGold());
            ge.addPlayer(b2.gameAI(), "AI Player", 10, 11111111);
            ge.start();
            return;
        }
        gameWindow.end(i);
    }
    
    public void setEquip(Army army, int i, BoardPoint mapPoint) {
        if (army ==null)
            return;
        if (engine.updateArmy(this, army.thisArmyId, i, mapPoint)) {
            armyData.refreshArmy(army.thisArmyId);
            armiesUp();
            goldUp();
        }
    }

    public void click(BoardPoint bp) {
        Map actionArea = armyData.getSelectedActions();
        if ((actionArea != null) && (actionArea.containsKey(bp))) 
        {
              engine.armyAction(this, armyData.getSelectedID().intValue(), bp);
        } 
        else 
        {
            Army ar = armyData.getArmy(bp);
            if (ar != null) {
                selected(ar);
            }
            else if ((mapArea.mapSet != null) && (mapArea.mapSet.contains(bp))) {
                String str = "Army " + armyNum;
                int i = engine.armySet(this, bp, str);
                if (i != -1) {
                    armyNum += 1;
                    armyData.addArmy(bp, Integer.valueOf(i), true);
                    mapArea.mapSet.remove(bp);
                    armiesUp();
                    positionUp();
                    buttCon.updateToPlace(engine.restArmies(this));
                }
            } 
            else {
                armyData.deselect();
                selectUpd();
            }
        }   
    }

    public void clickNot(){
        armyData.deselect();
        selectUpd();
    }

    public void started() {
        mapArea.mapSet = null;
        positionUp();
        engine.gameReady(this);
        goldUp();
        buttCon.gameStart();
    }

    public void turnEnd() {
        engine.turnEnd(this);
    }

    public void closing() {
        engine.quit(this);
    }
    
    public void restartGame() {
        gameWindow.setVisible(false);
        gameWindow.aef.setVisible(false);
        ControlPlayer cp = new ControlPlayer();
        if(engine.mapStr.equals("maps/B1.map")){
            Barrier1 b1 = new Barrier1();
            Barrier1AI biAI = new Barrier1AI();
            GameEngine ge = new GameEngine(b1.gameMap(), b1.winCondition(), b1.equipment());
            ge.addPlayer(cp, "Local Player", b1.playerArmiesNumber(), b1.startingGold());
            ge.addPlayer(biAI, "Test Player 1", 20, 20000);
            ge.start();
        }
        if(engine.mapStr.equals("maps/B2.map")){
            Barrier2 level1 = new Barrier2();
            GameEngine ge = new GameEngine(level1.getMap(), level1.winCondition(), level1.equipment());
            ge.addPlayer(cp, "Local Player", level1.playerArmiesNumber(), level1.startingGold());
            ge.addPlayer(level1.gameAI(), "AI Player", 10, 11111111);
            ge.start();
        }
        if(engine.mapStr.equals("maps/test.map")){
            TestGame testGame = new TestGame();
            GameAI gameAI = new GameAI();
            GameEngine ge = new GameEngine(testGame.getMap(), testGame.getWinCondition(), testGame.getEquipment());
            ge.addPlayer(cp, "Player", testGame.getMaxArmies(), testGame.getStartingGold());
            ge.addPlayer(gameAI, "AI Player", 5, 2000);
            ge.start();     
        }
        if(engine.mapStr.equals("maps/demo.map")){
            demo gg = new demo();
            demoAI ai = new demoAI();
            GameEngine ge = new GameEngine(gg.gameMap(), gg.winCondition(), gg.equipment());
            ge.addPlayer(cp, "Local Player", gg.playerArmiesNumber(), gg.startingGold());
            ge.addPlayer(ai, "Test Player 1", 5, 2000);
            ge.start();
        }
    }
  
    public void selected(Army army) {
        armyData.setSelected(army);
        selectUpd();
    }

    private void goldUp() {
        buttCon.updateGold(engine.goldNumber(this));
        equip.updateGold(engine.goldNumber(this));
    }

    private void positionUp() {
        gameMap.positionUp();
    }

    private void mapUpd() {
        gameMap.mapUp();
    }

    private void selectUpd() {
        gameMap.selectUpd();
        armyImage.selectUpd();
    }

    private void armiesUp() {  
        gameMap.armyUp();      
        armyImage.selectUpd();
    }
    // controlled button in the game window
    public class ButtonsControl extends JPanel
    {
        private ControlPlayer player;
        private JButton start;
        private JButton endTurn;
        private JButton restart;
        private JPanel preStart;
        private JPanel after;
        private JLabel gold;
        private JLabel armyPlaceLabel;

        public ButtonsControl(ControlPlayer cp){
            player = cp;
            setLayout(new BorderLayout());
            ButtActionListener ButtActionL = new ButtActionListener();
            preStart = new JPanel();
            preStart.setLayout(new BorderLayout());
            preStart.setBackground(Color.BLACK);
            armyPlaceLabel = new JLabel("army to place:");
            armyPlaceLabel.setForeground(Color.WHITE);
            armyPlaceLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
            preStart.add(armyPlaceLabel, "West");
            start = new JButton("Start Game");
            start.addActionListener(ButtActionL);
            start.setEnabled(false);
            preStart.add(start, "East");
            add(preStart, "East");
            after = new JPanel();
            after.setLayout(new BorderLayout());
            after.setBackground(Color.BLACK);
            endTurn = new JButton("End Turn");
            endTurn.addActionListener(ButtActionL);
            endTurn.setEnabled(false);
            after.add(endTurn, "West");
            restart = new JButton("Restart");
            restart.addActionListener(ButtActionL);
            restart.setEnabled(false);
            after.add(restart, "East");
            gold = new JLabel("Gold:");
            gold.setBackground(Color.BLACK);
            gold.setForeground(new Color(255,215,0));
            gold.setOpaque(true);
            gold.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
            add(gold, "West");
            setBackground(Color.BLACK);
        }

        public void updateGold(int ii) {
            gold.setText("Gold: " + ii + "          Objective:     " + engine.winner.objectives());
            validate();
            repaint();
        }

        public void updateToPlace(int ii) {
            armyPlaceLabel.setText("Armies to place: " + ii);
            validate();
            repaint();
        }

        public void start(int ii) {
            start.setEnabled(true);
            updateToPlace(ii);
        }

        public void gameStart() {
            remove(preStart);
            add(after, "East");
            restart.setEnabled(true);
            validate();
            repaint();
        }

        public void turn(boolean bool) {
            endTurn.setEnabled(bool);
        }

        public void lost() {
            removeAll();
            validate();
            repaint();
        }
        private class ButtActionListener implements ActionListener {
            private ButtActionListener() {
            }
            public void actionPerformed(ActionEvent ae) { 
                if (ae.getSource() == ButtonsControl.this.start)
                    ButtonsControl.this.player.started();
                else if (ae.getSource() == ButtonsControl.this.endTurn)
                    ButtonsControl.this.player.turnEnd();
                else if (ae.getSource() == ButtonsControl.this.restart)
                    ButtonsControl.this.player.restartGame();
            }
        }
    }
}