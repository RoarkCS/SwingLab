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

        JPanel functions = new JPanel();
        functions.setLayout(new BoxLayout(functions, BoxLayout.Y_AXIS));




        frame.add(p, BorderLayout.WEST);
        frame.setSize(500, 500);
        frame.pack();
        frame.setVisible(true);
    }
}