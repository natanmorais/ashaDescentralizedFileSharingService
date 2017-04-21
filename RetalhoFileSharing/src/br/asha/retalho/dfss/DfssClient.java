package br.asha.retalho.dfss;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import br.asha.retalho.dfss.helpers.FileHelper;
import br.asha.retalho.dfss.provider.SharedFilesProvider;
import br.asha.retalho.dfss.provider.SuperNodesProvider;
import br.asha.retalho.dfss.rmi.RmiClient;
import br.asha.retalho.dfss.utils.Utils;

public class DfssClient
{
    private String mFirstSuperNodeIp;
    private String myIp;
    private SuperNodesProvider.SuperNodeList mSuperNodeList;
    private String myName;

    public DfssClient(String pMyName, String firstSuperNodeIp)
    {
        mFirstSuperNodeIp = firstSuperNodeIp;
        myIp = Utils.ipify();
        myName = pMyName;
    }

    /**
     * @param name
     * @throws RemoteException
     * @throws NotBoundException
     * @throws MalformedURLException
     */
    public boolean criarUmNovaSubRede(String name)
            throws RemoteException, NotBoundException, MalformedURLException
    {
        RmiClient<INode> nodeClient = new RmiClient<>(mFirstSuperNodeIp, "NODE");
        mSuperNodeList = nodeClient.getRemoteObj().requestNewSubNet(myIp, name);

        if(mSuperNodeList == null)
        {
            return false;
        }

        for(SuperNodesProvider.SuperNode node : mSuperNodeList)
        {
            if(!myIp.equals(node.ip))
            {
                nodeClient = new RmiClient<>(node.ip, "NODE");
                nodeClient.getRemoteObj().requestNewSubNet(myIp, name);
            }
        }

        return true;
    }

    public void entrarSubRede(String subNetIp)
            throws NotBoundException, IOException
    {
        RmiClient<INode> nodeClient = new RmiClient<>(subNetIp, "NODE");
        if(nodeClient.getRemoteObj().requestNewMachine(myIp, myName) == 0)
        {
            OutputStream os = new FileOutputStream("mysupernode.asha");
            os.write(subNetIp.getBytes());
            os.close();
        }
    }

    public void novoArquivo(String subNetIp, String id, String desc, String name)
            throws RemoteException, MalformedURLException, NotBoundException
    {
        RmiClient<IFile> fileClient = new RmiClient<>(subNetIp, "FILE");
        fileClient.getRemoteObj().updateSharedFileList(id, myIp, desc, name, true);
    }

    public SharedFilesProvider.SharedFileList pegarListaArquivosDisponiveis(String subNetIp)
            throws RemoteException, MalformedURLException, NotBoundException
    {
        RmiClient<IFile> fileClient = new RmiClient<>(subNetIp, "FILE");
        return fileClient.getRemoteObj().getSharedFileList();
    }

    public SuperNodesProvider.SuperNodeList pegarListaSubRedesDisponiveis()
            throws RemoteException, NotBoundException, MalformedURLException
    {
        RmiClient<INode> nodeClient = new RmiClient<>(mFirstSuperNodeIp, "NODE");
        return nodeClient.getRemoteObj().requestAvailableSuperNodes();
    }

    public void requestFile(String ip, String name)
    {
        try
        {
            byte[] data = FileHelper.requestFileFrom(ip, name);
            if(data != null && data.length > 0)
            {
                try(OutputStream os = new FileOutputStream(name))
                {
                    os.write(data);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
