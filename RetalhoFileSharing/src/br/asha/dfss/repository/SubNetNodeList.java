package br.asha.dfss.repository;

import br.asha.dfss.model.Node;

/**
 * Lista de sub-redes existentes.
 */
public class SubNetNodeList extends Repository<Node> {

    public SubNetNodeList(String filename) {
        super(filename);
    }

    public synchronized static SubNetNodeList getInstance(String alias) {
        String key = alias + "-nosdaminhasubrede.asha";
        if (!INSTANCIAS.containsKey(key)) {
            INSTANCIAS.put(key, new SubNetNodeList(key));
        }
        return (SubNetNodeList) INSTANCIAS.get(key);
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
