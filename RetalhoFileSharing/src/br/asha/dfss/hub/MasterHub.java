package br.asha.dfss.hub;

import java.rmi.RemoteException;
import java.util.List;

import br.asha.dfss.HubType;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalMaster;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.utils.Utils;

public class MasterHub extends SuperNodeHub implements IMaster, ILocalMaster
{
    public MasterHub(String hubName, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        super(HubType.MASTER, hubName, ip);
    }

    public MasterHub(String hubName)
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        super(HubType.MASTER, hubName, Utils.ipify());
    }

    @Override
    @LocalMethod
    public String getUID()
    {
        String uid = getServerIp() + "+" + getHubName();
        //TODO Usar função de criptografia e converter para hexadecimal.
        return uid;
    }

    @Override
    @RemoteMethod
    public boolean requestNewSuperNode(String name)
            throws RemoteException
    {
        String clientIp = getClientIp();

        Utils.log("registerSuperNode: %s:%s", name, clientIp);

        //Registrar uma nova sub-rede (IP e Nome).
        if(getSuperNodeList().add(clientIp, name) &&
                getSuperNodeList().save())
        {
            Utils.log("Sub-rede %s:%s registrada", name, clientIp);
            return true;
        }
        else
        {
            Utils.log("Erro ao registrar a sub-rede %s:%s", name, clientIp);
            return false;
        }
    }

    @Override
    @RemoteMethod
    public List<SuperNode> requestAvailableSuperNodes()
            throws RemoteException
    {
        Utils.log("getAvailableSuperNodes");
        return getSuperNodeList().toList();
    }
}
