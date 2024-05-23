import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserChip extends JPanel {
    private JTextField textField;
    private JPanel parent;

    public UserChip(JPanel parent) {
        this.parent = parent;
        this.textField = new JTextField(20);
        this.setLayout(new BorderLayout());
        this.add(textField, BorderLayout.CENTER);

        textField.addActionListener(e -> {
            String input = textField.getText();
            if (isValidLinearFunction(input)) {
                setBackground(Color.WHITE);
            } else {
                setBackground(Color.RED);
            }
        });
    }

    private boolean isValidLinearFunction(String input) {
        return true;
    }

    public int getFunc(){
        return -1;
    }
}