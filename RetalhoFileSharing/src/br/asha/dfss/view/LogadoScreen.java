package br.asha.dfss.view;

import br.asha.dfss.hub.NodeHub;

public class LogadoScreen extends BaseScreen {

    private NodeHub mHub;

    public LogadoScreen(NodeHub hub) {
        mHub = hub;

        exibir();
    }
}
