/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test2;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * @author fir3destr0yer
 */
public class Computador1 {
    private final NodeHub mHub;

    public Computador1() throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        mHub = new NodeHub(false, "Computador 1");
        //Entrar na rede da Filial 1.
        for (Node subRede : mHub.queroAListaDeSubRedesAtuais()) {
            if (subRede.nomeSubRede.equals("Filial 1")) {
                mHub.queroEntrarEmUmaSubRede(subRede);
                break;
            }
        }
        //Disponibilizar um Arquivo.
        mHub.queroCompartilharUmArquivo(new File("1.txt"));
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        new Computador1();
    }
}
