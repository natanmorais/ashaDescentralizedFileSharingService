package br.asha.dfss.view;

import br.asha.dfss.hub.MasterHub;
import br.asha.dfss.hub.NodeHub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastrarScreen extends BaseScreen implements ActionListener {

    private JButton mCriarRedeButton = new JButton("Criar Rede");
    private JButton mCriarFilialButton = new JButton("Criar Filial");
    private JButton mEntrarEmUmaFilialButton = new JButton("Entrar uma Filial");

    public CadastrarScreen() {

        mCriarRedeButton.setPreferredSize(new Dimension(100, 50));
        mCriarFilialButton.setPreferredSize(new Dimension(100, 50));
        mEntrarEmUmaFilialButton.setPreferredSize(new Dimension(100, 50));
        mCriarRedeButton.addActionListener(this);
        mCriarFilialButton.addActionListener(this);
        mEntrarEmUmaFilialButton.addActionListener(this);

        add(mCriarRedeButton, 100, 100);
        add(mCriarFilialButton, 100, 160);
        add(mEntrarEmUmaFilialButton, 100, 240);

        exibir();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();
        if (o == mCriarFilialButton) {
            //ID unico da rede.
            String id = JOptionPane.showInputDialog("Insira o ID único da rede");
            String nome = JOptionPane.showInputDialog("Insira o nome da nova filial");

            try {
                NodeHub filial = new NodeHub(true, nome);
                filial.setIpDoMaster(id);
                filial.queroCriarUmaSubRede();

                JOptionPane.showMessageDialog(null,
                        "A sub-rede foi criada com sucesso!!!");

                new LogadoScreen(filial);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (o == mCriarRedeButton) {

            String nome = JOptionPane.showInputDialog("Insira o nome da rede");

            try {
                MasterHub master = new MasterHub(nome);
                new LogadoScreen(master);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (o == mEntrarEmUmaFilialButton) {
            new EntrarFilialScreen();
        }
    }
}


