/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.java.remote.test1;

import br.asha.dfss.hub.MasterHub;
import br.asha.dfss.model.SharedFile;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author fir3destr0yer
 */
public class Tiago {
    //CRIA MASTER
    //DISPONILIZA ARQUIVO PARA COMPARTILHAMENTO
    //SLEEP 8 MIN
    //BUSCA ARQUIVOS DISPONÍVEIS
    //BAIXA ARQUIVO DE NATAN

    public Tiago() throws IllegalAccessException, IOException, InstantiationException, InterruptedException {
        MasterHub mMaster = new MasterHub("Tiago", "200.235.84.219", 1098);
        mMaster.setIpDoMaster(null);
        mMaster.queroCompartilharUmArquivo(new File("/home/tiago/Área de Trabalho/a"));
        Thread.sleep(1000 * 60 * 3);
        List<SharedFile> files = mMaster.queroAListaDeArquivosCompartilhados();
        System.out.println(files);
        for (SharedFile sf : files) {
            if (sf.nome.endsWith("b.txt")) {
                byte[] data = mMaster.queroOArquivo(sf);
                try (OutputStream os = new FileOutputStream(new File(sf.nome).getName())) {
                    IOUtils.write(data, os);
                }
                break;
            }
        }
    }
}
