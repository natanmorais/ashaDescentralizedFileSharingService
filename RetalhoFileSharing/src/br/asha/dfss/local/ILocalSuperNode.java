package br.asha.dfss.local;

import br.asha.dfss.LocalMethod;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.repository.LocalFileRepository;

import java.io.File;
import java.util.List;

public interface ILocalSuperNode {
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
}
