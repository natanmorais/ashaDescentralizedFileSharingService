package br.asha.dfss.repository;

import br.asha.dfss.model.SuperNode;

public class SuperNodeRepository extends ListRepository<SuperNode> {

    public SuperNodeRepository(String filename) {
        super(filename);
    }

    public boolean add(String ip, String subNetName) {
        SuperNode sn = new SuperNode(ip, subNetName);
        return add(sn);
    }

    public SuperNode getByName(String name) {
        for (SuperNode sn : this) {
            if (sn.getSubnetName().equalsIgnoreCase(name)) {
                return sn;
            }
        }

        return null;
    }
}
