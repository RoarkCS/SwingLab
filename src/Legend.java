import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Legend extends JPanel {

    private GraphPanel p;
    private JTextField cursorLoc;
    private JLabel cameraPosLabel;
    private JLabel scaleLabel;
    private JLabel tickLabel;

    public Legend(GraphPanel p) {
        super();

        this.p = p;

        cursorLoc = new JTextField();
        cursorLoc.setEditable(false);
        cursorLoc.setFont(new Font("Verdana", Font.BOLD, 14));
        cursorLoc.setHorizontalAlignment(JTextField.CENTER);

        cameraPosLabel = new JLabel();
        cameraPosLabel.setFont(new Font("Verdana", Font.PLAIN, 12));

        scaleLabel = new JLabel();
        scaleLabel.setFont(new Font("Verdana", Font.PLAIN, 12));

        tickLabel = new JLabel();
        tickLabel.setFont(new Font("Verdana", Font.PLAIN, 12));

        setLayout(new GridLayout(4, 1));
        add(cursorLoc);
        add(cameraPosLabel);
        add(scaleLabel);
        add(tickLabel);

        p.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point realCoords = p.convertToReal(new Point(e.getX(), e.getY()));
                cursorLoc.setText(String.format("Mouse coordinates: (%.2f, %.2f)", realCoords.getX(), realCoords.getY()));
            }
        });
    }

    public void update(){
        updateCamPos();
        updateScale();
        updateTickFreq();
    }

    private void updateCamPos() {
        cameraPosLabel.setText("Camera Position: " + p.getCameraPos());
    }

    private void updateScale() {
        scaleLabel.setText("Scale: " + p.getScale() + " pixels per unit");
    }

    private void updateTickFreq() {
        tickLabel.setText(String.format("Tick Frequency: 1 tick every %.2f units", p.getTickFreq()));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 150);
    }
}