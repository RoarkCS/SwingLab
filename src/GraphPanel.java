import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

public class GraphPanel extends JPanel {

    private int[] X, Y;

    private int[] cameraPos = {5, 5};

    // scale means [scale] pixels per 1 unit
    private int scale = 10;

    private int HEIGHT = 500;
    private int WIDTH = 500;
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

    public int[] convertToViewPort(int[] realCoords) {
        int viewportX = (realCoords[0] - cameraPos[0]) * scale + WIDTH / 2;
        int viewportY = HEIGHT / 2 - (realCoords[1] - cameraPos[1]) * scale;
        return new int[] {viewportX, viewportY};
    }

    public int[] convertToReal(int[] viewportCoords) {
        int realX = (viewportCoords[0] - WIDTH / 2) / scale + cameraPos[0];
        int realY = cameraPos[1] - (viewportCoords[1] - HEIGHT / 2) / scale;
        return new int[] {realX, realY};
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

    public void drawLineThroughPoint(Graphics g, int[] point, double m){

        Optional<int[]> top = intersectionHorizontal(convertToReal(new int[]{0,0})[1], m, point[0], point[1]);
        Optional<int[]> bottom = intersectionHorizontal(convertToReal(new int[]{0,HEIGHT})[1], m, point[0], point[1]);
        int[] left = intersectionVertical(convertToReal(new int[]{0,0})[0], m, point[0], point[1]);
        int[] right = intersectionVertical(convertToReal(new int[]{WIDTH,0})[0], m, point[0], point[1]);

        boolean isTop = false;
        boolean isBottom = false;
        boolean isLeft = false;
        boolean isRight = false;

        if(top.isPresent()){
            if(convertToViewPort(top.get())[0] >= 0 && convertToViewPort(top.get())[0] <= WIDTH){
                isTop = true;
            }
        }

        if(bottom.isPresent()){
            if(convertToViewPort(bottom.get())[0] >= 0 && convertToViewPort(bottom.get())[0] <= WIDTH){
                isBottom = true;
            }
        }

        if(convertToViewPort(left)[1] >= 0 && convertToViewPort(left)[1] <= HEIGHT){
            isLeft = true;
        }

        if(convertToViewPort(right)[1] >= 0 && convertToViewPort(right)[1] <= HEIGHT){
            isRight = true;
        }

        if(isTop && isRight) {
            g.drawLine(convertToViewPort(top.get())[0], convertToViewPort(top.get())[1],
                    convertToViewPort(right)[0], convertToViewPort(right)[1]);
            return;
        }

        if(isTop && isBottom) {
            g.drawLine(convertToViewPort(top.get())[0], convertToViewPort(top.get())[1],
                    convertToViewPort(bottom.get())[0], convertToViewPort(bottom.get())[1]);
            return;
        }

        if(isTop && isLeft) {
            g.drawLine(convertToViewPort(top.get())[0], convertToViewPort(top.get())[1],
                    convertToViewPort(left)[0], convertToViewPort(left)[1]);
            return;
        }

        if(isRight && isBottom) {
            g.drawLine(convertToViewPort(right)[0], convertToViewPort(right)[1],
                    convertToViewPort(bottom.get())[0], convertToViewPort(bottom.get())[1]);
            return;
        }

        if(isRight && isLeft) {
            g.drawLine(convertToViewPort(right)[0], convertToViewPort(right)[1],
                    convertToViewPort(left)[0], convertToViewPort(left)[1]);
            return;
        }

        if(isBottom && isLeft) {
            g.drawLine(convertToViewPort(bottom.get())[0], convertToViewPort(bottom.get())[1],
                    convertToViewPort(left)[0], convertToViewPort(left)[1]);
            return;
        }

        System.out.println("The line "+"y="+m+"x+"+((-m*point[0])+point[1])+" does not intersect the screen");
    }

    public void drawVerticalLine(Graphics g, int x){
        int[] viewportCoordsTop = convertToViewPort(new int[]{x, 0});
        int[] viewportCoordsBottom = convertToViewPort(new int[]{x, HEIGHT});

        if (viewportCoordsTop[0] >= 0 && viewportCoordsTop[0] <= WIDTH) {
            System.out.println("Drawing vert line from ("+viewportCoordsTop[0]+","+0+") to ("+viewportCoordsBottom[0]+","+HEIGHT+")");
            g.drawLine(viewportCoordsTop[0], 0, viewportCoordsBottom[0], HEIGHT);
        } else {
            System.out.println("The line "+"x="+x+" does not intersect the screen");
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

        drawLineThroughPoint(g, new int[]{0,5}, -2.5);
    }
}
