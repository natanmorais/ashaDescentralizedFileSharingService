package test.java.remote.teste3;

import br.asha.dfss.hub.MasterHub;

import java.io.File;
import java.rmi.RemoteException;

public class Master {

    private final MasterHub hub;

    public Master() throws IllegalAccessException, RemoteException, InstantiationException {
        hub = new MasterHub("Asha");
        hub.queroCompartilharUmArquivo(new File("1.txt"));
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException {
        new Master();
    }
}
