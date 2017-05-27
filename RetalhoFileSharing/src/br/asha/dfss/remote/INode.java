package br.asha.dfss.remote;

import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.repository.SharedFileList;
import br.asha.dfss.repository.SubNetNodeList;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface INode extends Remote {

    SubNetNodeList alguemQuerEntrarNaMinhaRede(String nome)
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

    boolean altereOSuperNoDeUmaSubRede(String nome)
            throws RemoteException;

    boolean vouTransformarEmSuperNo(Object[] listas)
            throws RemoteException;

    void euTenhoUmNovoVizinho(Node node)
            throws RemoteException;

    boolean existeUmaNovaEleicao(long dataUltimaModificacao, long tamanho, long tempoDeInicio, int ganhador)
            throws RemoteException;

    Object[] oSuperNoDeAlguemCaiu()
            throws RemoteException;

    String mePassaSeuSuperNo()
            throws RemoteException;

    void agoraSouSeuSuperNo(String ip)
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
