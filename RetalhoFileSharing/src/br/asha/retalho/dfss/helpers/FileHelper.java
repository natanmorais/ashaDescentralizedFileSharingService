package br.asha.retalho.dfss.helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import br.asha.retalho.dfss.IFile;
import br.asha.retalho.dfss.rmi.RmiClient;

public class FileHelper
{
    public static void sendFileTo(String ip, File filename)
    {
        try(InputStream is = new FileInputStream(filename))
        {
            //Le o arquivo.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int length;
            byte[] buffer = new byte[1024];
            while((length = is.read(buffer)) > 0)
            {
                baos.write(buffer, 0, length);
            }

            //Enviar o arquivo.
            RmiClient<IFile> mFile = new RmiClient<>(ip, "FILE");
            mFile.getRemoteObj().writeFile(baos.toByteArray(), filename.getAbsolutePath());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static byte[] requestFileFrom(String ipServer, String ipClient, String name)
            throws RemoteException, NotBoundException, MalformedURLException
    {
        //Enviar o arquivo.
        RmiClient<IFile> mFile = new RmiClient<>(ipServer, "FILE");
        return mFile.getRemoteObj().requestFile(ipClient, name);
    }
}
