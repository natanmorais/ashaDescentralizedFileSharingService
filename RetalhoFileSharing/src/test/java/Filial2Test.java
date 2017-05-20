package test.java;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;

import java.rmi.RemoteException;

public class Filial2Test {
    public ILocalSuperNode mHub;

    public Filial2Test(String masterIp)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        mHub = new SuperNodeHub("Filial 2");
        mHub.createNewSubNet(masterIp, "Filial 2");

        Thread.sleep(1000 * 60 * 10);

        System.out.println("estou desligando");

        mHub.shutdown();
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        Filial2Test m = new Filial2Test(args[0]);
    }
}
