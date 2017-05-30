package test.java.remote.teste3;

import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class Computador1 {

    private NodeHub hub;

    public Computador1() throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException, MalformedURLException {
        hub = new NodeHub(false, "Computador 1");
        hub.setIpDoMaster("200.235.87.125");

        for (Node subRede : hub.queroAListaDeSubRedesAtuais()) {
            if (subRede.nomeSubRede.equals("Filial 1")) {
                hub.queroEntrarEmUmaSubRede(subRede);
                break;
            }
        }

        for (SharedFile sf : hub.queroAListaDeArquivosCompartilhados()) {
            if (sf.nome.endsWith("1.txt")) {
                byte[] data = hub.queroOArquivo(sf);
                if (data != null) {
                    try (FileOutputStream fos = new FileOutputStream(new File(sf.nome).getName())) {
                        IOUtils.write(data, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Thread.sleep(1000 * 60);

        hub.queroAListaDeArquivosCompartilhados();
    }

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException, InterruptedException, MalformedURLException {
        new Computador1();
    }
}
