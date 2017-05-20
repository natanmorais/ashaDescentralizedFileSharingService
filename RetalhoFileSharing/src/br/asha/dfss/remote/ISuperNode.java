package br.asha.dfss.remote;

import br.asha.dfss.RemoteMethod;
import br.asha.dfss.model.SharedFile;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ISuperNode extends Remote {
    @RemoteMethod
    boolean requestNewNode(String name)
            throws RemoteException;

    @RemoteMethod
    List<SharedFile> sendAvailableSharedFiles()
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

    @RemoteMethod
    boolean sendNetStatus()
            throws RemoteException;

    @RemoteMethod
    String sendSuperNodeFromSubNet(String name)
            throws RemoteException;

    @RemoteMethod
    Object[] leaveSuperNodeFunction()
            throws RemoteException;

    @RemoteMethod
    boolean transformSuperNode(Object[] listas)
            throws RemoteException;

    @RemoteMethod
    Object[] sendUpdate()
            throws RemoteException;

    @RemoteMethod
    String requestName()
            throws RemoteException;

    @RemoteMethod
    byte[] sendDataFile(String name)
            throws RemoteException;

    @RemoteMethod
    String findSuperNodeFromSubNet(String name)
            throws RemoteException;
}
