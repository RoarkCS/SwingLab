import javax.swing.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {

    ArrayList<Integer> X, Y;
    public GraphPanel(ArrayList<Integer> X, ArrayList<Integer> Y){
        this.X = X;
        this.Y = Y;
    }
}
