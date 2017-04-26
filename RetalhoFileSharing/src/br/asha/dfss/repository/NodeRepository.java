package br.asha.dfss.repository;

import br.asha.dfss.model.Node;
import br.asha.dfss.model.SuperNode;

public class NodeRepository extends ListRepository<Node>
{

    public NodeRepository(String filename)
    {
        super(filename);
    }

    public boolean add(String ip, String subNetName)
    {
        SuperNode sn = new SuperNode(ip, subNetName);
        return add(sn);
    }
}
