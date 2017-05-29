package br.asha.dfss.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends BaseScreen implements ActionListener {

    private JTextField mNome = new JTextField();
    private JButton mEntrar = new JButton("ENTRAR");

    public LoginScreen() {

        mEntrar.addActionListener(this);

        add(mNome, 100, 100);
        add(mEntrar, 100, 150);

        exibir();
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
