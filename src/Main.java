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

        p.drawInfLine(1,1,0.1);
        p.drawInfLine(1,1,0.3);
        p.drawInfLine(1,1,0.5);
        p.drawInfLine(1,1,0.7);
        p.drawInfLine(1,1,0.9);
        p.drawPoint(1,1);

        JPanel functions = new JPanel();
        functions.setLayout(new BoxLayout(functions, BoxLayout.Y_AXIS));

        functions.add(new UserChip(functions));

        frame.add(p, BorderLayout.WEST);
        frame.add(functions, BorderLayout.EAST);
        frame.setSize(800, 500);
        frame.pack();
        frame.setVisible(true);
    }
}