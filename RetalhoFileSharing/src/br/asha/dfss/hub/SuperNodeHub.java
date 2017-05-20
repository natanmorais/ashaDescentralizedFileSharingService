package br.asha.dfss.hub;

import br.asha.dfss.DfssHub;
import br.asha.dfss.HubType;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.RemoteMethod;
import br.asha.dfss.local.ILocalSuperNode;
import br.asha.dfss.model.Log;
import br.asha.dfss.model.Node;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.remote.IMaster;
import br.asha.dfss.remote.ISuperNode;
import br.asha.dfss.repository.*;
import br.asha.dfss.rmi.RmiClient;
import br.asha.dfss.utils.Utils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class SuperNodeHub extends DfssHub implements ISuperNode, ILocalSuperNode {
    private static final LocalFileRepository mLocalFileList =
            new LocalFileRepository("mfiles.asha");

    private static final SuperNodeRepository mSuperNodeList =
            new SuperNodeRepository("snodes.asha");

    private static final NodeRepository mNodeList =
            new NodeRepository("nodes.asha");

    private static final SharedFileRepository mSharedFileList =
            new SharedFileRepository("sharedfiles.asha");

    protected SuperNode mSuperNode = null;
    private String mSubNetName;

    protected SuperNodeHub(HubType hubType, String hubName, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException {
        super(hubType, hubName, ip);
    }

    public SuperNodeHub(String name, String ip)
            throws RemoteException, InstantiationException, IllegalAccessException {
        this(HubType.SUPER_NODE, name, ip);
    }

    public SuperNodeHub(String name)
            throws IllegalAccessException, RemoteException, InstantiationException {
        this(name, Utils.ipify());
    }

    public static NodeRepository getNodeList() {
        return mNodeList;
    }

    public static SharedFileRepository getSharedFileList() {
        return mSharedFileList;
    }

    public SuperNodeRepository getSuperNodeList() {
        return mSuperNodeList;
    }

    @Override
    @RemoteMethod
    public boolean requestNewNode(String name)
            throws RemoteException {
        String clientIp = getClientIp();

        Utils.log("requestNewNode: %s:%s", name, clientIp);

        //Registrar uma nova máquina (IP e Nome).
        if (getNodeList().add(clientIp, name) &&
                getNodeList().save()) {
            Utils.log("Maquina %s:%s registrada", name, clientIp);
            return true;
        } else {
            Utils.log("Erro ao registrar a maquina %s:%s", name, clientIp);
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
    public String sendSuperNodeFromSubNet(String name) {
        SuperNode sn = getSuperNodeList().getByName(name);
        if (sn != null) return sn.getIp();
        return null;
    }

    @Override
    @LocalMethod
    public String requestSuperNodeFromSubNet() {
        for (SuperNode sn : getSuperNodeList()) {
            try {
                RmiClient<ISuperNode> nodeClient = createClient(sn.getIp());
                return nodeClient.getRemoteObj().sendSuperNodeFromSubNet(getSubNetName());
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
                superNodeClient = createClient(ip);
            } catch (Exception e) {
                requestMySuperNode();
                return true;
            }

            for (SuperNode sn : getSuperNodeList()) {
                try {
                    RmiClient<ISuperNode> superNodeClient2 = createClient(sn.getIp());
                    superNodeClient2.getRemoteObj().insertNewSuperNode(getSubNetName());
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
            if (!sn.getIp().equals(getServerIp())) {
                //Envia para o super-nó.
                RmiClient<ISuperNode> superNodeClient = DfssHub.createClient(sn.getIp());
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
    public boolean insertNewSuperNode(String name)
            throws RemoteException {
        for (SuperNode sn : getSuperNodeList()) {
            if (sn.getSubnetName().equalsIgnoreCase(name)) {
                sn.setIp(getClientIp());
                getSuperNodeList().save();
                return true;
            }
        }
        return false;
    }

    private void removeSharedFileByBroadcast(SharedFile file) {
        for (SuperNode sn : getSuperNodeList().toList()) {
            if (!sn.getIp().equals(getServerIp())) {
                //Envia para o super-nó.
                RmiClient<ISuperNode> superNodeClient = createClient(sn.getIp());
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
    public String getSubNetName() {
        return mSubNetName;
    }

    public boolean addFileInSuperNode(SharedFile file) {
        getSharedFileList().add(file);
        addSharedFileByBroadcast(file);
        return true;
    }

    @Override
    @LocalMethod
    public boolean createNewSubNet(String masterIp, String subNetName) {
        mSubNetName = subNetName;
        RmiClient<IMaster> masterClient = null;

        try {
            masterClient = createClient(masterIp);
        } catch (Exception e) {
            return false;
        }

        if (masterClient != null) {
            try {
                //Registra a rede no master.
                //TODO Erro ao criar a rede pois já existe uma com o mesmo nome.
                return masterClient.getRemoteObj().requestNewSuperNode(getSubNetName());

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
            if (sn.getIp().equals(masterIp) || sn.getIp().equals(getServerIp())) continue;

            RmiClient<ISuperNode> superNodeClient = createClient(sn.getIp());

            if (superNodeClient != null) {
                //superNodeClient.getRemoteObj().insertNewSuperNode(getSubNetName());
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
            RmiClient<ISuperNode> superNodeClient = createClient(n.getIp());
            if (superNodeClient.getRemoteObj().transformSuperNode(listas)) {
                return;
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
                    RmiClient<ISuperNode> superNodeClient = createClient(sn.getIp());
                    superNodeClient.getRemoteObj().insertNewSuperNode(getSubNetName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (SuperNode sn : getSuperNodeList()) {
                RmiClient<ISuperNode> superNodeClient = null;
                try {
                    superNodeClient = createClient(sn.getIp());
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                listas = superNodeClient.getRemoteObj().leaveSuperNodeFunction();
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
    public String findSuperNodeFromSubNet(String name) {
        if (isSuperNode()) {
            SuperNode sn = getSuperNodeList().getByName(name);
            if (sn != null) return sn.getIp();
            return null;
        } else {
            try {
                RmiClient<ISuperNode> superNodeClient = createClient(mSuperNode.getIp());
                return superNodeClient.getRemoteObj().sendSuperNodeFromSubNet(name);
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
            RmiClient<ISuperNode> superNodeClient = createClient(mSuperNode.getIp());
            return superNodeClient.getRemoteObj().leaveSuperNodeFunction();
        }
    }

    @Override
    @LocalMethod
    public void requestMySuperNode() {
        for (Log log : LogRepository.getInstance()) {
            try {
                RmiClient<ISuperNode> nodeClient = createClient(log.getIp());
                String ip = nodeClient.getRemoteObj().findSuperNodeFromSubNet(getSubNetName());

                if (ip == null) {
                    continue;
                } else if (ip.equals(mSuperNode.getIp())) {
                    for (SuperNode sn : getSuperNodeList()) {
                        RmiClient<ISuperNode> superNodeClient = createClient(sn.getIp());
                        superNodeClient.getRemoteObj().insertNewSuperNode(getSubNetName());
                    }
                    //TODO Vai se tornar o super-nó.
                    Object[] listas = nodeClient.getRemoteObj().sendUpdate();
                    getSuperNodeList().replace((List<SuperNode>) listas[0]);
                    getSharedFileList().replace((List<SharedFile>) listas[1]);
                    getNodeList().replace((List<Node>) listas[2]);
                } else {
                    //TODO Eu sou da sua sub-rede
                    RmiClient<ISuperNode> superNodeClient = createClient(ip);
                    if (superNodeClient.getRemoteObj().requestNewNode(getSubNetName())) {
                        mSuperNode = new SuperNode(ip, getSubNetName());
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
        return getSubNetName();
    }

    @Override
    @RemoteMethod
    public byte[] sendDataFile(String name)
            throws RemoteException {
        File file = new File(name);

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
            superNodeClient = createClient(getClientIp());
        } catch (Exception e) {
            e.printStackTrace();
            requestMySuperNode();
            return false;
        }

        return superNodeClient.getRemoteObj().sendNetStatus();
    }

    @Override
    @LocalMethod
    public boolean requestDataFile(String ip, String name) {
        RmiClient<ISuperNode> nodeClient = null;

        try {
            nodeClient = createClient(ip);
        } catch (Exception e) {
            return false;
        }

        try {
            byte[] data = nodeClient.getRemoteObj().sendDataFile(name);
            try (OutputStream os = new FileOutputStream(name)) {
                os.write(data);
                LogRepository.getInstance().add(new Log(ip, new Date().getTime(), name));
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
        RmiClient<IMaster> masterClient = createClient(masterIp);

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
            superNodeClient = createClient(node.getIp());
        } catch (Exception e) {
            return false;
        }

        try {
            if (superNodeClient != null) {
                //TODO Tratar melhor o erro?
                if (superNodeClient.getRemoteObj().requestNewNode(getSubNetName())) {
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
                superNodeClient = createClient(getSuperNode().getIp());
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
                superNodeClient = createClient(getSuperNode().getIp());
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
                mSuperNode.getIp().equals(getServerIp());
    }
}
