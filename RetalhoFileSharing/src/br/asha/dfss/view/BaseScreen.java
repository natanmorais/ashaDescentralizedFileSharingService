package br.asha.dfss.view;

import javax.swing.*;
import java.awt.*;

public class BaseScreen extends JFrame {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;

    public BaseScreen() {
        setLayout(null);
    }

    public void add(Component c, int x, int y, int width, int height) {
        Insets insets = getInsets();
        c.setPreferredSize(new Dimension(width, height));
        c.setBounds(x + insets.left - width / 2, y + insets.top - height / 2,
                width, height);
        add(c);
    }

    public void exibir() {
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);      
    }
}
