package br.asha.dfss.local;

import br.asha.dfss.LocalMethod;
import br.asha.dfss.model.Node;
import br.asha.dfss.repository.Repository;

public interface ILocalNode {

    Repository<Node> queroCriarUmaSubRede(String ipDoMaster);

    Repository<Node> queroAListaDeSubRedesAtuais(String ipDoMaster);

    boolean queroEntrarEmUmaSubRede(Node subRede);

    boolean queroSaberSeEstaOnline(String ip);

    /*
    @LocalMethod
    boolean createNewSubNet(String masterIp, String subNetName);

    @LocalMethod
    String getSubNetName();

    LocalFileRepository getLocalFileList();

    @LocalMethod
    boolean addLocalFile(File file);

    @LocalMethod
    List<SuperNode> getAvailableSuperNodes(String masterIp);

    @LocalMethod
    boolean enterSubNet(SuperNode node);

    @LocalMethod
    boolean hasSuperNode();

    @LocalMethod
    SuperNode getSuperNode();

    @LocalMethod
    boolean addFile(SharedFile file);

    @LocalMethod
    boolean removeFile(SharedFile file);

    @LocalMethod
    boolean modifyFile(SharedFile oldFile, SharedFile newFile);

    @LocalMethod
    List<SharedFile> getAvailableSharedFiles();

    @LocalMethod
    boolean requestDataFile(String ip, String name);

    @LocalMethod
    boolean requestNetStatus();

    @LocalMethod
    void shutdown();

    @LocalMethod
    void requestMySuperNode();

    @LocalMethod
    String requestSuperNodeFromSubNet();

    @LocalMethod
    boolean letMeBeTheSuperNode(String ip);
    */
}
