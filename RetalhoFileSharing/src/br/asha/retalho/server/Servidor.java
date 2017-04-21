package br.asha.retalho.server;

import java.io.IOException;

import br.asha.retalho.dfss.DfssServer;
import br.asha.retalho.dfss.utils.Utils;


public class Servidor
{
    private String myIp;
    private String mHash;

    public Servidor()
            throws IOException
    {
    }

    public static void main(String[] args)
            throws IOException, InstantiationException, IllegalAccessException
    {
        new Servidor().start();
    }

    public void start()
            throws IllegalAccessException, IOException, InstantiationException
    {
        myIp = Utils.ipify();
        DfssServer server = new DfssServer(myIp);
    }

    public void createNewServer(String name)
    {
        mHash = myIp + "+" + name;
    }

    public String getHash()
    {
        return mHash;
    }
}
