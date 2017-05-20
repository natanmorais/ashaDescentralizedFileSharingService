package test.java;

import br.asha.dfss.hub.SuperNodeHub;
import br.asha.dfss.local.ILocalSuperNode;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;

import java.rmi.RemoteException;

public class TransferFileReceiveTest {

    private ILocalSuperNode mSuperNode;

    public TransferFileReceiveTest(String masterIp) {
        try {
            mSuperNode = new SuperNodeHub("Asha");
            for (SuperNode sn : mSuperNode.getAvailableSuperNodes(masterIp)) {
                if (sn.getSubnetName().equals("Tiago")) {
                    mSuperNode.enterSubNet(sn);
                }
            }

            for (SharedFile sf : mSuperNode.getAvailableSharedFiles()) {
                if (sf.getName().equals("oi.txt")) {
                    mSuperNode.requestDataFile(sf.getIp(), sf.getName());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TransferFileReceiveTest(args[0]);
    }
}
