import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataGenerator extends JPanel {

    private static final int MAX_DISPERSION = 75, RANGE_LOW = 0, RANGE_HIGH = 100;
    private static final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 12);
    private static final Font LABEL_FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Verdana", Font.PLAIN, 10);

    private GraphPanel p;
    private JTextField mField;
    private JTextField bField;
    private JSlider dispersionSlider;
    private JSlider numDataPointsSlider;

    public DataGenerator(GraphPanel p) {
        super();
        this.p = p;
        setLayout(new GridLayout(5, 1));

        add(createTitleLabel());
        add(createEquationPanel());
        add(createDispersionSliderPanel());
        add(createNumDataPointsSliderPanel());
        add(createButtonPanel());
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Data Generator", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        return titleLabel;
    }

    private JPanel createEquationPanel() {
        JPanel equationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mField = createTextField();
        bField = createTextField();
        equationPanel.add(new JLabel("y = "));
        equationPanel.add(mField);
        equationPanel.add(new JLabel("x + "));
        equationPanel.add(bField);
        return equationPanel;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(5);
        textField.setFont(LABEL_FONT);
        return textField;
    }

    private JPanel createDispersionSliderPanel() {
        dispersionSlider = new JSlider(0, 100, 0);
        dispersionSlider.setMajorTickSpacing(20);
        dispersionSlider.setMinorTickSpacing(5);
        dispersionSlider.setPaintTicks(true);
        dispersionSlider.setPaintLabels(true);

        JLabel sliderLabel = new JLabel("Dispersion:");
        sliderLabel.setFont(LABEL_FONT);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(sliderLabel, BorderLayout.WEST);
        sliderPanel.add(dispersionSlider, BorderLayout.CENTER);
        return sliderPanel;
    }

    private JPanel createNumDataPointsSliderPanel() {
        numDataPointsSlider = new JSlider(10, 100, 20);  // Set range from 10 to 1000 with default 100
        numDataPointsSlider.setMajorTickSpacing(10);
        numDataPointsSlider.setMinorTickSpacing(1);
        numDataPointsSlider.setPaintTicks(true);
        numDataPointsSlider.setPaintLabels(true);

        JLabel sliderLabel = new JLabel("Number of Data Points:");
        sliderLabel.setFont(LABEL_FONT);

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(sliderLabel, BorderLayout.WEST);
        sliderPanel.add(numDataPointsSlider, BorderLayout.CENTER);
        return sliderPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

        JButton drawButton = createButton("Draw");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawButton();
            }
        });

        JButton generateButton = createButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateButton();
            }
        });

        JButton approximateButton = createButton("Approximate");
        approximateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approximate();
            }
        });

        buttonPanel.add(drawButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(approximateButton);
        return buttonPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        return button;
    }

    private boolean getMB(){
        try {
            Double.parseDouble(mField.getText());
            Double.parseDouble(bField.getText());

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void drawButton() {
        System.out.println("Draw button clicked");

        if(getMB()) {
            double m = Double.parseDouble(mField.getText());
            double b = Double.parseDouble(bField.getText());

            p.drawInfLine(0, b, m);
        } else {
            System.out.println("b & m weren't parsable!");
        }
    }

    private void generateButton() {
        System.out.println("Generate button clicked");

        if(getMB()) {
            double m = Double.parseDouble(mField.getText());
            double b = Double.parseDouble(bField.getText());

            int numDataPoints = numDataPointsSlider.getValue();
            int dispersion = dispersionSlider.getValue();

            generateData(dispersion, RANGE_LOW, RANGE_HIGH, m, b, numDataPoints);
        } else {
            System.out.println("b & m weren't parsable!");
        }
    }

    private void generateData(int percentDispersion, int rangeLow, int rangeHigh, double m, double b, int numDataPoints){
        for (int i = 0; i < numDataPoints; i++) {
            double x = Math.random()*(rangeHigh-rangeLow)+rangeLow;
            double y = m*x + b;

            double randVector = ((Math.random()*2)-1)*(MAX_DISPERSION*percentDispersion*0.01);

            y += randVector;

            p.addPoint(x,y);
        }
    }

    private void approximate() {
        System.out.println("Approximate button clicked");

        LinearRegression lr = new LinearRegression();

        if(!p.getData().isEmpty()) {
            lr.fit(p.getData());

            p.drawInfLine(0, lr.getIntercept(), lr.getSlope());
        } else {
            System.out.println("No data to approximate!");
        }
    }
//    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(300, 200);
//    }
}