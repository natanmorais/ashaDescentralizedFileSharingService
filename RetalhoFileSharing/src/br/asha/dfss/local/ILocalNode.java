package br.asha.dfss.local;

import java.io.File;
import java.util.List;

import br.asha.dfss.IHub;
import br.asha.dfss.LocalMethod;
import br.asha.dfss.model.SharedFile;
import br.asha.dfss.model.SuperNode;
import br.asha.dfss.repository.LocalFileRepository;

public interface ILocalNode extends IHub
{
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
}
