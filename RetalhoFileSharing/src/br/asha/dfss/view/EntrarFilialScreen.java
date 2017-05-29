package br.asha.dfss.view;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntrarFilialScreen extends BaseScreen implements ActionListener, ListSelectionListener {

    private JTextField mNomeComputadorInput = new JTextField();
    private JTextField mIDRedeInput = new JTextField();
    private JButton mPesquisarRedes = new JButton("PESQUISAR");
    private JList<Node> mListaDeRedes = new JList<>();
    private JButton mEntrarButton = new JButton("ENTRAR");
    private NodeHub hub;
    private Node noSelecionado;

    public EntrarFilialScreen() {

        mListaDeRedes.addListSelectionListener(this);

        add(mNomeComputadorInput, 50, 50);
        add(mIDRedeInput, 50, 100);
        add(mListaDeRedes, 0, 150);
        add(mEntrarButton, 200, 450);

        exibir();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object o = e.getSource();

        if (o == mPesquisarRedes) {
            try {
                hub = new NodeHub(false, mNomeComputadorInput.getText());
                String id = mIDRedeInput.getText();
                hub.setIpDoMaster(id);

                DefaultListModel<Node> model = new DefaultListModel<>();
                for (Node node : hub.queroAListaDeSubRedesAtuais()) {
                    model.addElement(node);
                }

                mListaDeRedes.setModel(model);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (o == mEntrarButton) {
            if (noSelecionado != null) {
                hub.queroEntrarEmUmaSubRede(noSelecionado);
                new LogadoScreen(hub);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int sel = mListaDeRedes.getMaxSelectionIndex();
        if (mListaDeRedes.isSelectedIndex(sel)) {
            noSelecionado = mListaDeRedes.getModel().getElementAt(sel);
        }
    }
}
