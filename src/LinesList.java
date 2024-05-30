import java.util.ArrayList;

public class LinesList {

    private final boolean debugMode = false;

    private ArrayList<Point> linePoints = new ArrayList<>();
    private ArrayList<Double> lineSlopes = new ArrayList<>();
    private ArrayList<Point> verticalPoints = new ArrayList<>();

    public void addLine(double x, double y, double m) {
        Point point = new Point(x,y);
        linePoints.add(point);
        lineSlopes.add(m);

        if (debugMode) {
            System.out.println("The line with a slope of "+m+" that goes through point ("+point.getX()+","+point.getY()+") has been added to the linepoints and lineslopes!");
            System.out.println();
        }
    }

    public void addLine(double x, double y){
        Point point = new Point(x,y);
        verticalPoints.add(point);
    }

    public Point getPoint(int index){
        return linePoints.get(index);
    }

    public double getSlope(int index){
        return lineSlopes.get(index);
    }

    public Point getVert(int index){
        return verticalPoints.get(index);
    }

    public int getLinesLength(){
        return lineSlopes.size();
    }

    public int getVertLinesLength(){
        return verticalPoints.size();
    }

    public void clear() {
        linePoints.clear();
        lineSlopes.clear();
        verticalPoints.clear();
    }
}
