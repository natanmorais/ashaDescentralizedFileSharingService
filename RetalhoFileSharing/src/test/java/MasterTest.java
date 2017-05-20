package test.java;

import br.asha.dfss.hub.MasterHub;
import br.asha.dfss.local.ILocalMaster;

import java.rmi.RemoteException;

public class MasterTest {

    public ILocalMaster mHub;

    public MasterTest()
            throws IllegalAccessException, RemoteException, InstantiationException {
        mHub = new MasterHub("Asha", "Filial 0");
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        MasterTest m = new MasterTest();
    }
}
