package br.asha.dfss.remote;

import br.asha.dfss.RemoteMethod;
import br.asha.dfss.model.Node;
import br.asha.dfss.repository.Repository;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMaster extends INode, Remote {

    Repository<Node> alguemQuerCriarUmaRede(String nome)
            throws RemoteException;

    Repository<Node> alguemQuerAListaDeSubRedes()
        throws RemoteException;

    /*
    boolean requestNewSuperNode(String name)
            throws RemoteException;

    List<SuperNode> requestAvailableSuperNodes()
            throws RemoteException;
            */
}
