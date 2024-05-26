import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("SwingLab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphPanel p = new GraphPanel();

        p.setAutoTick(true);
        p.setAutoCam(true);

        p.drawPoint(5,1);
        p.drawPoint(1,5);

        JPanel UI = new JPanel();
        UI.setLayout(new GridLayout(4, 1));
        Legend legend = new Legend(p);

        UI.add(legend);
        p.setLegend(legend);

        frame.add(p, BorderLayout.WEST);
        frame.add(UI, BorderLayout.EAST);
        frame.setSize(800, 500);
        frame.pack();
        frame.setVisible(true);
    }
}