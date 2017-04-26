package br.asha.dfss.hub;

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

import br.asha.dfss.DfssHub;
import br.asha.dfss.HubType;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalNode;
import br.asha.dfss.model.LocalFile;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.remote.INode;
import br.asha.dfss.remote.ISuperNode;
import br.asha.dfss.repository.LocalFileRepository;
import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.utils.Utils;

public abstract class NodeHub extends DfssHub implements INode, ILocalNode
{
    private static final LocalFileRepository mLocalFileList =
            new LocalFileRepository("mfiles.asha");

    private SuperNode mSuperNode = null;

    protected NodeHub(HubType hubType, String hubName, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        super(hubType, hubName, ip);
    }

    public NodeHub(String hubName, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        this(HubType.MASTER, hubName, ip);
    }

    public NodeHub(String hubName)
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        this(hubName, Utils.ipify());
    }

    @Override
    @RemoteMethod
    public String requestName()
            throws RemoteException
    {
        return getHubName();
    }

    @Override
    @RemoteMethod
    public byte[] requestDataFile(String name)
            throws RemoteException
    {
        LocalFile file = getLocalFileList().getByName(name);
        if(file != null) return file.getData();
        return null;
    }

    @Override
    @LocalMethod
    public LocalFileRepository getLocalFileList()
    {
        return mLocalFileList;
    }

    @Override
    @LocalMethod
    public boolean addLocalFile(File file)
    {
        try
        {
            getLocalFileList().add(file.getParent(), file.getName(), Utils.generateSHA1ForFile(file));
            getLocalFileList().save();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    @Override
    @LocalMethod
    public List<SuperNode> getAvailableSuperNodes(String masterIp)
    {
        RmiClient<IMaster> masterClient = createClient(masterIp);

        try
        {
            if(masterClient != null)
            {
                return masterClient.getRemoteObj().requestAvailableSuperNodes();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @LocalMethod
    public boolean enterSubNet(SuperNode node)
    {
        RmiClient<ISuperNode> superNodeClient = createClient(node.getIp());

        try
        {
            if(superNodeClient != null)
            {
                //TODO Tratar melhor o erro?
                if(superNodeClient.getRemoteObj().requestNewNode(getHubName()))
                {
                    mSuperNode = node;
                    return true;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    @LocalMethod
    public boolean hasSuperNode()
    {
        return getSuperNode() != null;
    }

    @Override
    @LocalMethod
    public SuperNode getSuperNode()
    {
        return mSuperNode;
    }

    @Override
    @LocalMethod
    public boolean addFile(SharedFile file)
    {
        if(hasSuperNode())
        {
            RmiClient<ISuperNode> superNodeClient = createClient(getSuperNode().getIp());

            if(superNodeClient != null)
            {
                try
                {
                    return superNodeClient.getRemoteObj().requestAddSharedFile(file);
                }
                catch(RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    @Override
    @LocalMethod
    public boolean removeFile(SharedFile file)
    {
        return false;
    }

    @Override
    @LocalMethod
    public boolean modifyFile(SharedFile oldFile, SharedFile newFile)
    {
        return false;
    }

    @Override
    @LocalMethod
    public List<SharedFile> getAvailableSharedFiles()
    {
        if(hasSuperNode())
        {
            RmiClient<ISuperNode> superNodeClient = createClient(getSuperNode().getIp());

            if(superNodeClient != null)
            {
                try
                {
                    return superNodeClient.getRemoteObj().requestAvailableSharedFiles();
                }
                catch(RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
