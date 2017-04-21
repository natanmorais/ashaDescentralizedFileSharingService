package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SubNetMachinesProvider
{
    private static final String PATH = "machines.asha";
    private static final File NODES_FILE = new File(PATH);

    private MachineList mList;

    public SubNetMachinesProvider()
            throws IOException
    {
        if(!NODES_FILE.exists() && !NODES_FILE.createNewFile())
        {
            throw new IOException("Não foi possivel criar o arquivo.");
        }

        Gson gson = new Gson();
        mList = gson.fromJson(new FileReader(NODES_FILE), MachineList.class);

        if(mList == null)
        {
            mList = new MachineList();
            save();
        }
    }

    public void add(Machine machine)
    {
        mList.add(machine);
        save();
    }

    public void remove(Machine machine)
    {
        mList.remove(machine);
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

    public static class Machine implements Serializable
    {
        public String ip;
        public String name;
    }

    public static class MachineList extends ArrayList<Machine>
    {
    }
}
