package br.asha.dfss.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Super-Nó.
 */
public class Node implements Serializable {

    @SerializedName("ip")
    public String ip;
    @SerializedName("nome")
    public String nome;
    @SerializedName("nomeSubRede")
    public String nomeSubRede;

    public Node() {
    }

    public Node(String ip, String nome, String nomeSubRede) {
        this.ip = ip;
        this.nome = nome;
        this.nomeSubRede = nomeSubRede;
    }

    @Override
    public String toString() {
        return "Node {" +
                "ip='" + ip + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return nome.equals(node.nome) &&
                (nomeSubRede != null ?
                        nomeSubRede.equals(node.nomeSubRede) :
                        node.nomeSubRede == null);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + (nomeSubRede != null ? nomeSubRede.hashCode() : 0);
        return result;
    }
}
