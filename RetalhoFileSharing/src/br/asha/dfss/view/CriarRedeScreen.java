package br.asha.dfss.view;

import br.asha.dfss.hub.NodeHub;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela de criar rede (Filial).
 */
public class CriarRedeScreen extends BaseScreen implements ActionListener {

    private JPlaceHolderTextField mIdInput = new JPlaceHolderTextField(null, "IP");
    private JPlaceHolderTextField mNomeInput = new JPlaceHolderTextField(null, "Nome");
    private JButton mCriarButton = new JButton("CRIAR");
    private NodeHub mHub;

    public CriarRedeScreen() {
        mCriarButton.addActionListener(this);
        //Adiciona-os.
        add(mIdInput, WIDTH / 2, 170, 400, 20);
        add(mNomeInput, WIDTH / 2, 195, 400, 20);
        add(mCriarButton, WIDTH / 2, 240, 400, 40);
        //Show me!
        exibir();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();
        if (o == mCriarButton) {
            //Campos válidos?
            if (mNomeInput.getText().length() <= 0 || mIdInput.getText().length() <= 0) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                return;
            }

            //Cria o nó e a rede.
            try {
                mHub = new NodeHub(true, mNomeInput.getText());
                mHub.setIpDoMaster(mIdInput.getText());
                mHub.queroCriarUmaSubRede();
                new LogadoScreen(mHub);
                super.dispose();
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro: Não foi possível criar a rede.");
                return;
            }
        }

        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
        new InicioScreen();
    }
}
