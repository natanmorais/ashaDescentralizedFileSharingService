package test.java;

import br.asha.dfss.hub.MasterHub;
import br.asha.dfss.local.ILocalMaster;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.utils.Utils;

import java.rmi.RemoteException;
import java.util.Date;

public class TransferFileSendTest {

    private ILocalMaster mMaster;

    public TransferFileSendTest() {
        try {
            mMaster = new MasterHub("Asha", "Tiago");
            mMaster.addFile(new SharedFile(Utils.ipify(), "oi.txt", "JIEURU4RI39", new Date().getTime()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TransferFileSendTest();
    }
}
