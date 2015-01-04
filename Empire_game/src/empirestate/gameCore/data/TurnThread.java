package empirestate.gameCore.data;

import empirestate.gameCore.data.Player;

public class TurnThread extends Thread
{
    Player pp;

    public TurnThread(Player player){
        pp = player;
    }

    public void run(){
        pp.turnStart();
    }
}