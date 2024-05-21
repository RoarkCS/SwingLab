import javax.swing.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("SwingLab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphPanel p = new GraphPanel(new int[] {1, 2, 3, 4, 5}, new int[]{2, 4, 6, 8, 10}, 10, 20, 1);

        System.out.println(Arrays.toString(p.convertToViewPort(p.convertToReal(new int[]{10, 10})))); // 10 10
        System.out.println(Arrays.toString(p.convertToReal(p.convertToViewPort(new int[]{2310, 13290})))); // 10 10

        frame.add(p);

        frame.setSize(500, 500);
        frame.pack();
        frame.setVisible(true);
    }
}