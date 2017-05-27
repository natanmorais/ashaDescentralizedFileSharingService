/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test2;

import br.asha.dfss.hub.NodeHub;

import java.rmi.RemoteException;

/**
 * @author fir3destr0yer
 */
public class Filial1 {

    private final NodeHub mHub;

    public Filial1() throws IllegalAccessException, RemoteException, InstantiationException {
        mHub = new NodeHub(true, "Filial 1");
        //Criar a sub-rede
        mHub.queroCriarUmaSubRede();
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException {
        new Filial1();
    }
}
