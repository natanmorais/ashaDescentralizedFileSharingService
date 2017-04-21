package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SharedFilesProvider
{
    private static final String PATH = "shared_files.asha";
    private static final File NODES_FILE = new File(PATH);

    private SharedFileList mList;

    public SharedFilesProvider()
            throws IOException
    {
        if(!NODES_FILE.exists() && !NODES_FILE.createNewFile())
        {
            throw new IOException("Não foi possivel criar o arquivo.");
        }

        Gson gson = new Gson();
        mList = gson.fromJson(new FileReader(NODES_FILE), SharedFileList.class);

        if(mList == null)
        {
            mList = new SharedFileList();
            save();
        }
    }

    public void add(SharedFile arquivo)
    {
        mList.add(arquivo);
        save();
    }

    public void remove(SharedFile arquivo)
    {
        mList.remove(arquivo);
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

    public SharedFileList toList()
    {
        return mList;
    }

    public void updateList(SharedFileList list)
    {
        mList = list;
        save();
    }

    public static class SharedFile implements Serializable
    {
        public String id;
        public String ip;
        public String desc;
        public String name;
    }

    public static class SharedFileList extends ArrayList<SharedFile>
    {
    }
}
