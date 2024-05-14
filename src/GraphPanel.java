import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

public class GraphPanel extends JPanel {

    private int[] X, Y;

    private int[] cameraPos = {200, 400};

    // scale means [scale] pixels per 1 unit
    private int scale = 1;

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
        int viewportX = (WIDTH / 2 - (cameraPos[0] - realCoords[0])) * scale;
        int viewportY = (HEIGHT / 2 + (cameraPos[1] - realCoords[1])) * scale;
        return new int[] {viewportX, viewportY};
    }

    public int[] convertToReal(int[] viewportCoords) {
        int realX = cameraPos[0] - ((WIDTH / 2 - viewportCoords[0]) / scale);
        int realY = cameraPos[1] + ((HEIGHT / 2 - viewportCoords[1]) / scale);
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

        System.out.println(Arrays.toString(top.get()));
        System.out.println(Arrays.toString(bottom.get()));
        System.out.println(Arrays.toString(left));
        System.out.println(Arrays.toString(right));

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

        System.out.println(isTop);
        System.out.println(isBottom);
        System.out.println(isLeft);
        System.out.println(isRight);

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

        System.out.println("Apparently, none of the lines intersected where they were supposed to!");
    }

    public void drawAxes(Graphics g){
        //horizontal
        g.drawLine(0,((HEIGHT /2) + cameraPos[1]), WIDTH,((HEIGHT /2) + cameraPos[1]));

        // vertical
        g.drawLine(((WIDTH /2) - cameraPos[0]),0,((WIDTH /2) - cameraPos[0]), HEIGHT);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawAxes(g);

        drawLineThroughPoint(g, new int[]{0,0}, 2.0);
    }
}
