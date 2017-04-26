package br.asha.dfss.hub;

import java.rmi.RemoteException;
import java.util.List;

import br.asha.dfss.DfssHub;
import br.asha.dfss.HubType;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalSuperNode;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.remote.ISuperNode;
import br.asha.dfss.repository.NodeRepository;
import br.asha.dfss.repository.SharedFileRepository;
import br.asha.dfss.repository.SuperNodeRepository;
import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.utils.Utils;

public class SuperNodeHub extends NodeHub implements ISuperNode, ILocalSuperNode
{
    private static final SuperNodeRepository mSuperNodeList =
            new SuperNodeRepository("snodes.asha");

    private static final NodeRepository mNodeList =
            new NodeRepository("nodes.asha");

    private static final SharedFileRepository mSharedFileList =
            new SharedFileRepository("sharedfiles.asha");

    private String mSubNetName;

    protected SuperNodeHub(HubType hubType, String hubName, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        super(hubType, hubName, ip);
    }

    public SuperNodeHub(String name, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        this(HubType.SUPER_NODE, name, ip);
    }

    public SuperNodeHub(String name)
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        this(name, Utils.ipify());
    }

    public static NodeRepository getNodeList()
    {
        return mNodeList;
    }

    public static SharedFileRepository getSharedFileList()
    {
        return mSharedFileList;
    }

    public SuperNodeRepository getSuperNodeList()
    {
        return mSuperNodeList;
    }

    @Override
    @RemoteMethod
    public String requestName()
            throws RemoteException
    {
        Utils.log("requestName: %s", mSubNetName);
        return mSubNetName;
    }

    @Override
    @RemoteMethod
    public boolean requestNewNode(String name)
            throws RemoteException
    {
        String clientIp = getClientIp();

        Utils.log("requestNewNode: %s:%s", name, clientIp);

        //Registrar uma nova máquina (IP e Nome).
        if(getNodeList().add(clientIp, name) &&
                getNodeList().save())
        {
            Utils.log("Maquina %s:%s registrada", name, clientIp);
            return true;
        }
        else
        {
            Utils.log("Erro ao registrar a maquina %s:%s", name, clientIp);
            return false;
        }
    }

    @Override
    @RemoteMethod
    public List<SharedFile> requestAvailableSharedFiles()
            throws RemoteException
    {
        Utils.log("requestAvailableSharedFiles: %d", getSharedFileList().toList().size());

        return getSharedFileList().toList();
    }

    @Override
    @RemoteMethod
    public boolean requestAddSharedFile(SharedFile file)
            throws RemoteException
    {
        Utils.log("requestAddSharedFile: %s", file);
        //Registra o arquivo.
        //TODO espero q não dê merda!!
        if(getSharedFileList().add(file))
        {
            getSharedFileList().save();
            //Envia para os outros super-nós.
            addSharedFileByBroadcast(file);
            return true;
        }
        else
        {
            //Se já registrou quer dizer q já enviou para os outros.
            return false;
        }
    }

    private void addSharedFileByBroadcast(SharedFile file)
    {
        for(SuperNode sn : getSuperNodeList().toList())
        {
            if(!sn.getIp().equals(getServerIp()))
            {
                //Envia para o super-nó.
                RmiClient<ISuperNode> superNodeClient = DfssHub.createClient(sn.getIp());
                if(superNodeClient != null)
                {
                    try
                    {
                        superNodeClient.getRemoteObj().requestAddSharedFile(file);
                    }
                    catch(RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean requestRemoveSharedFile(SharedFile file)
            throws RemoteException
    {
        Utils.log("requestRemoveSharedFile: %s", file);
        //Registra o arquivo.
        //TODO espero q não dê merda!!
        if(getSharedFileList().remove(file))
        {
            getSharedFileList().save();
            //Envia para os outros super-nós.
            removeSharedFileByBroadcast(file);
            return true;
        }
        else
        {
            //Se já removeu quer dizer q já enviou para os outros.
            return false;
        }
    }

    private void removeSharedFileByBroadcast(SharedFile file)
    {
        for(SuperNode sn : getSuperNodeList().toList())
        {
            if(!sn.getIp().equals(getServerIp()))
            {
                //Envia para o super-nó.
                RmiClient<ISuperNode> superNodeClient = DfssHub.createClient(sn.getIp());
                if(superNodeClient != null)
                {
                    try
                    {
                        superNodeClient.getRemoteObj().requestAddSharedFile(file);
                    }
                    catch(RemoteException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public String getSubNetName()
    {
        return mSubNetName;
    }

    @Override
    @LocalMethod
    public boolean createNewSubNet(String masterIp, String subNetName)
    {
        mSubNetName = subNetName;

        RmiClient<IMaster> masterClient = createClient(masterIp);

        if(masterClient != null)
        {
            try
            {
                //Registra a rede no master.
                //TODO Erro ao criar a rede pois já existe uma com o mesmo nome.
                return masterClient.getRemoteObj().requestNewSuperNode(getSubNetName());
            }
            catch(RemoteException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            //TODO Erro a conectar com o Master.
        }

        return false;
    }
}
