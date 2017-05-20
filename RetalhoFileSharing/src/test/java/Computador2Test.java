package test.java;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;

import java.rmi.RemoteException;
import java.util.List;

public class Computador2Test {
    public ILocalSuperNode mHub;

    public Computador2Test(String masterIp)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        mHub = new SuperNodeHub("Computador 2");
        for (SuperNode sn : mHub.getAvailableSuperNodes(masterIp)) {
            if (sn.getSubnetName().equals("Filial 2")) {
                mHub.enterSubNet(sn);
                break;
            }
        }

        if (mHub.getSuperNode() != null) {
            List<SharedFile> files = mHub.getAvailableSharedFiles();
            for (SharedFile file : files) {
                if (file.getName().equals("1.txt")) {
                    mHub.requestDataFile(file.getIp(), file.getName());
                }
            }
        }

        Thread.sleep(1000 * 60 * 11);

        System.out.println(mHub.getAvailableSharedFiles());
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        Computador2Test m = new Computador2Test(args[0]);
    }
}
