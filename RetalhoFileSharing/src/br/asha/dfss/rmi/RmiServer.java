package br.asha.dfss.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

/**
 * Servidor.
 * Contém o objeto que será acessado remotamente.
 *
 * @author Tiago Henrique de Melo.
 */
public class RmiServer {
    private static final int PORT = 1098;

    private static Registry mRegistry = null;

    private Remote mRemote;
    private String mName;
    private String mUri;
    private String mIp;
    private boolean mIsRunning;
    private int mPort;

    /**
     * Cria uma instância da classe Server.
     *
     * @param remote Objeto que será acessado remotamente.
     * @param ip     Endereço IP.
     * @param port   Porta.
     * @param name   Nome do objeto.
     */
    public RmiServer(Remote remote, String ip, int port, String name)
            throws IllegalAccessException, InstantiationException {
        mRemote = remote;
        mName = name;
        mPort = port;
        mIp = ip;
    }

    /**
     * Cria uma instância da classe Server.
     *
     * @param remote Objeto que será acessado remotamente.
     * @param ip     Endereço IP.
     * @param name   Nome do objeto.
     */
    public RmiServer(Remote remote, String ip, String name)
            throws InstantiationException, IllegalAccessException {
        this(remote, ip, PORT, name);
    }

    public void start() throws RemoteException, MalformedURLException {
        if (mRegistry == null) {
            mRegistry = LocateRegistry.createRegistry(mPort);
        }

        mIsRunning = true;

        RemoteServer.setLog(System.out);
        //Cria um registro que aceita pedidos pela porta especificada.
        //Caminho com o ip, porta e nome.
        mUri = "rmi://" + mIp + ":" + mPort + "/" + mName;
        System.out.println(mUri);
        //Vincula o caminho com um objeto que será acessado remotamente.
        Naming.rebind(mUri, mRemote);
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public String getName() {
        return mName;
    }

    public String getIp() {
        return mIp;
    }

    public String getUri() {
        return mUri;
    }

    public void stop() {
        try {
            mIsRunning = !UnicastRemoteObject.unexportObject(mRemote, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
