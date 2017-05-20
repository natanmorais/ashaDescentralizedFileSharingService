package test.java;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.utils.Utils;

import java.rmi.RemoteException;
import java.util.Date;

public class Computador1Test {
    public ILocalSuperNode mHub;

    public Computador1Test(String masterIp)
            throws IllegalAccessException, RemoteException, InstantiationException {
        mHub = new SuperNodeHub("Computador 1");
        for (SuperNode sn : mHub.getAvailableSuperNodes(masterIp)) {
            if (sn.getSubnetName().equals("Filial 1")) {
                mHub.enterSubNet(sn);
                break;
            }
        }

        if(mHub.getSuperNode() != null) {
            mHub.addFile(new SharedFile(Utils.ipify(), "1.txt", "", new Date().getTime()));
        }
    }

    public static void main(String[] args)
            throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        Computador1Test m = new Computador1Test(args[0]);
    }
}
