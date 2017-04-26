package br.asha.dfss;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.rmi.RmiServer;

public abstract class DfssHub extends UnicastRemoteObject implements IHub
{
    //Tipo de máquina.
    private HubType mHubType;
    //Nome da máquina.
    private String mHubName;
    //Server
    private RmiServer mServer;

    public DfssHub(HubType hubType, String hubName, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        super();
        mHubType = hubType;
        mHubName = hubName;
        mServer = new RmiServer(this, ip, "RETALHO");
        mServer.start();
    }

    public static <T extends Remote> RmiClient<T> createClient(String ip)
    {
        try
        {
            return new RmiClient<>(ip, "RETALHO");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @LocalMethod
    public RmiServer getServer()
    {
        return mServer;
    }

    @Override
    @LocalMethod
    public HubType getHubType()
    {
        return mHubType;
    }

    @Override
    @LocalMethod
    public String getHubName()
    {
        return mHubName;
    }

    @Override
    @LocalMethod
    public String getServerIp()
    {
        return mServer.getIp();
    }

    @LocalMethod
    protected String getClientIp()
    {
        try
        {
            return getClientHost();
        }
        catch(ServerNotActiveException e)
        {
            return null;
        }
    }

    @Override
    @LocalMethod
    public void stop()
    {
        mServer.stop();
    }
}
