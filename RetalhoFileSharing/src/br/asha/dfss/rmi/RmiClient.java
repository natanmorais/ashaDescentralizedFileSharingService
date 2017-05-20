package br.asha.dfss.rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;

/**
 * Cliente.
 * Responsável por acessar um objeto remotamente por meio de um ip e um nome.
 *
 * @param <T> Tipo do objeto a ser acessado.
 * @author Tiago Henrique de Melo.
 */
public class RmiClient<T extends Remote> {
    private static final int PORT = 1098;

    static {
        try {
            RMISocketFactory.setSocketFactory(new RMISocketFactory() {
                public Socket createSocket(String host, int port)
                        throws IOException {
                    Socket socket = new Socket();
                    socket.setSoTimeout(1000);
                    socket.setSoLinger(false, 0);
                    socket.connect(new InetSocketAddress(host, port), 1000);
                    return socket;
                }

                public ServerSocket createServerSocket(int port)
                        throws IOException {
                    return new ServerSocket(port);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final T remote;

    /**
     * Cria uma instância da classe Client.
     *
     * @param ip   Endereço IP do local onde se quer acessar o objeto.
     * @param name Nome do objeto a ser acessado.
     */
    public RmiClient(String ip, String name)
            throws RemoteException, NotBoundException, MalformedURLException {
        //Acessa um objeto remotamente através do ip do local onde está o objeto e seu nome.
        remote = (T) Naming.lookup("rmi://" + ip + ":" + PORT + "/" + name);
    }

    /**
     * Obtém o objeto remoto.
     */
    public T getRemoteObj() {
        return remote;
    }
}
