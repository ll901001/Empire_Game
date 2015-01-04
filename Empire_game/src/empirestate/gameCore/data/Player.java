package empirestate.gameCore.data;
import empirestate.gameCore.BoardArea;
import empirestate.gameCore.BoardArea;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.BoardPoint;
import empirestate.gameCore.Equip;
import empirestate.gameCore.Equip;
import empirestate.gameCore.GameEngine;
import empirestate.gameCore.GameEngine;
import java.util.List;
import java.util.Map;
import java.util.Set;
// control game process
public abstract interface Player
{
    public abstract void start(GameEngine core, BoardArea mapData, int i1, int i2);

    public abstract void equipUp(List<Equip> equipList);

    public abstract void end(int i);

    public abstract void lost();

    public abstract void turnStart();

    public abstract void mapDataUp(BoardArea mapData);

    public abstract void mapUp(Map<BoardPoint, Integer> map, Set<BoardPoint> set, Action action);

    public abstract void updateTurn(int i);
}