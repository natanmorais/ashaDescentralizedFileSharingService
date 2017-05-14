package br.asha.dfss.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.asha.dfss.RemoteMethod;
import br.asha.dfss.model.SharedFile;

public interface ISuperNode extends INode, Remote
{
    @RemoteMethod
    boolean requestNewNode(String name)
            throws RemoteException;

    @RemoteMethod
    List<SharedFile> requestAvailableSharedFiles()
            throws RemoteException;

    @RemoteMethod
    boolean requestAddSharedFile(SharedFile file)
            throws RemoteException;

    @RemoteMethod
    boolean requestRemoveSharedFile(SharedFile file)
            throws RemoteException;

    @RemoteMethod
    boolean insertNewSuperNode(String name)
            throws RemoteException;
}
