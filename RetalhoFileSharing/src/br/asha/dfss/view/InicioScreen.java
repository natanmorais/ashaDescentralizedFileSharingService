package br.asha.dfss.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioScreen extends BaseScreen implements ActionListener {

    private JButton mLoginButton = new JButton("LOGAR");
    private JButton mCadastrarButton = new JButton("CADASTRAR");

    public InicioScreen() {
        mLoginButton.addActionListener(this);
        mLoginButton.setPreferredSize(new Dimension(100, 50));
        mCadastrarButton.addActionListener(this);
        mCadastrarButton.setPreferredSize(new Dimension(100, 50));

        add(mLoginButton, 200, 300);
        add(mCadastrarButton, 200, 360);

        exibir();
    }

    public static void main(String[] args) {
        new InicioScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();
        if (o == mCadastrarButton) {
            new CadastrarScreen();
        } else if (o == mLoginButton) {
            new LoginScreen();
        }
    }
}
