package br.asha.dfss;

import br.asha.dfss.rmi.RmiServer;

public interface IHub
{
    @LocalMethod
    RmiServer getServer();

    @LocalMethod
    HubType getHubType();

    @LocalMethod
    String getSubNetName();

    @LocalMethod
    String getServerIp();

    @LocalMethod
    void stop();
}
