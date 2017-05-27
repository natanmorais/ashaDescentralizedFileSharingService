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
public class Filial2Religado {
    /*---ESPERA O FILIAL2 DESLIGAR E COMPUTADOR 2 ASSUMIR LUGAR---*/

    //FAZ PROCESSO DE RELIGAMENTO
    
    /*---VERIFICA SE REASSUMIU SUA POSIÇÃO E FAZ DESLIGAMENTO ABRUPTO---*/

    private final NodeHub mHub;

    public Filial2Religado() throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        mHub = new NodeHub(true, "Filial 2");
        mHub.religarComputador();
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException {
        new Filial2Religado();
    }
}
