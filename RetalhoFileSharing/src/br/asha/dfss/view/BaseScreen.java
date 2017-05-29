package br.asha.dfss.view;

import javax.swing.*;
import java.awt.*;

public class BaseScreen extends JFrame {

    public BaseScreen() {
        setLayout(null);
    }

    public void add(Component c, int x, int y) {
        Insets insets = getInsets();
        Dimension size = c.getPreferredSize();
        c.setBounds(x + insets.left, y + insets.top,
                size.width, size.height);
        add(c);
    }

    public void exibir() {
        setSize(600, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
