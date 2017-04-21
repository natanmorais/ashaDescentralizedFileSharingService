package br.asha.retalho.dfss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.asha.retalho.dfss.rmi.RmiServer;
import br.asha.retalho.dfss.utils.Utils;

public class DfssServer
{
    private final RmiServer mServer;

    /**
     * Cria o servidor com o ip global.
     */
    public DfssServer()
            throws InstantiationException, IllegalAccessException, RemoteException
    {
        this(Utils.ipify());
    }

    /**
     * Cria o servidor com um ip especifico.
     */
    public DfssServer(String ip)
            throws RemoteException, InstantiationException, IllegalAccessException
    {
        mServer = new RmiServer(new FileImpl(), ip, "FILE");
    }

    /**
     * Implementa as requisicoes relacionadas aos arquivos.
     */
    private static class FileImpl extends UnicastRemoteObject implements IFile
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
                    return baos.toByteArray();
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
    }
}
