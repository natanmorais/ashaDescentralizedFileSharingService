package br.asha.dfss.repository;

import br.asha.dfss.model.Node;

import java.util.ArrayList;
import java.util.List;

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

    public synchronized boolean add(String ip, String name, String subNetName) {
        return super.add(new Node(ip, name, subNetName));
    }

    public synchronized List<Node> getByName(String name) {
        List<Node> nos = new ArrayList<>();

        for (Node sn : this) {
            if (sn.nome.equals(name)) {
                nos.add(sn);
            }
        }

        return nos;
    }

    public synchronized List<Node> getBySubNetName(String name) {
        List<Node> nos = new ArrayList<>();

        for (Node sn : this) {
            if (sn.nomeSubRede.equals(name)) {
                nos.add(sn);
            }
        }

        return nos;
    }
}
