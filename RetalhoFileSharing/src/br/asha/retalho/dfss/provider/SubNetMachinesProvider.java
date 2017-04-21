package br.asha.retalho.dfss.provider;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    }

    public void add(Machine machine)
    {
        mList.add(machine);
    }

    public void remove(Machine machine)
    {
        mList.remove(machine);
    }

    public void clear()
    {
        mList.clear();
    }

    public static class Machine
    {
        public String ip;
        public String name;
    }

    public static class MachineList extends ArrayList<Machine>
    {
    }
}
