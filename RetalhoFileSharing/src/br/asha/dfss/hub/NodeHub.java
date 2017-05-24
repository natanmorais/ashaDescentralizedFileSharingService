package br.asha.dfss.hub;

import br.asha.dfss.DfssHub;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalNode;
import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.remote.INode;
import br.asha.dfss.repository.SharedFileList;
import br.asha.dfss.repository.SubNetList;
import br.asha.dfss.repository.SubNetNodeList;
import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.utils.Utils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.List;

public class NodeHub extends DfssHub implements INode, ILocalNode {

    private boolean euSouUmSuperNo;
    private Node meuSuperNo;
    private String ipDoMaster;

    public NodeHub(boolean euSouUmSuperNo, String nome, String ip, int porta)
            throws RemoteException, InstantiationException, IllegalAccessException {
        super(nome, ip, porta);
        this.euSouUmSuperNo = euSouUmSuperNo;
        init();
    }

    public NodeHub(boolean euSouUmSuperNo, String nome, int porta)
            throws IllegalAccessException, RemoteException, InstantiationException {
        super(nome, Utils.ipify(), porta);
        this.euSouUmSuperNo = euSouUmSuperNo;
        init();
    }

    public NodeHub(boolean euSouUmSuperNo, String nome)
            throws IllegalAccessException, RemoteException, InstantiationException {
        super(nome, Utils.ipify());
        this.euSouUmSuperNo = euSouUmSuperNo;
        init();
    }

    private void init() {
        if (euSouUmSuperNo) {
            SubNetNodeList.getInstance(getNome()).add(getMeuIp(), getNome(), getNome());
        }
        SharedFileList.getInstance(getNome()).carregar();
    }

    public void setIpDoMaster(String ipDoMaster) {
        this.ipDoMaster = ipDoMaster;
    }

