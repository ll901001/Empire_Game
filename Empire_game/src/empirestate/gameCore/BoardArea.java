
package empirestate.gameCore;
import java.util.HashSet;
import java.util.Set;

public class BoardArea
{
    public int[][] wholeMap;
    public Set<BoardPoint> mapSet;
    // get a set of boardpoint 
    public BoardArea(int[][] boardMap, Set<BoardPoint> startLocations)
    {
        mapSet = new HashSet();
        for (BoardPoint mp : startLocations)
        {
            mapSet.add(new BoardPoint(mp));
        } 
        // save map
        wholeMap = new int[boardMap.length][boardMap[0].length];
        for (int i = 0; i < boardMap.length; i++)
        {
            for (int j = 0; j < boardMap[i].length; j++)
            {
                wholeMap[i][j] = boardMap[i][j];
            }
        }
    }

    public BoardArea()
    {
        wholeMap = ((int[][])null);
        mapSet = null;
    }
}