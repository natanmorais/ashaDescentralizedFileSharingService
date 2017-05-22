package test.java.local;

import br.asha.dfss.hub.MasterHub;
import br.asha.dfss.hub.NodeHub;
import br.asha.dfss.model.Node;
import br.asha.dfss.repository.Repository;
import br.asha.dfss.repository.SubNetList;
import br.asha.dfss.utils.Utils;

import java.io.File;
import java.rmi.RemoteException;

public class Test {

    public static void main(String[] args) throws IllegalAccessException, RemoteException, InstantiationException {
        //Master.
        MasterHub mMaster = new MasterHub("Asha", "127.0.0.1", 1098);
        //Novo super-nó.
        NodeHub mTiago = new NodeHub(true, "Tiago", "127.0.0.1", 1097);
        mTiago.setIpDoMaster("127.0.0.1");
        //Novo nó.
        NodeHub mNatan = new NodeHub(false, "Natan", "127.0.0.1", 1096);
        mNatan.setIpDoMaster("127.0.0.1");
        //Tiago quer criar uma rede.
        SubNetList listaDeSuperNos = (SubNetList)mTiago.queroCriarUmaSubRede();
        Utils.log("%s", listaDeSuperNos);

        //Natan quer entrar em uma sub-rede.
        //Usará o primeiro da lista.
        Utils.log("Entrou na sub-rede: %b",
                //Como Asha e Tiago tem o mesmo ip, vai entrar na Rede Asha pois usa a porta padrão
                mNatan.queroEntrarEmUmaSubRede(listaDeSuperNos.getByName("Asha")));
        //Asha é que vai acabar recebendo..
        mNatan.queroCompartilharUmArquivo(new File("meuArquivosParaCompartilhar/oi.txt"));
    }
}