    /**
     * Um super-nó quer criar uma sub-rede.
     *
     * @return retorna a lista de sub-redes existentes.
     */
    @LocalMethod
    @Override
    public SubNetList queroCriarUmaSubRede() {
        Utils.log("queroCriarUmaSubRede(%s)", ipDoMaster);

        //Só um super-nó pode criar uma rede.
        if (!euSouUmSuperNo) {
            return null;
        }

        //Cria o cliente.
        RmiClient<IMaster> c = criarUmCliente(ipDoMaster);
        //O Master aceitou a conexão.
        if (c != null) {
            Utils.log("o Master aceitou a conexao");
            try {
                //Pede ao master para registrar-se.
                SubNetList listaDeSubRedes =
                        c.getRemoteObj().alguemQuerCriarUmaRede(getNome());
                Utils.log("O Master retornou: %s", listaDeSubRedes);
                //Substitui sua lista com a lista retornada do Master.
                if (listaDeSubRedes != null) {
                    SubNetList.getInstance(getNome()).replace(listaDeSubRedes);
                    Utils.log("Substituí a lista de sub-redes");
                    return listaDeSubRedes;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //TODO Erro desconhecido.
                return null;
            }
        } else {
            Utils.log("o master recusou a conexao");
            //TODO O Mestre está morto.
        }

        return null;
    }

    @LocalMethod
    @Override
    public SubNetList queroAListaDeSubRedesAtuais() {
        Utils.log("queroAListaDeSubRedesAtuais");

        //Cria o cliente.
        RmiClient<IMaster> c = criarUmCliente(ipDoMaster);
        //O Master aceitou a conexão.
        if (c != null) {
            Utils.log("o master aceitou a conexao");
            try {
                //Retorna a lista de sub-redes cadastrada no master.
                return c.getRemoteObj().alguemQuerAListaDeSubRedes();
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Utils.log("o master recusou a conexao");
            return null;
        }
    }

    /**
     * Quero entrar em uma sub-rede. Já escolhi uma rede chamando queroAListaDeSubRedesAtuais().
     *
     * @param subRede A sub-rede que quer entrar.
     */
    @LocalMethod
    @Override
    public boolean queroEntrarEmUmaSubRede(Node subRede) {
        Utils.log("queroEntrarEmUmaSubRede(%s)", subRede);

        //Cria o cliente.
        RmiClient<INode> c = criarUmCliente(subRede.ip);
        //O Super-Nó aceitou a conexão.
        if (c != null) {
            Utils.log("o super-nó aceitou a conexao");
            try {
                //Cadastro edefutado com sucesso.
                if (c.getRemoteObj().alguemQuerEntrarNaMinhaRede(getNome())) {
                    meuSuperNo = subRede;
                    return true;
                } else {
                    return false;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            Utils.log("o super-nó recusou a conexao");
            return false;
        }
    }

    @RemoteMethod
    @Override
    public boolean alguemQuerEntrarNaMinhaRede(String nome)
            throws RemoteException {
        Utils.log("alguemQuerEntrarNaMinhaRede(%s)", nome);

        //IP do cara que quer entrar em uma rede.
        final String ipDoCliente = getIpDoCliente();
        //Adiciona o cara.
        return SubNetNodeList.getInstance(getNome()).add(new Node(ipDoCliente, nome, getNome()));
    }

    @LocalMethod
    @Override
    public boolean queroSaberSeEstaOnline(String ip) {
        Utils.log("queroSaberSeEstaOnline(%s)", ip);

        //Cria o cliente.
        RmiClient<INode> c = criarUmCliente(ip);
        try {
            //Alguém aceitou a conexão e está disponível.
            return c != null && c.getRemoteObj().alguemQuerSaberSeEstouOnline();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RemoteMethod
    @Override
    public boolean alguemQuerSaberSeEstouOnline()
            throws RemoteException {
        Utils.log("alguemQuerSaberSeEstouOnline()");
        return true;
    }

    @LocalMethod
    @Override
    public boolean queroCompartilharUmArquivo(File file) {
        SharedFile sharedFile;

        try {
            sharedFile = new SharedFile(getMeuIp(), file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Utils.log("queroCompartilharUmArquivo(%s)", sharedFile);

        //Eu sou um nó.
        if (!euSouUmSuperNo && meuSuperNo != null) {
            RmiClient<INode> superNo = criarUmCliente(meuSuperNo.ip);
            if (superNo != null) {
                try {
                    return superNo.getRemoteObj().alguemQuerCompartilharUmArquivo(sharedFile, true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        //Eu sou um super-nó.
        else if (euSouUmSuperNo && ipDoMaster != null) {
            RmiClient<INode> master = criarUmCliente(ipDoMaster);
            if (master != null) {
                try {
                    return master.getRemoteObj().alguemQuerCompartilharUmArquivo(sharedFile, true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        //Eu sou o master
        else if (this instanceof MasterHub) {
            try {
                return alguemQuerCompartilharUmArquivo(sharedFile, true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    @RemoteMethod
    public boolean alguemQuerCompartilharUmArquivo(SharedFile file, boolean reenviar)
            throws RemoteException {
        Utils.log("alguemQuerCompartilharUmArquivo(%s, %b)", file, reenviar);

        //O mesmo arquivo (SHA e Nome batem) mas ips diferentes. Varios computadores com o mesmo arquivo.
        //O mesmo arquivo (Nomes batem) mas conteudo diferente. Duas versões de um mesmo arquivo na rede.
        //Arquivos diferentes mas ip iguais. Computador com vários arq. pra compartilhar.
        if (SharedFileList.getInstance(getNome()).add(file)) {
            Utils.log("O arquivo foi adicionado");
            if (reenviar) {
                Utils.log("Reenviando...");
                //Reenvia para outros nós.
                for (Node node : SubNetList.getInstance(getNome())) {
                    //Não enviar pra mim mesmo.
                    if (!node.ip.equals(getMeuIp())) {
                        //Abre a conexao com um super-nó.
                        RmiClient<INode> superNo = criarUmCliente(node.ip);
                        //Conexao aceita.
                        if (superNo != null) {
                            try {
                                //Informa o compartilhamento do arquivo ao super-nó.
                                superNo.getRemoteObj().alguemQuerCompartilharUmArquivo(file, false);
                            } catch (Exception ignored) {
                                //nada
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @LocalMethod
    @Override
    public SharedFileList queroAListaDeArquivosCompartilhados() {
        Utils.log("queroAListaDeArquivosCompartilhados()");

        //Eu sou um nó.
        if (!euSouUmSuperNo && meuSuperNo != null) {
            RmiClient<INode> superNo = criarUmCliente(meuSuperNo.ip);
            if (superNo != null) {
                try {
                    return superNo.getRemoteObj().alguemQuerAListaDeArquivosCompartilhados();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        //Eu sou um super-nó.
        else if (euSouUmSuperNo && ipDoMaster != null) {
            try {
                return alguemQuerAListaDeArquivosCompartilhados();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        //Eu sou o master
        else if (this instanceof MasterHub) {
            try {
                return alguemQuerAListaDeArquivosCompartilhados();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @RemoteMethod
    @Override
    public SharedFileList alguemQuerAListaDeArquivosCompartilhados()
            throws RemoteException {
        Utils.log("%s: alguemQuerAListaDeArquivosCompartilhados()", getNome());
        return SharedFileList.getInstance(getNome());
    }

    //Executa essa funcao para cada arquivo encontrado (com o mesmo nome) se houver um erro.
    //O dado retornado será salvo atraves de um SaveDialog e posteriormente o usuario poderá compartilhá-lo.
    //No programa haverá uma lista local com os arq. que podem ser compartilhados.
    @LocalMethod
    @Override
    public byte[] queroOArquivo(SharedFile file) {
        Utils.log("queroOArquivo(%s)", file);

        RmiClient<INode> no = criarUmCliente(file.ip);
        if (no != null) {
            try {
                return no.getRemoteObj().alguemQuerUmArquivo(file.nome);
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    @RemoteMethod
    @Override
    public byte[] alguemQuerUmArquivo(String nome)
            throws RemoteException {
        Utils.log("alguemQuerUmArquivo(%s)", nome);

        File file = new File(nome);
        try (FileInputStream fis = new FileInputStream(file)) {
            return IOUtils.readFully(fis, (int) file.length());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * O Super-nó pode perguntar quem é o super-nó da rede dele após ser religado
     * para tomar sua posição.
     */
    @LocalMethod
    @Override
    public Node quemEOSuperNoDaSubRede(String nomeDaSubRede) {
        //Perguntar ao master quem é.
        //Eu posso ser um super-nó que acabou de ser religado.
        if (euSouUmSuperNo && ipDoMaster != null) {
            RmiClient<INode> master = criarUmCliente(ipDoMaster);
            if (master != null) {
                try {
                    return master.getRemoteObj().alguemQuerSaberOSuperNoDaSubRede(nomeDaSubRede);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @RemoteMethod
    @Override
    public Node alguemQuerSaberOSuperNoDaSubRede(String nomeDaRede)
            throws RemoteException {
        List<Node> nos = SubNetList.getInstance(getNome()).getBySubNetName(nomeDaRede);
        return nos.size() > 0 ? nos.get(0) : null;
    }

    /*
    @Override
    @RemoteMethod
    public boolean requestNewNode(String nome)
            throws RemoteException {
        String clientIp = getIpDoCliente();

        Utils.log("requestNewNode: %s:%s", nome, clientIp);

        //Registrar uma nova máquina (IP e Nome).
        if (getNodeList().add(clientIp, nome) &&
                getNodeList().save()) {
            Utils.log("Maquina %s:%s registrada", nome, clientIp);
            return true;
        } else {
            Utils.log("Erro ao registrar a maquina %s:%s", nome, clientIp);
            return false;
        }
    }

    @Override
    @RemoteMethod
    public List<SharedFile> sendAvailableSharedFiles()
            throws RemoteException {
        Utils.log("sendAvailableSharedFiles: %d", getSharedFileList().toList().size());

        return getSharedFileList().toList();
    }

    @Override
    @RemoteMethod
    public boolean requestAddSharedFile(SharedFile file)
            throws RemoteException {
        Utils.log("requestAddSharedFile: %s", file);
        //Registra o arquivo.
        //TODO espero q não dê merda!!
        if (getSharedFileList().add(file)) {
            getSharedFileList().save();
            //Envia para os outros super-nós.
            addSharedFileByBroadcast(file);
            return true;
        } else {
            //Se já registrou quer dizer q já enviou para os outros.
            return false;
        }
    }

    @Override
    @RemoteMethod
    public boolean sendNetStatus() {
        return true;
    }

    @Override
    public String sendSuperNodeFromSubNet(String nome) {
        SuperNode sn = getSuperNodeList().getByName(nome);
        if (sn != null) return sn.getIp();
        return null;
    }

    @Override
    @LocalMethod
    public String requestSuperNodeFromSubNet() {
        for (SuperNode sn : getSuperNodeList()) {
            try {
                RmiClient<ISuperNode> nodeClient = criarUmCliente(sn.getIp());
                return nodeClient.getRemoteObj().sendSuperNodeFromSubNet(getNome());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    @LocalMethod
    public boolean letMeBeTheSuperNode(String ip) {
        try {
            RmiClient<ISuperNode> superNodeClient = null;

            try {
                superNodeClient = criarUmCliente(ip);
            } catch (Exception e) {
                requestMySuperNode();
                return true;
            }

            for (SuperNode sn : getSuperNodeList()) {
                try {
                    RmiClient<ISuperNode> superNodeClient2 = criarUmCliente(sn.getIp());
                    superNodeClient2.getRemoteObj().insertNewSuperNode(getNome());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Object[] listas = superNodeClient.getRemoteObj().leaveSuperNodeFunction();
            if (listas != null && listas.length == 3) {
                getSuperNodeList().replace((List<SuperNode>) listas[0]);
                getSharedFileList().replace((List<SharedFile>) listas[1]);
                getNodeList().replace((List<Node>) listas[2]);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @RemoteMethod
    public Object[] leaveSuperNodeFunction() {
        Object[] listas = new Object[3];
        listas[0] = getSuperNodeList().toList();
        listas[1] = getSharedFileList().toList();
        listas[2] = getNodeList().toList();
        return listas;
    }

    private void addSharedFileByBroadcast(SharedFile file) {
        for (SuperNode sn : getSuperNodeList().toList()) {
            if (!sn.getIp().equals(getMeuIp())) {
                //Envia para o super-nó.
                RmiClient<ISuperNode> superNodeClient = DfssHub.criarUmCliente(sn.getIp());
                if (superNodeClient != null) {
                    try {
                        superNodeClient.getRemoteObj().requestAddSharedFile(file);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    @RemoteMethod
    public boolean requestRemoveSharedFile(SharedFile file)
            throws RemoteException {
        Utils.log("requestRemoveSharedFile: %s", file);
        //Registra o arquivo.
        //TODO espero q não dê merda!!
        if (getSharedFileList().remove(file)) {
            getSharedFileList().save();
            //Envia para os outros super-nós.
            removeSharedFileByBroadcast(file);
            return true;
        } else {
            //Se já removeu quer dizer q já enviou para os outros.
            return false;
        }
    }

    @RemoteMethod
    @Override
    public boolean insertNewSuperNode(String nome)
            throws RemoteException {
        for (SuperNode sn : getSuperNodeList()) {
            if (sn.getSubnetName().equalsIgnoreCase(nome)) {
                sn.setIp(getIpDoCliente());
                getSuperNodeList().save();
                return true;
            }
        }
        return false;
    }

    private void removeSharedFileByBroadcast(SharedFile file) {
        for (SuperNode sn : getSuperNodeList().toList()) {
            if (!sn.getIp().equals(getMeuIp())) {
                //Envia para o super-nó.
                RmiClient<ISuperNode> superNodeClient = criarUmCliente(sn.getIp());
                if (superNodeClient != null) {
                    try {
                        superNodeClient.getRemoteObj().requestAddSharedFile(file);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public String getNome() {
        return mSubNetName;
    }

    public boolean addFileInSuperNode(SharedFile file) {
        getSharedFileList().add(file);
        getSharedFileList().save();
        addSharedFileByBroadcast(file);
        return true;
    }

    @Override
    @LocalMethod
    public boolean createNewSubNet(String masterIp, String subNetName) {
        mSubNetName = subNetName;
        RmiClient<IMaster> masterClient = null;

        try {
            masterClient = criarUmCliente(masterIp);
        } catch (Exception e) {
            return false;
        }

        if (masterClient != null) {
            try {
                //Registra a rede no master.
                //TODO Erro ao criar a rede pois já existe uma com o mesmo nome.
                return masterClient.getRemoteObj().requestNewSuperNode(getNome());

                //TODO Enviar para os outros super-nós da lista avisando que ele é um novo super-nó.
                //sendIAmNewSuperNode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            //TODO Erro a conectar com o Master.
        }

        return false;
    }

    private void sendIAmNewSuperNode(String masterIp, List<SuperNode> superNodesList) {
        for (SuperNode sn : superNodesList) {
            if (sn.getIp().equals(masterIp) || sn.getIp().equals(getMeuIp())) continue;

            RmiClient<ISuperNode> superNodeClient = criarUmCliente(sn.getIp());

            if (superNodeClient != null) {
                //superNodeClient.getRemoteObj().insertNewSuperNode(getNome());
            }
        }
    }

    @Override
    @LocalMethod
    public void shutdown() {
        Object[] listas = new Object[3];
        listas[0] = getSuperNodeList().toList();
        listas[1] = getSharedFileList().toList();
        listas[2] = getNodeList().toList();

        for (Node n : getNodeList()) {
            RmiClient<ISuperNode> superNodeClient = criarUmCliente(n.getIp());
            try {
                if (superNodeClient.getRemoteObj().transformSuperNode(listas)) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @RemoteMethod
    public boolean transformSuperNode(Object[] listas) {
        if (listas != null && listas.length == 3) {
            getSuperNodeList().replace((List<SuperNode>) listas[0]);
            getSharedFileList().replace((List<SharedFile>) listas[1]);
            getNodeList().replace((List<Node>) listas[2]);
            for (SuperNode sn : getSuperNodeList()) {
                try {
                    RmiClient<ISuperNode> superNodeClient = criarUmCliente(sn.getIp());
                    superNodeClient.getRemoteObj().insertNewSuperNode(getNome());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (SuperNode sn : getSuperNodeList()) {
                RmiClient<ISuperNode> superNodeClient = null;
                try {
                    superNodeClient = criarUmCliente(sn.getIp());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    listas = superNodeClient.getRemoteObj().leaveSuperNodeFunction();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return false;
                }
                getSuperNodeList().replace((List<SuperNode>) listas[0]);
                getSharedFileList().replace((List<SharedFile>) listas[1]);
                getNodeList().replace((List<Node>) listas[2]);
                break;
            }
            return true;
        }
        return false;
    }

    @Override
    @RemoteMethod
    public String findSuperNodeFromSubNet(String nome) {
        if (isSuperNode()) {
            SuperNode sn = getSuperNodeList().getByName(nome);
            if (sn != null) return sn.getIp();
            return null;
        } else {
            try {
                RmiClient<ISuperNode> superNodeClient = criarUmCliente(mSuperNode.getIp());
                return superNodeClient.getRemoteObj().sendSuperNodeFromSubNet(nome);
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    @RemoteMethod
    public Object[] sendUpdate() {
        if (isSuperNode()) {
            Object[] listas = new Object[3];
            listas[0] = getSuperNodeList().toList();
            listas[1] = getSharedFileList().toList();
            listas[2] = getNodeList().toList();
            return listas;
        } else {
            RmiClient<ISuperNode> superNodeClient = criarUmCliente(mSuperNode.getIp());
            try {
                return superNodeClient.getRemoteObj().leaveSuperNodeFunction();
            } catch (RemoteException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    @LocalMethod
    public void requestMySuperNode() {
        for (Log log : LogRepository.getInstance()) {
            try {
                RmiClient<ISuperNode> nodeClient = criarUmCliente(log.getIp());
                String ip = nodeClient.getRemoteObj().findSuperNodeFromSubNet(getNome());

                if (ip == null) {
                    continue;
                } else if (ip.equals(mSuperNode.getIp())) {
                    for (SuperNode sn : getSuperNodeList()) {
                        RmiClient<ISuperNode> superNodeClient = criarUmCliente(sn.getIp());
                        superNodeClient.getRemoteObj().insertNewSuperNode(getNome());
                    }
                    //TODO Vai se tornar o super-nó.
                    Object[] listas = nodeClient.getRemoteObj().sendUpdate();
                    getSuperNodeList().replace((List<SuperNode>) listas[0]);
                    getSharedFileList().replace((List<SharedFile>) listas[1]);
                    getNodeList().replace((List<Node>) listas[2]);
                } else {
                    //TODO Eu sou da sua sub-rede
                    RmiClient<ISuperNode> superNodeClient = criarUmCliente(ip);
                    if (superNodeClient.getRemoteObj().requestNewNode(getNome())) {
                        mSuperNode = new SuperNode(ip, getNome());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @RemoteMethod
    public String requestName()
            throws RemoteException {
        return getNome();
    }

    @Override
    @RemoteMethod
    public byte[] sendDataFile(String nome)
            throws RemoteException {
        File file = new File(nome);

        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                return IOUtils.readFully(fis, (int) file.length());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    @LocalMethod
    public boolean requestNetStatus() {
        RmiClient<ISuperNode> superNodeClient = null;

        try {
            superNodeClient = criarUmCliente(getIpDoCliente());
        } catch (Exception e) {
            e.printStackTrace();
            requestMySuperNode();
            return false;
        }

        try {
            return superNodeClient.getRemoteObj().sendNetStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @LocalMethod
    public boolean requestDataFile(String ip, String nome) {
        RmiClient<ISuperNode> nodeClient = null;

        try {
            nodeClient = criarUmCliente(ip);
        } catch (Exception e) {
            return false;
        }

        try {
            byte[] data = nodeClient.getRemoteObj().sendDataFile(nome);
            try (OutputStream os = new FileOutputStream(nome)) {
                os.write(data);
                LogRepository.getInstance().add(new Log(ip, new Date().getTime(), nome));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    @LocalMethod
    public LocalFileRepository getLocalFileList() {
        return mLocalFileList;
    }

    @Override
    @LocalMethod
    public boolean addLocalFile(File file) {
        try {
            getLocalFileList().add(file.getParent(), file.getName(), Utils.generateSHA1ForFile(file));
            getLocalFileList().save();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @LocalMethod
    public List<SuperNode> getAvailableSuperNodes(String masterIp) {
        RmiClient<IMaster> masterClient = criarUmCliente(masterIp);

        try {
            if (masterClient != null) {
                return masterClient.getRemoteObj().requestAvailableSuperNodes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @LocalMethod
    public boolean enterSubNet(SuperNode node) {
        RmiClient<ISuperNode> superNodeClient = null;

        try {
            superNodeClient = criarUmCliente(node.getIp());
        } catch (Exception e) {
            return false;
        }

        try {
            if (superNodeClient != null) {
                //TODO Tratar melhor o erro?
                if (superNodeClient.getRemoteObj().requestNewNode(getNome())) {
                    mSuperNode = node;
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    @LocalMethod
    public boolean hasSuperNode() {
        return getSuperNode() != null;
    }

    @Override
    @LocalMethod
    public SuperNode getSuperNode() {
        return mSuperNode;
    }

    @Override
    @LocalMethod
    public boolean addFile(SharedFile file) {
        RmiClient<ISuperNode> superNodeClient = null;

        if (hasSuperNode()) {
            try {
                superNodeClient = criarUmCliente(getSuperNode().getIp());
            } catch (Exception e) {
                e.printStackTrace();
                requestMySuperNode();
            }

            if (superNodeClient != null) {
                try {
                    return superNodeClient.getRemoteObj().requestAddSharedFile(file);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else {
            addFileInSuperNode(file);
        }

        return false;
    }

    @Override
    @LocalMethod
    public boolean removeFile(SharedFile file) {
        return false;
    }

    @Override
    @LocalMethod
    public boolean modifyFile(SharedFile oldFile, SharedFile newFile) {
        return false;
    }

    @Override
    @LocalMethod
    public List<SharedFile> getAvailableSharedFiles() {
        if (hasSuperNode()) {
            RmiClient<ISuperNode> superNodeClient = null;

            try {
                superNodeClient = criarUmCliente(getSuperNode().getIp());
            } catch (Exception e) {
                requestMySuperNode();
                return null;
            }

            if (superNodeClient != null) {
                try {
                    return superNodeClient.getRemoteObj().sendAvailableSharedFiles();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    protected boolean isSuperNode() {
        return mSuperNode == null ||
                mSuperNode.getIp().equals(getMeuIp());
    }
    */
}
