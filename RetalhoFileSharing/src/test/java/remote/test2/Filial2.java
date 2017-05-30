/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test2;

import br.asha.dfss.hub.NodeHub;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * @author fir3destr0yer
 */
public class Filial2 {
    private final NodeHub mHub;

    public Filial2() throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException, MalformedURLException {
        mHub = new NodeHub(true, "Filial 2");
        //Criar a sub-rede
        mHub.queroCriarUmaSubRede();
        //6 min.
        Thread.sleep(1000 * 60 * 6);
        mHub.vouDesligar();
        System.exit(0);
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException, MalformedURLException {
        new Filial2();
    }
}
