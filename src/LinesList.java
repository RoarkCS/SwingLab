import java.util.ArrayList;

public class LinesList {

    private final boolean debugMode = true;

    private ArrayList<int[]> linePoints = new ArrayList<>();
    private ArrayList<Double> lineSlopes = new ArrayList<>();
    private ArrayList<int[]> verticalPoints = new ArrayList<>();

    public void addLine(int x, int y, double m) {
        int[] point = {x, y};
        linePoints.add(point);
        lineSlopes.add(m);

        if (debugMode) {
            System.out.println("The line with a slope of "+m+" that goes through point ("+point[0]+","+point[1]+") has been added to the linepoints and lineslopes!");
        }
    }

    public void addLine(int x, int y){
        int[] point = {x, y};
        verticalPoints.add(point);
    }

    public int[] getPoint(int index){
        return linePoints.get(index);
    }

    public double getSlope(int index){
        return lineSlopes.get(index);
    }

    public int[] getVert(int index){
        return verticalPoints.get(index);
    }

    public int getLinesLength(){
        return lineSlopes.size();
    }

    public int getVertLinesLength(){
        return verticalPoints.size();
    }
}
