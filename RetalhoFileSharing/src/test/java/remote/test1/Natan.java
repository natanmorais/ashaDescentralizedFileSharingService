/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test1;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.repository.SharedFileList;
import br.asha.dfss.repository.SubNetList;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author fir3destr0yer
 */
public class Natan {
    //CRIA NÓ NORMAL
    //BUSCA REDES DISPONÍVEIS
    //ENTRA NA REDE DO TIAGO
    //DISPONIBILIZA ARQUIVO PARA COMPARTILHAMENTO
    //BUSCA ARQUIVOS DISPONÍVEIS
    //BAIXA ARQUIVO DE TIAGO

    public Natan() throws IllegalAccessException, IOException, InstantiationException {
        NodeHub mNode = new NodeHub(false, "Natan", "200.235.88.53", 1098);
        mNode.setIpDoMaster("200.235.84.219");
        SubNetList list = mNode.queroAListaDeSubRedesAtuais();
        System.out.println(list);
        for (Node node : list) {
            if (node.nomeSubRede.equals("Tiago")) {
                mNode.queroEntrarEmUmaSubRede(node);
                return;
            }
        }
        mNode.queroCompartilharUmArquivo(new File("b.txt"));
        SharedFileList list2 = mNode.queroAListaDeArquivosCompartilhados();
        System.out.println(list2);
        for (SharedFile sf : list2) {
            if (sf.nome.endsWith("a")) {
                byte[] data = mNode.queroOArquivo(sf);
                try (OutputStream os = new FileOutputStream(new File(sf.nome).getName())) {
                    IOUtils.write(data, os);
                }
                break;
            }
        }
    }
    
    public static void main(String[] args ){
        try {
            new Natan();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Natan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Natan.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Natan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
