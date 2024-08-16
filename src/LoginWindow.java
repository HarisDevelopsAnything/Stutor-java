import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private JPanel MainPanel;
    private JButton loginButton;
    private JTextField usernameTextField;
    private JPasswordField passwordPasswordField;
    private Image icon= Toolkit.getDefaultToolkit().getImage("icons/stutor-color.png");
    public LoginWindow() {
        setContentPane(MainPanel);
        setTitle("Stutor | Login");
        setSize(400, 250);
        setIconImage(icon);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginWindow();
    }

}
