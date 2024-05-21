import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;
import java.util.Optional;

public class GraphPanel extends JPanel {

    private int[] X, Y;

    private int[] cameraPos = {0, 0};

    // scale means [scale] pixels per 1 unit
    private int scale = 10;

    private int HEIGHT = 500;
    private int WIDTH = 500;

    // 1 tick per [tickFreq] units
    private boolean drawTicks = true;
    private int tickFreq = 10;
    // ticklength by unit
    private int tickLen = 10;

    public GraphPanel(int[] X, int[] Y){
        this.X = X;
        this.Y = Y;

        setBackground(Color.WHITE);
    }

    public GraphPanel(int[] X, int[] Y, int camX, int camY){
        this.X = X;
        this.Y = Y;

        cameraPos[0] = camX;
        cameraPos[1] = camY;

        setBackground(Color.WHITE);
    }

    public GraphPanel(int[] X, int[] Y, int camX, int camY, int scale){
        this.X = X;
        this.Y = Y;

        cameraPos[0] = camX;
        cameraPos[1] = camY;

        this.scale = scale;

        setBackground(Color.WHITE);
    }

    public GraphPanel(int[] X, int[] Y, int quadrant){
        this.X = X;
        this.Y = Y;

        switch (quadrant) {
            case 1:
                cameraPos = new int[] {10,10};
            case 2:
                cameraPos = new int[] {-10,10};
            case 3:
                cameraPos = new int[] {-10,-10};
            case 4:
                cameraPos = new int[] {10,-10};
        }

        setBackground(Color.WHITE);
    }

    public int[] convertToViewPort(int[] realCoords) {
        int viewportX = (realCoords[0] - cameraPos[0]) * scale + WIDTH / 2;
        int viewportY = (realCoords[1] - cameraPos[1]) * -scale + HEIGHT / 2;
        return new int[] {viewportX, viewportY};
    }

    public int[] convertToReal(int[] viewportCoords) {
        int realX = (viewportCoords[0] - WIDTH / 2) / scale + cameraPos[0];
        int realY = (viewportCoords[1] - (HEIGHT/2)) / -scale + cameraPos[1];
        return new int[] {realX, realY};
    }

    public boolean vertOnViewport(Optional<int[]> point){
        return point.filter(ints -> convertToViewPort(ints)[0] >= 0 && convertToViewPort(ints)[0] <= WIDTH).isPresent();

    }

    public boolean horizOnViewport(int[] point){
        return convertToViewPort(point)[1] >= 0 && convertToViewPort(point)[1] <= HEIGHT;
    }

    public static Optional<int[]> intersectionHorizontal(int y, double m, int xi, int yi) {
        if (m == 0) {
            return Optional.empty();
        }
        return Optional.of(new int[]{(int) Math.round(((y - yi) / m) + xi), y});
    }

    public static int[] intersectionVertical(int x, double m, int xi, int yi) {
        return new int[]{x, (int) Math.round(m * (x - xi) + yi)};
    }

    public void drawLineViewPort(Graphics g, int[] point1, int[] point2){
        point1 = convertToViewPort(point1);
        point2 = convertToViewPort(point2);

        g.drawLine(point1[0],point1[1],point2[0],point2[1]);
    }

    public void drawLineThroughPoint(Graphics g, int[] point, double m){

        Optional<int[]> top = intersectionHorizontal(convertToReal(new int[]{0,0})[1], m, point[0], point[1]);
        Optional<int[]> bottom = intersectionHorizontal(convertToReal(new int[]{0,HEIGHT})[1], m, point[0], point[1]);
        int[] left = intersectionVertical(convertToReal(new int[]{0,0})[0], m, point[0], point[1]);
        int[] right = intersectionVertical(convertToReal(new int[]{WIDTH,0})[0], m, point[0], point[1]);

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

        System.out.println("The line "+"y="+m+"x+"+((-m*point[0])+point[1])+" does not intersect the screen");
    }

    public void drawVerticalLine(Graphics g, int x){
        int[] viewportCoordsTop = convertToViewPort(new int[]{x, 0});
        int[] viewportCoordsBottom = convertToViewPort(new int[]{x, HEIGHT});

        if (viewportCoordsTop[0] >= 0 && viewportCoordsTop[0] <= WIDTH) {
            g.drawLine(viewportCoordsTop[0], 0, viewportCoordsBottom[0], HEIGHT);
        } else {
            System.out.println("The line "+"x="+x+" does not intersect the screen");
        }
    }

    public void drawVertTick(Graphics g, int x, int y){

        int[] p1 = convertToViewPort(new int[] {x,y});
        int[] p2 = convertToViewPort(new int[] {x,y});

        g.drawLine(p1[0],p1[1]+tickLen/2,p2[0],p2[1]-tickLen/2);
    }

    public void drawHorizTick(Graphics g, int x, int y){
//        System.out.println("Drawing line from ("+x+","+(y+tickLen/2)+") to ("+x+","+(y-tickLen/2)+")");

        int[] p1 = convertToViewPort(new int[] {x,y});
        int[] p2 = convertToViewPort(new int[] {x,y});

        g.drawLine(p1[0]+tickLen/2,p1[1],p2[0]-tickLen/2,p2[1]);
    }

    public void drawTicks(Graphics g){
        //X-axis ticks
        // if x axis on screen
        if(convertToReal(new int[] {0,HEIGHT})[1] <= tickLen/2 && convertToReal(new int[] {0,0})[1] >= -tickLen/2){
            System.out.println("x axis on screen");

            int leftEdgeRealX = convertToReal(new int[] {0,0})[0];
            int rightEdgeRealX = convertToReal(new int[] {WIDTH,0})[0];

            for(int i = leftEdgeRealX + tickFreq - (leftEdgeRealX%tickFreq); i < rightEdgeRealX; i += tickFreq){
                System.out.println("drawing a vert tick!");
                drawVertTick(g, i, 0);
            }
        }

        if(convertToReal(new int[] {0,0})[0] <= tickLen/2 && convertToReal(new int[] {WIDTH,0})[0] >= -tickLen/2){
            System.out.println("Y axis on screen");

            int upEdgeRealY = convertToReal(new int[] {0,0})[1];
            int downEdgeRealY = convertToReal(new int[] {0,HEIGHT})[1];

            for(int i = upEdgeRealY + tickFreq - (upEdgeRealY%tickFreq); i > downEdgeRealY; i -= tickFreq){
                System.out.println("drawing a horiz tick!");
                drawHorizTick(g, 0, i);
            }
        }
    }

    public void drawAxes(Graphics g){
        //horizontal
        drawLineThroughPoint(g,new int[] {0,0},0);

        // vertical
        drawVerticalLine(g,0);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawAxes(g);

        if(drawTicks){
            drawTicks(g);
        }

        drawLineThroughPoint(g,new int[] {0,10}, -2.5);

    }
}
