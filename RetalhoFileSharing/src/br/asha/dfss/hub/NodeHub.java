package br.asha.dfss.hub;

import br.asha.dfss.DfssHub;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalNode;
import br.asha.dfss.model.Log;
import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.remote.INode;
import br.asha.dfss.repository.*;
import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.utils.Utils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

public class NodeHub extends DfssHub implements INode, ILocalNode {

    private final long tempoDeInicio;
    private boolean euSouUmSuperNo;
    private Node meuSuperNo;
    private String ipDoMaster;

    public NodeHub(boolean euSouUmSuperNo, String nome, String ip, int porta)
            throws RemoteException, InstantiationException, IllegalAccessException, MalformedURLException {
        super(nome, ip, porta);
        this.euSouUmSuperNo = euSouUmSuperNo;
        tempoDeInicio = System.currentTimeMillis();
        init();
    }

    public NodeHub(boolean euSouUmSuperNo, String nome, int porta)
            throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        super(nome, Utils.ipify(), porta);
        this.euSouUmSuperNo = euSouUmSuperNo;
        tempoDeInicio = System.currentTimeMillis();
        init();
    }

    public NodeHub(boolean euSouUmSuperNo, String nome)
            throws IllegalAccessException, RemoteException, InstantiationException, MalformedURLException {
        super(nome, Utils.ipify());
        this.euSouUmSuperNo = euSouUmSuperNo;
        tempoDeInicio = System.currentTimeMillis();
        init();
    }

    public boolean isEuSouUmSuperNo() {
        return euSouUmSuperNo;
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
                Object[] listas =
                        c.getRemoteObj().alguemQuerCriarUmaRede(getNome());
                if (listas != null) {
                    Utils.log("O Master retornou: as listas");
                    //Substitui sua lista com a lista retornada do Master.
                    SubNetList.getInstance(getNome()).replace((Repository<Node>) listas[0]);
                    SharedFileList.getInstance(getNome()).replace((Repository<SharedFile>) listas[1]);
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
                SubNetNodeList vizinhos = c.getRemoteObj().alguemQuerEntrarNaMinhaRede(getNome());
                if (vizinhos != null) {
                    Utils.log("Consegui entrar na sub-rede %s", subRede.nome);
                    meuSuperNo = subRede;
                    //Salvo os vizinhos já existentes.
                    SubNetNodeList.getInstance(getNome()).replace(vizinhos);
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
    public SubNetNodeList alguemQuerEntrarNaMinhaRede(String nome)
            throws RemoteException {
        Utils.log("alguemQuerEntrarNaMinhaRede(%s)", nome);

        //IP do cara que quer entrar em uma rede.
        final String ipDoCliente = getIpDoCliente();
        //Adiciona o cara.
        if (SubNetNodeList.getInstance(getNome()).add(new Node(ipDoCliente, nome, getNome()))) {
            //Avisar os outros computadores que alguem entrou na sub-rede.
            for (Node node : SubNetList.getInstance(getNome())) {
                RmiClient<INode> nodeCliente = criarUmCliente(node.ip);
                if (nodeCliente != null) {
                    nodeCliente.getRemoteObj().euTenhoUmNovoVizinho(node);
                }
            }
            return SubNetNodeList.getInstance(getNome());
        } else {
            return null;
        }
    }

    /**
     * Um novo nó foi adiciona a uma sub-rede. Então, este nó é avisado.
     *
     * @param node Novo nó adicionado.
     */
    @Override
    @RemoteMethod
    public void euTenhoUmNovoVizinho(Node node)
            throws RemoteException {
        SubNetNodeList.getInstance(getNome()).add(node);
    }

    /**
     * Alterar um super-nó da minha lista.
     *
     * @param nome Nome da novo super-nó.
     */
    @RemoteMethod
    @Override
    public boolean altereOSuperNoDeUmaSubRede(String nome) {
        Utils.log("altereOSuperNoDeUmaSubRede(%s)", nome);

        for (Node no : SubNetList.getInstance(getNome())) {
            if (no.nomeSubRede.equals(nome)) {
                no.ip = getIpDoCliente();
                SubNetList.getInstance(getNome()).save();
                Utils.log("O super-nó foi alterado");
                return true;
            }
        }

        return false;
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

        Utils.log("Estou tentando compartilhar um arquivo %s", file);

        try {
            sharedFile = new SharedFile(getMeuIp(), file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Utils.log("queroCompartilharUmArquivo(%s)", sharedFile);

        //Eu sou um nó.
        if (!euSouUmSuperNo && meuSuperNo != null) {
            RmiClient<INode> superNoCliente = criarUmCliente(meuSuperNo.ip);
            if (superNoCliente != null) {
                try {
                    return superNoCliente.getRemoteObj().alguemQuerCompartilharUmArquivo(sharedFile, true);
                } catch (RemoteException e) {
                    //e.printStackTrace();
                }
            } else {
                iniciarUmaNovaEleicao();
            }
        }
        //Eu sou um super-nó.
        else if (euSouUmSuperNo && ipDoMaster != null) {
            RmiClient<INode> masterCliente = criarUmCliente(ipDoMaster);
            if (masterCliente != null) {
                try {
                    return masterCliente.getRemoteObj().alguemQuerCompartilharUmArquivo(sharedFile, true);
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
                    //e.printStackTrace();
                }
            } else {
                iniciarUmaNovaEleicao();
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
                byte[] data = no.getRemoteObj().alguemQuerUmArquivo(file.nome);
                if (data != null) {
                    LogList.getInstance(getNome()).add(file.ip, System.currentTimeMillis(), file.nome);
                }
                return data;
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
            byte[] data = IOUtils.readFully(fis, (int) file.length());
            if (data != null) {
                LogList.getInstance(getNome()).add(getIpDoCliente(), System.currentTimeMillis(), nome);
            }
            return data;
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
        Utils.log("quemEOSuperNoDaSubRede(%s)", nomeDaSubRede);

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
        Utils.log("alguemQuerSaberOSuperNoDaSubRede(%s)", nomeDaRede);

        List<Node> nos = SubNetList.getInstance(getNome()).getBySubNetName(nomeDaRede);
        return nos.size() > 0 ? nos.get(0) : null;
    }

    @Override
    @LocalMethod
    public void vouDesligar() {
        Utils.log("vouDesligar()");

        Object[] listas = new Object[3];
        listas[0] = SubNetList.getInstance(getNome());
        listas[1] = SharedFileList.getInstance(getNome());
        listas[2] = SubNetNodeList.getInstance(getNome());

        for (Node n : SubNetNodeList.getInstance(getNome())) {
            if (!n.ip.equals(getMeuIp())) {
                RmiClient<INode> node = criarUmCliente(n.ip);
                if (node != null) {
                    try {
                        if (node.getRemoteObj().vouTransformarEmSuperNo(listas)) {
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @RemoteMethod
    @Override
    public boolean vouTransformarEmSuperNo(Object[] listas) {
        Utils.log("vouTransformarEmSuperNo()");

        if (listas != null && listas.length == 3) {
            SubNetList.getInstance(getNome()).replace((Repository<Node>) listas[0]);
            SharedFileList.getInstance(getNome()).replace((Repository<SharedFile>) listas[1]);
            SubNetNodeList.getInstance(getNome()).replace((Repository<Node>) listas[2]);
            //Estou avisando todos os super-nós
            avisarMeusVizinhos();
            avisarOsSuperNos();
            return true;
        } else {
            return false;
        }
    }

    private void continuarUmaEleicao(long dataUltimaModificacao, long tamanho, long tempoDeInicio, int ganhador) {
        Utils.log("continuarUmaEleicao( %d, %d, %d, %b)", dataUltimaModificacao, tamanho, tempoDeInicio, ganhador);

        int minhaPosicao = 0;

        //Pegar os computadores da lista de vizinhos
        SubNetNodeList vizinhos = SubNetNodeList.getInstance(getNome());
        for (int i = 0; i < vizinhos.size(); i++) {
            //Pego eu na lista.
            if (vizinhos.get(i).ip.equals(getMeuIp())) {
                minhaPosicao = i;
                break;
            }
        }

        //A data de modificação dos arquivos de super-nó são mais recentes.
        if (SubNetNodeList.getInstance(getNome()).lastModified() > dataUltimaModificacao) {
            chamarOProximoDaLista(SubNetNodeList.getInstance(getNome()).lastModified(),
                    tamanho,
                    tempoDeInicio,
                    minhaPosicao);
        }

        //O tamanho do meu log é maior.
        if (LogList.getInstance(getNome()).size() > tamanho) {
            chamarOProximoDaLista(dataUltimaModificacao,
                    LogList.getInstance(getNome()).lastModified(),
                    tempoDeInicio,
                    minhaPosicao);
        }

        //Estou executando há mais tempo.
        if (this.tempoDeInicio > tempoDeInicio) {
            chamarOProximoDaLista(dataUltimaModificacao,
                    tamanho,
                    this.tempoDeInicio,
                    minhaPosicao);
        } else {
            chamarOProximoDaLista(dataUltimaModificacao,
                    tamanho,
                    tempoDeInicio,
                    ganhador);
        }
    }

    private boolean chamarOProximoDaLista(long dataUltimaModificacao, long tamanho, long tempoDeInicio, int ganhador) {
        Utils.log("chamarOProximoDaLista( %d, %d, %d, %b)", dataUltimaModificacao, tamanho, tempoDeInicio, ganhador);

        boolean tentativa = false;

        //Pegar os computadores da lista de vizinhos
        SubNetNodeList vizinhos = SubNetNodeList.getInstance(getNome());
        for (int i = 0; i < vizinhos.size(); i++) {
            //Pego eu na lista.
            if (vizinhos.get(i).ip.equals(getMeuIp())) {
                tentativa = true;
            }

            //Tento comunicar com o próximo.
            if (tentativa &&
                    !vizinhos.get(i).ip.equals(getMeuIp()) &&
                    !vizinhos.get(i).ip.equals(meuSuperNo.ip)) {
                RmiClient<INode> vizinhoCliente = criarUmCliente(vizinhos.get(i).ip);
                if (vizinhoCliente != null) {
                    try {
                        //Se o próximo ver a solicitacao de eleicao, sai da função.
                        if (vizinhoCliente.getRemoteObj().existeUmaNovaEleicao(
                                dataUltimaModificacao,
                                tamanho,
                                tempoDeInicio,
                                ganhador)) return true;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }

    private void iniciarUmaNovaEleicao() {
        Utils.log("iniciarUmaNovaEleicao");

        //Pegar os computadores da lista de vizinhos
        SubNetNodeList vizinhos = SubNetNodeList.getInstance(getNome());
        for (int i = 0; i < vizinhos.size(); i++) {
            //Pego eu na lista.
            if (vizinhos.get(i).ip.equals(getMeuIp())) {
                if (!chamarOProximoDaLista(SubNetNodeList.getInstance(getNome()).lastModified(),
                        LogList.getInstance(getNome()).size(),
                        tempoDeInicio,
                        i)) {
                    existeUmaNovaEleicao(0, 0, 0, i);
                }
                return;
            }
        }
    }

    private void avisarMeusVizinhos() {
        Utils.log("avisarMeusVizinhos");

        for (Node no : SubNetNodeList.getInstance(getNome())) {
            if (!no.ip.equals(getMeuIp()) &&
                    SubNetNodeList.getInstance(getNome()).indexOf(no) != 0) {
                RmiClient<INode> noCliente = criarUmCliente(no.ip);
                try {
                    if (noCliente != null) {
                        noCliente.getRemoteObj().agoraSouSeuSuperNo(getIpDoCliente());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void avisarOsSuperNos() {
        Utils.log("avisarOsSuperNos");

        for (Node superNo : SubNetList.getInstance(getNome())) {
            RmiClient<INode> superNoCliente = criarUmCliente(superNo.ip);
            if (superNoCliente != null) {
                try {
                    if (meuSuperNo != null)
                        superNoCliente.getRemoteObj().altereOSuperNoDeUmaSubRede(meuSuperNo.nomeSubRede);
                    else
                        superNoCliente.getRemoteObj().altereOSuperNoDeUmaSubRede(getNome());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        //Atualizar minha propria lista que eu sou o super-nó da minha rede.
        for (Node no : SubNetList.getInstance(getNome())) {
            if (meuSuperNo != null) {
                if (no.nomeSubRede.equals(meuSuperNo.nomeSubRede)) {
                    no.ip = getMeuIp();
                    SubNetList.getInstance(getNome()).save();
                    break;
                }
            } else {
                if (no.nomeSubRede.equals(getNome())) {
                    no.ip = getMeuIp();
                    SubNetList.getInstance(getNome()).save();
                    break;
                }
            }
        }
    }

    @Override
    @RemoteMethod
    public void agoraSouSeuSuperNo(String ip)
            throws RemoteException {
        Utils.log("agoraSouSeuSuperNo(%s)", ip);

        meuSuperNo.ip = ip;
    }

    @Override
    @RemoteMethod
    public boolean existeUmaNovaEleicao(long dataUltimaModificacao, long tamanho, long tempoDeInicio, int ganhador) {
        Utils.log("existeUmaNovaEleicao");

        //Se o ganhador for eu mesmo.
        if (SubNetNodeList.getInstance(getNome()).get(ganhador).ip.equals(getMeuIp())) {
            //Comunicar com o super-nó para se tornar um super-nó.
            if (SubNetList.getInstance(getNome()).exists()) {
                for (Node superNo : SubNetList.getInstance(getNome())) {
                    RmiClient<INode> nodeCliente = criarUmCliente(superNo.ip);
                    if (nodeCliente != null) {
                        try {
                            Object[] lista =
                                    nodeCliente.getRemoteObj().oSuperNoDeAlguemCaiu();
                            SubNetList.getInstance(getNome()).replace((Repository<Node>) lista[0]);
                            SharedFileList.getInstance(getNome()).replace((Repository<SharedFile>) lista[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            return true;
                        } finally {
                            avisarMeusVizinhos();
                            avisarOsSuperNos();
                        }
                    }
                }
            } else {
                //Comunicar com o master para se tornar um super-nó.
                RmiClient<INode> masterCliente = criarUmCliente(ipDoMaster);
                try {
                    if (masterCliente != null && masterCliente.getRemoteObj().alguemQuerSaberSeEstouOnline()) {
                        Object[] lista =
                                masterCliente.getRemoteObj().oSuperNoDeAlguemCaiu();
                        SubNetList.getInstance(getNome()).replace((Repository<Node>) lista[0]);
                        SharedFileList.getInstance(getNome()).replace((Repository<SharedFile>) lista[1]);
                        try {
                            return true;
                        } finally {
                            avisarMeusVizinhos();
                            avisarOsSuperNos();
                        }
                    } else {
                        for (Log log : LogList.getInstance(getNome())) {
                            RmiClient<INode> nodeCliente = criarUmCliente(log.ip);
                            if (nodeCliente != null) {
                                //Pega o super-nó de alguma sub-rede.
                                String ipDoSuperNo = nodeCliente.getRemoteObj().mePassaSeuSuperNo();
                                //Não estou falando com o super-nó da minha própria sub-rede.
                                if (!meuSuperNo.ip.equals(ipDoSuperNo)) {
                                    //Comunicar com um super-nó de outra rede para se tornar um super-nó.
                                    RmiClient<INode> superNoCliente = criarUmCliente(ipDoSuperNo);
                                    if (superNoCliente != null) {
                                        Object[] lista =
                                                nodeCliente.getRemoteObj().oSuperNoDeAlguemCaiu();
                                        SubNetList.getInstance(getNome()).replace((Repository<Node>) lista[0]);
                                        SharedFileList.getInstance(getNome()).replace((Repository<SharedFile>) lista[1]);
                                        try {
                                            return true;
                                        } finally {
                                            avisarMeusVizinhos();
                                            avisarOsSuperNos();
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                return true;
            } finally {
                //Não sou o ganhador.
                continuarUmaEleicao(dataUltimaModificacao, tamanho, tempoDeInicio, ganhador);
            }
        }

        return true;
    }

    /**
     * O super-nó de alguem caiu e querem a lista de sub-rede e arquivos compartilhados.
     */
    @Override
    @RemoteMethod
    public Object[] oSuperNoDeAlguemCaiu()
            throws RemoteException {
        Utils.log("oSuperNoDeAlguemCaiu");

        return new Object[]{
                SubNetList.getInstance(getNome()),
                SharedFileList.getInstance(getNome())
        };
    }

    /**
     * Alguém quer saber o ip do meu super-nó.
     */
    @Override
    @RemoteMethod
    public String mePassaSeuSuperNo()
            throws RemoteException {
        Utils.log("mePassaSeuSuperNo");

        return meuSuperNo != null ? meuSuperNo.ip : null;
    }

    @LocalMethod
    @Override
    public void religarSuperNo() {
        Utils.log("religarSuperNo");

        for (Node superNo : SubNetList.getInstance(getNome())) {
            Utils.log("%s", superNo);
            RmiClient<INode> superNoCliente = criarUmCliente(superNo.ip);
            if (superNoCliente != null) {
                String ip;
                try {
                    ip = superNoCliente.getRemoteObj().qualOIpDaSubRede(getNome());
                    Utils.log(ip);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }

                if (ip == null) {
                    avisarOsSuperNos();
                } else if (ip.equals(getMeuIp())) {
                    return;
                } else {
                    Utils.log("Estou querendo tomar meu lugar de volta");
                    superNoCliente = criarUmCliente(ip);
                    if (superNoCliente != null) {
                        Object[] listas;
                        try {
                            listas = superNoCliente.getRemoteObj().queroTomarSeuLugar();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            return;
                        }

                        if (listas != null) {
                            SubNetList.getInstance(getNome()).replace((Repository<Node>) listas[0]);
                            SharedFileList.getInstance(getNome()).replace((Repository<SharedFile>) listas[1]);
                            SubNetNodeList.getInstance(getNome()).replace((Repository<Node>) listas[2]);
                            avisarMeusVizinhos();
                            avisarOsSuperNos();
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    @RemoteMethod
    public String qualOIpDaSubRede(String nomeDaSubRede)
            throws RemoteException {
        Utils.log("qualOIpDaSubRede(%s)", nomeDaSubRede);

        for (Node subRede : SubNetList.getInstance(getNome())) {
            if (subRede.nomeSubRede.equals(nomeDaSubRede)) {
                return subRede.ip;
            }
        }
        return null;
    }

    @Override
    @RemoteMethod
    public Object[] queroTomarSeuLugar()
            throws RemoteException {
        Utils.log("queroTomarSeuLugar");

        return new Object[]{
                SubNetList.getInstance(getNome()),
                SharedFileList.getInstance(getNome()),
                SubNetNodeList.getInstance(getNome())
        };
    }

    @LocalMethod
    @Override
    public boolean religarComputador() {
        Utils.log("religarComputador");

        SubNetNodeList.getInstance(getNome()).carregar();

        int existe = -1;
        List<Node> no = SubNetNodeList.getInstance(getNome()).getByName(getNome());
        for (Node n : no) {
            if (n.ip.equals(getMeuIp())) {
                existe = SubNetNodeList.getInstance(getNome()).indexOf(n);
            }
        }

        if (SubNetNodeList.getInstance(getNome()).exists() && existe >= 0) {
            if (existe == 0) {
                euSouUmSuperNo = true;
                SubNetList.getInstance(getNome()).carregar();
                SharedFileList.getInstance(getNome()).carregar();
                return true;
            }
            return true;
        }

        return false;
    }
}
