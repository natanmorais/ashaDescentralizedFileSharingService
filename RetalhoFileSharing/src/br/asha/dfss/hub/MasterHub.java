package br.asha.dfss.hub;

import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalMaster;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.repository.SharedFileList;
import br.asha.dfss.repository.SubNetList;
import br.asha.dfss.utils.Utils;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

public class MasterHub extends NodeHub implements IMaster, ILocalMaster {

    public MasterHub(String nome, String ip, int porta)
            throws RemoteException, InstantiationException, IllegalAccessException, MalformedURLException {
        super(true, nome, ip, porta);
        init();
    }

    public MasterHub(String nome, int porta)
            throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        super(true, nome, Utils.ipify(), porta);
        init();
    }

    public MasterHub(String nome)
            throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        super(true, nome);
        init();
    }

    private void init() {
        SubNetList.getInstance(getNome()).add(getMeuIp(), getNome(), getNome());
    }

    @RemoteMethod
    @Override
    public Object[] alguemQuerCriarUmaRede(String nome)
            throws RemoteException {
        Utils.log("alguemQuerCriarUmaRede(%s)", nome);

        //IP do cara que quer criar uma rede.
        final String ipDoCliente = getIpDoCliente();
        //Adicione-o.
        if (SubNetList.getInstance(getNome()).add(ipDoCliente, nome, nome)) {
            //Retorna a lista de IPs.
            return new Object[]{
                    SubNetList.getInstance(getNome()),
                    SharedFileList.getInstance(getNome())};
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
}
