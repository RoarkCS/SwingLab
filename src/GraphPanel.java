import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private int[] X, Y;

    private int[] cameraPos = {100, 100};

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

    public void drawLineThroughPoint(Graphics g, int[] point, double m) {
        int[] viewportCoords = convertToViewPort(point);

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

        drawLineThroughPoint(g, new int[]{0,0}, 2);
    }
}
