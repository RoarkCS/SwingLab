import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class GraphPanel extends JPanel {

    private final boolean debugMode = true;

    private ArrayList<Integer> X = new ArrayList<>();
    private ArrayList<Integer> Y = new ArrayList<>();
    private int[] cameraPos = {0, 0};
    private int scale = 10;
    private static final int HEIGHT = 500, WIDTH = 500;
    private boolean drawTicks = true;
    private int tickFreq = 10, tickLen = 10;
    private int pointRadius = 2;
    private boolean autoCam = false;
    private boolean autoTickFreq = false;
    private int autoCamPadding = 10;
    private int numTicksOnScreen = 10;
    private int leftEdgeRealX, rightEdgeRealX, upEdgeRealY, downEdgeRealY;
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
        for (int i: X) {
            this.X.add(i);
        }
        for (int i: Y) {
            this.Y.add(i);
        }

        this.cameraPos[0] = camX;
        this.cameraPos[1] = camY;
        this.scale = scale;
        setBackground(Color.WHITE);
        updateEdges();
    }

    public GraphPanel(int[] X, int[] Y, int quadrant){
        for (int i: X) {
            this.X.add(i);
        }
        for (int i: Y) {
            this.Y.add(i);
        }

        if (quadrant == 1) {
            this.cameraPos = new int[]{10, 10};
        } else if (quadrant == 2) {
            this.cameraPos = new int[]{-10, 10};
        } else if (quadrant == 3) {
            this.cameraPos = new int[]{-10, -10};
        } else if (quadrant == 4) {
            this.cameraPos = new int[] {10,-10};
        }

        setBackground(Color.WHITE);
        updateEdges();
    }

    public int[] convertToViewPort(int[] realCoords) {
        int viewportX = (realCoords[0] - cameraPos[0]) * scale + WIDTH / 2;
        int viewportY = (realCoords[1] - cameraPos[1]) * -scale + HEIGHT / 2;
        return new int[] {viewportX, viewportY};
    }

    public int[] convertToReal(int[] viewportCoords) {
        int realX = (viewportCoords[0] - WIDTH / 2) / scale + cameraPos[0];
        int realY = (viewportCoords[1] - HEIGHT / 2) / -scale + cameraPos[1];
        return new int[] {realX, realY};
    }

    private void autoCam() {
        if(X.isEmpty()){
            return;
        }

        int xMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;

        for (int i = 0; i < X.size(); i++) {
            xMin = Math.min(xMin, X.get(i));
            xMax = Math.max(xMax, X.get(i));
            yMin = Math.min(yMin, Y.get(i));
            yMax = Math.max(yMax, Y.get(i));
        }

        int xRange = xMax - xMin;
        int yRange = yMax - yMin;

        scale = Math.min(WIDTH / (xRange + autoCamPadding), HEIGHT / (yRange + autoCamPadding));
        cameraPos[0] = xMin + xRange / 2;
        cameraPos[1] = yMin + yRange / 2;
        updateEdges();
    }

    private void updateEdges() {
        leftEdgeRealX = convertToReal(new int[]{0, 0})[0];
        rightEdgeRealX = convertToReal(new int[]{WIDTH, 0})[0];
        upEdgeRealY = convertToReal(new int[]{0, 0})[1];
        downEdgeRealY = convertToReal(new int[]{0, HEIGHT})[1];
    }

    private void autoTick() {
        if(X.isEmpty()){
            tickFreq = 10;
        }

        int xRange = rightEdgeRealX - leftEdgeRealX;
        int yRange = upEdgeRealY - downEdgeRealY;

        int xTickFreq = (int) Math.ceil((double) xRange / numTicksOnScreen);
        int yTickFreq = (int) Math.ceil((double) yRange / numTicksOnScreen);
        tickFreq = Math.max(Math.max(xTickFreq, yTickFreq), 1);
    }

    private boolean onViewPort(int[] point) {
        if (convertToViewPort(point)[0] >= 0 && convertToViewPort(point)[0] <= WIDTH){
            return convertToViewPort(point)[1] >= 0 && convertToViewPort(point)[1] <= HEIGHT;
        }
        return false;
    }

    private void plotPoint(Graphics g, int[] point){
        if(onViewPort(point)){
            point = convertToViewPort(point);
            g.setColor(Color.blue);
            g.fillOval(point[0]-pointRadius,point[1]-pointRadius,pointRadius*2,pointRadius*2);
            g.setColor(Color.black);
        }
    }

    private void plotPoints(Graphics g){
        for (int i = 0; i < X.size(); i++) {
            plotPoint(g,new int[]{X.get(i),Y.get(i)});
        }
    }

    private void connectPoints(Graphics g){
        for (int i = 0; i < X.size()-1; i++) {
            int[] point1 = new int[] {X.get(i),Y.get(i)};
            int[] point2 = new int[] {X.get(i+1),Y.get(i+1)};

            if(onViewPort(point1) || onViewPort(point2)){
                point1 = convertToViewPort(point1);
                point2 = convertToViewPort(point2);

                g.setColor(Color.blue);
                g.drawLine(point1[0],point1[1],point2[0],point2[1]);
                g.setColor(Color.black);
            }
        }
    }

    private boolean vertOnViewport(Optional<int[]> point){
        return point.filter(ints -> convertToViewPort(ints)[0] >= 0 && convertToViewPort(ints)[0] <= WIDTH).isPresent();

    }

    private boolean horizOnViewport(int[] point){
        return convertToViewPort(point)[1] >= 0 && convertToViewPort(point)[1] <= HEIGHT;
    }

    private static Optional<int[]> intersectionHorizontal(int y, double m, int xi, int yi) {
        if (m == 0) {
            return Optional.empty();
        }
        return Optional.of(new int[]{(int) Math.round(((y - yi) / m) + xi), y});
    }

    private static int[] intersectionVertical(int x, double m, int xi, int yi) {
        return new int[]{x, (int) Math.round(m * (x - xi) + yi)};
    }

    private void drawLineViewPort(Graphics g, int[] point1, int[] point2){
        point1 = convertToViewPort(point1);
        point2 = convertToViewPort(point2);

        g.drawLine(point1[0],point1[1],point2[0],point2[1]);
    }

    private void drawLineThroughPoint(Graphics g, int[] point, double m){
        Optional<int[]> top = intersectionHorizontal(upEdgeRealY, m, point[0], point[1]);
        Optional<int[]> bottom = intersectionHorizontal(downEdgeRealY, m, point[0], point[1]);
        int[] left = intersectionVertical(leftEdgeRealX, m, point[0], point[1]);
        int[] right = intersectionVertical(rightEdgeRealX, m, point[0], point[1]);

        boolean isTop = vertOnViewport(top);
        boolean isBottom = vertOnViewport(bottom);
        boolean isLeft = horizOnViewport(left);
        boolean isRight = horizOnViewport(right);

        if(isTop && isRight) {
            drawLineViewPort(g, top.get(), right);
            return;
        }

        if(isTop && isBottom) {
            drawLineViewPort(g, top.get(), bottom.get());
            return;
        }

        if(isTop && isLeft) {
            drawLineViewPort(g, top.get(),left);
            return;
        }

        if(isRight && isBottom) {
            drawLineViewPort(g, right, bottom.get());
            return;
        }

        if(isRight && isLeft) {
            drawLineViewPort(g, right, left);
            return;
        }

        if(isBottom && isLeft) {
            drawLineViewPort(g, bottom.get(), left);
            return;
        }

        if (debugMode) {
            System.out.println("The line with a slope of "+m+" that goes through point ("+point[0]+","+point[1]+") is not being rendered!");
        }
    }

    private void drawVerticalLine(Graphics g, int x){
        int[] viewportCoordsTop = convertToViewPort(new int[]{x, 0});
        int[] viewportCoordsBottom = convertToViewPort(new int[]{x, HEIGHT});

        if (viewportCoordsTop[0] >= 0 && viewportCoordsTop[0] <= WIDTH) {
            g.drawLine(viewportCoordsTop[0], 0, viewportCoordsBottom[0], HEIGHT);
        } else {
            System.out.println("The line "+"x="+x+" does not intersect the screen");
        }
    }

    private void drawVertTick(Graphics g, int x, int y) {
        int[] p = convertToViewPort(new int[] {x, y});
        g.drawLine(p[0], p[1] + tickLen / 2, p[0], p[1] - tickLen / 2);
    }

    private void drawHorizTick(Graphics g, int x, int y) {
        int[] p = convertToViewPort(new int[] {x, y});
        g.drawLine(p[0] + tickLen / 2, p[1], p[0] - tickLen / 2, p[1]);
    }

    private void drawTicks(Graphics g) {
        // X-axis ticks
        if (convertToReal(new int[] {0, HEIGHT})[1] <= tickLen / 2 && convertToReal(new int[] {0, 0})[1] >= -tickLen / 2) {
            int leftEdgeRealX = convertToReal(new int[] {0, 0})[0];
            int rightEdgeRealX = convertToReal(new int[] {WIDTH, 0})[0];

            int startX = leftEdgeRealX - (leftEdgeRealX % tickFreq);
            if (startX < leftEdgeRealX) {
                startX += tickFreq;
            }

            for (int i = startX; i <= rightEdgeRealX; i += tickFreq) {
                drawVertTick(g, i, 0);
            }
        }

        // Y-axis ticks
        if (convertToReal(new int[] {0, 0})[0] <= tickLen / 2 && convertToReal(new int[] {WIDTH, 0})[0] >= -tickLen / 2) {
            int upEdgeRealY = convertToReal(new int[] {0, 0})[1];
            int downEdgeRealY = convertToReal(new int[] {0, HEIGHT})[1];

            int startY = upEdgeRealY - (upEdgeRealY % tickFreq);
            if (startY > upEdgeRealY) {
                startY -= tickFreq;
            }

            for (int i = startY; i >= downEdgeRealY; i -= tickFreq) {
                drawHorizTick(g, 0, i);
            }
        }
    }

    private void drawAxes(Graphics g){
        //horizontal
        drawLineThroughPoint(g,new int[] {0,0},0);

        // vertical
        drawVerticalLine(g,0);
    }

    private void drawAllLines(Graphics g){
        for (int i = 0; i < linesList.getLinesLength(); i++) {
            drawLineThroughPoint(g, linesList.getPoint(i), linesList.getSlope(i));

            if (debugMode) {
                System.out.println("Called line thru point with The line with a slope of "+linesList.getSlope(i)+" that goes through point ("+linesList.getPoint(i)[0]+","+linesList.getPoint(i)[1]+")!");
            }
        }

        for (int i = 0; i < linesList.getVertLinesLength(); i++) {
            drawVerticalLine(g, linesList.getVert(i)[0]);
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
        X.add(x);
        Y.add(y);
        repaint();
    }

    public int getScale(){
        return scale;
    }

    public void setScale(int s){
        scale = s;
        repaint();
    }

    public void setCameraPos(int x, int y){
        cameraPos = new int[] {x,y};
        repaint();
    }

    public int[] getCameraPos(){
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
