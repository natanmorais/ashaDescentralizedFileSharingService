/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test2;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * @author fir3destr0yer
 */
public class Computador2 {
    private final NodeHub mHub;

    public Computador2() throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        mHub = new NodeHub(false, "Computador 1");
        //Entrar na rede da Filial 2.
        for (Node subRede : mHub.queroAListaDeSubRedesAtuais()) {
            if (subRede.nomeSubRede.equals("Filial 2")) {
                mHub.queroEntrarEmUmaSubRede(subRede);
                break;
            }
        }
        //Pegar um Arquivo.
        for (SharedFile sf : mHub.queroAListaDeArquivosCompartilhados()) {
            if (sf.nome.endsWith("1.txt")) {
                byte[] data = mHub.queroOArquivo(sf);
                if (data != null) {
                    try (FileOutputStream fos = new FileOutputStream(new File(sf.nome).getName())) {
                        IOUtils.write(data, fos);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        new Computador2();
    }
}
