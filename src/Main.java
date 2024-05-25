import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("SwingLab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphPanel p = new GraphPanel();
        p.setAutoCam(true);
        p.setAutoTick(true);

        p.drawInfLine(1,1,0.1001); //Graphs correctly
        p.drawInfLine(1,1,0.1); // Graphs poorly
        p.drawPoint(1,1);

        frame.add(p);
        frame.setSize(500, 500);
        frame.pack();
        frame.setVisible(true);
    }
}