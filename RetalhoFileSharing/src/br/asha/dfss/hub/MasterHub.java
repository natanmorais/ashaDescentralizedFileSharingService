package br.asha.dfss.hub;

import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalMaster;
import br.asha.dfss.model.Node;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.repository.SubNetNodeList;
import br.asha.dfss.repository.Repository;
import br.asha.dfss.repository.SubNetList;
import br.asha.dfss.utils.Utils;

import java.rmi.RemoteException;

public class MasterHub extends NodeHub implements IMaster, ILocalMaster {

    public MasterHub(String nome, String ip, int porta)
            throws RemoteException, InstantiationException, IllegalAccessException {
        super(true, nome, ip, porta);
        init();
    }

    public MasterHub(String nome, int porta)
            throws IllegalAccessException, RemoteException, InstantiationException {
        super(true, nome, Utils.ipify(), porta);
        init();
    }

    public MasterHub(String nome)
            throws IllegalAccessException, RemoteException, InstantiationException {
        super(true, nome);
        init();
    }

    private void init() {
        SubNetList.getInstance(getNome()).add(getMeuIp(), getNome(), getNome());
    }

    @RemoteMethod
    @Override
    public SubNetList alguemQuerCriarUmaRede(String nome)
            throws RemoteException {
        Utils.log("alguemQuerCriarUmaRede(%s)", nome);

        //IP do cara que quer criar uma rede.
        final String ipDoCliente = getIpDoCliente();
        //Adicione-o.
        if (SubNetList.getInstance(getNome()).add(ipDoCliente, nome, nome)) {
            //Retorna a lista de IPs.
            return SubNetList.getInstance(getNome());
        } else {
            return null;
        }
    }

    @RemoteMethod
    @Override
    public SubNetList alguemQuerAListaDeSubRedes()
            throws RemoteException {
        return SubNetList.getInstance(getNome());
    }

    /*

    @Override
    @RemoteMethod
    public boolean requestNewSuperNode(String nome)
            throws RemoteException {
        String clientIp = getIpDoCliente();

        Utils.log("registerSuperNode: %s:%s", nome, clientIp);

        //Registrar uma nova sub-rede (IP e Nome).
        if (getSuperNodeList().add(clientIp, nome) &&
                getSuperNodeList().save()) {
            Utils.log("Sub-rede %s:%s registrada", nome, clientIp);
            return true;
        } else {
            Utils.log("Erro ao registrar a sub-rede %s:%s", nome, clientIp);
            return false;
        }
    }

    @Override
    @RemoteMethod
    public List<SuperNode> requestAvailableSuperNodes()
            throws RemoteException {
        Utils.log("getAvailableSuperNodes");
        return getSuperNodeList().toList();
    }

    */
}
