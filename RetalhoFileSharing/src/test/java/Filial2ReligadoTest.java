package test.java;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;

import java.rmi.RemoteException;
import java.util.Date;

public class Filial2ReligadoTest {
    public ILocalSuperNode mHub;

    public Filial2ReligadoTest()
            throws IllegalAccessException, RemoteException, InstantiationException {
        mHub = new SuperNodeHub("Filial 2");

        String ip = mHub.requestSuperNodeFromSubNet();
        System.out.println(mHub.letMeBeTheSuperNode(ip));
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        Filial2ReligadoTest m = new Filial2ReligadoTest();
    }
}
