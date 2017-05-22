package br.asha.dfss.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Super-Nó.
 */
public class Node implements Serializable {

    @SerializedName("ip")
    public String ip;
    @SerializedName("name")
    public String name;

    public Node() {
    }

    public Node(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Node {" +
                "ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node superNode = (Node) o;

        return name.equals(superNode.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
