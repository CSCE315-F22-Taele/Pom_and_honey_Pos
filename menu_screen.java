import javax.swing.*;
import java.awt.Color;
import java.awt.*;

public class menu_screen extends JFrame {

    public static void main(String[] args) {
        JFrame frame = new JFrame("ORDER");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // JLabel label1 =
        JButton button1 = new JButton("New window!");
        button1.setBounds(50, 50, 825, 200);
        button1.setFont(new Font("Arial", Font.BOLD, 40));
        Color c = new Color(0, 255, 0);
        button1.setBackground(c);
        c = new Color(255, 0, 0);
        frame.add(button1);
        c = new Color(20, 20, 20);

        frame.setSize(1000, 1000);
        frame.setLayout(null); // using no layout managers
        frame.setVisible(true); // making the frame visible
        frame.setBackground(c);
    }
}