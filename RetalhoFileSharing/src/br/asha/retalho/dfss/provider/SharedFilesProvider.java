package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    }

    public void add(SharedFile arquivo)
    {
        mList.add(arquivo);
    }

    public void remove(SharedFile arquivo)
    {
        mList.remove(arquivo);
    }

    public void clear()
    {
        mList.clear();
    }

    public SharedFileList toList()
    {
        return mList;
    }

    public static class SharedFile
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
