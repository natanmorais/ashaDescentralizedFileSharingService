/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test2;

import br.asha.dfss.hub.MasterHub;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * @author fir3destr0yer
 */
public class Master {
    private final MasterHub mHub;

    public Master() throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        mHub = new MasterHub("Asha");
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        new Master();
    }
}
