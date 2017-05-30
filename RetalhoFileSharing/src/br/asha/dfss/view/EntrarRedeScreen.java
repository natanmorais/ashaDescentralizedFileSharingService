package br.asha.dfss.view;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela de entrar em uma rede (Usuário).
 */
public class EntrarRedeScreen extends BaseScreen implements ActionListener {

    private JPlaceHolderTextField mIdInput = new JPlaceHolderTextField(null, "IP");
    private JPlaceHolderTextField mNomeInput = new JPlaceHolderTextField(null, "Nome");
    private JButton mEntrarButton = new JButton("ENTRAR");
    private JButton mPesquisarButton = new JButton("PESQUISAR");
    private JList<Node> mListaSuperNos = new JList<>();
    private NodeHub mHub;

    public EntrarRedeScreen() {
        mEntrarButton.addActionListener(this);
        mPesquisarButton.addActionListener(this);
        //Adiciona-os.
        add(mIdInput, WIDTH / 2, 70, 400, 20);
        add(mNomeInput, WIDTH / 2, 95, 400, 20);
        add(mPesquisarButton, WIDTH / 2, 135, 400, 40);
        add(mEntrarButton, WIDTH / 2, 440, 400, 40);
        add(mListaSuperNos, WIDTH / 2, 290, 400, 250);
        //Show me!
        exibir();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();
        //Entrar em uma rede.
        if (o == mEntrarButton) {
            if (mHub == null) return;
            //Pega a rede selecionada.
            Node node = mListaSuperNos.getSelectedValue();
            //Rede foi selecionada mesmo?
            if (node == null) {
                JOptionPane.showMessageDialog(null, "Selecione uma rede, por favor.");
                return;
            }
            //Entra em uma rede.
            if (!mHub.queroEntrarEmUmaSubRede(node)) {
                JOptionPane.showMessageDialog(null, "Erro: Não foi possível conectar à rede: " + node.nomeSubRede);
                return;
            }
            //Mostre me a tela principal.
            new LogadoScreen(mHub);
            super.dispose();
            return;
        }
        //Exibe a lista de redes disponiveis.
        else if (o == mPesquisarButton) {
            if (mNomeInput.getText().length() <= 0 || mIdInput.getText().length() <= 0) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
                return;
            }

            try {
                mHub = new NodeHub(false, mNomeInput.getText());
                mHub.setIpDoMaster(mIdInput.getText());
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro: Os campos preenchidos são inválidos.");
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
