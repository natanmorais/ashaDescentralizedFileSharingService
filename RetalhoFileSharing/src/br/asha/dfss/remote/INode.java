package br.asha.dfss.remote;

import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.repository.Repository;
import br.asha.dfss.repository.SharedFileList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {

    boolean alguemQuerEntrarNaMinhaRede(String nome)
            throws RemoteException;

    boolean alguemQuerSaberSeEstouOnline()
            throws RemoteException;

    boolean alguemQuerCompartilharUmArquivo(SharedFile file, boolean reenviar)
        throws RemoteException;

    SharedFileList alguemQuerAListaDeArquivosCompartilhados()
            throws RemoteException;

    byte[] alguemQuerUmArquivo(String nome)
            throws RemoteException;

    Node alguemQuerSaberOSuperNoDaSubRede(String nomeDaRede)
            throws RemoteException;

    /*
    @RemoteMethod
    boolean requestNewNode(String nome)
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
    boolean insertNewSuperNode(String nome)
            throws RemoteException;

    @RemoteMethod
    boolean sendNetStatus()
            throws RemoteException;

    @RemoteMethod
    String sendSuperNodeFromSubNet(String nome)
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
    byte[] sendDataFile(String nome)
            throws RemoteException;

    @RemoteMethod
    String findSuperNodeFromSubNet(String nome)
            throws RemoteException;
            */
}
