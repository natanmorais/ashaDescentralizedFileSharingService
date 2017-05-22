package br.asha.dfss.repository;

import br.asha.dfss.model.Node;

/**
 * Lista de máquinas que estão em uma específica sub-rede.
 */
public class SubNetList extends Repository<Node> {

    public SubNetList(String filename) {
        super(filename);
    }

    public synchronized static SubNetList getInstance(String alias) {
        String key = alias + "-subredes.asha";
        if (!INSTANCIAS.containsKey(key)) {
            INSTANCIAS.put(key, new SubNetList(key));
        }
        return (SubNetList) INSTANCIAS.get(key);
    }

    public synchronized boolean add(String ip, String name) {
        return super.add(new Node(ip, name));
    }

    public synchronized Node getByName(String name) {
        for (Node sn : this) {
            if (sn.name.equals(name)) {
                return sn;
            }
        }

        return null;
    }
}
