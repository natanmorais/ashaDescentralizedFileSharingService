package br.asha.dfss.view;

import br.asha.dfss.hub.MasterHub;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela inicial.
 */
public class InicioScreen extends BaseScreen implements ActionListener {

    private JButton mMatrizButton = new JButton("MATRIZ");
    private JButton mFilialButton = new JButton("FILIAL");
    private JButton mUsuarioButton = new JButton("USUÁRIO");

    public InicioScreen() {
        //Botoes da tela inicial.
        mMatrizButton.addActionListener(this);
        mFilialButton.addActionListener(this);
        mUsuarioButton.addActionListener(this);
        //Adiciona-os.
        add(mMatrizButton, WIDTH / 2, 150, 150, 95);
        add(mFilialButton, WIDTH / 2, 250, 150, 95);
        add(mUsuarioButton, WIDTH / 2, 350, 150, 95);
        //Show me!
        exibir();
    }

    public static void main(String[] args) {
        new InicioScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();
        if (o == mMatrizButton) {

            String nome = JOptionPane.showInputDialog("Digite um nome para a sua rede:");
            if (nome != null && nome.length() > 0) {
                try {
                    MasterHub hub = new MasterHub(nome);
                    new LogadoScreen(hub);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: Não foi possível criar a rede");
                    ex.printStackTrace();
                    return;
                }
            } else {
                return;
            }
        } else if (o == mFilialButton) {
            new CriarRedeScreen();
        } else if (o == mUsuarioButton) {
            new EntrarRedeScreen();
        } else {
            return;
        }

        dispose();
    }
}
