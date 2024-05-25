import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public class GraphPanel extends JPanel {

    private final boolean debugMode = true;

    private ArrayList<Point> data = new ArrayList<>();
    private Point cameraPos = new Point(0,0);
    private double scale = 10;
    private static final int HEIGHT = 500, WIDTH = 500;
    private boolean drawTicks = true;
    private int tickFreq = 10, tickLen = 10;
    private int pointRadius = 2;
    private boolean autoCam = false;
    private boolean autoTickFreq = false;
    private int autoCamPadding = 10;
    private double leftEdgeRealX, rightEdgeRealX, upEdgeRealY, downEdgeRealY;
    private LinesList linesList = new LinesList();

    public GraphPanel(){
        autoCam = true;
        autoTickFreq = true;
    }

    public GraphPanel(int[] X, int[] Y){
        this(X, Y, 10, 10, 10);
        autoCam = true;
        autoTickFreq = true;
    }

    public GraphPanel(int[] X, int[] Y, int camX, int camY, int scale){
        for (int i = 0; i < X.length; i++) {
            data.add(new Point(X[i],Y[i]));
        }

        cameraPos.setX(camX);
        cameraPos.setY(camY);
        this.scale = scale;
        setBackground(Color.WHITE);
        updateEdges();
    }

    public GraphPanel(int[] X, int[] Y, int quadrant){
        for (int i = 0; i < X.length; i++) {
            data.add(new Point(X[i],Y[i]));
        }

        if (quadrant == 1) {
            cameraPos.setX(10);
            cameraPos.setY(10);
        } else if (quadrant == 2) {
            cameraPos.setX(-10);
            cameraPos.setY(10);
        } else if (quadrant == 3) {
            cameraPos.setX(-10);
            cameraPos.setY(-10);
        } else if (quadrant == 4) {
            cameraPos.setX(10);
            cameraPos.setY(-10);
        }

        setBackground(Color.WHITE);
        updateEdges();
    }

    public PointInt convertToViewPort(Point realCoords) {
        int viewportX = (int) Math.round((realCoords.getX() - cameraPos.getX()) * scale + (double) WIDTH / 2);
        int viewportY = (int) Math.round((realCoords.getY() - cameraPos.getY()) * -scale + (double) HEIGHT / 2);
        return new PointInt(viewportX,viewportY);
    }

    public Point convertToReal(Point viewportCoords) {
        double realX = (viewportCoords.getX() - (double) WIDTH / 2) / scale + cameraPos.getX();
        double realY = (viewportCoords.getY() - (double) HEIGHT / 2) / -scale + cameraPos.getY();
        return new Point(realX, realY);
    }

    private void autoCam() {
        if(data.isEmpty()){
            return;
        }

        double xMin = Double.MAX_VALUE;
        double xMax = Double.MIN_VALUE;
        double yMin = Double.MAX_VALUE;
        double yMax = Double.MIN_VALUE;

        for (Point datum : data) {
            xMin = Math.min(xMin, datum.getX());
            xMax = Math.max(xMax, datum.getX());
            yMin = Math.min(yMin, datum.getY());
            yMax = Math.max(yMax, datum.getY());
        }

        double xRange = xMax - xMin;
        double yRange = yMax - yMin;

        scale = (int) Math.round(Math.min(WIDTH / (xRange + autoCamPadding), HEIGHT / (yRange + autoCamPadding)));
        cameraPos.setX(xMin + xRange / 2);
        cameraPos.setY(yMin + yRange / 2);
        updateEdges();
    }

    private void updateEdges() {
        leftEdgeRealX = convertToReal(new Point(0, 0)).getX();
        rightEdgeRealX = convertToReal(new Point(WIDTH, 0)).getX();
        upEdgeRealY = convertToReal(new Point(0, 0)).getY();
        downEdgeRealY = convertToReal(new Point(0, HEIGHT)).getY();
    }

    private void autoTick() {
        if(data.isEmpty()){
            tickFreq = 10;
        }

        double xRange = rightEdgeRealX - leftEdgeRealX;
        double yRange = upEdgeRealY - downEdgeRealY;

        int numTicksOnScreen = 10;
        int xTickFreq = (int) Math.ceil( xRange / numTicksOnScreen);
        int yTickFreq = (int) Math.ceil( yRange / numTicksOnScreen);
        tickFreq = Math.max( Math.max( xTickFreq , yTickFreq ) , 1 );
    }

    private boolean onViewPort(Point point) {
        if (convertToViewPort(point).getX() >= 0 && convertToViewPort(point).getX() <= WIDTH){
            return convertToViewPort(point).getY() >= 0 && convertToViewPort(point).getY() <= HEIGHT;
        }
        return false;
    }

    private void plotPoint(Graphics g, Point point){
        if(onViewPort(point)){
            PointInt viewPortPoint = convertToViewPort(point);
            g.setColor(Color.blue);
            g.fillOval(viewPortPoint.getX()-pointRadius,viewPortPoint.getY()-pointRadius,pointRadius*2,pointRadius*2);
            g.setColor(Color.black);
        }
    }

    private void plotPoints(Graphics g){
        for (Point point : data) {
            plotPoint(g, point);
        }
    }

    private void connectPoints(Graphics g){
        for (int i = 0; i < data.size()-1; i++) {
            Point point1 = data.get(i);
            Point point2 = data.get(i+1);

            if(onViewPort(point1) || onViewPort(point2)){
                PointInt pointA = convertToViewPort(point1);
                PointInt pointB = convertToViewPort(point2);

                g.setColor(Color.blue);
                g.drawLine(pointA.getX(),pointA.getY(),pointB.getX(),pointB.getY());
                g.setColor(Color.black);
            }
        }
    }

    private static Optional<Point> intersectionHorizontal(double y, double m, double xi, double yi) {
        if (m == 0) {
            return Optional.empty();
        }
        return Optional.of(new Point(((y - yi) / m) + xi, y));
    }

    private static Point intersectionVertical(double x, double m, double xi, double yi) {
        return new Point(x, (int) Math.round(m * (x - xi) + yi));
    }

    private void drawLineViewPort(Graphics g, Point point1, Point point2){
        PointInt pointA = convertToViewPort(point1);
        PointInt pointB = convertToViewPort(point2);

        if(debugMode){
            System.out.println("Drawing line from: "+point1+" to "+point2);
            System.out.println("Drawing line from: "+pointA+" to "+pointB);
            System.out.println();
        }

        g.drawLine(pointA.getX(),pointA.getY(),pointB.getX(),pointB.getY());
    }

    private void drawLineThroughPoint(Graphics g, Point point, double m){
        if(Math.abs(m-0.1) < 0.000001 || Math.abs(m-0.3) < 0.000001 || Math.abs(m-0.5) < 0.000001 || Math.abs(m-0.7) < 0.000001 || Math.abs(m-0.9) < 0.000001){
            m += 0.00001;
        }

        Optional<Point> top = intersectionHorizontal(upEdgeRealY, m, point.getX(), point.getY());
        Optional<Point> bottom = intersectionHorizontal(downEdgeRealY, m, point.getX(), point.getY());
        Point left = intersectionVertical(leftEdgeRealX, m, point.getX(), point.getY());
        Point right = intersectionVertical(rightEdgeRealX, m, point.getX(), point.getY());

        boolean isTop = top.isPresent() && onViewPort(top.get());
        boolean isBottom = bottom.isPresent() && onViewPort(bottom.get());
        boolean isLeft = onViewPort(left);
        boolean isRight = onViewPort(right);

        if (debugMode) {
            System.out.println("Point: " + point + ", Slope: " + m);
            System.out.println("Top: " + top.map(Point::toString).orElse("empty") + ", isTop: " + isTop);
            System.out.println("Bottom: " + bottom.map(Point::toString).orElse("empty") + ", isBottom: " + isBottom);
            System.out.println("Left: " + left + ", isLeft: " + isLeft);
            System.out.println("Right: " + right + ", isRight: " + isRight);

            System.out.println();
        }

        if (isTop && isRight) {
            drawLineViewPort(g, top.get(), right);

        } else if (isTop && isBottom) {
            drawLineViewPort(g, top.get(), bottom.get());

        } else if (isTop && isLeft) {
            drawLineViewPort(g, top.get(), left);

        } else if (isRight && isBottom) {
            drawLineViewPort(g, right, bottom.get());

        } else if (isRight && isLeft) {
            drawLineViewPort(g, right, left);

        } else if (isBottom && isLeft) {
            drawLineViewPort(g, bottom.get(), left);

        } else if (debugMode) {
            System.out.println("The line with a slope of " + m + " that goes through point (" + point.getX() + "," + point.getY() + ") is not being rendered!");
        }
    }

    private void drawVerticalLine(Graphics g, double realX) {
        Point topPoint = new Point(realX, upEdgeRealY);
        Point bottomPoint = new Point(realX, downEdgeRealY);

        PointInt viewportCoordsTop = convertToViewPort(topPoint);
        PointInt viewportCoordsBottom = convertToViewPort(bottomPoint);

        if (viewportCoordsTop.getX() >= 0 && viewportCoordsTop.getX() <= WIDTH) {
            g.drawLine(viewportCoordsTop.getX(), 0, viewportCoordsBottom.getX(), HEIGHT);
        } else {
            if (debugMode) {
                System.out.println("The line x=" + realX + " does not intersect the screen");
                System.out.println();
            }
        }
    }

    private void drawVertTick(Graphics g, double x) {
        PointInt p = convertToViewPort(new Point(x, 0));
        g.drawLine(p.getX(), p.getY() + tickLen / 2, p.getX(), p.getY() - tickLen / 2);
    }

    private void drawHorizTick(Graphics g, double y) {
        PointInt p = convertToViewPort(new Point(0, y));
        g.drawLine(p.getX() + tickLen / 2, p.getY(), p.getX() - tickLen / 2, p.getY());
    }

    private void drawTicks(Graphics g) {

        if (downEdgeRealY <= tickLen / 2.0 && upEdgeRealY >= -tickLen / 2.0) {
            double startX = leftEdgeRealX - (leftEdgeRealX % tickFreq);
            if (startX < leftEdgeRealX) {
                startX += tickFreq;
            }

            for (double i = startX; i <= rightEdgeRealX; i += tickFreq) {
                drawVertTick(g, i);
            }
        }

        if (leftEdgeRealX <= tickLen / 2.0 && rightEdgeRealX >= -tickLen / 2.0) {
            double startY = upEdgeRealY - (upEdgeRealY % tickFreq);
            if (startY > upEdgeRealY) {
                startY -= tickFreq;
            }

            for (double i = startY; i >= downEdgeRealY; i -= tickFreq) {
                drawHorizTick(g, i);
            }
        }
    }

    private void drawAxes(Graphics g){
        //horizontal
        drawLineThroughPoint(g,new Point(0,0),0);

        // vertical
        drawVerticalLine(g,0);
    }

    private void drawAllLines(Graphics g){
        for (int i = 0; i < linesList.getLinesLength(); i++) {
            drawLineThroughPoint(g, linesList.getPoint(i), linesList.getSlope(i));

            if (debugMode) {
                System.out.println("Called line thru point with The line with a slope of "+linesList.getSlope(i)+" that goes through point "+linesList.getPoint(i)+")!");

                System.out.println();
            }
        }

        for (int i = 0; i < linesList.getVertLinesLength(); i++) {
            drawVerticalLine(g, linesList.getVert(i).getX());
        }
    }

    // PUBLIC FUNCTIONS

    public void drawInfLine(int x, int y, double m){
        linesList.addLine(x,y,m);
        repaint();
    }

    public void drawInfLine(int x, int y){
        linesList.addLine(x,y);
        repaint();
    }

    public void drawPoint(int x, int y){
        data.add(new Point(x,y));
        repaint();
    }

    public void drawPoint(double x, double y){
        data.add(new Point(x,y));
        repaint();
    }

    public double getScale(){
        return scale;
    }

    public void setScale(int s){
        scale = s;
        repaint();
    }

    public void setCameraPos(int x, int y){
        cameraPos = new Point(x,y);
        repaint();
    }

    public Point getCameraPos(){
        return cameraPos;
    }

    public void setDrawTicks(boolean b){
        drawTicks = b;
        repaint();
    }

    public void setTickFreq(int x){
        tickFreq = x;
        repaint();
    }

    public int getTickFreq(){
        return tickFreq;
    }

    public void setTickLen(int x){
        tickLen = x;
        repaint();
    }

    public void setPointRadius(int x){
        pointRadius = x;
        repaint();
    }

    public void setAutoCam(boolean b){
        autoCam = b;
        repaint();
    }

    public void setAutoTick(boolean b){
        autoTickFreq = b;
        repaint();
    }

    public void setAutoCamPadding(int x){
        autoCamPadding = x;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(autoCam){autoCam();}
        if(autoTickFreq){autoTick();}
        updateEdges();

        drawAxes(g);

        if(drawTicks){drawTicks(g);}

        drawAllLines(g);

        plotPoints(g);
        connectPoints(g);
    }
}
