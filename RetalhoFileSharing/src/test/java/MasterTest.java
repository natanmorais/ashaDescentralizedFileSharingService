package test.java;

import java.rmi.RemoteException;

import br.asha.dfss.hub.MasterHub;
import br.asha.dfss.local.ILocalMaster;

public class MasterTest
{
    public ILocalMaster mHub;

    public MasterTest()
            throws IllegalAccessException, RemoteException, InstantiationException
    {
        mHub = new MasterHub("M", "192.168.0.3");
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException
    {
        MasterTest m = new MasterTest();
    }
}
