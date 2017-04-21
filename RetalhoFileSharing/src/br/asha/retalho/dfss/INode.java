package br.asha.retalho.dfss;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import br.asha.retalho.dfss.provider.SuperNodesProvider;

public interface INode extends Remote
{
    /**
     * Uma nova sub-rede foi criada e necessario inserir na lista do super-nó.
     *
     * @param ip         o ip do super-nó da nova sub-rede.
     * @param subNetName Nome da nova sub-rede.
     */
    SuperNodesProvider.SuperNodeList requestNewSubNet(String ip, String subNetName)
            throws RemoteException;

    /**
     * Uma máquina requisita a lista de sub-redes disponíveis para a conexão.
     */
    SuperNodesProvider.SuperNodeList requestAvailableSuperNodes()
            throws RemoteException;

    /**
     * Uma máquina requista sua entrada em uma sub-rede especifica.
     *
     * @param ip   O ip da máquina que deseja entrar na sub-rede.
     * @param name O nome da máquina.
     */
    int requestNewMachine(String ip, String name)
            throws RemoteException;

    boolean requestRemoveSubNet(String name)
            throws RemoteException;

    SuperNodesProvider.SuperNode verifySuperNodeDown(String ip, String name)
            throws RemoteException, MalformedURLException, NotBoundException;

    String whichIsYourSuperNode()
            throws RemoteException;

    boolean areYouUp()
            throws RemoteException;
}
