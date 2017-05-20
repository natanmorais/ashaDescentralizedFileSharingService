package test.java;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;

import java.rmi.RemoteException;

public class Filial1Test {
    public ILocalSuperNode mHub;

    public Filial1Test(String masterIp)
            throws IllegalAccessException, RemoteException, InstantiationException {
        mHub = new SuperNodeHub("Filial 1");
        mHub.createNewSubNet(masterIp, "Filial 1");
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        Filial1Test m = new Filial1Test(args[0]);
    }
}
