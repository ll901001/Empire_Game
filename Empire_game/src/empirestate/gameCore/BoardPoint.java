
package empirestate.gameCore;


public class BoardPoint  {
    public int _x;
    public int _y;
    
    public BoardPoint(){}
    public BoardPoint(int x, int y){
        _x = x;
        _y = y;
    }

    public BoardPoint(BoardPoint mapPoint){
        _x = mapPoint._x;
        _y = mapPoint._y;
    }

    public int hashCode(){
        return 31 * _x + _y;
    }

    public boolean equals(Object ob){
        return (ob != null) && ((ob instanceof BoardPoint)) && (((BoardPoint)ob)._x == _x) && (((BoardPoint)ob)._y == _y);
    }
}