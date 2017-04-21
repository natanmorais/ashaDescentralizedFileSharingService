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
import java.io.FileInputStream;

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
        String nomeSubRede = nodeClient.getRemoteObj().requestNewMachine(myIp, myName); 
        if(nomeSubRede != null)
        {
            OutputStream os = new FileOutputStream("mysupernode.asha");
            os.write(subNetIp.getBytes());
            os.write("+".getBytes());
            os.write(nomeSubRede.getBytes());
            os.close();
        }
    }
    
    public void religamentoSistema() {
        try {
            FileInputStream is = new FileInputStream("mysupernode.asha");
            byte[] data = new byte[128];
            int length = is.read(data);
            String file = new String(data, 0, length);
            String ip = file.split("\\+")[0];
            String name = file.split("\\+")[1];
            is.close();
            
            try {
                RmiClient<INode> nodeClient = new RmiClient<>(ip, "NODE");
                nodeClient.getRemoteObj().areYouUp();
                return;
            }catch(Exception e){
                //Ler IP de algum computador que fez transferência em um LOG
                String ipPC = "200.235.88.221";
                RmiClient<INode> nodeClient = new RmiClient<>(ipPC, "NODE");
                String superNode = nodeClient.getRemoteObj().whichIsYourSuperNode();
                nodeClient = new RmiClient<>(superNode, "NODE");
                SuperNodesProvider.SuperNode sn = nodeClient.getRemoteObj().verifySuperNodeDown(ip, name);
                if(sn==null){
                    //Desligamento Abrupto.
                } else {
                    nodeClient = new RmiClient<>(sn.ip, "NODE");
                    nodeClient.getRemoteObj().requestNewMachine(myIp, myName);
                }
            }
            
        } catch (Exception ex) {
            //deu ruim
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
