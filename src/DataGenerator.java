import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataGenerator extends JPanel {

    private static final int MAX_DISPERSION = 75;
    private static final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 12);
    private static final Font LABEL_FONT = new Font("Verdana", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Verdana", Font.PLAIN, 10);

    private GraphPanel p;
    private JTextField mField;
    private JTextField bField;
    private JSlider dispersionSlider;
    private JSlider numDataPointsSlider;
    private JTextField rangeLowField;
    private JTextField rangeHighField;
    private final boolean debugMode = false;

    public DataGenerator(GraphPanel p) {
        super();
        this.p = p;
        setLayout(new GridLayout(6, 1));

        add(createTitleLabel());
        add(createEquationPanel());
        add(createRangePanel());
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

    private JPanel createRangePanel() {
        JPanel rangePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rangeLowField = createTextField();
        rangeHighField = createTextField();

        rangeLowField.setText("-10");
        rangeHighField.setText("10");

        rangePanel.add(new JLabel("From: "));
        rangePanel.add(rangeLowField);
        rangePanel.add(new JLabel(" - "));
        rangePanel.add(rangeHighField);

        return rangePanel;
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
        numDataPointsSlider = new JSlider(10, 100, 20);
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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));

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

        JButton clearButton = createButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearButton();
            }
        });

        buttonPanel.add(drawButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(approximateButton);
        buttonPanel.add(clearButton);

        return buttonPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        return button;
    }

    private boolean getMB() {
        try {
            Double.parseDouble(mField.getText());
            Double.parseDouble(bField.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean getRange() {
        try {
            Double.parseDouble(rangeLowField.getText());
            Double.parseDouble(rangeHighField.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void drawButton() {
        if (debugMode) System.out.println("Draw button clicked");

        if (getMB()) {
            double m = Double.parseDouble(mField.getText());
            double b = Double.parseDouble(bField.getText());

            p.drawInfLine(0, b, m);
        } else {
            if (debugMode) System.out.println("b & m weren't parsable!");
        }
    }

    private void generateButton() {
        if (debugMode) System.out.println("Generate button clicked");

        if (getMB() && getRange()) {
            double m = Double.parseDouble(mField.getText());
            double b = Double.parseDouble(bField.getText());

            int numDataPoints = numDataPointsSlider.getValue();
            int dispersion = dispersionSlider.getValue();

            int rangeLow = Integer.parseInt(rangeLowField.getText());
            int rangeHigh = Integer.parseInt(rangeHighField.getText());

            if (rangeLow < rangeHigh) {
                generateData(dispersion, rangeLow, rangeHigh, m, b, numDataPoints);
            } else {
                if (debugMode) System.out.println("Range Low must be less than Range High!");
            }
        } else {
            if (debugMode) System.out.println("b, m, or range values weren't parsable!");
        }
    }

    private void generateData(int percentDispersion, int rangeLow, int rangeHigh, double m, double b, int numDataPoints) {
        for (int i = 0; i < numDataPoints; i++) {
            double x = Math.random() * (rangeHigh - rangeLow) + rangeLow;
            double y = m * x + b;

            double randVector = ((Math.random() * 2) - 1) * (MAX_DISPERSION * percentDispersion * 0.01);

            y += randVector;

            p.addPoint(x, y);
        }
    }

    private void approximate() {
        if (debugMode) System.out.println("Approximate button clicked");

        LinearRegression lr = new LinearRegression();

        if (!p.getData().isEmpty()) {
            lr.fit(p.getData());

            p.drawInfLine(0, lr.getIntercept(), lr.getSlope());
        } else {
            if (debugMode) System.out.println("No data to approximate!");
        }
    }

    private void clearButton() {
        if (debugMode) System.out.println("Clear button clicked");
        p.clear();
    }
}