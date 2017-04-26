package br.asha.dfss.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.asha.dfss.model.SuperNode;

public interface IMaster extends ISuperNode, Remote
{
    boolean requestNewSuperNode(String name)
            throws RemoteException;

    List<SuperNode> requestAvailableSuperNodes()
            throws RemoteException;
}
