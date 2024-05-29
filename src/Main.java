import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("SwingLab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphPanel p = new GraphPanel();

        p.setAutoTick(true);
        p.setAutoCam(true);

        JPanel UI = new JPanel();
        UI.setLayout(new BoxLayout(UI,BoxLayout.Y_AXIS));
        Legend legend = new Legend(p);
        DataGenerator dataGenerator = new DataGenerator(p);

        UI.add(legend);
        UI.add(dataGenerator);
        p.setLegend(legend);

        frame.add(p, BorderLayout.WEST);
        frame.add(UI, BorderLayout.EAST);
        frame.setSize(800, 500);
        frame.pack();
        frame.setVisible(true);
    }
}