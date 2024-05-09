import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("SwingLab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphPanel p = new GraphPanel(new int[] {1, 2, 3, 4, 5}, new int[]{2, 4, 6, 8, 10});
        frame.add(p);

        frame.setSize(500, 300);
        frame.pack();
        frame.setVisible(true);
    }
}