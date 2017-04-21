package br.asha.retalho.dfss.rmi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;

/**
 * Servidor.
 * Contém o objeto que será acessado remotamente.
 *
 * @author Tiago Henrique de Melo.
 */
public class RmiServer
{
    private static final int PORT = 1098;

    /**
     * Cria uma instância da classe Server.
     *
     * @param remote Objeto que será acessado remotamente.
     * @param ip     Endereço IP.
     * @param name   Nome do objeto.
     */
    public RmiServer(Remote remote, String ip, String name)
            throws IllegalAccessException, InstantiationException
    {
        init(remote, ip, name);
    }

    private void init(Remote remote, String ip, String name)
    {
        try
        {
            RemoteServer.setLog(System.out);
            //Cria um registro que aceita pedidos pela porta especificada.
            Registry registry = LocateRegistry.createRegistry(PORT);
            //Caminho com o ip, porta e nome.
            String uri = "rmi://" + ip + ":" + PORT + "/" + name;
            System.out.println(uri);
            //Vincula o caminho com um objeto que será acessado remotamente.
            Naming.rebind(uri, remote);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
