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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!ip.equals(node.ip)) return false;
        if (!nome.equals(node.nome)) return false;
        return nomeSubRede.equals(node.nomeSubRede);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + nomeSubRede.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Node {" +
                "ip='" + ip + '\'' +
                ", nome='" + nome + '\'' +
                ", nomeSubRede='" + nomeSubRede + '\'' +
                '}';
    }
}
