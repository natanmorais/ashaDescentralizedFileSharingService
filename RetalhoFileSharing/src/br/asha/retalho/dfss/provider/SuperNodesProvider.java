package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SuperNodesProvider
{
    private static final String PATH = "nodes.asha";
    private static final File NODES_FILE = new File(PATH);

    private SuperNodeList mList;

    public SuperNodesProvider()
            throws IOException
    {
        if(!NODES_FILE.exists() && !NODES_FILE.createNewFile())
        {
            throw new IOException("Não foi possivel criar o arquivo.");
        }

        Gson gson = new Gson();
        mList = gson.fromJson(new FileReader(NODES_FILE), SuperNodeList.class);

        if(mList == null)
        {
            mList = new SuperNodeList();
            save();
        }
    }

    public void add(SuperNode node)
    {
        mList.add(node);
        save();
    }

    public void remove(SuperNode node)
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

    public SuperNodeList toList()
    {
        return mList;
    }

    public static class SuperNode implements Serializable
    {
        public String ip;
        public String subnetName;
    }

    public static class SuperNodeList extends ArrayList<SuperNode>
    {
    }
}
