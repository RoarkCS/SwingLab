import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    int[] X, Y;

    int[] cameraPos = {0,0};
    int[] scale = {0, 0};
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

    public GraphPanel(int[] X, int[] Y, int camX, int camY, int scaleX, int scaleY){
        this.X = X;
        this.Y = Y;

        cameraPos[0] = camX;
        cameraPos[1] = camY;

        scale[0] = scaleX;
        scale[1] = scaleY;

        setBackground(Color.WHITE);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //horizontal
        g.drawLine(0,(cameraPos[1]+HEIGHT/2),WIDTH,(cameraPos[1]+HEIGHT/2));

        // vertical
        g.drawLine((cameraPos[0]+WIDTH/2),0,(cameraPos[0]+WIDTH/2),HEIGHT);
    }
}
