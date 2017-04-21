package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TransferFileProvider
{
    private static final String PATH = "transferedfiles.asha";
    private static final File NODES_FILE = new File(PATH);

    private static TransferFileProvider mTransferFile;

    private TransferFileList mList;

    public TransferFileProvider()
            throws IOException
    {
        if(!NODES_FILE.exists() && !NODES_FILE.createNewFile())
        {
            throw new IOException("Não foi possivel criar o arquivo.");
        }

        Gson gson = new Gson();
        mList = gson.fromJson(new FileReader(NODES_FILE), TransferFileList.class);

        if(mList == null)
        {
            mList = new TransferFileList();
            save();
        }
    }

    public static TransferFileProvider getInstance()
    {
        if(mTransferFile == null)
        {
            try
            {
                mTransferFile = new TransferFileProvider();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return mTransferFile;
    }

    public void add(String ip, String filename)
    {
        TransferFile tf = new TransferFile();
        tf.ip = ip;
        tf.filename = filename;
        mList.add(tf);
        save();
    }

    public void remove(TransferFile node)
    {
        mList.remove(node);
        save();
    }

    public void clear()
    {
        mList.clear();
        save();
    }

    public void save()
    {
        Gson gson = new Gson();
        String json = gson.toJson(mList);

        try
        {
            OutputStream os = new FileOutputStream(NODES_FILE);
            os.write(json.getBytes());
            os.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public TransferFileList toList()
    {
        return mList;
    }

    public static class TransferFile implements Serializable
    {
        public String ip;
        public String filename;
        public long date;

        public TransferFile()
        {
            date = new Date().getTime();
        }
    }

    public static class TransferFileList extends ArrayList<TransferFileProvider.TransferFile>
    {
    }
}
