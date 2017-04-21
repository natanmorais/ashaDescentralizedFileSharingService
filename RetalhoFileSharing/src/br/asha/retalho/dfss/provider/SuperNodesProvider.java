package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    }

    public void add(SuperNode node)
    {
        mList.add(node);
    }

    public void remove(SuperNode node)
    {
        mList.remove(node);
    }

    public void clear()
    {
        mList.clear();
    }

    public SuperNodeList toList()
    {
        return mList;
    }

    public static class SuperNode
    {
        public String ip;
        public String subnetName;
    }

    public static class SuperNodeList extends ArrayList<SuperNode>
    {
    }
}
