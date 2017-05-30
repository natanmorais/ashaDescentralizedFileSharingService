package test.java.remote.teste3;

import br.asha.dfss.hub.NodeHub;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class Filial1 {

    private final NodeHub hub;

    public Filial1() throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException, MalformedURLException {
        hub = new NodeHub(true, "Filial 1");
        hub.setIpDoMaster("200.235.87.125");
        hub.religarComputador();
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException, MalformedURLException {
        new Filial1();
    }
}

