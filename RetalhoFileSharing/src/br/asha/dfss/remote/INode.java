package br.asha.dfss.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {

    boolean alguemQuerEntrarNaMinhaRede(String nome)
            throws RemoteException;

    boolean alguemQuerSaberSeEstouOnline()
            throws RemoteException;

    /*
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
            */
}
