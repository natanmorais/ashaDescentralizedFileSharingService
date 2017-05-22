package br.asha.dfss;

import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.rmi.RmiServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Core de um Hub.
 */
public abstract class DfssHub extends UnicastRemoteObject implements IHub {

    private String mNome;
    private RmiServer mServidor;

    /**
     * Cria uma instância de um Hub.
     */
    public DfssHub(String nome, String ip, int porta)
            throws RemoteException, InstantiationException, IllegalAccessException {
        super();
        mNome = nome;
        mServidor = new RmiServer(this, ip, porta, "RETALHO");
        mServidor.start();
    }

    /**
     * Cria uma instância de um Hub.
     */
    public DfssHub(String nome, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException {
        super();
        mNome = nome;
        mServidor = new RmiServer(this, ip, "RETALHO");
        mServidor.start();
    }

    public static <T extends Remote> RmiClient<T> criarUmCliente(String ip) {
        try {
            return new RmiClient<>(ip, "RETALHO");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pega o servidor.
     */
    @Override
    @LocalMethod
    public RmiServer getServidor() {
        return mServidor;
    }

    /**
     * Pega o nome da rede.
     */
    @Override
    @LocalMethod
    public String getNome() {
        return mNome;
    }

    /**
     * Pega o meu IP.
     */
    @Override
    @LocalMethod
    public String getMeuIp() {
        return mServidor.getIp();
    }

    /**
     * Pega o IP do cliente.
     */
    @LocalMethod
    protected String getIpDoCliente() {
        try {
            return getClientHost();
        } catch (ServerNotActiveException e) {
            return null;
        }
    }
}
