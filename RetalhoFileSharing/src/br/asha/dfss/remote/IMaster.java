package br.asha.dfss.remote;

import br.asha.dfss.RemoteMethod;
import br.asha.dfss.model.Node;
import br.asha.dfss.repository.Repository;
import br.asha.dfss.repository.SubNetList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMaster extends INode, Remote {

    SubNetList alguemQuerCriarUmaRede(String nome)
            throws RemoteException;

    SubNetList alguemQuerAListaDeSubRedes()
        throws RemoteException;

    /*
    boolean requestNewSuperNode(String name)
            throws RemoteException;

    List<SuperNode> requestAvailableSuperNodes()
            throws RemoteException;
            */
}
