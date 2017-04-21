package br.asha.retalho.dfss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.asha.retalho.dfss.provider.SharedFilesProvider;
import br.asha.retalho.dfss.provider.SubNetMachinesProvider;
import br.asha.retalho.dfss.provider.SuperNodesProvider;
import br.asha.retalho.dfss.rmi.RmiClient;
import br.asha.retalho.dfss.rmi.RmiServer;
import br.asha.retalho.dfss.utils.Utils;

public class DfssServer
{
    private final RmiServer mFileServer;
    private final RmiServer mNodeServer;
    private OnFileListener mFileListener;

    private SuperNodesProvider mSuperNodeList;
    private SubNetMachinesProvider mMachineList;
    private SharedFilesProvider mSharedFileList;

    /**
     * Cria o servidor com o ip global.
     */
    public DfssServer()
            throws InstantiationException, IllegalAccessException, IOException
    {
        this(Utils.ipify());
    }

    /**
     * Cria o servidor com um ip especifico.
     */
    public DfssServer(String ip)
            throws IOException, InstantiationException, IllegalAccessException
    {
        mFileServer = new RmiServer(new FileImpl(), ip, "FILE");
        mNodeServer = new RmiServer(new NodeImpl(), ip, "NODE");
        mSuperNodeList = new SuperNodesProvider();
        mMachineList = new SubNetMachinesProvider();
        mSharedFileList = new SharedFilesProvider();
    }

    public interface OnFileListener
    {
        void onFileRequested(byte[] data);
    }

    private class NodeImpl extends UnicastRemoteObject implements INode
    {

        protected NodeImpl()
                throws RemoteException
        {
        }

        @Override
        public SuperNodesProvider.SuperNodeList requestNewSubNet(String ip, String subNetName)
                throws RemoteException
        {
            System.out.println(String.format("requestNewSubNet ip:%s subNetName: %s", ip, subNetName));
            SuperNodesProvider.SuperNode sn = new SuperNodesProvider.SuperNode();
            sn.ip = ip;
            sn.subnetName = subNetName;
            mSuperNodeList.add(sn);
            return mSuperNodeList.toList();
        }

        @Override
        public SuperNodesProvider.SuperNodeList requestAvailableSuperNodes()
                throws RemoteException
        {
            System.out.println(String.format("requestAvailableSuperNodes"));
            return mSuperNodeList.toList();
        }

        @Override
        public int requestNewMachine(String ip, String name)
                throws RemoteException
        {
            System.out.println(String.format("requestNewMachine ip:%s name: %s", ip, name));
            SubNetMachinesProvider.Machine m = new SubNetMachinesProvider.Machine();
            m.ip = ip;
            m.name = name;
            mMachineList.add(m);
            return 0;
        }
    }

    /**
     * Implementa as requisicoes relacionadas aos arquivos.
     */
    private class FileImpl extends UnicastRemoteObject implements IFile
    {

        protected FileImpl()
                throws RemoteException
        {
        }

        @Override
        public boolean writeFile(byte[] data, String name)
        {
            try
            {
                System.out.printf("Escrita do arquivo %s de %d bytes\n", name, data.length);

                //Cria o arquivo e escreve seu conteúdo.
                OutputStream os = new FileOutputStream(name);
                os.write(data);
                os.close();
                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

        /**
         * O cliente requisitou um arquivo. Retorna seu conteúdo caso exista.
         *
         * @param name nome do arquivo.
         * @return conteúdo do arquivo.
         */
        @Override
        public byte[] requestFile(String name)
                throws RemoteException
        {
            System.out.printf("Requisição do arquivo %s\n", name);

            //Caminho para o arquivo.
            File file = new File("/home/tiago/Downloads", name);
            //Arquivo existe?
            if(file.exists())
            {
                //Abre o fluxo.
                try(InputStream is = new FileInputStream(file))
                {
                    //Le todo o conteúdo do arquivo e armazena num buffer.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int length;
                    byte[] buffer = new byte[2048];
                    while((length = is.read(buffer)) > 0)
                    {
                        baos.write(buffer, 0, length);
                    }

                    //Retorna todo o conteúdo do arquivo.
                    System.out.printf("O arquivo %s tem %d bytes\n", name, baos.size());
                    byte[] data = baos.toByteArray();
                    if(mFileListener != null)
                    {
                        mFileListener.onFileRequested(data);
                    }
                    return data;
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        @Override
        public int updateSharedFileList(String id, String ip, String desc, String name, boolean firstReceptor)
                throws RemoteException
        {
            System.out.println(String.format("updateSharedFileList id:%s ip:%s desc:%s name: %s", id, ip, desc, name));

            SharedFilesProvider.SharedFile sf = new SharedFilesProvider.SharedFile();
            sf.id = id;
            sf.ip = ip;
            sf.desc = desc;
            sf.name = name;
            mSharedFileList.add(sf);

            if(firstReceptor)
            {
                for(SuperNodesProvider.SuperNode sn : mSuperNodeList.toList())
                {
                    try
                    {
                        RmiClient<IFile> fileClient = new RmiClient<>(sn.ip, "FILE");
                        fileClient.getRemoteObj().updateSharedFileList(id, ip, desc, name, false);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            return 0;
        }

        @Override
        public SharedFilesProvider.SharedFileList getSharedFileList()
                throws RemoteException
        {
            System.out.println(String.format("getSharedFileList"));
            return mSharedFileList.toList();
        }
    }
}
